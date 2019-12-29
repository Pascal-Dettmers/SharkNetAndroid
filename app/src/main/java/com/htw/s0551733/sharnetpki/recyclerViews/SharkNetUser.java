package com.htw.s0551733.sharnetpki.recyclerViews;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

import main.de.htw.berlin.s0551733.sharknetpki.interfaces.User;

public class SharkNetUser implements User, Serializable {

    @SerializedName("uuid")
    private final String uuid;

    @SerializedName("alias")
    private String alias;

    public SharkNetUser(String uuid, String alias) {
        this.uuid = uuid;
        this.alias = alias;
    }

    @Override
    public String getUuid() {
        return uuid;
    }

    @Override
    public String getAlias() {
        return alias;
    }

    @Override
    public void setAlias(String newAlias) {
        this.alias = newAlias;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SharkNetUser that = (SharkNetUser) o;
        return Objects.equals(uuid, that.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }



}
