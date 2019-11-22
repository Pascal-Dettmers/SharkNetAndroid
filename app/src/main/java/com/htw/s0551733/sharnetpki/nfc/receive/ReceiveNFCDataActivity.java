package com.htw.s0551733.sharnetpki.nfc.receive;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.htw.s0551733.sharnetpki.R;
import com.htw.s0551733.sharnetpki.storage.SharedPreferencesHandler;
import com.htw.s0551733.sharnetpki.util.Constants;
import com.htw.s0551733.sharnetpki.nfc.NfcChecks;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

import main.de.htw.berlin.s0551733.sharknetpki.SharknetPublicKey;

import static com.htw.s0551733.sharnetpki.util.SerializationHelper.byteToObj;

public class ReceiveNFCDataActivity extends AppCompatActivity {

    private NfcAdapter nfcAdapter = null;
    private final String TAG = this.getClass().getName();
    private SharedPreferencesHandler sharedPreferencesHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_nfcdata);

        this.nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        NfcChecks.preliminaryNfcChecks(nfcAdapter, this);

        ProgressBar progressBar = findViewById(R.id.receivePublicKeyProgressBar);
        progressBar.setVisibility(ProgressBar.VISIBLE);

        sharedPreferencesHandler = new SharedPreferencesHandler(this.getApplicationContext());
    }

    private void processIntent(Intent intent) {

        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            if (intent.getType().equals("application/net.sharksystem.send.public.key")) {
                processSendPublic(intent);
            } else {
                Log.d(TAG, "processIntent: Wrong intent type");
            }
        }
    }

    private void processSendPublic(Intent intent) {
        Parcelable[] rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        SharknetPublicKey receiveData = null;

        if (rawMessages != null) {
            NdefMessage[] messages = new NdefMessage[rawMessages.length];
            for (int i = 0; i < rawMessages.length; i++) {
                messages[i] = (NdefMessage) rawMessages[i];
            }

            byte[] receiveDataPayload = messages[0].getRecords()[0].getPayload();

            try {
                receiveData = (SharknetPublicKey) byteToObj(receiveDataPayload);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

            Toast.makeText(this, "beam successful", Toast.LENGTH_LONG).show();
            showAlert(receiveData);
        }
    }

    private void showAlert(SharknetPublicKey receiveData) {
        new MaterialAlertDialogBuilder(this.getApplicationContext())
                .setTitle("Title")
                .setMessage("Message")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        persistData(receiveData);
                    }
                })
                .setNegativeButton("No", null)
                .setCancelable(false)
                .show();
    }

    private void persistData(SharknetPublicKey receivedData) {
        Gson gson = new Gson();
        Type keyListType = new TypeToken<Set<SharknetPublicKey>>() {
        }.getType();

        String keyListJson = sharedPreferencesHandler.getValue(Constants.KEY_LIST);
        HashSet<SharknetPublicKey> keyList = gson.fromJson(keyListJson, keyListType);

        if (keyList == null) {
            HashSet<SharknetPublicKey> initialKeyList = new HashSet<>();
            initialKeyList.add(receivedData);
            String newKeyListJson = gson.toJson(initialKeyList);
            sharedPreferencesHandler.writeValue(Constants.KEY_LIST, newKeyListJson);
        } else {
            if (!keyList.contains(receivedData)) {
                keyList.add(receivedData);
                String newKeyListJson = gson.toJson(keyList);
                sharedPreferencesHandler.writeValue(Constants.KEY_LIST, newKeyListJson);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (nfcAdapter != null) {
            enableForegroundDispatch();
        }
        processIntent(this.getIntent());
    }

    @Override
    protected void onPause() {
        super.onPause();
        disableForeground();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        processIntent(intent);
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

}
