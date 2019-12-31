package com.htw.s0551733.sharnetpki.storage.database.dataModel;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import main.de.htw.berlin.s0551733.sharknetpki.interfaces.SharkNetPublicKey;

@Dao
public interface PublicKeyDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPublicKey(SharkNetPublicKey sharkNetPublicKey);

    @Delete()
    void deletePublicKey(SharkNetPublicKey publicKeyPackage);

    @Query("SELECT * from publickey_table ORDER BY publicKeyOwnerAlias")
    LiveData<List<SharkNetPublicKey>> getAllPublicKeysLiveData();

    @Query("SELECT * from publickey_table ORDER BY publicKeyOwnerAlias")
    List<SharkNetPublicKey> getAllPublicKeys();

}

