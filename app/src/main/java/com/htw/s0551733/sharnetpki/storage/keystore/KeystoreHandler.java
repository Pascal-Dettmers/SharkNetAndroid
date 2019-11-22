package com.htw.s0551733.sharnetpki.storage.keystore;

public interface KeystoreHandler {

    byte[] encrypt(byte[] toEncrypt);

    byte[] decrypt(byte[] toDecrypt);

}
