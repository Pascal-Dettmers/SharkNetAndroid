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
import com.htw.s0551733.sharnetpki.recyclerViews.SharkNetCert;
import com.htw.s0551733.sharnetpki.recyclerViews.SharkNetKey;
import com.htw.s0551733.sharnetpki.recyclerViews.SharkNetUser;
import com.htw.s0551733.sharnetpki.storage.datastore.DataStorage;
import com.htw.s0551733.sharnetpki.util.Constants;
import com.htw.s0551733.sharnetpki.util.SharedPreferencesHandler;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

import main.de.htw.berlin.s0551733.sharknetpki.SharkNetPKI;
import main.de.htw.berlin.s0551733.sharknetpki.impl.SharkNetException;
import main.de.htw.berlin.s0551733.sharknetpki.interfaces.SharkNetCertificate;
import main.de.htw.berlin.s0551733.sharknetpki.interfaces.SharkNetPublicKey;

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
                    sendMyOwnPublicKey();
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
                .setMessage("Set your Alias")
                .setView(viewInflated)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        generateMyOwnSharkNetPublicKey(aliasInput.getText().toString());
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        generateMyOwnSharkNetPublicKey("Mustermann");
                    }
                })
                .setCancelable(false)
                .show();
    }

    private void initPKI() {
        HashSet<SharkNetPublicKey> publicKeySet = storage.getKeySet();
        HashSet<SharkNetCertificate> certificateSet = new HashSet<>();

        try {
            this.inputStream = openFileInput("keystore.pksc12");
        } catch (FileNotFoundException e) {
            this.inputStream = null;
        }

        try {
            //init PKI
            char[] keystorePW = storage.getKeystorePassword().toCharArray();
            SharkNetPKI.init(keystorePW, inputStream, publicKeySet, certificateSet);
            //persist key for the first time
            if (this.inputStream == null) {
                OutputStream outputStream = openFileOutput("keystore.pksc12", Context.MODE_PRIVATE);
                SharkNetPKI.getInstance().persistKeyStore(outputStream);
            }

        } catch (SharkNetException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    //todo cert in datastorage
//    private HashSet<SharkNetCert> getCertificateSet() {
//        HashSet<SharkNetPublicKey> keySet = storage.getKeySet();
//
//        if (keySet != null) {
//            return keySet;
//        } else {
//            return new HashSet<>();
//        }
//    }

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

    private void sendMyOwnPublicKey() throws IOException {
        SharkNetPublicKey dataToSend = storage.getMyOwnPublicKey();
        initNfcMessageManager(objToByte(dataToSend), Constants.PUBLIC_KEY_INTENT_FILTER);
    }

    private void initNfcMessageManager(byte[] dataToSend, String intentFilter) {
        if (dataToSend != null) {
            NfcMessageManager outcomingNfcCallback = new NfcMessageManager(intentFilter.getBytes(Charset.forName("US-ASCII")), dataToSend);
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
    public void onPublicKeyReceived() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Succesfull")
                .setMessage("Key succesfull received!")
                .setPositiveButton("Ok", null)
                .setCancelable(false)
                .show();
    }

    @Override
    public void onCertificateReceived() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Succesfull")
                .setMessage("Certificate succesfull received!")
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

    public void sendReceivedPublicKeyAsCertificate(SharkNetPublicKey key) {
        try {
            SharkNetKey myOwnPublicKey = storage.getMyOwnPublicKey();
            if (key.getExpirationDate().after(new Date())) {

                // Generate X509 Certificate
                PrivateKey privateKey = SharkNetPKI.getInstance().getPrivateKey();
                X509Certificate x509Certificate = SharkNetPKI.getInstance().generateCertificate(key.getPublicKey(), privateKey, myOwnPublicKey.getOwner().getAlias(), key.getOwner().getAlias());
                // Generate SharkNetCertificate
                String certInBase64 = Base64.encodeToString(objToByte(x509Certificate), Base64.DEFAULT);
                SharkNetUser sharkNetUser = new SharkNetUser(key.getOwner().getUuid(), key.getOwner().getAlias());
                SharkNetCert certificate = new SharkNetCert(sharkNetUser, certInBase64, myOwnPublicKey.getSharkNetUser());
                // setup NFC
                initNfcMessageManager(objToByte(certificate), Constants.RECEIVED_PUBLIC_KEY_CERT_INTENT_FILTER);
            } else {
                Toast.makeText(this.getApplicationContext(), "This key is not valid anymore", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
