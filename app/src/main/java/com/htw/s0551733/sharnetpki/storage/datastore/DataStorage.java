package com.htw.s0551733.sharnetpki.storage.datastore;

import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.htw.s0551733.sharnetpki.recyclerViews.SharkNetCert;
import com.htw.s0551733.sharnetpki.recyclerViews.SharkNetKey;
import com.htw.s0551733.sharnetpki.storage.keystore.RSAKeystoreHandler;
import com.htw.s0551733.sharnetpki.util.Constants;
import com.htw.s0551733.sharnetpki.util.SharedPreferencesHandler;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.UUID;

import main.de.htw.berlin.s0551733.sharknetpki.interfaces.SharkNetCertificate;
import main.de.htw.berlin.s0551733.sharknetpki.interfaces.SharkNetPublicKey;

import static com.htw.s0551733.sharnetpki.util.SerializationHelper.byteToObj;
import static com.htw.s0551733.sharnetpki.util.SerializationHelper.objToByte;


public class DataStorage {

    private SharedPreferencesHandler sharedPreferencesHandler;
    private Gson gson;
    public final String TAG = this.getClass().getSimpleName();

    public DataStorage(SharedPreferencesHandler sharedPreferencesHandler) {
        this.sharedPreferencesHandler = sharedPreferencesHandler;
        this.gson = new Gson();
    }


    public HashSet<SharkNetPublicKey> getKeySet() {
        Type keyListType = new TypeToken<HashSet<SharkNetKey>>() {
        }.getType();
        HashSet<SharkNetPublicKey> keySet = new HashSet<>();

        if(sharedPreferencesHandler.getValue(Constants.KEY_LIST) != null) {
            // Get Keys in Json
            String keyHashSetJson = sharedPreferencesHandler.getValue(Constants.KEY_LIST);
            // to SharkNetKeyObject
            HashSet<SharkNetKey> keySetTmp = gson.fromJson(keyHashSetJson, keyListType);
            // create HashSet<SharkNetPublic> Interface
            keySetTmp.forEach((sharkNetKey -> keySet.add(sharkNetKey)));
        }
        return keySet;
    }

    public HashSet<SharkNetCertificate> getCertificateSet() {
        Type keyListType = new TypeToken<HashSet<SharkNetCertificate>>() {
        }.getType();
        HashSet<SharkNetCertificate> certSet = new HashSet<>();

        if(sharedPreferencesHandler.getValue(Constants.CERTIFICATE_LIST) != null) {

            // Get Cert in Json
            String certHashSetJson = sharedPreferencesHandler.getValue(Constants.CERTIFICATE_LIST);
            // to SharkNetKeyObject
            HashSet<SharkNetCert> certSetTmp = gson.fromJson(certHashSetJson, keyListType);
            // create HashSet<SharkNetPublic> Interface
            certSetTmp.forEach((sharkNetKey -> certSet.add(sharkNetKey)));
        }
        return certSet;
    }

    public void addKeySet(HashSet<SharkNetPublicKey> keySet) {
        String newKeyHashSetJson = gson.toJson(keySet);
        sharedPreferencesHandler.writeValue(Constants.KEY_LIST, newKeyHashSetJson);
    }

    public void addCertificateSet(HashSet<SharkNetCertificate> certSet) {
        String newCertHashSetJson = gson.toJson(certSet);
        sharedPreferencesHandler.writeValue(Constants.CERTIFICATE_LIST, newCertHashSetJson);
    }

    public void deleteCertificate(SharkNetCertificate sharkNetCert) {
        HashSet<SharkNetCertificate> certificateSet = getCertificateSet();
        certificateSet.remove(sharkNetCert);
        addCertificateSet(certificateSet);
    }

    public void deleteSharkNetKey(SharkNetPublicKey sharkNetKey) {
        HashSet<SharkNetPublicKey> sharkNetPublicKeys = getKeySet();
        sharkNetPublicKeys.remove(sharkNetKey);
        addKeySet(sharkNetPublicKeys);
    }


    public SharkNetKey getMyOwnPublicKey() {
        Type type = new TypeToken<SharkNetKey>() {
        }.getType();

        if (sharedPreferencesHandler.getValue(Constants.MY_OWN_SHARKNET_KEY) != null) {
            String sharkNetPublicKeyJson = sharedPreferencesHandler.getValue(Constants.MY_OWN_SHARKNET_KEY);
            Log.d(TAG, "addMyOwnPublicKey: " + sharkNetPublicKeyJson);

            SharkNetKey publicKey = gson.fromJson(sharkNetPublicKeyJson, type);
            return publicKey;
        } else {
            return null;
        }

    }

    public void addMyOwnPublicKey(SharkNetKey publicKey) {
        String publicKeyJson = gson.toJson(publicKey);
        Log.d(TAG, "addMyOwnPublicKey: " + publicKeyJson);
        sharedPreferencesHandler.writeValue(Constants.MY_OWN_SHARKNET_KEY, publicKeyJson);
    }

    public String getKeystorePassword() throws IOException, ClassNotFoundException {
        String keyStorePasswordCrypt = sharedPreferencesHandler.getValue(Constants.ENCYPTED_KEYSTORE_PASSWORD);
        if(keyStorePasswordCrypt != null) {
            byte[] decodePassword = Base64.decode(keyStorePasswordCrypt, Base64.DEFAULT);
            byte[] decryptPassword = RSAKeystoreHandler.getInstance().decrypt(decodePassword);
            return (String) byteToObj(decryptPassword);
        } else {
            String password = generateUUID(); // generate random string
            byte[] passwordInBytes = objToByte(password);
            byte[] encryptPassword = RSAKeystoreHandler.getInstance().encrypt(passwordInBytes);
            String encryptPasswordInBase64 = Base64.encodeToString(encryptPassword, Base64.DEFAULT); // encode to base64 to store it
            sharedPreferencesHandler.writeValue(Constants.ENCYPTED_KEYSTORE_PASSWORD,encryptPasswordInBase64); // persist password
            return password;
        }

    }

    private String generateUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
