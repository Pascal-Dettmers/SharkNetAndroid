package com.htw.s0551733.sharnetpki.storage;


public interface SharkIdentityStorage {
    CharSequence getOwnerID();
    CharSequence getOwnerName();
    void setOwnerName(CharSequence name);
    void setOwnerID(CharSequence ownerID);

}
