package com.htw.s0551733.sharnetpki.storage.Identitystore;

import android.content.Context;

import com.htw.s0551733.sharnetpki.storage.datastore.DataStorage;
import com.htw.s0551733.sharnetpki.util.Constants;
import com.htw.s0551733.sharnetpki.util.SharedPreferencesHandler;

import main.de.htw.berlin.s0551733.sharknetpki.SharknetPublicKey;

public class IdentityStorage implements SharkIdentityStorage {

    private static IdentityStorage instance;
    private Context currentContext;
    private SharknetPublicKey sharknetPublicKey;
    private DataStorage dataStorage;

    private IdentityStorage(Context ctx, SharknetPublicKey sharknetPublicKey) {
        this.currentContext = ctx;
        this.sharknetPublicKey = sharknetPublicKey;
        this.dataStorage = new DataStorage(new SharedPreferencesHandler(currentContext));
    }

    public static SharkIdentityStorage getIdentityStorage(Context ctx, SharknetPublicKey sharknetPublicKey) {
        if (IdentityStorage.instance == null) {
            IdentityStorage.instance = new IdentityStorage(ctx, sharknetPublicKey);
        }
        return IdentityStorage.instance;
    }

    @Override
    public SharknetPublicKey getMyOwnSharkNetKey() {
        return dataStorage.getMyOwnPublicKey();
    }

}
