package com.example.adwatna1project;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    TextView back ;

    //for firebase
    EditText mEmail , mPassword;
    Button mLogin;
    TextView forgetPassword;
    private FirebaseAuth fAuth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        back=findViewById(R.id.back1);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //firebase code
        mEmail = findViewById(R.id.email_login);
        mPassword = findViewById(R.id.password_login);
        mLogin = findViewById(R.id.sign_button);
        forgetPassword = findViewById(R.id.forget_password_tv);
        fAuth = FirebaseAuth.getInstance();

        //when click forget password
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showForgetPasswordDialog();
            }
        });

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = mEmail.getText().toString().trim();
                final String password = mPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Please Enter Email");
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
                    mPassword.setError("Password is less than 6 Character");
                    return;
                }
                fAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(LoginActivity.this, "Login Complete", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(getApplicationContext(),WelcomePage.class));
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(LoginActivity.this, "Login is Failed", Toast.LENGTH_LONG).show();
                                }

                                // ...
                            }
                        });
            }
        });

    }

    //forget password methods

    public void showForgetPasswordDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this, R.style.AlerDialog);
        View view = LayoutInflater.from(LoginActivity.this)
                .inflate(R.layout.forget_password_dialog, (ViewGroup) findViewById(R.id.dialog_layout_container));
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        final EditText emailEditText = view.findViewById(R.id.email_forget_password);
        emailEditText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        emailEditText.setMinEms(16);
        view.findViewById(R.id.recover_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                beginRecoverPassword(email);

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

    private void beginRecoverPassword(String email) {
        fAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Email has been sent", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(LoginActivity.this, "Failed...", Toast.LENGTH_LONG).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }
}