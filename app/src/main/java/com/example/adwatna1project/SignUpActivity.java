package com.example.adwatna1project;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {
    RadioGroup radioGroup;
    EditText editText;
    TextView back;
    RadioButton btn1,btn2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        back =findViewById(R.id.back2);
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

//if user clicked on already checked radio back
        View.OnTouchListener radioButtonOnTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (((RadioButton) v).isChecked()) {
                    // If the back was already checked, uncheck them all
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

        // to make male and female back disappear after choosing one
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

        // back to get back
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}