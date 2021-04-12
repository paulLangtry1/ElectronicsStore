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
import android.widget.EditText;
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
import java.util.Collections;
import java.util.Comparator;

public class SearchItems extends AppCompatActivity implements MyAdapter.OnContractListener {

    ArrayList<Item> allitemsadmin = new ArrayList<Item>();
    ArrayList<Item> sortedbyprice = new ArrayList<Item>();

    private static final String Basket = "ShoppingCart";

    private FirebaseDatabase database;
    private StorageReference profilepic;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private DatabaseReference ref,dbref;
    private Button btnsearch,btnclear,btnpriceup,btnpricedown,btncatup,btncatdown,btnmakeup,btnmakedown;
    private EditText etcontent;
    MyAdapter myAdapter;
    private FirebaseUser user;
    RecyclerView mRecyclerView;
    private String uid;
    private int admin=0;
    private String tempid = "";
    private String enteredvalue = "";
    private int quantity = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_items);

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        dbref= FirebaseDatabase.getInstance().getReference(Basket);

        btnsearch = findViewById(R.id.btnsearch);
        etcontent = findViewById(R.id.etsearchcontent);
        btnclear = findViewById(R.id.btnclear);
        btnpriceup = findViewById(R.id.btnpriceup);
        btnpricedown = findViewById(R.id.btnpricedown);
        btncatup = findViewById(R.id.btncatup);
        btncatdown = findViewById(R.id.btncatdown);
        btnmakedown = findViewById(R.id.tbnmakedown);
        btnmakeup = findViewById(R.id.tbnmakeup);
        admin = getIntent().getExtras().getInt("admin");


        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        profilepic = storage.getReference();

        mRecyclerView.setHasFixedSize(true);




        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                enteredvalue = etcontent.getText().toString();

                search();

            }
        });

        btnclear.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(SearchItems.this,SearchItems.class);
                startActivity(intent);

            }
        });
        btnpriceup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                sortbyPrice();

                myAdapter.notifyDataSetChanged();




            }
        });
        btnpricedown.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            sortpricedown();
            myAdapter.notifyDataSetChanged();


        }
    });

        btncatup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                catup();
                myAdapter.notifyDataSetChanged();

            }
        });
        btncatdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                catdown();
                myAdapter.notifyDataSetChanged();

            }
        });
        btnmakeup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeup();
                myAdapter.notifyDataSetChanged();

            }
        });
        btnmakedown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makedown();
                myAdapter.notifyDataSetChanged();

            }
        });






    }


    public void search()
    {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        myAdapter = new MyAdapter(allitemsadmin,this::onContractClick);
        mRecyclerView.setAdapter(myAdapter);

        ref.child("Item").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children)
                {
                    Item contract = child.getValue(Item.class);
                    if (contract.getTitle().equalsIgnoreCase(enteredvalue) || contract.getManufacturer().equalsIgnoreCase(enteredvalue) || contract.getCategory().equalsIgnoreCase(enteredvalue))
                    {
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
    public void sortbyPrice()
    {
        Collections.sort(allitemsadmin, new Comparator<Item>()
        {
            @Override
            public int compare(Item o1, Item o2)
            {
                return o1.getPrice().compareTo(o2.getPrice());
            }

        });


    }
    public void sortpricedown()
    {
        Collections.sort(allitemsadmin, new Comparator<Item>()
        {
            @Override
            public int compare(Item o1, Item o2)
            {
                return o2.getPrice().compareTo(o1.getPrice());
            }

        });


    }
    public void makeup()
    {
        Collections.sort(allitemsadmin, new Comparator<Item>()
        {
            @Override
            public int compare(Item o1, Item o2)
            {
                return o1.getManufacturer().compareToIgnoreCase(o2.getManufacturer());
            }

        });


    }
    public void makedown()
    {
        Collections.sort(allitemsadmin, new Comparator<Item>()
        {
            @Override
            public int compare(Item o1, Item o2)
            {
                return o2.getManufacturer().compareToIgnoreCase(o1.getManufacturer());
            }

        });


    }
    public void catup()
    {
        Collections.sort(allitemsadmin, new Comparator<Item>()
        {
            @Override
            public int compare(Item o1, Item o2)
            {
                return o1.getTitle().compareToIgnoreCase(o2.getTitle());
            }

        });


    }

    public void catdown()
    {
        Collections.sort(allitemsadmin, new Comparator<Item>()
        {
            @Override
            public int compare(Item o1, Item o2)
            {
                return o2.getTitle().compareToIgnoreCase(o1.getTitle());
            }

        });


    }

    @Override
    public void onContractClick(int position)
    {
        allitemsadmin.get(position);
        String contractID = allitemsadmin.get(position).getItemid();

        if(admin==1)
        {
            ref.child("Item").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Iterable<DataSnapshot> children = snapshot.getChildren();
                    for (DataSnapshot child : children) {
                        Item contract = child.getValue(Item.class);
                        if (contract.getItemid().equals(contractID))
                        {
                            // Create custom dialog object
                            final Dialog dialog = new Dialog(SearchItems.this);
                            // Include dialog.xml file
                            dialog.setContentView(R.layout.dialog_admin_view);
                            // Set dialog title
                            dialog.setTitle("Custom Dialog");

                            // set values for custom dialog components - text, image and button
                            TextView text = (TextView) dialog.findViewById(R.id.itemdetails);
                            TextView text2 = (TextView) dialog.findViewById(R.id.tvreview1);
                            text.setText(contract.getTitle());
                            text2.setText("Quantity: " + contract.getQuantity());
                            ImageView image = (ImageView) dialog.findViewById(R.id.imageDialog);
                            try
                            {
                                profilepic = storage.getReferenceFromUrl("gs://electronicstore-e3b89.appspot.com/images/item" + contract.getTitle());

                                File file =    File.createTempFile("image","jpeg");

                                profilepic.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>()
                                {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot)
                                    {
                                        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                                        image.setImageBitmap(bitmap);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(SearchItems.this,"Image failed to load",Toast.LENGTH_LONG).show();
                                    }
                                });


                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            dialog.show();

                            Button declineButton = (Button) dialog.findViewById(R.id.btnsubmit);
                            Button stockaddone =  (Button) dialog.findViewById(R.id.btnsubmit2);
                            Button gohome =  (Button) dialog.findViewById(R.id.btnsubmit3);
                            // if decline button is clicked, close the custom dialog
                            declineButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v)
                                {


                                    String id = contract.getItemid();
                                    tempid = id;
                                    create();

                                    Intent intent = new Intent(SearchItems.this, adminhome.class);
                                    startActivity(intent);
                                /*

                                int quantity = contract.getQuantity();


                                quantity--;
                                DatabaseReference c1v2= FirebaseDatabase.getInstance().getReference().child("Item").child(id).child("quantity");
                                c1v2.setValue(quantity);

                                 */












                                }
                            });


                            stockaddone.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v)
                                {
                                    int stock = contract.getQuantity();
                                    stock++;
                                    ref.child("Item").child(contract.getItemid()).child("quantity").setValue(stock);


                                }
                            });

                            gohome.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(SearchItems.this, adminhome.class);
                                    startActivity(intent);
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
        else

        ref.child("Item").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    Item contract = child.getValue(Item.class);
                    if (contract.getItemid().equals(contractID))
                    {
                        // Create custom dialog object
                        final Dialog dialog = new Dialog(SearchItems.this);
                        // Include dialog.xml file
                        dialog.setContentView(R.layout.dialog_layout);
                        // Set dialog title
                        dialog.setTitle("Custom Dialog");

                        // set values for custom dialog components - text, image and button
                        TextView text = (TextView) dialog.findViewById(R.id.itemdetails);
                        TextView text2 = (TextView) dialog.findViewById(R.id.tvreview1);
                        text.setText(contract.getTitle());
                        text2.setText("Quantity: " + contract.getQuantity());
                        ImageView image = (ImageView) dialog.findViewById(R.id.imageDialog);
                        try
                        {
                            profilepic = storage.getReferenceFromUrl("gs://electronicstore-e3b89.appspot.com/images/item" + contract.getTitle());

                            File file =    File.createTempFile("image","jpeg");

                            profilepic.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>()
                            {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot)
                                {
                                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                                    image.setImageBitmap(bitmap);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SearchItems.this,"Image failed to load",Toast.LENGTH_LONG).show();
                                }
                            });


                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                        dialog.show();

                        Button declineButton = (Button) dialog.findViewById(R.id.btnsubmit);
                        // if decline button is clicked, close the custom dialog
                        declineButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {

                                //String id = contract.getItemid();
                                tempid = contractID;




                                //quantity = Integer.parseInt(contract.getQuantity());






                                create();

                                Intent intent = new Intent(SearchItems.this, UserHomeActivity.class);
                                startActivity(intent);

                                //decrementstock();



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

    public void decrementstock()
    {

          quantity--;

          DatabaseReference c1v2= FirebaseDatabase.getInstance().getReference().child("Item").child(tempid).child("quantity");
          c1v2.setValue(String.valueOf(quantity));

    }


    public void create()
    {
        ref.child("Item").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    {
                        Item contract = child.getValue(Item.class);
                        if (contract.getItemid().equals(tempid))
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


                            Item basket = new Item(category, manufacturer, title, quantity, price, itemurl, itemid,userid);


                                dbref.child(keyid).setValue(basket);

                            //String keyId = dbRef.push().getKey();
                            //dbRef.child(keyId).setValue(contract);







                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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

        Intent intent = new Intent(SearchItems.this,UserHomeActivity.class);
        startActivity(intent);



    }















}