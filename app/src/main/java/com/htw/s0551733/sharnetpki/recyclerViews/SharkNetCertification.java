package com.htw.s0551733.sharnetpki.recyclerViews;

import android.util.Base64;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.io.Serializable;
import java.security.cert.Certificate;
import java.util.Objects;

import main.de.htw.berlin.s0551733.sharknetpki.SharknetCertificate;
import main.de.htw.berlin.s0551733.sharknetpki.User;

import static com.htw.s0551733.sharnetpki.util.SerializationHelper.byteToObj;

public class SharkNetCertification implements Serializable, Comparable<SharkNetCertification>, SharknetCertificate {

    @SerializedName("alias")
    private String alias;

    @SerializedName("uuid")
    private String uuid;

    @SerializedName("certInBase64")
    private String certInBase64;

    @SerializedName("signer")
    private User signer;

    public SharkNetCertification(String alias, String uuid, String certInBase64, User signer) {
        this.alias = alias;
        this.uuid = uuid;
        this.certInBase64 = certInBase64;
        this.signer = signer;
    }

    public String getAlias() {
        return alias;
    }

    public String getUuid() {
        return uuid;
    }

    @Override
    public Certificate getCertificate() {
        Certificate certificate = null;
        try {
            certificate = (Certificate) byteToObj(Base64.decode(certInBase64, Base64.DEFAULT));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return certificate;
    }

    public String getCertInBase64() {
        return certInBase64;
    }

    public User getSigner() {
        return (User) signer;
    }

    @Override
    public int compareTo(@NonNull SharkNetCertification o) {
        return this.uuid.compareTo(o.uuid);
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;

        if (obj instanceof SharkNetCertification) {
            SharkNetCertification pojo = (SharkNetCertification) obj;
            result = pojo.uuid.equals(this.uuid);
        }
        return result;
    }

    public int hashCode() {
        return Objects.hash(getUuid());
    }

}
