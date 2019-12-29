package com.htw.s0551733.sharnetpki.storage.keystore;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;

import com.htw.s0551733.sharnetpki.recyclerViews.SharkNetUser;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.spec.MGF1ParameterSpec;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;

import main.de.htw.berlin.s0551733.sharknetpki.interfaces.PKI;
import main.de.htw.berlin.s0551733.sharknetpki.interfaces.SharkNetCertificate;
import main.de.htw.berlin.s0551733.sharknetpki.interfaces.SharkNetPublicKey;
import timber.log.Timber;

/**
 * Pure Android Implementation of a SharkNetPKI
 */
//public final class RSAKeystoreHandler implements PKI {
public final class RSAKeystoreHandler  {

    private static RSAKeystoreHandler rsaKeystoreHandler = null;

    public final static int ANY_PURPOSE = KeyProperties.PURPOSE_ENCRYPT |
            KeyProperties.PURPOSE_DECRYPT | KeyProperties.PURPOSE_SIGN |
            KeyProperties.PURPOSE_VERIFY;

    private static final String TAG = "RSAKeystoreHandler";

    private static KeyStore keyStore;

    /**
     * KeySize has to be 2048 according to https://developer.android.com/training/articles/keystore#HardwareSecurityModule
     */
    private static final int KEY_SIZE = 2048;

    private static final int KEY_DURATION_YEARS = 1;

    private final static String KEY_ALIAS = "KeyPair";

    private final static String AndroidKeyStore = "AndroidKeyStore";

    private HashSet<SharkNetPublicKey> sharknetPublicKeys;
    private HashSet<SharkNetCertificate> sharknetCertificates;

    private RSAKeystoreHandler() {
        setupKeystore();
    }

    public static RSAKeystoreHandler getInstance() {
        if (rsaKeystoreHandler == null) {
            rsaKeystoreHandler = new RSAKeystoreHandler();
        }
        return rsaKeystoreHandler;
    }

    public void setupKeystore() {
        try {
            keyStore = KeyStore.getInstance(AndroidKeyStore);
            keyStore.load(null);
            if (checkRSAKeyPairAvailable()) {
                generateRSAKeyPair();
            }
        } catch (Exception e) {
            Log.d(TAG, "setupKeystore: " + e.getMessage());
        }
    }

    private boolean checkRSAKeyPairAvailable() {
        try {
            if (!keyStore.containsAlias(KEY_ALIAS)) {
                return true;
            } else {
                return false;
            }
        } catch (KeyStoreException e) {
            Timber.e(e);
        }
        return false;
    }

    public void resetKeystore() {
        try {
            keyStore.deleteEntry(KEY_ALIAS);
            generateRSAKeyPair();
        } catch (KeyStoreException e) {
            Timber.e(e);
        }
    }

    private void generateRSAKeyPair() {
        try {
            Calendar start = Calendar.getInstance();
            Calendar end = Calendar.getInstance();
            //Jahr von heute plus YEAR Jahre
            end.add(Calendar.YEAR, KEY_DURATION_YEARS);
            // Todo let user decide in days but not more than 1 year
//            end.add(Calendar.DATE, 5);
//
//            final long now = java.lang.System.currentTimeMillis();
//            final long validityDays = 10000L;


            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(
                    KeyProperties.KEY_ALGORITHM_RSA, "AndroidKeyStore");
            keyPairGenerator.initialize(
                    new KeyGenParameterSpec.Builder(KEY_ALIAS, ANY_PURPOSE)
                            .setRandomizedEncryptionRequired(false)
                            .setDigests(
                                    KeyProperties.DIGEST_NONE, KeyProperties.DIGEST_MD5,
                                    KeyProperties.DIGEST_SHA1, KeyProperties.DIGEST_SHA224,
                                    KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA384,
                                    KeyProperties.DIGEST_SHA512)

                            .setSignaturePaddings(KeyProperties.SIGNATURE_PADDING_RSA_PSS)
                            .setEncryptionPaddings(
                                    KeyProperties.ENCRYPTION_PADDING_NONE,
                                    KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1,
                                    KeyProperties.ENCRYPTION_PADDING_RSA_OAEP)
                            .setUserAuthenticationRequired(false)
                            .setKeyValidityStart(start.getTime())
                            .setKeyValidityEnd(end.getTime())
                            .setKeySize(KEY_SIZE)
                            .build());

            keyPairGenerator.generateKeyPair();

        } catch (NoSuchAlgorithmException e) {
            Log.d(TAG, "generateRSAKeyPair: " + e.getMessage());
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            Log.d(TAG, "generateRSAKeyPair: " + e.getMessage());
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
            Log.d(TAG, "generateRSAKeyPair: " + e.getMessage());
        }
    }

    public byte[] decrypt(byte[] toDecrypt) {
        byte[] decryptedBytes = null;

        try {
            PrivateKey privateKey = (PrivateKey) keyStore.getKey(KEY_ALIAS, null);
            OAEPParameterSpec sp = new OAEPParameterSpec("SHA-256", "MGF1", new MGF1ParameterSpec("SHA-1"), PSource.PSpecified.DEFAULT);
            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey, sp);

            decryptedBytes = cipher.doFinal(toDecrypt);

        } catch (Exception e) {
            Log.d(TAG, "decrypt: Exception occured: " + e.getMessage());

            if (e instanceof UnrecoverableKeyException) {
                resetKeystore();
                Log.d(TAG, "Keystore reset due to unrecoverable Key");
            }
        }

