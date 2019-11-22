package com.htw.s0551733.sharnetpki.recyclerViews;

import android.util.Base64;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.io.Serializable;
import java.security.PublicKey;
import java.util.Date;

import main.de.htw.berlin.s0551733.sharknetpki.SharknetPublicKey;

import static com.htw.s0551733.sharnetpki.util.SerializationHelper.byteToObj;

public class SharkNetKey implements Serializable, Comparable<SharkNetKey>, SharknetPublicKey {

    @SerializedName("alias")
    private String alias;

    @SerializedName("uuid")
    private String uuid;

    @SerializedName("publicKeyInBase64")
    private String publicKeyInBase64;

    @SerializedName("publicKeyInBase64")
    private Date validityPeriod;

    public SharkNetKey(String alias, String uuid, String publicKeyInBase64, Date validityPeriod) {
        this.alias = alias;
        this.uuid = uuid;
        this.publicKeyInBase64 = publicKeyInBase64;
        this.validityPeriod = validityPeriod;
    }

    public String getAlias() {
        return alias;
    }

    public String getUuid() {
        return uuid;
    }

    @Override
    public PublicKey getPublicKey() {
        PublicKey publicKey = null;
        try {
            publicKey = (PublicKey) byteToObj(Base64.decode(this.publicKeyInBase64, Base64.DEFAULT));
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return publicKey;
    }

    @Override
    public Date getValidityPeriod() {
        return this.validityPeriod;
    }

    @Override
    public int compareTo(@NonNull SharkNetKey o) {
        return this.uuid.compareTo(o.uuid);
    }


    @Override
    public boolean equals(Object obj) {
        boolean result = false;

        if (obj instanceof SharkNetKey) {
            SharkNetKey pojo = (SharkNetKey) obj;
            result = pojo.uuid.equals(this.uuid);
        }
        return result;
    }
}
