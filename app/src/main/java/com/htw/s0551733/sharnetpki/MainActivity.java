package com.htw.s0551733.sharnetpki;

import android.content.DialogInterface;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.htw.s0551733.sharnetpki.nfc.NfcMessageManager;
import com.htw.s0551733.sharnetpki.pager.SharkNetPagerAdapter;
import com.htw.s0551733.sharnetpki.recyclerViews.SharkNetKey;
import com.htw.s0551733.sharnetpki.storage.IdentityStorage;
import com.htw.s0551733.sharnetpki.storage.SharedPreferencesHandler;
import com.htw.s0551733.sharnetpki.storage.SharkIdentityStorage;
import com.htw.s0551733.sharnetpki.storage.keystore.RSAKeystoreHandler;
import com.htw.s0551733.sharnetpki.util.Constants;
import com.htw.s0551733.sharnetpki.nfc.NfcChecks;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.security.KeyStoreException;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import main.de.htw.berlin.s0551733.sharknetpki.SharknetCertificate;
import main.de.htw.berlin.s0551733.sharknetpki.SharknetPublicKey;
import main.de.htw.berlin.s0551733.sharknetpki.impl.SharkNetException;
import main.de.htw.berlin.s0551733.sharknetpki.impl.SharknetPKI;

import static com.htw.s0551733.sharnetpki.util.SerializationHelper.objToByte;

public class MainActivity extends AppCompatActivity {

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter pagerAdapter;

    /**
     * The Tab-Icons as an integer
     */
    private int[] tabIcons = {
            R.drawable.ic_public_key_black_24dp,
            R.drawable.ic_tab_certificate_black_24dp
    };

    private SharkIdentityStorage storage;
    private SharedPreferencesHandler sharedPreferencesHandler;
    private RSAKeystoreHandler keystore;
    private NfcAdapter nfcAdapter;
    private SharknetPKI pki;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        storage = IdentityStorage.getIdentityStorage(this.getApplication());

        this.nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        NfcChecks.preliminaryNfcChecks(nfcAdapter, this);

        sharedPreferencesHandler = new SharedPreferencesHandler(this.getApplicationContext());

        BottomAppBar bar = findViewById(R.id.bottom_app_bar);
        setSupportActionBar(bar);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    setPushMessage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        appStartSetUp();
        initPKI();
        setUpTabLayout();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            BottomNavigatorFragment bottomNavDrawerFragment = new BottomNavigatorFragment();
            bottomNavDrawerFragment.show(getSupportFragmentManager(), bottomNavDrawerFragment.getTag());
        }
        return true;
    }

    private void appStartSetUp() {
        generateUUID();
        setPublicKeyAlias();
    }

    private void generateUUID() {
        if (storage.getOwnerID() != null && storage.getOwnerID().equals("")) {
        } else {
            createUUID();
        }
    }

    public void createUUID() {
        UUID uuid = UUID.randomUUID();
        storage.setOwnerID(uuid.toString());
    }

    private void setPublicKeyAlias() {
        if (storage.getAlias() != null) {
        } else {
            createAliasDialog();
        }
    }

    private void createAliasDialog() {
        LayoutInflater inflater = this.getLayoutInflater();
        View viewInflated = inflater.inflate(R.layout.dialog_set_alias, null);
        final EditText aliasInput = viewInflated.findViewById(R.id.inputAlias);


        new MaterialAlertDialogBuilder(this)
                .setTitle("Alias")
                .setMessage("Set your Alias")
                .setView(viewInflated)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        storage.setAlias(aliasInput.getText().toString());
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        storage.setAlias("Mustermann");
                    }
                })
                .setCancelable(false)
                .show();
    }

    private void initPKI() {
        Set<SharknetPublicKey> publicKeySet = getPublicKeySet();
        Set<SharknetCertificate> certificateSet = getCertificateSet();
        char[] keystorePW = getKeystorePW();
        try {
            pki = SharknetPKI.init(keystorePW, publicKeySet, certificateSet);
        } catch (SharkNetException e) {
            e.printStackTrace();
        }
    }

    private char[] getKeystorePW() {
        return null;
    }

    private Set<SharknetCertificate> getCertificateSet() {
        sharedPreferencesHandler = new SharedPreferencesHandler(this.getApplicationContext());
        Gson gson = new Gson();
        Type typeOfCertificateList = new TypeToken<HashSet<SharknetCertificate>>() {
        }.getType();

        String certificateListInJson = sharedPreferencesHandler.getValue(Constants.KEY_LIST);

        if (certificateListInJson != null) {
            return gson.<HashSet<SharknetCertificate>>fromJson(certificateListInJson, typeOfCertificateList);
        } else {
            return new HashSet<>();
        }
    }

    private Set<SharknetPublicKey> getPublicKeySet() {
        sharedPreferencesHandler = new SharedPreferencesHandler(this.getApplicationContext());
        Gson gson = new Gson();
        Type typeOfKeyList = new TypeToken<HashSet<SharkNetKey>>() {
        }.getType();

        String keyListInJson = sharedPreferencesHandler.getValue(Constants.KEY_LIST);

        if (keyListInJson != null) {
            return gson.<HashSet<SharknetPublicKey>>fromJson(keyListInJson, typeOfKeyList);
        } else {
            return new HashSet<>();
        }
    }

    private void setUpTabLayout() {
        // Instantiate a ViewPager, a SharkNetPagerAdapter and tabLayout.
        mPager = findViewById(R.id.view_pager);
        pagerAdapter = new SharkNetPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(pagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs_list);
        tabLayout.setupWithViewPager(mPager);
        Objects.requireNonNull(tabLayout.getTabAt(0)).setIcon(tabIcons[0]);
        Objects.requireNonNull(tabLayout.getTabAt(1)).setIcon(tabIcons[1]);
    }

    /**
     * @throws IOException
     */
    private void setPushMessage() throws IOException {
        Calendar expirationDate = Calendar.getInstance();
        expirationDate.add(Calendar.YEAR, Constants.DEFAULT_KEY_DURATION_YEARS);
        byte[] encodedRawPublicKey = null;
        try {
            encodedRawPublicKey = objToByte(pki.getMyOwnPublicKey());
        } catch (IOException | KeyStoreException e) {
            e.printStackTrace();
        }
        String publicKeyInBase64 = Base64.encodeToString(encodedRawPublicKey, Base64.DEFAULT);
        SharknetPublicKey dataToSend = new SharkNetKey(storage.getAlias().toString(), storage.getOwnerID().toString(), publicKeyInBase64, expirationDate.getTime());
        initNfcMessageManager(objToByte(dataToSend));
    }


    private void initNfcMessageManager(byte[] encodedSharkNetPublicKey) {
        if (encodedSharkNetPublicKey != null) {
            NfcMessageManager outcomingNfcCallback = new NfcMessageManager("application/net.sharksystem.send.public.key".getBytes(Charset.forName("US-ASCII")), encodedSharkNetPublicKey);
            this.nfcAdapter.setOnNdefPushCompleteCallback(outcomingNfcCallback, this);
            this.nfcAdapter.setNdefPushMessageCallback(outcomingNfcCallback, this);
            this.nfcAdapter.invokeBeam(this);
        } else {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

}
