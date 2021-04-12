package com.example.electronicsstoreapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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

import java.util.ArrayList;

import static java.text.DateFormat.getDateTimeInstance;

public class checkout extends AppCompatActivity implements MyAdapter.OnContractListener
{
    ArrayList<Item> allitems = new ArrayList<Item>();

    private static final String Basket = "CartHistory";

    private FirebaseDatabase database;
    private TextView tvtotalcost,tvdiscount;
    private DatabaseReference ref,dbref;
    MyAdapter myAdapter;
    private FirebaseUser user;
    private Button btncalculate,btncancel,btnpay;
    RecyclerView mRecyclerView;
    private String uid;
    private ArrayList<Double> prices = new ArrayList<Double>();
    private int admin=0;
    private int currentquantity=0;
    private String passeditemid = "";
    private String temptodelete ="";
    private double discount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        admin = getIntent().getExtras().getInt("admin");


        tvtotalcost = findViewById(R.id.tvtotalcost);
        tvdiscount = findViewById(R.id.tvtotalcost2);
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
                ref.child("ShoppingCart").addListenerForSingleValueEvent(new ValueEventListener() {
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

                                if(admin==1)
                                {
                                    Intent intent = new Intent(checkout.this, adminhome.class);
                                    startActivity(intent);
                                }
                                else
                                {
                                    Intent intent = new Intent(checkout.this, UserHomeActivity.class);
                                    startActivity(intent);
                                }



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

        btnpay.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {








                createcarthistory();






                decreasestock();
                clearbasket();






                if(admin==1)
                {

                    Intent intent = new Intent(checkout.this, adminhome.class);
                    startActivity(intent);
                }
                else
                {
                    addloyalty();
                    Intent intent = new Intent(checkout.this, UserHomeActivity.class);
                    startActivity(intent);
                }





            }
        });







    }

    @Override
    public void onContractClick(int position)
    {
        //allitems.get(position);
        //String uid = allitems.get(position).getNoteID();
        //Intent intent = new Intent(ViewAllNotes.this,CurrentNote.class);
       // intent.putExtra( "uid", uid);
        //startActivity(intent);
    }

    public void totalprice()
    {
        isdiscount();

        double sum = 0;
        for(double i : prices)
        {
            sum = sum+i;
        }

        double sumwithdiscount = sum*discount;
        sum = sum - sumwithdiscount;


        tvtotalcost.setText(": " + sum);
        tvdiscount.setText(": " + discount);

    }
    public void clearbasket()
    {

        ref.child("ShoppingCart").child(temptodelete).removeValue();

    }
    public void addloyalty()
    {
        ref.child("Customer").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children)
                {
                    Customer note = child.getValue(Customer.class);
                    if (note.getUserID().equals(uid))
                    {
                        int currentquantity = note.getDiscount();
                        currentquantity++;

                        ref.child("Customer").child(uid).child("discount").setValue(currentquantity);

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //   Log.m("DBE Error","Cancel Access DB");
            }
        });

    }


    public void decreasestock()
    {
        ref.child("Item").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children)
                {
                    Item note = child.getValue(Item.class);
                    if (note.getItemid().equals(passeditemid))
                    {
                        int currentquantity = note.getQuantity();
                        currentquantity--;

                        ref.child("Item").child(passeditemid).child("quantity").setValue(currentquantity);

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //   Log.m("DBE Error","Cancel Access DB");
            }
        });




    }

    public void createcarthistory()
    {
        ref.child("ShoppingCart").addListenerForSingleValueEvent(new ValueEventListener()
        {
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
                            int quantity = 1;
                            String keyid = dbref.push().getKey();
                            String price = contract.getPrice();
                            String itemurl = keyid;
                            String itemid = contract.getItemid();
                            String userid = uid;
                            passeditemid = itemid;

                            temptodelete = contract.getItemurl();


                            Item basket = new Item(category, manufacturer, title, quantity, price, itemurl, itemid,userid);

                            dbref.child(keyid).setValue(basket);



                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void isdiscount()
    {
        ref.child("Customer").addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    {
                        Customer contract = child.getValue(Customer.class);
                        if (contract.getUserID().equals(uid))
                        {
                            if(contract.getDiscount()>=3 && contract.getDiscount()<5)
                            {
                                discount = 0.10;
                            }
                            else if (contract.getDiscount()>=5 && contract.getDiscount()<8)
                            {
                                discount = 0.20;
                            }
                            else if (contract.getDiscount()>=8 && contract.getDiscount()>100)
                            {
                                discount = 0.35;
                            }
                            else if (contract.getDiscount()>=100)
                            {
                                discount = 0.50;
                            }



                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }




}