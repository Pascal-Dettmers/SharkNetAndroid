package com.htw.s0551733.sharnetpki.storage.datastore;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.htw.s0551733.sharnetpki.recyclerViews.SharkNetKey;
import com.htw.s0551733.sharnetpki.util.Constants;
import com.htw.s0551733.sharnetpki.util.SharedPreferencesHandler;

import java.lang.reflect.Type;
import java.util.HashSet;

import main.de.htw.berlin.s0551733.sharknetpki.SharknetPublicKey;

public class DataStorage {

    private SharedPreferencesHandler sharedPreferencesHandler;
    private Gson gson;

    public DataStorage(SharedPreferencesHandler sharedPreferencesHandler) {
        this.sharedPreferencesHandler = sharedPreferencesHandler;
        this.gson = new Gson();
    }

    public HashSet<SharknetPublicKey> getKeySet() {
        Type keyListType = new TypeToken<HashSet<SharkNetKey>>() {
        }.getType();

        String keyHashSetJson = sharedPreferencesHandler.getValue(Constants.KEY_LIST);
        HashSet<SharknetPublicKey> keySet = gson.fromJson(keyHashSetJson, keyListType);
        return keySet;
    }

    public void addKeySet(HashSet<SharknetPublicKey> keySet) {
        String newKeyHashSetJson = gson.toJson(keySet);
        sharedPreferencesHandler.writeValue(Constants.KEY_LIST, newKeyHashSetJson);
    }

    public SharknetPublicKey getMyOwnPublicKey() {
        Type keyListType = new TypeToken<SharkNetKey>() {
        }.getType();

        if(sharedPreferencesHandler.getValue(Constants.MY_OWN_SHARKNET_KEY)!=null) {
            String sharkNetPublicKeyJson = sharedPreferencesHandler.getValue(Constants.MY_OWN_SHARKNET_KEY);
            SharknetPublicKey publicKey = gson.fromJson(sharkNetPublicKeyJson, keyListType);
            return publicKey;
        } else {
            return null;
        }

    }

    public void addMyOwnPublicKey(SharknetPublicKey publicKey) {
        String publicKeyJson = gson.toJson(publicKey);
        sharedPreferencesHandler.writeValue(Constants.MY_OWN_SHARKNET_KEY, publicKeyJson);
    }

    public String getKeystorePassword() {
        return sharedPreferencesHandler.getValue(Constants.ENCYPTED_KEYSTORE_PASSWORD);
    }

    public void addKeyStorePassword(String password) {
        sharedPreferencesHandler.writeValue(Constants.ENCYPTED_KEYSTORE_PASSWORD, password);
    }
}