        return decryptedBytes;
    }

    public byte[] encrypt(byte[] toEncrypt) {
        byte[] encryptedBytes = null;

        try {

            PublicKey publicKey = keyStore.getCertificate(KEY_ALIAS).getPublicKey();
            OAEPParameterSpec sp = new OAEPParameterSpec("SHA-256", "MGF1", new MGF1ParameterSpec("SHA-1"), PSource.PSpecified.DEFAULT);
            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey, sp);

            encryptedBytes = cipher.doFinal(toEncrypt);

        } catch (Exception e) {
            Timber.d(e, "encrypt: Exception occurred: %s", e.getMessage());
        }
        return encryptedBytes;
    }

    public PublicKey getPublicKey() {
        PublicKey publicKey = null;
        try {
            publicKey = keyStore.getCertificate(KEY_ALIAS).getPublicKey();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return publicKey;
    }

    public Certificate getCertificate() {
        Certificate cert = null;
        try {
            cert = keyStore.getCertificate(KEY_ALIAS);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return cert;
    }

    // Todo https://docs.oracle.com/javase/7/docs/api/java/security/Signature.html
    // Todo https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#Signature
    public byte[] signData(byte[] data) {

        try {

            PrivateKey privateKey = (PrivateKey) keyStore.getKey(KEY_ALIAS, null);
            Signature signature = Signature.getInstance("SHA256withRSA/PSS");
            signature.initSign(privateKey);
            signature.update(data);
            return signature.sign();

        } catch (Exception e) {
            Log.d(TAG, "signData: " + e.getMessage());
        }
        return null;
    }

    public boolean verifySignature(byte[] data, byte[] signedData, Certificate certificate) {
        Signature signature = null;
        boolean valid = false;

        try {

            signature = Signature.getInstance("SHA256withRSA/PSS");
            signature.initVerify(certificate);
            signature.update(data);
            valid = signature.verify(signedData);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            // Todo return valid false
            e.printStackTrace();
        }

        return valid;

    }

//    @Override
//    public List<SharkNetUser> getUsers() {
//        final List<SharkNetUser> users = new ArrayList<>();
//        for (SharkNetPublicKey key : sharknetPublicKeys) {
//            users.add(new SharkNetUser(key.getUuid(), key.getAlias()));
//        }
//        for (SharkNetCertificate cert : sharknetCertificates) {
//            users.add(new SharkNetUser(cert.getUuid(), cert.getAlias()));
//        }
//        return users;
//    }
//
//    @Override
//    public PublicKey getPublicKey(String uuid) {
//        PublicKey wantedKey = null;
//        for (SharkNetPublicKey key : sharknetPublicKeys) {
//            if (key.getUuid().equals(uuid)) {
//                wantedKey = key.getPublicKey();
//                break;
//            }
//        }
//        if (wantedKey == null) {
//            for (SharkNetCertificate cert : sharknetCertificates) {
//                if (cert.getUuid().equals(uuid)) {
//                    wantedKey = cert.getCertificate().getPublicKey();
//                    break;
//                }
//            }
//        }
//        return wantedKey;
//    }
//
//    @Override
//    public HashSet<SharkNetPublicKey> getPublicKeys() {
//        return this.sharknetPublicKeys;
//    }
//
//    @Override
//    public HashSet<SharkNetCertificate> getCertificates() {
//        return this.sharknetCertificates;
//    }
//
//    @Override
//    public Certificate getCertificate(String uuid) {
//        Certificate wantedCertificate = null;
//        for (SharkNetCertificate cert : sharknetCertificates) {
//            if (cert.getUuid().equals(uuid)) {
//                wantedCertificate = cert.getCertificate();
//                break;
//            }
//        }
//        return wantedCertificate;
//    }
//
//    @Override
//    public void addCertificate(SharkNetCertificate sharknetCertificate) {
//        sharknetCertificates.add(sharknetCertificate);
//    }
//
//    @Override
//    public void addPublicKey(SharkNetPublicKey sharknetPublicKey) {
//        this.sharknetPublicKeys.add(sharknetPublicKey);
//    }
//
//    @Override
//    public PublicKey getMyOwnPublicKey() throws KeyStoreException {
//        return getPublicKey();
//    }
//
//    @Override
//    public boolean verifySignature(Certificate certToVerify, PublicKey potentialSignerPublicKey) {
//        boolean result = true;
//        try {
//            certToVerify.verify(potentialSignerPublicKey);
//        } catch (Exception e) {
//            if (e instanceof InvalidKeyException) {
//                System.out.println("wrong Key");
//                return !result;
//            }
//            if (e instanceof SignatureException) {
//                System.out.println("Signature error");
//                return !result;
//            } else {
//                return !result;
//            }
//        }
//        return result;
//    }
//
//    @Override
//    public void removePublicKey(SharkNetPublicKey sharknetPublicKey) {
//        this.sharknetPublicKeys.remove(sharknetPublicKey);
//    }
//
//    @Override
//    public void removeCertificate(SharkNetCertificate sharknetCertificate) {
//        this.sharknetCertificates.remove(sharknetCertificate);
//    }
}
