package com.example.electronicsstoreapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class TakeinRating extends AppCompatActivity
{
    private RatingBar ratingBarexp;
    private EditText etcomment;
    private DatabaseReference ref,dbref;
    private FirebaseDatabase database;
    private String id;
    private static final String Review = "Review";
    private String uid;
    private FirebaseUser user;
    private EditText etcontent;
    private RatingBar rb;
    private Button btnsubmit;
    private TextView tv;
    private ImageView imglogo;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_takein_rating);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        id = getIntent().getExtras().getString("id");
        dbref= FirebaseDatabase.getInstance().getReference(Review);

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        btnsubmit = findViewById(R.id.btnsubmit);
        etcontent = findViewById(R.id.etcontent);
        rb = findViewById(R.id.ratingBarexperience);
        tv = findViewById(R.id.itemdetails);
        imglogo = findViewById(R.id.imageViewlogo);



        ref.child("CartHistory").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    Item contract = child.getValue(Item.class);
                    if (contract.getItemurl().equals(id))
                    {


                        tv.setText(contract.getManufacturer() + " " + contract.getTitle());

                        // if decline button is clicked, close the custom dialog
                        btnsubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {

                                ref.child("CartHistory").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        Iterable<DataSnapshot> children = snapshot.getChildren();
                                        for (DataSnapshot child : children) {
                                            {
                                                Item contract = child.getValue(Item.class);
                                                if (contract.getItemurl().equals(id))
                                                {
                                                    String category = contract.getCategory();
                                                    String manufacturer = contract.getManufacturer();
                                                    String title = contract.getTitle();
                                                    String rating = String.valueOf(rb.getRating());
                                                    String content = etcontent.getText().toString();
                                                    String userid = contract.getUserid();


                                                    String keyid = dbref.push().getKey();
                                                    String reviewid = keyid;
                                                    String itemid = contract.getItemid();



                                                    feedback fb = new feedback(category, title, manufacturer, rating, content, userid, reviewid,itemid);


                                                    dbref.child(keyid).setValue(fb);

                                                    //String keyId = dbRef.push().getKey();
                                                    //dbRef.child(keyId).setValue(contract);

                                                    Intent intent = new Intent(TakeinRating.this, UserHomeActivity.class);
                                                    startActivity(intent);


                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });


                            }
                        });




                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                //   Log.m("DBE Error","Cancel Access DB");
            }
        });


    }
}