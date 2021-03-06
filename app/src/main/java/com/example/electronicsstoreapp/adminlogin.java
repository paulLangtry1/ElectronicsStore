package com.example.electronicsstoreapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class adminlogin extends AppCompatActivity {

    private EditText etCompEmail, etCompPassword;
    private Button btnCompLogin, btnCompReg;
    private FirebaseAuth mAuth;
    private static final String USER = "Admin";
    private static final String TAG = "adminlogin";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminlogin);

        btnCompLogin = findViewById(R.id.btnCompLogin);
        btnCompReg = findViewById(R.id.btnCompReg);
        etCompEmail = findViewById(R.id.etCompEmail);
        etCompPassword = findViewById(R.id.etCompPassword);
        mAuth = FirebaseAuth.getInstance();

        btnCompLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String companyEmail = etCompEmail.getText().toString();
                String password = etCompPassword.getText().toString();
                if (companyEmail.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter credentials", Toast.LENGTH_LONG).show();
                    return;
                }
                mAuth.signInWithEmailAndPassword(companyEmail, password)
                        .addOnCompleteListener(adminlogin.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(adminlogin.this, "Successfully Logged In",Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user
                                    Toast.makeText(adminlogin.this, "Incorrect Username Or Password",Toast.LENGTH_SHORT).show();
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());


                                }

                                // ...
                            }
                        });

            }
        });





        //bring user to company register page
        btnCompReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent compreg = new Intent(adminlogin.this, adminReg.class);
                startActivity(compreg);
            }
        });

    }

    public void updateUI(FirebaseUser currentUser)
    {

        Intent homeIntent = new Intent(adminlogin.this, adminhome.class);
        homeIntent.putExtra("companyEmail",currentUser.getEmail());
        startActivity(homeIntent);
    }}