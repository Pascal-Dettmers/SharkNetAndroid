package com.htw.s0551733.sharnetpki.storage.database.dataModel;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import main.de.htw.berlin.s0551733.sharknetpki.interfaces.SharkNetCertificate;

@Dao
public interface CertificationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCertification(SharkNetCertificate certification);

    @Delete
    void deleteCertification(SharkNetCertificate certification);

    @Query("SELECT * from SharkNetCert ORDER BY aliasPublicKeyOwner")
    LiveData<List<SharkNetCertificate>> getAllCertificationsLiveData();

    @Query("SELECT * from SharkNetCert ORDER BY aliasPublicKeyOwner")
    List<SharkNetCertificate> getAllCertifications();

}

