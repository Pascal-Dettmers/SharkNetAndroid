package com.htw.s0551733.sharnetpki;

import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.htw.s0551733.sharnetpki.nfc.NfcChecks;
import com.htw.s0551733.sharnetpki.nfc.NfcMessageManager;
import com.htw.s0551733.sharnetpki.nfc.receive.NfcCallback;
import com.htw.s0551733.sharnetpki.nfc.receive.NfcDataManager;
import com.htw.s0551733.sharnetpki.pager.SharkNetPagerAdapter;
import com.htw.s0551733.sharnetpki.recyclerViews.SharkNetKey;
import com.htw.s0551733.sharnetpki.recyclerViews.SharkNetUser;
import com.htw.s0551733.sharnetpki.storage.datastore.DataStorage;
import com.htw.s0551733.sharnetpki.storage.keystore.RSAKeystoreHandler;
import com.htw.s0551733.sharnetpki.util.Constants;
import com.htw.s0551733.sharnetpki.util.SharedPreferencesHandler;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.security.KeyStoreException;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

import main.de.htw.berlin.s0551733.sharknetpki.SharkNetPKI;
import main.de.htw.berlin.s0551733.sharknetpki.impl.SharkNetException;
import main.de.htw.berlin.s0551733.sharknetpki.interfaces.SharkNetCertificate;
import main.de.htw.berlin.s0551733.sharknetpki.interfaces.SharkNetPublicKey;

import static com.htw.s0551733.sharnetpki.util.SerializationHelper.byteToObj;
import static com.htw.s0551733.sharnetpki.util.SerializationHelper.objToByte;

public class MainActivity extends AppCompatActivity implements NfcCallback {

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

    /**
     * Storage Users Own SharkNetKey and Received Public Keys and Certificates
     */
    private DataStorage storage;

    /**
     * Adapter for NFC operations
     */
    private NfcAdapter nfcAdapter;

    /**
     * Managed receiving NFC data
     */
    private NfcDataManager nfcDataManager;

    /**
     * FileInputStream for laoding the KeyStore
     */
    private FileInputStream inputStream;


    public final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        storage = new DataStorage(new SharedPreferencesHandler(this.getApplicationContext()));
        prepareNFC();

        BottomAppBar bar = findViewById(R.id.bottom_app_bar);
        setSupportActionBar(bar);

