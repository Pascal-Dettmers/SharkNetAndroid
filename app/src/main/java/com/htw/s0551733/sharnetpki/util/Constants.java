package com.htw.s0551733.sharnetpki.util;

public class Constants {

    private Constants() {
        throw new AssertionError("Should not be constructed");
    }

    public static final String KEY_ALIAS_USER = "keyAliasUser";
    public static final String UUID_USER = "uuidUser";
    public static final String MY_OWN_SHARKNET_KEY = "nyOwnSharkNetKey";
    public static final String FILENAME = "SharkNet2PreferenceFilename";
    public static final String KEY_LIST = "allReceivedKeys";
    public static final String CERTIFICATE_LIST = "allReceivedCertificates";
    public static final String KEYSTORE_PW = "keystorePW";
    public static final String KEY_DURATION_YEARS = "keyDurationYears";
    public static final String IDENTITY_AVATAR_URI = "identityAvatarUri";
    public static final int DEFAULT_KEY_DURATION_YEARS = 1;
    public static final String ENCYPTED_KEYSTORE_PASSWORD = "encryptedPassword";
    public static final String PUBLIC_KEY_INTENT_FILTER = "application/net.sharksystem.send.public.key";
    public static final String RECEIVED_PUBLIC_KEY_CERT_INTENT_FILTER = "application/net.sharksystem.send.certification";




}

