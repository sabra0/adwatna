package com.example.adwatna1project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ItemDetailsActivity extends AppCompatActivity {

    TextView titleTextView , descTextView , priceTextView ,backTo,ownerNameTextView;
    ImageView itemImageView,chatIcon;
    CircleImageView profileImage;

    DatabaseReference userRef;

    String ownerId;
    String name;
    String retrieveProfileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        backTo = findViewById(R.id.back1);

        backTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ItemDetailsActivity.this,WelcomePage.class));
                finish();
            }
        });

        titleTextView = findViewById(R.id.item_title_text_view);
        descTextView = findViewById(R.id.item_desc_text_view);
        priceTextView = findViewById(R.id.item_price_text_view);
        itemImageView = findViewById(R.id.item_image_view);
        profileImage = findViewById(R.id.profile_image);
        ownerNameTextView = findViewById(R.id.username_text_view);
        chatIcon = findViewById(R.id.chat_icon);

        userRef = FirebaseDatabase.getInstance().getReference();

        byte[] bytes = getIntent().getByteArrayExtra("image");
        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        String price = getIntent().getStringExtra("price");
        ownerId = getIntent().getStringExtra("ownerID");
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);


        titleTextView.setText(title);
        itemImageView.setImageBitmap(bitmap);
        descTextView.setText(description);
        priceTextView.setText(price);

        retrieveUserInfo();
    }

    private void retrieveUserInfo() {
        userRef.child("Users").child(ownerId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name = dataSnapshot.child("Name").getValue().toString();
                if (dataSnapshot.hasChild("image")){
                    retrieveProfileImage = dataSnapshot.child("image").getValue().toString();
                    Picasso.get().load(retrieveProfileImage).into(profileImage);
                }
                ownerNameTextView.setText(name);

                chatIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(),ChatActivity.class);
                        intent.putExtra("ownerId",ownerId);
                        intent.putExtra("ownerName",name);
                        intent.putExtra("ownerImage",retrieveProfileImage);
                        startActivity(intent);
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
