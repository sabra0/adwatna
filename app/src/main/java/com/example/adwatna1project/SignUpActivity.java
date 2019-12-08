package com.example.adwatna1project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    RadioGroup radioGroup;
    EditText editText;
    Button button ;
    RadioButton btn1,btn2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        button=findViewById(R.id.back2_btn);
        editText= findViewById(R.id.gender);
        radioGroup=findViewById(R.id.gender_layout);


        //to prevent user from writing in gender EditText
        editText.setFocusable(false);

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioGroup.setVisibility(View.VISIBLE);
            }
        });

        // to make male and female button disappear after choosing one
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.btn1||checkedId==R.id.btn2){

                    radioGroup.setVisibility(View.GONE);
                }
            }
        });

        // button to get back
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SignUpActivity.this,Welcome1Activity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
