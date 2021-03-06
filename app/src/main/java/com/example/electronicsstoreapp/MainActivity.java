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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private EditText etName, etPassword, etConfirmPW, etEmail,etPhoneno,etaddress;
    private Button btnSubmit;
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser userfire;
    private static final String USER = "Customer";
    private static final String TAG = "MainActivity";
    private Customer user;
    private String uid;





    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference();



        etName = findViewById(R.id.etName);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPW = findViewById(R.id.etConfirmPW);
        btnSubmit = findViewById(R.id.btnSubmit);
        etEmail = findViewById(R.id.etEmail);
        etPhoneno = findViewById(R.id.etPhoneno);
        etaddress = findViewById(R.id.etAddress);

        database = FirebaseDatabase.getInstance();
        mDatabase = database.getInstance().getReference(USER);
        mAuth = FirebaseAuth.getInstance();









        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String email = etEmail.getText().toString();
                String username = etName.getText().toString();
                String password = etPassword.getText().toString();
                String confirmpw = etConfirmPW.getText().toString();
                String phoneNo=etPhoneno.getText().toString();
                String address = etaddress.getText().toString();
                String userID = "";
                int discount = 0;


                if(email.isEmpty() || username.isEmpty() || password.isEmpty() || confirmpw.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please enter all details",Toast.LENGTH_LONG).show();
                    return;
                }
                if (password.equals(confirmpw) && password.length()>=6)
                {
                    user = new Customer(email,password,username,phoneNo,userID,address,discount);
                    registerUser(email,password);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Passwords do not match Or Less Than 6 Characters",Toast.LENGTH_LONG).show();
                    //return;
                }

            }
        });


    }


    public void registerUser(String email, String password)
    {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());

                            updateUI(null);
                        }

                        // ...
                    }
                });

    }
    public void updateUI(FirebaseUser currentUser)
    {
        String uid=currentUser.getUid();
        mDatabase.child(uid).setValue(user);
        Intent intent = new Intent(this, LoginActivityUser.class);
        startActivity(intent);
    }

}