package com.example.adwatna1project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.lang.UProperty;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

public class UploadItemActivity extends AppCompatActivity {

    EditText titleEditText , descEditText , priceEditText;
    ImageView itemImageView;
    Button uploadBtn;
    TextView backTo;

    String currentItemId;
    FirebaseAuth mAuth;
    DatabaseReference itemRef;
    StorageReference itemImageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uplaod_item);

        backTo = findViewById(R.id.back1);
        titleEditText = findViewById(R.id.item_title_edit_text);
        descEditText = findViewById(R.id.item_desc_edit_text);
        priceEditText = findViewById(R.id.item_price_edit_text);
        itemImageView = findViewById(R.id.item_image_view);
        uploadBtn = findViewById(R.id.upload_btn);

        backTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UploadItemActivity.this,WelcomePage.class));
            }
        });

        mAuth = FirebaseAuth.getInstance();
        itemRef = FirebaseDatabase.getInstance().getReference();
        itemImageRef = FirebaseStorage.getInstance().getReference().child("Items Images");

        currentItemId = itemRef.push().getKey();

        itemImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAndCropImage();
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemTitle = titleEditText.getText().toString().trim();
                String itemDesc = descEditText.getText().toString().trim();
                String itemPrice = priceEditText.getText().toString().trim();

                if (TextUtils.isEmpty(itemTitle)) {
                    titleEditText.setError("Please Enter Item Name");
                    Toast.makeText(UploadItemActivity.this, "Please Enter Item Name", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(itemDesc)) {
                    descEditText.setError("Please Enter Item Description");
                    Toast.makeText(UploadItemActivity.this, "Please Enter Item Description", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(itemPrice)) {
                    priceEditText.setError("Please Enter Item Price");
                    Toast.makeText(UploadItemActivity.this, "Please Enter Item Price", Toast.LENGTH_LONG).show();
                    return;
                }
                else {
                    HashMap<String, Object> profileMap = new HashMap<>();
                    profileMap.put("uid", currentItemId);
                    profileMap.put("title", itemTitle);
                    profileMap.put("description", itemDesc);
                    profileMap.put("price", itemPrice);
                    itemRef.child("Data").child(currentItemId).updateChildren(profileMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(UploadItemActivity.this, "Item Updated Successfully", Toast.LENGTH_LONG).show();
                                    } else {
                                        String message = task.getException().toString();
                                        Toast.makeText(UploadItemActivity.this, "Error: " + message, Toast.LENGTH_LONG).show();
                                    }

                                }
                            });
                }

            }
        });

    }

    //crop image and save uri in storage and database functions
    private void selectAndCropImage() {
        // start picker to get image for cropping and then use the image in cropping activity
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                final Uri resultUri = result.getUri();
                final StorageReference filePath = itemImageRef.child(currentItemId + ".jpg");
                filePath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                final String downloadUrl = uri.toString();
                                Picasso.get().load(downloadUrl).into(itemImageView);
                                itemRef.child("Data").child(currentItemId).child("image").setValue(downloadUrl)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(UploadItemActivity.this, "Item image stored to firebase database successfully.", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    String message = task.getException().getMessage();
                                                    Toast.makeText(UploadItemActivity.this, "Error Occurred..." + message, Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        });
                    }


                });
            }
        }
    }

}

