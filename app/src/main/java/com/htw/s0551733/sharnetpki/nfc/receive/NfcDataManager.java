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
import com.htw.s0551733.sharnetpki.util.Constants;
import com.htw.s0551733.sharnetpki.util.SharedPreferencesHandler;

import java.io.IOException;
import java.util.HashSet;

import main.de.htw.berlin.s0551733.sharknetpki.SharkNetPKI;
import main.de.htw.berlin.s0551733.sharknetpki.interfaces.SharkNetCertificate;
import main.de.htw.berlin.s0551733.sharknetpki.interfaces.SharkNetPublicKey;

import static com.htw.s0551733.sharnetpki.util.SerializationHelper.byteToObj;

public class NfcDataManager {

    private final String TAG = this.getClass().getSimpleName();
    private Context context;
    private DataStorage dataStorage;
    private NfcCallback nfcCallback;

    public NfcDataManager(Context context, NfcCallback nfcCallback) {
        this.context = context;
        this.dataStorage = new DataStorage(new SharedPreferencesHandler(context));
        this.nfcCallback = nfcCallback;
    }

    public void processIntent(Intent intent) {

        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction()) && intent.getType() != null) {

            switch (intent.getType()) {
                case Constants.PUBLIC_KEY_INTENT_FILTER:
                    processSendPublic(intent);
                    break;
                case Constants.RECEIVED_PUBLIC_KEY_CERT_INTENT_FILTER:
                    processSendCertificate(intent);
                    break;
                default:
                        Log.d(TAG, "processIntent: Wrong intent type");

            }
        }
    }

    private void processSendCertificate(Intent intent) {
        Parcelable[] rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        SharkNetCertificate receiveData = null;

        if (rawMessages != null) {
            NdefMessage[] messages = new NdefMessage[rawMessages.length];
            for (int i = 0; i < rawMessages.length; i++) {
                messages[i] = (NdefMessage) rawMessages[i];
            }

            byte[] receiveDataPayload = messages[0].getRecords()[0].getPayload();

            try {
                receiveData = (SharkNetCertificate) byteToObj(receiveDataPayload);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

            Toast.makeText(this.context, "beam successful", Toast.LENGTH_LONG).show();
            showCertificateKeyAlert(receiveData);
        }
    }

    private void processSendPublic(Intent intent) {
        Parcelable[] rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        SharkNetPublicKey receiveData = null;

        if (rawMessages != null) {
            NdefMessage[] messages = new NdefMessage[rawMessages.length];
            for (int i = 0; i < rawMessages.length; i++) {
                messages[i] = (NdefMessage) rawMessages[i];
            }

            byte[] receiveDataPayload = messages[0].getRecords()[0].getPayload();

            try {
                receiveData = (SharkNetPublicKey) byteToObj(receiveDataPayload);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

            Toast.makeText(this.context, "beam successful", Toast.LENGTH_LONG).show();
            showPublicKeyAlert(receiveData);
        }
    }

    private void showPublicKeyAlert(SharkNetPublicKey receiveData) {
        new MaterialAlertDialogBuilder(context)
                .setTitle("Authentication")
                .setMessage("Do you really want to save this Key from " + receiveData.getOwner().getAlias())
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        persistPublicKey(receiveData);
                    }
                })
                .setNegativeButton("No", null)
                .setCancelable(false)
                .show();
    }

    private void showCertificateKeyAlert(SharkNetCertificate receiveData) {
        new MaterialAlertDialogBuilder(context)
                .setTitle("Authentication")
                .setMessage("Do you really want to save this Certificate for " + receiveData.getSubject().getAlias() + " signed by " + receiveData.getSigner().getAlias() + "?")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        persistCertificate(receiveData);
                    }
                })
                .setNegativeButton("No", null)
                .setCancelable(false)
                .show();
    }

    private void persistPublicKey(SharkNetPublicKey receivedData) {

        HashSet<SharkNetPublicKey> keySet = SharkNetPKI.getInstance().getSharkNetPublicKeys();
        HashSet<SharkNetPublicKey> newKeyList = dataStorage.getKeySet();
        if (!keySet.contains(receivedData)) {

            SharkNetPKI.getInstance().addPublicKey(receivedData);
            newKeyList.add(receivedData);
            dataStorage.addKeySet(SharkNetPKI.getInstance().getSharkNetPublicKeys());
            nfcCallback.onPublicKeyReceived();
        }
    }

    private void persistCertificate(SharkNetCertificate receivedData) {

        HashSet<SharkNetCertificate> certSet = SharkNetPKI.getInstance().getSharkNetCertificates();
        HashSet<SharkNetCertificate> newCertList = dataStorage.getCertificateSet();
        if (!certSet.contains(receivedData)) {

            SharkNetPKI.getInstance().addCertificate(receivedData);
            newCertList.add(receivedData);
            dataStorage.addCertificateSet(SharkNetPKI.getInstance().getSharkNetCertificates());
            nfcCallback.onCertificateReceived();
        }
    }
}
