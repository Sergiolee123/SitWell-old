package com.fyp.sitwell;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    private Button   mSignUpBtn , mLoginBtn;
    private EditText email,password;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();

        mLoginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                finish();
                startActivity(myIntent);
            }
        });

        mSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_email    = email.getText().toString();
                String str_password = password.getText().toString();
                if(TextUtils.isEmpty(str_email.trim()) || TextUtils.isEmpty(str_email.trim())){
                    Toast.makeText(RegisterActivity.this, "Enter email and password.",
                            Toast.LENGTH_SHORT).show();
                }else{
                    auth.createUserWithEmailAndPassword(str_email, str_password)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "createUserWithEmail:success");
                                        Toast.makeText(RegisterActivity.this, "Create success.",
                                                Toast.LENGTH_SHORT).show();
                                        FirebaseUser user = auth.getCurrentUser();
                                        updateUI(user);
                                    } else {
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        updateUI(null);
                                    }
                                }
                            });
                }



            }
        });
    }

    private void init(){
        mSignUpBtn = findViewById(R.id.button_signup);
        mLoginBtn  = findViewById(R.id.button_login);
        email      = findViewById(R.id.edit_email);
        password   = findViewById(R.id.edit_password);
        auth       = FirebaseAuth.getInstance();
    }

    private void updateUI(FirebaseUser user) {

    }


}