package com.htw.s0551733.sharnetpki;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.htw.s0551733.sharnetpki.storage.IdentityStorage;
import com.htw.s0551733.sharnetpki.storage.SharedPreferencesHandler;
import com.htw.s0551733.sharnetpki.storage.SharkIdentityStorage;

import java.io.IOException;
import java.security.KeyStoreException;

import main.de.htw.berlin.s0551733.sharknetpki.impl.SharknetPKI;

public class IdentityActivity extends AppCompatActivity {

    private TextView tvAlias;
    private TextView tvPublicKey;
    private TextView tvUuid;
    private ImageButton ibEditAlias;
    private ImageView ivAliasPic;
    private SharkIdentityStorage storage;
    private Uri selectedImage;

    /**
     * Intent Request Code
     */
    private static final int GALLERY_REQUEST_CODE = 42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identity);

        storage = IdentityStorage.getIdentityStorage(this.getApplicationContext());

        setUpViews();
    }

    private void setUpViews() {
        setAlias();

        this.tvUuid =  findViewById(R.id.tv_uuid);
        this.tvUuid.setText(storage.getOwnerID());

        this.tvPublicKey =  findViewById(R.id.tv_public_key);
        try {
            String publicKeyEncodedToString = Base64.encodeToString(SharknetPKI.getInstance().getMyOwnPublicKey().getEncoded(), Base64.DEFAULT);
            tvPublicKey.setText(publicKeyEncodedToString.substring(24));
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }


        this.ibEditAlias = findViewById(R.id.imageButton_edit_alias);
        ibEditAlias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeAliasDialog();
            }
        });

        // Todo Save URI
        this.ivAliasPic = findViewById(R.id.imageView_alias_pic);
        ivAliasPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickFromGallery();
            }
        });
    }

    private void setAlias() {
        this.tvAlias =  findViewById(R.id.tv_alias_identity_activity);
        this.tvAlias.setText(storage.getAlias());
    }


    // Todo Dialog more generic
    public void changeAliasDialog() {
        LayoutInflater inflater = this.getLayoutInflater();
        View viewInflated = inflater.inflate(R.layout.dialog_set_alias, null);
        final EditText aliasiInput = viewInflated.findViewById(R.id.inputAlias);

        new MaterialAlertDialogBuilder(this)
                .setTitle("Alias")
                .setMessage("Change your Alias")
                .setView(viewInflated)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        storage.setAlias(aliasiInput.getText().toString());
                        setAlias();
                    }
                })
                .setNegativeButton("No", null)
                .setCancelable(false)
                .show();
    }

    private void pickFromGallery() {
        //Create an Intent with action as ACTION_PICK
        Intent intent = new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        // Launching the Intent
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case GALLERY_REQUEST_CODE:
                    selectedImage = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                        ivAliasPic.setImageBitmap(bitmap);
                        SharedPreferencesHandler sharedPreferencesHandler = new SharedPreferencesHandler(this.getApplicationContext());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }


    }
}
