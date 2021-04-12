package com.example.electronicsstoreapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class display_purchase_history extends AppCompatActivity implements MyAdapter.OnContractListener {

    ArrayList<Item> allitemsadmin = new ArrayList<Item>();


    private FirebaseDatabase database;
    private StorageReference profilepic;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private DatabaseReference ref,dbref;
    MyAdapter myAdapter;
    private FirebaseUser user;
    RecyclerView mRecyclerView;
    private String uid;
    private int admin=0;
    private String passeduid;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_purchase_history);

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);


        passeduid = getIntent().getExtras().getString("passeduid");


        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        profilepic = storage.getReference();

        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        myAdapter = new MyAdapter(allitemsadmin,this::onContractClick);
        mRecyclerView.setAdapter(myAdapter);

        ref.child("CartHistory").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    Item contract = child.getValue(Item.class);
                    if (contract.getUserid().equals(passeduid)) {
                        allitemsadmin.add(contract);

                        myAdapter.notifyItemInserted(allitemsadmin.size() - 1);
                    }
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
       // String contractID = allitemsadmin.get(position).getUserID();



    }
}