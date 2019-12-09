package com.example.adwatna1project;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
        btn1=findViewById(R.id.btn1);
        btn2=findViewById(R.id.btn2);

//to prevent user from writing in gender EditText
        editText.setFocusable(false);

//controls what happens on click on gender text
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radioGroup.getVisibility()==View.VISIBLE)
                    radioGroup.setVisibility(View.GONE);//hides radio group(male,female)

                else //if radio group is un visible
                radioGroup.setVisibility(View.VISIBLE);//shows radio group
            }
        });

//if user clicked on already checked radio button
        View.OnTouchListener radioButtonOnTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (((RadioButton) v).isChecked()) {
                    // If the button was already checked, uncheck them all
                    radioGroup.clearCheck();
                    editText.setText(null); // clear gender EditText
                    // Prevent the system from re-checking it
                    return true;
                }
                return false;
            }
        };
        btn1.setOnTouchListener(radioButtonOnTouchListener);
        btn2.setOnTouchListener(radioButtonOnTouchListener);

        // to make male and female button disappear after choosing one
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if(checkedId==R.id.btn1){

                    editText.setText(btn1.getText());
                    radioGroup.setVisibility(View.GONE);
                }
                else if(checkedId==R.id.btn2){

                    editText.setText(btn2.getText());
                    radioGroup.setVisibility(View.GONE);
                }
            }
        });

        // button to get back
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}