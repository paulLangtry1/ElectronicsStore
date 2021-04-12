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

public class View_customers extends AppCompatActivity implements customerAdapter.OnContractListener {

    ArrayList<Customer> allitemsadmin = new ArrayList<Customer>();

    private static final String Basketcart = "ShoppingCart";

    private FirebaseDatabase database;
    private StorageReference profilepic;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private DatabaseReference ref,dbref;
    customerAdapter myAdapter;
    private FirebaseUser user;
    RecyclerView mRecyclerView;
    private String uid;
    private int admin=0;
    private String tempid;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_customers);

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        dbref= FirebaseDatabase.getInstance().getReference(Basketcart);



        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        profilepic = storage.getReference();

        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        myAdapter = new customerAdapter(allitemsadmin,this::onContractClick);
        mRecyclerView.setAdapter(myAdapter);

        ref.child("Customer").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    Customer contract = child.getValue(Customer.class);
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
        String contractID = allitemsadmin.get(position).getUserID();

        Intent intent = new Intent(View_customers.this,display_purchase_history.class);
        intent.putExtra("passeduid",contractID);
        startActivity(intent);



    }
}