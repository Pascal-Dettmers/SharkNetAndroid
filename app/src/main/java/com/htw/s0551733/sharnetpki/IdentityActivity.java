package com.htw.s0551733.sharnetpki;

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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.htw.s0551733.sharnetpki.recyclerViews.SharkNetKey;
import com.htw.s0551733.sharnetpki.storage.datastore.DataStorage;
import com.htw.s0551733.sharnetpki.util.SharedPreferencesHandler;

import java.io.IOException;
import java.text.SimpleDateFormat;

import main.de.htw.berlin.s0551733.sharknetpki.interfaces.SharkNetPublicKey;

public class IdentityActivity extends AppCompatActivity {

    private TextView tvAlias;
    private TextView tvPublicKey;
    private TextView tvUuid;
    private ImageButton ibEditAlias;
    private ImageButton ibCloseActivity;
    private TextView tvExparationDate;
    private ImageView ivAvatar;
    private DataStorage storage;
    private Uri selectedImage;
    private SharkNetKey myOwnPublicKey;

    /**
     * Intent Request Code
     */
    private static final int GALLERY_REQUEST_CODE = 42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identity);

        storage = new DataStorage(new SharedPreferencesHandler(this));
        this.myOwnPublicKey = storage.getMyOwnPublicKey();
        setUpViews();
    }

    private void setUpViews() {

        setAlias();

        this.tvUuid = findViewById(R.id.tv_uuid);
        this.tvUuid.setText(myOwnPublicKey.getOwner().getUuid());

        this.tvPublicKey = findViewById(R.id.tv_public_key);
        String publicKeyEncodedToString = Base64.encodeToString(myOwnPublicKey.getPublicKey().getEncoded(), Base64.DEFAULT);
        tvPublicKey.setText(publicKeyEncodedToString.substring(44));


        this.ibEditAlias = findViewById(R.id.imageButton_edit_alias);
        ibEditAlias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeAliasDialog();
            }
        });

        this.tvExparationDate = findViewById(R.id.tv_expiration_date);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        this.tvExparationDate.setText(format.format(myOwnPublicKey.getExpirationDate()));

        this.ibCloseActivity = findViewById(R.id.imageButton_close);
        this.ibCloseActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Todo Save URI
        this.ivAvatar = findViewById(R.id.imageView_avatar);
        ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickFromGallery();
            }
        });
    }

    private void setAlias() {
        this.tvAlias = findViewById(R.id.tv_alias_identity_activity);
        this.tvAlias.setText(myOwnPublicKey.getOwner().getAlias());
    }


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
                        changeAlias(aliasiInput.getText().toString());
                    }
                })
                .setNegativeButton("No", null)
                .setCancelable(false)
                .show();
    }

    private void changeAlias(String newAlias) {
        this.myOwnPublicKey.setAlias(newAlias);
        storage.addMyOwnPublicKey(this.myOwnPublicKey);
        this.tvAlias.setText(myOwnPublicKey.getOwner().getAlias());
    }

    private void pickFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case GALLERY_REQUEST_CODE:
                    selectedImage = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                        ivAvatar.setImageBitmap(bitmap);
                        SharedPreferencesHandler sharedPreferencesHandler = new SharedPreferencesHandler(this.getApplicationContext());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }


    }
}
