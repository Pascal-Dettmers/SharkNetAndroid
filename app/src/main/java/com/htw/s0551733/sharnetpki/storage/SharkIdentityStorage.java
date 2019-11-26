package com.htw.s0551733.sharnetpki.storage;


public interface SharkIdentityStorage {
    CharSequence getOwnerID();
    CharSequence getAlias();
    void setAlias(CharSequence name);
    void setOwnerID(CharSequence ownerID);

}
