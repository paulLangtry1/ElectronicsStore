package com.example.electronicsstoreapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ViewItemReviews extends AppCompatActivity implements MyAdapter.OnContractListener {

    ArrayList<feedback> allitemsadmin = new ArrayList<feedback>();

    private static final String Basketcart = "ShoppingCart";

    private FirebaseDatabase database;
    private StorageReference profilepic;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private DatabaseReference ref,dbref;
    Reviewadapter myAdapter;
    private FirebaseUser user;
    RecyclerView mRecyclerView;
    private String uid;
    private int admin=0;
    private String tempid;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item_reviews);

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        dbref= FirebaseDatabase.getInstance().getReference(Basketcart);

       // admin = getIntent().getExtras().getInt("admin");


        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        profilepic = storage.getReference();

        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        myAdapter = new Reviewadapter(allitemsadmin,this::onContractClick);
        mRecyclerView.setAdapter(myAdapter);

        ref.child("Review").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    feedback contract = child.getValue(feedback.class);
                    //  if (contract.getUID().equals(uid)) {
                    allitemsadmin.add(contract);

                    myAdapter.notifyItemInserted(allitemsadmin.size() - 1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //   Log.m("DBE Error","Cancel Access DB");
            }
        });
    }




    @Override
    public void onContractClick(int position)
    {
        allitemsadmin.get(position);
        String contractID = allitemsadmin.get(position).getReviewid();

        ref.child("Review").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    feedback contract = child.getValue(feedback.class);
                    if (contract.getReviewid().equals(contractID))
                    {
                        // Create custom dialog object
                        final Dialog dialog = new Dialog(ViewItemReviews.this);
                        // Include dialog.xml file
                        dialog.setContentView(R.layout.dialog_review_layout);
                        // Set dialog title
                        dialog.setTitle("Custom Dialog");

                        // set values for custom dialog components - text, image and button
                        TextView text = (TextView) dialog.findViewById(R.id.itemdetails);
                        TextView text2 = (TextView) dialog.findViewById(R.id.tvreview1);
                        text.setText(contract.getTitle());
                        text2.setText("Comment: " + contract.getContent());


                        dialog.show();

                        Button declineButton = (Button) dialog.findViewById(R.id.btnsubmit);
                        // if decline button is clicked, close the custom dialog
                        declineButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {


                                 dialog.dismiss();





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

        Intent intent = new Intent(ViewItemReviews.this,UserHomeActivity.class);
        startActivity(intent);



    }




}