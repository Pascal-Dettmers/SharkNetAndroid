package com.htw.s0551733.sharnetpki.nfc.receive;

public interface NfcCallback {

    void onPublicKeyReceived();
    void onCertificateReceived();
}

