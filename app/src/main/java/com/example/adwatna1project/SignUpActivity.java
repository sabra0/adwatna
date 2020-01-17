package com.example.adwatna1project;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    RadioGroup radioGroup;
    EditText editText;
    TextView back;
    RadioButton btn1,btn2;

    private Firebase mRef;


    //for firebase
    EditText mFullName,mEmail,mPassword,mConfirmPassword,mCollege;
    Button mRegister;
    RadioButton radioMale,radioFemale;
    DatabaseReference databaseReference;
    //FirebaseDatabase firebaseDatabase;
    FirebaseAuth fAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        back =findViewById(R.id.back2);
        editText= findViewById(R.id.gender);
        radioGroup=findViewById(R.id.gender_layout);
        btn1=findViewById(R.id.btn1);
        btn2=findViewById(R.id.btn2);

//fire base reference
        mRef=new Firebase("https://adwatna-adfb8.firebaseio.com/Users");

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
        //firebase code
        mFullName = findViewById(R.id.name_editText);
        mEmail = findViewById(R.id.email_login);
        mPassword = findViewById(R.id.password_login);
        mConfirmPassword = findViewById(R.id.password_confirm);
        mCollege = findViewById(R.id.birthday);
        mRegister = findViewById(R.id.sign_button);
        radioMale = findViewById(R.id.btn1);
        radioFemale = findViewById(R.id.btn2);

        fAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("User");

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = mFullName.getText().toString();
                final String email = mEmail.getText().toString();
                final String password = mPassword.getText().toString();
                final String confirmPassword = mConfirmPassword.getText().toString();
                final String college = mCollege.getText().toString();
                String gender="";

                if(radioMale.isChecked()){
                    gender="Male";
                }
                if(radioFemale.isChecked()){
                    gender="Female";
                }
                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Please Enter Email");
                    return;
                }
                if (TextUtils.isEmpty(name)) {
                    mFullName.setError("Please Enter Full Name");
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    mEmail.setError("Invalid Email");
                    mEmail.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Please Enter Password");
                    return;
                }
                if(password.length()<6){
                    mPassword.setError("Password less than 6 Character");
                    return;
                }
                if (TextUtils.isEmpty(confirmPassword)) {
                    mPassword.setError("Please Enter confirmPassword");
                    return;
                }
                if(!(confirmPassword.equals(password))){
                    mConfirmPassword.setError("Wrong password");
                    return;
                }
                if (TextUtils.isEmpty(college)) {
                    mCollege.setError("Please Enter College");
                    return;
                }
//to use gender in inner class must be final and
// if we defined it as final at the top we will not be able to change it's value
//while changing in radio buttons.
                final String finalGender = gender;

                fAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(SignUpActivity.this, "Registration Complete", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(getApplicationContext(),WelcomePage.class));

                                    //now updating data base with new user info
                                    Firebase userChild = mRef.push();

                                    Firebase nameChild = userChild.child("Name");
                                    nameChild.setValue(name);
                                    Firebase mailChild = userChild.child("Email");
                                    mailChild.setValue(email);
                                    Firebase passChild = userChild.child("password");
                                    passChild.setValue(password);
                                    Firebase genderChild = userChild.child("Gender");
                                    genderChild.setValue(finalGender);
                                    Firebase collageChild = userChild.child("Collage");
                                    collageChild.setValue(college);

                                } else {
                                    // If sign up fails, display a message to the user.
                                    Toast.makeText(SignUpActivity.this, "Registration is Failed", Toast.LENGTH_LONG).show();
                                }

                                // ...
                            }
                        });
            }
        });
    }
}
//                                    // Sign in success, update UI with the signed-in user's information
//                                    User user = new User(name,email,password,confirmPassword, finalGender,college);
//                                    FirebaseDatabase.getInstance().getReference("User")
//                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<Void> task) {
//                                            Toast.makeText(SignUpActivity.this, "Registration Complete", Toast.LENGTH_LONG).show();
//                                            startActivity(new Intent(getApplicationContext(),WelcomePage.class));
//                                        }
//                                    });