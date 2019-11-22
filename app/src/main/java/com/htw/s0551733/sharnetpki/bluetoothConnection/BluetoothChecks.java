package com.htw.s0551733.sharnetpki.bluetoothConnection;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.provider.Settings;
import android.widget.Toast;

/**
 * This class implement all necessary checks for the nfc communication
 */
public class BluetoothChecks {

    public static void preliminaryBluetoothChecks(BluetoothAdapter bluetoothAdapter, Activity activity) {
        isBluetoothSupported(bluetoothAdapter, activity);
        isBluetoothEnabled(bluetoothAdapter, activity);

    }


    private static void isBluetoothSupported(BluetoothAdapter bluetoothAdapter, Activity activity) {
        if (bluetoothAdapter == null) {
            Toast.makeText(activity, "Nfc is not supported on this device", Toast.LENGTH_SHORT).show();
            new AlertDialog.Builder(activity)
                    .setTitle("NFC not supported")
                    .setMessage("NFC is on this device not supported or the chip is damaged. NFC is curcial for this app otherwise y can't use one of the main Feature. This app will close.")
                    .setCancelable(false)
                    .setNegativeButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            activity.finish();
                        }
                    }).create().show();
        }
    }

    private static void isBluetoothEnabled(BluetoothAdapter bluetoothAdapter, Activity activity) {
        if (bluetoothAdapter!=null && !bluetoothAdapter.isEnabled()) {

            new AlertDialog.Builder(activity)
                    .setTitle("Do u want to enable NFC on this device?")
                    .setMessage("NFC disabled on this device. Turn on to proceed.")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
                        activity.startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            activity.finish();
                        }
                    }).create().show();
        }
    }
}
