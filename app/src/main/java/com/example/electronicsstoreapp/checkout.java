package com.example.electronicsstoreapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import static java.text.DateFormat.getDateTimeInstance;

public class checkout extends AppCompatActivity implements MyAdapter.OnContractListener
{
    ArrayList<Item> allitems = new ArrayList<Item>();

    private static final String Basket = "CartHistory";

    private FirebaseDatabase database;
    private TextView tvtotalcost;
    private DatabaseReference ref,dbref;
    MyAdapter myAdapter;
    private FirebaseUser user;
    private Button btncalculate,btncancel,btnpay;
    RecyclerView mRecyclerView;
    private String uid;
    private ArrayList<Double> prices = new ArrayList<Double>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);


        tvtotalcost = findViewById(R.id.tvtotalcost);
        btncalculate = findViewById(R.id.calculatetotalcost);
        btncancel = findViewById(R.id.btncancel);
        btnpay = findViewById(R.id.btnpay);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        dbref= FirebaseDatabase.getInstance().getReference(Basket);



        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        myAdapter = new MyAdapter(allitems,this::onContractClick);
        mRecyclerView.setAdapter(myAdapter);

        ref.child("ShoppingCart").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    Item note = child.getValue(Item.class);
                    if (note.getUserid().equals(uid)) {
                        allitems.add(note);
                        double price = Double.parseDouble(note.getPrice());
                        prices.add(price);
                    }
                    myAdapter.notifyItemInserted(allitems.size() - 1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //   Log.m("DBE Error","Cancel Access DB");
            }
        });

        btncalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                totalprice();

            }
        });

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ref.child("ShoppingCart").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        Iterable<DataSnapshot> children = snapshot.getChildren();
                        for (DataSnapshot child : children)
                        {
                            Item note = child.getValue(Item.class);
                            if (note.getUserid().equals(uid))
                            {
                                //String title = note.getTitle();
                                String id = note.getItemid();
                                String specificcartitem = note.getItemurl();


                                //DatabaseReference c1v2= FirebaseDatabase.getInstance().getReference().child("Item").child(id).child("quantity");
                               // c1v2.setValue();

                                ref.child("ShoppingCart").child(specificcartitem).removeValue();



                            }
                        }

                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        //   Log.m("DBE Error","Cancel Access DB");
                                    }
                                });


                            }





        });

        btnpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                ref.child("ShoppingCart").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Iterable<DataSnapshot> children = snapshot.getChildren();
                        for (DataSnapshot child : children) {
                            {
                                Item contract = child.getValue(Item.class);
                                if (contract.getUserid().equals(uid))
                                {
                                    String category = contract.getCategory();
                                    String manufacturer = contract.getManufacturer();
                                    String title = contract.getTitle();
                                    String quantity = "1";
                                    String keyid = dbref.push().getKey();
                                    String price = contract.getPrice();
                                    String itemurl = keyid;
                                    String itemid = contract.getItemid();
                                    String userid = uid;

                                    String temptodelete = contract.getItemurl();


                                    Item basket = new Item(category, manufacturer, title, quantity, price, itemurl, itemid,userid);


                                    dbref.child(keyid).setValue(basket);

                                    //String keyId = dbRef.push().getKey();
                                    //dbRef.child(keyId).setValue(contract);
                                    ref.child("ShoppingCart").child(temptodelete).removeValue();



                                    Intent intent = new Intent(checkout.this, adminhome.class);
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

    @Override
    public void onContractClick(int position)
    {
        allitems.get(position);
        //String uid = allitems.get(position).getNoteID();
        //Intent intent = new Intent(ViewAllNotes.this,CurrentNote.class);
       // intent.putExtra( "uid", uid);
        //startActivity(intent);
    }

    public void totalprice()
    {
        /*
        ref.child("ShoppingCart").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    Item note = child.getValue(Item.class);
                    if (note.getItemid().equals(uid))
                    {
                        double price = Double.parseDouble(note.getPrice());
                        prices.add(price);

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //   Log.m("DBE Error","Cancel Access DB");
            }
        });

         */


        double sum = 0;
        for(double i : prices)
        {
            sum = sum+i;
        }


        tvtotalcost.setText(": " + sum);

    }


}