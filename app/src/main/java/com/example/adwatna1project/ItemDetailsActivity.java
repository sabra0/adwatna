package com.example.adwatna1project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemDetailsActivity extends AppCompatActivity {

    TextView titleTextView , descTextView , priceTextView ,backTo;
    ImageView itemImageView;

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

        byte[] bytes = getIntent().getByteArrayExtra("image");
        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        String price = getIntent().getStringExtra("price");
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);

        titleTextView.setText(title);
        itemImageView.setImageBitmap(bitmap);
        descTextView.setText(description);
        priceTextView.setText(price);
    }
}
