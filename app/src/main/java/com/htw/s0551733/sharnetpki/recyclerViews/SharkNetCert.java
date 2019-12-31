package com.htw.s0551733.sharnetpki.recyclerViews;

import android.util.Base64;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;

import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.io.Serializable;
import java.security.cert.Certificate;
import java.util.Objects;

import main.de.htw.berlin.s0551733.sharknetpki.interfaces.User;

import static com.htw.s0551733.sharnetpki.util.SerializationHelper.byteToObj;

@Entity(tableName = "certification_table", indices = @Index(value = {""}, unique = true))
public class SharkNetCert implements Serializable, Comparable<SharkNetCert>, main.de.htw.berlin.s0551733.sharknetpki.interfaces.SharkNetCertificate {

    @NonNull
    @ColumnInfo(name = "subject")
    @SerializedName("subject")
    private SharkNetUser subject;

    @NonNull
    @ColumnInfo(name = "certInBase64")
    @SerializedName("certInBase64")
    private String certInBase64;

    @NonNull
    @ColumnInfo(name = "signer")
    @SerializedName("signer")
    private SharkNetUser signer;


    public SharkNetCert(SharkNetUser subject, String certInBase64, SharkNetUser signer) {
        this.subject = subject;
        this.certInBase64 = certInBase64;
        this.signer = signer;
    }

    @Override
    public User getSubject() {
        return this.subject;
    }

    @Override
    public Certificate getCertificate() {
        Certificate certificate = null;
        try {
            certificate = (Certificate) byteToObj(Base64.decode(certInBase64, Base64.DEFAULT));
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return certificate;
    }

    public String getCertInBase64() {
        return this.certInBase64;
    }

    public User getSigner() {
        return this.signer;
    }

    @Override
    public int compareTo(@NonNull SharkNetCert o) {
        return this.subject.getUuid().compareTo(o.subject.getUuid());
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;

        if (obj instanceof SharkNetCert) {
            SharkNetCert pojo = (SharkNetCert) obj;
            result = pojo.subject.getUuid().equals(this.subject.getUuid());
        }
        return result;
    }

    public int hashCode() {
        return Objects.hash(subject.getUuid());
    }

    public void setSubject(SharkNetUser subject) {
        this.subject = subject;
    }

    public void setCertInBase64(String certInBase64) {
        this.certInBase64 = certInBase64;
    }

    public void setSigner(SharkNetUser signer) {
        this.signer = signer;
    }
}
