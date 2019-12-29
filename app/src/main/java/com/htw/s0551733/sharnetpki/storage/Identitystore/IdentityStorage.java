package com.htw.s0551733.sharnetpki.storage.Identitystore;

import android.content.Context;

import com.htw.s0551733.sharnetpki.storage.datastore.DataStorage;
import com.htw.s0551733.sharnetpki.util.SharedPreferencesHandler;

import main.de.htw.berlin.s0551733.sharknetpki.interfaces.SharkNetPublicKey;

public class IdentityStorage implements SharkIdentityStorage {

    private static IdentityStorage instance;
    private Context currentContext;
    private SharkNetPublicKey sharknetPublicKey;
    private DataStorage dataStorage;

    private IdentityStorage(Context ctx, SharkNetPublicKey sharknetPublicKey) {
        this.currentContext = ctx;
        this.sharknetPublicKey = sharknetPublicKey;
        this.dataStorage = new DataStorage(new SharedPreferencesHandler(currentContext));
    }

    public static SharkIdentityStorage getIdentityStorage(Context ctx, SharkNetPublicKey sharknetPublicKey) {
        if (IdentityStorage.instance == null) {
            IdentityStorage.instance = new IdentityStorage(ctx, sharknetPublicKey);
        }
        return IdentityStorage.instance;
    }

    @Override
    public SharkNetPublicKey getMyOwnSharkNetKey() {
        return dataStorage.getMyOwnPublicKey();
    }

}
