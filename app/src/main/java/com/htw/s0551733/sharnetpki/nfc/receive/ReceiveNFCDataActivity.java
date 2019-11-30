package com.htw.s0551733.sharnetpki.nfc.receive;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.htw.s0551733.sharnetpki.storage.datastore.DataStorage;
import com.htw.s0551733.sharnetpki.util.SharedPreferencesHandler;

import java.io.IOException;
import java.util.HashSet;

import main.de.htw.berlin.s0551733.sharknetpki.SharknetPublicKey;
import main.de.htw.berlin.s0551733.sharknetpki.impl.SharknetPKI;

import static com.htw.s0551733.sharnetpki.util.SerializationHelper.byteToObj;

public class ReceiveNFCDataActivity {

    private final String TAG = this.getClass().getSimpleName();
    private Context context;
    private DataStorage dataStorage;
    private NFCCallback nfcCallback;

    public ReceiveNFCDataActivity(Context context, NFCCallback nfcCallback) {
        this.context = context;
        this.dataStorage = new DataStorage(new SharedPreferencesHandler(context));
        this.nfcCallback = nfcCallback;
    }

    public void processIntent(Intent intent) {

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

            Toast.makeText(this.context, "beam successful", Toast.LENGTH_LONG).show();
            showAlert(receiveData);
        }
    }

    private void showAlert(SharknetPublicKey receiveData) {
        new MaterialAlertDialogBuilder(context)
                .setTitle("Authentication")
                .setMessage("Do you really want to save this Key from " + receiveData.getAlias())
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

        HashSet<SharknetPublicKey> keySet = SharknetPKI.getInstance().getPublicKeys();
        if (!keySet.contains(receivedData)) {
            SharknetPKI.getInstance().addPublicKey(receivedData);
            dataStorage.addKeySet(SharknetPKI.getInstance().getPublicKeys());
            HashSet<SharknetPublicKey> publicKeys = SharknetPKI.getInstance().getPublicKeys();
            Log.d(TAG, "persistData: " + publicKeys.size());
            nfcCallback.onDataReceived();
        }
    }
}
