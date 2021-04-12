package com.example.electronicsstoreapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserHomeActivity extends AppCompatActivity {

    private EditText etSearch;
    private Button btnSearch;
    private TextView tvWelcome;
    private ImageView btnviewitems,btnleavereview,btncheckout;
    private FirebaseDatabase db;
    private DatabaseReference dbRef;
    private FirebaseUser user;
    private String uid;
    private Customer currentUser;
    private int admin =  0;







    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);


        tvWelcome = findViewById(R.id.tvWelcome);
        btncheckout = findViewById(R.id.btncheckout);
        btnleavereview = findViewById(R.id.btnaddreview);
        btnSearch = findViewById(R.id.btnsearchitemsuser);
        btnviewitems = findViewById(R.id.btnviewitemuser);






        db = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        dbRef=db.getReference();
        uid=user.getUid();

        dbRef.child("Customer").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    if (child.getKey().equals(uid)) {
                        currentUser = child.getValue(Customer.class);
                        String currentName = currentUser.getUsername();
                        tvWelcome.setText("     Welcome " + currentName + "!");

                        dbRef.child("Customer").child(uid).child("userID").setValue(uid);


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnviewitems.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(UserHomeActivity.this,ViewItems.class);
                intent.putExtra( "admin", admin);
                startActivity(intent);

            }
        });

        btncheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(UserHomeActivity.this,checkout.class);
                intent.putExtra( "admin", admin);
                startActivity(intent);


            }
        });
        btnleavereview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserHomeActivity.this,Review.class);
                startActivity(intent);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserHomeActivity.this,SearchItems.class);
                intent.putExtra( "admin", admin);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item1:
                home();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void home()
    {

        Intent intent = new Intent(UserHomeActivity.this,UserHomeActivity.class);
        startActivity(intent);



    }
    public void ViewAll()
    {
        Intent intent = new Intent(UserHomeActivity.this, UserHomeActivity.class);
        startActivity(intent);





    }

    public void viewUpdate()
    {




    }
    public void ViewAllNotes()
    {



    }
    public void SearchAllNotes()
    {

        //Intent intent = new Intent(HomeActivity.this,TakeinSearch.class);
       // startActivity(intent);


    }
}