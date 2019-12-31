package com.htw.s0551733.sharnetpki;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.htw.s0551733.sharnetpki.recyclerViews.SharkNetKey;
import com.htw.s0551733.sharnetpki.recyclerViews.SharkNetUser;

import org.junit.Test;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

import main.de.htw.berlin.s0551733.sharknetpki.interfaces.SharkNetPublicKey;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    public final String TAG = "EXAMPLE_JUNI_TEST";

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
    @Test
    public void gsonInterfaceAdapter() {

        SharkNetPublicKey sharkNetPublicKeyA = new SharkNetKey(new SharkNetUser("123","test"),"abc",new Date());
        SharkNetPublicKey sharkNetPublicKeyB = new SharkNetKey(new SharkNetUser("123","test"),"abc",new Date());

        HashSet<SharkNetPublicKey> set = new HashSet<>();
        set.add(sharkNetPublicKeyA);
        set.add(sharkNetPublicKeyB);

        Gson gson = new Gson();
        String newKeyHashSetJson = gson.toJson(set);

        Log.d(TAG, "gson: "+newKeyHashSetJson);

        //Create our gson instance
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(SharkNetPublicKey.class, new InterfaceAdapter());
        Gson gsonWithBuilder = builder.create();

        SharkNetPublicKey[] sharkNetPublicKeys = gsonWithBuilder.fromJson(newKeyHashSetJson, SharkNetPublicKey[].class);
        HashSet<SharkNetPublicKey> keySet = new HashSet<>(Arrays. asList(sharkNetPublicKeys));

        Log.d(TAG, "gson: "+keySet);

    }

    @Test
    public void gson() {

        Type keyListType = new TypeToken<HashSet<SharkNetKey>>() {
        }.getType();

        SharkNetPublicKey sharkNetPublicKeyA = new SharkNetKey(new SharkNetUser("123","test"),"abc",new Date());
        SharkNetPublicKey sharkNetPublicKeyB = new SharkNetKey(new SharkNetUser("123","test"),"abc",new Date());

        HashSet<SharkNetPublicKey> set = new HashSet<>();
        set.add(sharkNetPublicKeyA);
        set.add(sharkNetPublicKeyB);

        Gson gson = new Gson();
        String newKeyHashSetJson = gson.toJson(set);
        Log.d(TAG, "newKeyHashSetJson: "+newKeyHashSetJson);

        HashSet<SharkNetKey> keySet = gson.fromJson(newKeyHashSetJson, keyListType);
        Log.d(TAG, "newKeyHashSetJson: "+newKeyHashSetJson);

    }

    @Test
    public void gsonWithConverting() {

        Type keyListType = new TypeToken<HashSet<SharkNetKey>>() {
        }.getType();

        SharkNetPublicKey sharkNetPublicKeyA = new SharkNetKey(new SharkNetUser("123","test"),"abc",new Date());
        SharkNetPublicKey sharkNetPublicKeyB = new SharkNetKey(new SharkNetUser("123","test"),"abc",new Date());

        HashSet<SharkNetPublicKey> set = new HashSet<>();
        set.add(sharkNetPublicKeyA);
        set.add(sharkNetPublicKeyB);

        Gson gson = new Gson();
        String newKeyHashSetJson = gson.toJson(set);
        Log.d(TAG, "newKeyHashSetJson: "+newKeyHashSetJson);

        HashSet<SharkNetKey> keySet = gson.fromJson(newKeyHashSetJson, keyListType);
        Log.d(TAG, "newKeyHashSetJson: "+newKeyHashSetJson);

        // Converting
        HashSet<SharkNetPublicKey> converting = new HashSet<>();
        keySet.forEach((sharkNetKey -> converting.add(sharkNetKey)));



    }
}

