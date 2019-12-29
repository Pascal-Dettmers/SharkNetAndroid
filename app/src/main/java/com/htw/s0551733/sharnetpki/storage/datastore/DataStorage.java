package com.htw.s0551733.sharnetpki.storage.datastore;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.htw.s0551733.sharnetpki.recyclerViews.SharkNetKey;
import com.htw.s0551733.sharnetpki.util.Constants;
import com.htw.s0551733.sharnetpki.util.SharedPreferencesHandler;

import java.lang.reflect.Type;
import java.util.HashSet;

import main.de.htw.berlin.s0551733.sharknetpki.interfaces.SharkNetPublicKey;


public class DataStorage {

    private SharedPreferencesHandler sharedPreferencesHandler;
    private Gson gson;
    public final String TAG = this.getClass().getSimpleName();

    public DataStorage(SharedPreferencesHandler sharedPreferencesHandler) {
        this.sharedPreferencesHandler = sharedPreferencesHandler;
        this.gson = new Gson();
    }

    public HashSet<SharkNetPublicKey> getKeySet() {
        Type keyListType = new TypeToken<HashSet<SharkNetPublicKey>>() {
        }.getType();

        String keyHashSetJson = sharedPreferencesHandler.getValue(Constants.KEY_LIST);
        HashSet<SharkNetPublicKey> keySet = gson.fromJson(keyHashSetJson, keyListType);
        return keySet;
    }

    public void addKeySet(HashSet<SharkNetPublicKey> keySet) {
        String newKeyHashSetJson = gson.toJson(keySet);
        sharedPreferencesHandler.writeValue(Constants.KEY_LIST, newKeyHashSetJson);
    }

    public SharkNetKey getMyOwnPublicKey() {
        Type type = new TypeToken<SharkNetKey>() {
        }.getType();

        if(sharedPreferencesHandler.getValue(Constants.MY_OWN_SHARKNET_KEY)!=null) {
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

    public String getKeystorePassword() {
        return sharedPreferencesHandler.getValue(Constants.ENCYPTED_KEYSTORE_PASSWORD);
    }

    public void addKeyStorePassword(String password) {
        sharedPreferencesHandler.writeValue(Constants.ENCYPTED_KEYSTORE_PASSWORD, password);
    }
}
