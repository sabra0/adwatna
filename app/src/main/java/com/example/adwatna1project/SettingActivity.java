package com.example.adwatna1project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivity extends AppCompatActivity {

    TextView userNameTextView , emailTextView  , collegeTextView ,backTo;
    ImageView nameEdit , emailEdit , collegeEdit;
    CircleImageView profileImage;

    DatabaseReference userRef;
    String currentUserId;
    FirebaseAuth auth;
    StorageReference userProfileImageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        backTo = findViewById(R.id.back1);
        userNameTextView = findViewById(R.id.name_edit_text);
        emailTextView = findViewById(R.id.email_edit_text);
        collegeTextView = findViewById(R.id.college_edit_text);
        nameEdit = findViewById(R.id.edit_icon1);
        emailEdit = findViewById(R.id.edit_icon2);
        collegeEdit = findViewById(R.id.edit_icon3);
        profileImage = findViewById(R.id.set_profile_image);

        auth = FirebaseAuth.getInstance();
        currentUserId = auth.getCurrentUser().getUid();

        userRef = FirebaseDatabase.getInstance().getReference();
        userProfileImageRef = FirebaseStorage.getInstance().getReference().child("ProfileImages");

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectAndCropImage();
            }
        });

        backTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this,ProfileActivity.class));
            }
        });

        nameEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this, R.style.AlerDialog);
                View view = LayoutInflater.from(SettingActivity.this)
                        .inflate(R.layout.edit_username_dialog, (ViewGroup) findViewById(R.id.dialog_layout_container));
                builder.setView(view);
                final AlertDialog alertDialog = builder.create();
                final EditText userNameEditText = view.findViewById(R.id.edit_username);
                view.findViewById(R.id.update_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String username = userNameEditText.getText().toString();
                        userRef.child("Users").child(currentUserId).child("Name").setValue(username)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(SettingActivity.this, "Saved", Toast.LENGTH_SHORT)
                                                    .show();
                                        }
                                        else {
                                            Toast.makeText(SettingActivity.this, "Failed", Toast.LENGTH_SHORT)
                                                    .show();
                                        }
                                    }
                                });

                    }
                });
                view.findViewById(R.id.cancel_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                if (alertDialog.getWindow() != null) {
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                alertDialog.show();
            }
        });

        emailEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this, R.style.AlerDialog);
                View view = LayoutInflater.from(SettingActivity.this)
                        .inflate(R.layout.edit_email_dialog, (ViewGroup) findViewById(R.id.dialog_layout_container));
                builder.setView(view);
                final AlertDialog alertDialog = builder.create();
                final EditText emailEditText = view.findViewById(R.id.edit_email);
                view.findViewById(R.id.update_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String email = emailEditText.getText().toString();
                        userRef.child("Users").child(currentUserId).child("Email").setValue(email)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(SettingActivity.this, "Saved", Toast.LENGTH_SHORT)
                                                    .show();
                                        }
                                        else {
                                            Toast.makeText(SettingActivity.this, "Failed", Toast.LENGTH_SHORT)
                                                    .show();
                                        }
                                    }
                                });

                    }
                });
                view.findViewById(R.id.cancel_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                if (alertDialog.getWindow() != null) {
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                alertDialog.show();
            }
        });

        collegeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this, R.style.AlerDialog);
                View view = LayoutInflater.from(SettingActivity.this)
                        .inflate(R.layout.edit_college_dialog, (ViewGroup) findViewById(R.id.dialog_layout_container));
                builder.setView(view);
                final AlertDialog alertDialog = builder.create();
                final EditText collageEditText = view.findViewById(R.id.edit_college);
                view.findViewById(R.id.update_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String collage = collageEditText.getText().toString();
                        userRef.child("Users").child(currentUserId).child("Collage").setValue(collage)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(SettingActivity.this, "Saved", Toast.LENGTH_SHORT)
                                                    .show();
                                        }
                                        else {
                                            Toast.makeText(SettingActivity.this, "Failed", Toast.LENGTH_SHORT)
                                                    .show();
                                        }
                                    }
                                });

                    }
                });
                view.findViewById(R.id.cancel_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                if (alertDialog.getWindow() != null) {
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                alertDialog.show();
            }
        });

        retrieveUserInfo();
    }

    //retrieve user information from database
    private void retrieveUserInfo(){
        userRef.child("Users").child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String name = dataSnapshot.child("Name").getValue().toString();
                    String email = dataSnapshot.child("Email").getValue().toString();
                    String college = dataSnapshot.child("Collage").getValue().toString();
                    if (dataSnapshot.hasChild("image")){
                    String retrieveProfileImage = dataSnapshot.child("image").getValue().toString();
                    Picasso.get().load(retrieveProfileImage).into(profileImage);
                    }
                    userNameTextView.setText(name);
                    emailTextView.setText(email);
                    collegeTextView.setText(college);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
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
                final StorageReference filePath = userProfileImageRef.child(currentUserId + ".jpg");
                filePath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                final String downloadUrl = uri.toString();
                                userRef.child("Users").child(currentUserId).child("image").setValue(downloadUrl)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(SettingActivity.this, "Profile image stored to firebase database successfully.", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    String message = task.getException().getMessage();
                                                    Toast.makeText(SettingActivity.this, "Error Occurred..." + message, Toast.LENGTH_SHORT).show();
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
