package com.htw.s0551733.sharnetpki.recyclerViews;

import android.util.Base64;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.io.Serializable;
import java.security.PublicKey;
import java.util.Date;
import java.util.Objects;

import main.de.htw.berlin.s0551733.sharknetpki.interfaces.SharkNetPublicKey;
import main.de.htw.berlin.s0551733.sharknetpki.interfaces.User;

import static com.htw.s0551733.sharnetpki.util.SerializationHelper.byteToObj;

public class SharkNetKey implements Serializable, Comparable<SharkNetKey>, SharkNetPublicKey {

    @SerializedName("user")
    private SharkNetUser user;

    @SerializedName("publicKeyInBase64")
    private String publicKeyInBase64;

    @SerializedName("expirationDate")
    private Date expirationDate;



    public SharkNetKey(SharkNetUser user, String publicKeyInBase64, Date expirationDate) {
        this.user = user;
        this.publicKeyInBase64 = publicKeyInBase64;
        this.expirationDate = expirationDate;
    }

    @Override
    public User getOwner() {
        return this.user;
    }

    @Override
    public void setAlias(String s) {
        this.user.setAlias(s);
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
    public Date getExpirationDate() {
        return this.expirationDate;
    }

    @Override
    public int compareTo(@NonNull SharkNetKey o) {
        return this.user.getUuid().compareTo(o.user.getUuid());
    }


    @Override
    public boolean equals(Object obj) {
        boolean result = false;

        if (obj instanceof SharkNetKey) {
            SharkNetKey pojo = (SharkNetKey) obj;
            result = pojo.user.getUuid().equals(this.user.getUuid());
        }
        return result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.user.getUuid());
    }

    public User getUser() {
        return user;
    }

    public void setUser(SharkNetUser user) {
        this.user = user;
    }

    public String getPublicKeyInBase64() {
        return publicKeyInBase64;
    }

    public void setPublicKeyInBase64(String publicKeyInBase64) {
        this.publicKeyInBase64 = publicKeyInBase64;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }
}
