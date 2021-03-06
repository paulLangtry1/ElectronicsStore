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

public class LoginActivityUser extends AppCompatActivity {
    private EditText etEmailLogin, etPasswordLogin;
    private Button btnCreateAccount, btnLogin, btnCompanyLogin;
    private FirebaseAuth mAuth;
    private static final String USER = "user";
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);

        etEmailLogin = findViewById(R.id.etEmailLogin);
        etPasswordLogin = findViewById(R.id.etPasswordLogin);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);
        btnLogin = findViewById(R.id.btnLogin);
        btnCompanyLogin = findViewById(R.id.btnCompanyLogin);

        mAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                String email = etEmailLogin.getText().toString();
                String password = etPasswordLogin.getText().toString();
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter credentials", Toast.LENGTH_LONG).show();
                    return;
                }
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivityUser.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(LoginActivityUser.this, "Successfully Logged In",Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user
                                    Toast.makeText(LoginActivityUser.this, "Incorrect Username Or Password",Toast.LENGTH_SHORT).show();
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());


                                }

                                // ...
                            }
                        });

            }
        });


        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent regintent = new Intent(LoginActivityUser.this, MainActivity.class);
                startActivity(regintent);

            }
        });

        btnCompanyLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent compintent = new Intent(LoginActivityUser.this, adminlogin.class);
                startActivity(compintent);

            }
        });


    }
    public void updateUI(FirebaseUser currentUser)
    {
        Intent homeIntent = new Intent(LoginActivityUser.this, UserHomeActivity.class);
        homeIntent.putExtra("email",currentUser.getEmail());
        startActivity(homeIntent);
    }


    // @Override
    //public void onStart() {
    //   super.onStart();
    //  // Check if user is signed in (non-null) and update UI accordingly.
    // FirebaseUser currentUser = mAuth.getCurrentUser();
    //if(currentUser!=null)
    // {
    //    updateUI(currentUser);
    // }

    //}

}