        initFAB();
        initPKI();
        appStartSetUp();
        setUpTabLayout();
    }

    private void prepareNFC() {
        this.nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        NfcChecks.preliminaryNfcChecks(nfcAdapter, this);
        nfcDataManager = new NfcDataManager(this, this);
    }

    private void initFAB() {
        FloatingActionButton fab = findViewById(R.id.fab);
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
        if (storage.getMyOwnPublicKey() == null) {
            createAliasDialog();
        }

    }

    private void generateMyOwnSharkNetPublicKey(String alias) {
        String uuid = generateUUID();
        Calendar expirationDate = Calendar.getInstance();
        expirationDate.add(Calendar.YEAR, Constants.DEFAULT_KEY_DURATION_YEARS);
        try {
            byte[] encodedRawPublicKey = objToByte(SharkNetPKI.getInstance().getMyOwnPublicKey());
            String myOwnPublicKey = Base64.encodeToString(encodedRawPublicKey, Base64.DEFAULT);
            storage.addMyOwnPublicKey(new SharkNetKey(new SharkNetUser(uuid, alias), myOwnPublicKey, expirationDate.getTime()));
        } catch (KeyStoreException | IOException e) {
            e.printStackTrace();
        }
    }

    private String generateUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
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
                        generateMyOwnSharkNetPublicKey(aliasInput.getText().toString());
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        generateMyOwnSharkNetPublicKey("Mustermann");
                    }
                })
                .setCancelable(false)
                .show();
    }

    private void initPKI() {
        HashSet<SharkNetPublicKey> publicKeySet = getPublicKeySet();
        HashSet<SharkNetCertificate> certificateSet = new HashSet<>();

        try {
            this.inputStream = openFileInput("keystore.pksc12");
        } catch (FileNotFoundException e) {
            this.inputStream = null;
        }

        try {
            char[] keystorePW = getKeystorePW().toCharArray();
            SharkNetPKI.init(keystorePW, inputStream, publicKeySet, certificateSet);
        } catch (SharkNetException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    private String getKeystorePW() throws IOException, ClassNotFoundException {
        if (storage.getKeystorePassword() == null) {
            String password = generateUUID(); // generate random string
            byte[] passwordInBytes = objToByte(password);
            byte[] encryptPassword = RSAKeystoreHandler.getInstance().encrypt(passwordInBytes);
            String encryptPasswordInBase64 = Base64.encodeToString(encryptPassword, Base64.DEFAULT); // encode to base64 to store it
            storage.addKeyStorePassword(encryptPasswordInBase64); // persist password
            return password;
        } else {
            String encryptedKeystorePasswordInBase64 = storage.getKeystorePassword();
            byte[] decodePassword = Base64.decode(encryptedKeystorePasswordInBase64, Base64.DEFAULT);
            byte[] decryptPassword = RSAKeystoreHandler.getInstance().decrypt(decodePassword);
            return (String) byteToObj(decryptPassword);
        }
    }

// Todo what to do with certs?

//    private HashSet<SharkNetCertificate> getCertificateSet() {
//        sharedPreferencesHandler = new SharedPreferencesHandler(this.getApplicationContext());
//        Gson gson = new Gson();
//        Type typeOfCertificateList = new TypeToken<HashSet<SharkNetCertificate>>() {
//        }.getType();
//
//        String certificateListInJson = sharedPreferencesHandler.getValue(Constants.CERTIFICATE_LIST);
//
//        if (certificateListInJson != null) {
//            return gson.<HashSet<SharkNetCertificate>>fromJson(certificateListInJson, typeOfCertificateList);
//        } else {
//            return new HashSet<>();
//        }
//    }

    private HashSet<SharkNetPublicKey> getPublicKeySet() {
        DataStorage dataStorage = new DataStorage(new SharedPreferencesHandler(this));
        HashSet<SharkNetPublicKey> keySet = dataStorage.getKeySet();

        if (keySet != null) {
            return keySet;
        } else {
            return new HashSet<>();
        }
    }

    private void setUpTabLayout() {
        // Instantiate a ViewPager, SharkNetPagerAdapter and tabLayout.
        mPager = findViewById(R.id.view_pager);
        pagerAdapter = new SharkNetPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(pagerAdapter);
        TabLayout tabLayout = findViewById(R.id.tabs_list);
        tabLayout.setupWithViewPager(mPager);
        Objects.requireNonNull(tabLayout.getTabAt(0)).setIcon(tabIcons[0]);
        Objects.requireNonNull(tabLayout.getTabAt(1)).setIcon(tabIcons[1]);
    }

    private void setPushMessage() throws IOException {
        SharkNetPublicKey dataToSend = storage.getMyOwnPublicKey();
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

    // Todo beschreiben in der Arbeit: https://stackoverflow.com/questions/26943935/what-does-enableforegrounddispatch-and-disableforegrounddispatch-do
    // enableForegroundDispatch gives your current foreground activity priority in receiving NFC events over all other actvities.
    private void enableForegroundDispatch() {
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
                getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
    }

    private void disableForeground() {
        if (this.nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (nfcAdapter != null) {
            enableForegroundDispatch();
        }
        nfcDataManager.processIntent(this.getIntent());
        HashSet<SharkNetPublicKey> publicKeys = SharkNetPKI.getInstance().getSharkNetPublicKeys();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        nfcDataManager.processIntent(intent);
    }


    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        disableForeground();
    }

    @Override
    public void onDataReceived() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Succesfull")
                .setMessage("Key was succesfull received!")
                .setPositiveButton("Ok", null)
                .setCancelable(false)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            OutputStream outputStream = openFileOutput("keystore.pksc12", Context.MODE_PRIVATE);
            SharkNetPKI.getInstance().persistKeyStore(outputStream);
        } catch (IOException | SharkNetException e) {
            e.printStackTrace();
        }
    }
}
