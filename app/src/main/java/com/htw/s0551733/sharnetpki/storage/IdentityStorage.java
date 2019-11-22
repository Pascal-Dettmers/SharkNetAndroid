package com.htw.s0551733.sharnetpki.storage;

import android.content.Context;

import com.htw.s0551733.sharnetpki.util.Constants;

// TODO do i need dis? and Rename Owner or User?
public class IdentityStorage implements SharkIdentityStorage {
    private static IdentityStorage instance;

    private CharSequence ownerName;
    private CharSequence ownerID;

    private Context currentContext;
    private SharedPreferencesHandler sharedPreferencesHandler;



    public IdentityStorage(Context ctx) {
        this.currentContext = ctx;
        this.sharedPreferencesHandler = new SharedPreferencesHandler(this.currentContext);

        if(sharedPreferencesHandler.getValue(Constants.KEY_ALIAS_USER)!= null) {
            this.ownerName = sharedPreferencesHandler.getValue(Constants.KEY_ALIAS_USER);
        }
        if(sharedPreferencesHandler.getValue(Constants.UUID_USER)!= null) {
            this.ownerID = sharedPreferencesHandler.getValue(Constants.UUID_USER);
        }

    }

    public static SharkIdentityStorage getIdentityStorage(Context ctx) {
        if(IdentityStorage.instance == null) {
            IdentityStorage.instance = new IdentityStorage(ctx);
        }

        IdentityStorage.instance.setCurrentContext(ctx);

        return IdentityStorage.instance;
    }


    private void setCurrentContext(Context ctx) {
        this.currentContext = ctx;
    }

    @Override
    public void setOwnerName(CharSequence userName) {
        this.ownerName = userName;

        sharedPreferencesHandler.writeValue(Constants.KEY_ALIAS_USER, userName.toString());
    }

    @Override
    public void setOwnerID(CharSequence ownerID) {
        this.ownerID = ownerID;

        sharedPreferencesHandler.writeValue(Constants.UUID_USER, ownerID.toString());
    }

    @Override
    public CharSequence getOwnerID() {
//        return this.ownerID;
        return sharedPreferencesHandler.getValue(Constants.UUID_USER);
    }

    @Override
    public CharSequence getOwnerName() {
//        return this.ownerName;
        return sharedPreferencesHandler.getValue(Constants.KEY_ALIAS_USER);

    }
}
