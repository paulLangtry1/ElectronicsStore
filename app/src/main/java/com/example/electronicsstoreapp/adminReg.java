package com.example.electronicsstoreapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class adminReg extends AppCompatActivity {

    private EditText etRegCompEmail,etRegCompName,etRegCompPW,etRegCompConfirmPW,etAddress,etPhone;
    private Button btnRegCompany;
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private static final String COMPANY = "Admin";
    private static final String TAG = "adminReg";
    private Admin admin;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_reg);

        etRegCompConfirmPW = findViewById(R.id.etRegCompConfirmPW);
        etRegCompPW = findViewById(R.id.etRegCompPW);
        etRegCompEmail = findViewById(R.id.etRegCompEmail);
        etRegCompName = findViewById(R.id.etRegCompName);
        btnRegCompany = findViewById(R.id.btnRegCompany);


        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference(COMPANY);
        mAuth = FirebaseAuth.getInstance();

        btnRegCompany.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String companyemail = etRegCompEmail.getText().toString();
                String companyname = etRegCompName.getText().toString();
                String password = etRegCompPW.getText().toString();
                String confirmpw = etRegCompConfirmPW.getText().toString();


                if(companyemail.isEmpty() || companyname.isEmpty() || password.isEmpty() || confirmpw.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please enter all details",Toast.LENGTH_LONG).show();
                    return;
                }
                if (password.equals(confirmpw) && password.length()>=6)
                {
                    admin = new Admin(companyemail,password,companyname);
                    registerUser(companyemail,password);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Passwords do not match Or Password is less than 6 characters",Toast.LENGTH_LONG).show();
                    //return;
                }
            }
        });





    }

    private void registerUser(String companyemail, String password)
    {
        mAuth.createUserWithEmailAndPassword(companyemail, password)
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
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(adminReg.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }
    public void updateUI(FirebaseUser currentUser)
    {
        String uid=currentUser.getUid();
        mDatabase.child(uid).setValue(admin);
        Intent intent = new Intent(this, adminlogin.class);
        startActivity(intent);
    }
}