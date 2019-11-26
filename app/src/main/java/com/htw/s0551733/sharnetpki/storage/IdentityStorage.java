package com.htw.s0551733.sharnetpki.storage;

import android.content.Context;

import com.htw.s0551733.sharnetpki.util.Constants;

public class IdentityStorage implements SharkIdentityStorage {

    private static IdentityStorage instance;
    private Context currentContext;
    private SharedPreferencesHandler sharedPreferencesHandler;

    private IdentityStorage(Context ctx) {
        this.currentContext = ctx;
        this.sharedPreferencesHandler = new SharedPreferencesHandler(this.currentContext);
    }

    public static SharkIdentityStorage getIdentityStorage(Context ctx) {
        if (IdentityStorage.instance == null) {
            IdentityStorage.instance = new IdentityStorage(ctx);
        }
        return IdentityStorage.instance;
    }

    @Override
    public void setAlias(CharSequence userName) {
        sharedPreferencesHandler.writeValue(Constants.KEY_ALIAS_USER, userName.toString());
    }

    @Override
    public void setOwnerID(CharSequence ownerID) {
        sharedPreferencesHandler.writeValue(Constants.UUID_USER, ownerID.toString());
    }

    @Override
    public CharSequence getOwnerID() {
        return sharedPreferencesHandler.getValue(Constants.UUID_USER);
    }

    @Override
    public CharSequence getAlias() {
        return sharedPreferencesHandler.getValue(Constants.KEY_ALIAS_USER);
    }
}
