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

public class ViewItems extends AppCompatActivity implements MyAdapter.OnContractListener{

    ArrayList<Item> allitemsadmin = new ArrayList<Item>();

    private static final String Basket = "ShoppingCart";

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
    private String tempid;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_items);

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        dbref= FirebaseDatabase.getInstance().getReference(Basket);

        admin = getIntent().getExtras().getInt("admin");


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

        ref.child("Item").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    Item contract = child.getValue(Item.class);
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
        String contractID = allitemsadmin.get(position).getItemid();

        ref.child("Item").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    Item contract = child.getValue(Item.class);
                    if (contract.getItemid().equals(contractID))
                    {
                        // Create custom dialog object
                        final Dialog dialog = new Dialog(ViewItems.this);
                        // Include dialog.xml file
                        dialog.setContentView(R.layout.dialog_layout);
                        // Set dialog title
                        dialog.setTitle("Custom Dialog");

                        // set values for custom dialog components - text, image and button
                        TextView text = (TextView) dialog.findViewById(R.id.itemdetails);
                        TextView text2 = (TextView) dialog.findViewById(R.id.tvreview1);
                        text.setText(contract.getTitle());
                        text2.setText("Quanity: " + contract.getQuantity());
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
                                    Toast.makeText(ViewItems.this,"Image failed to load",Toast.LENGTH_LONG).show();
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

                                int quantity = Integer.parseInt(contract.getQuantity());
                                String id = contract.getItemid();
                                tempid = id;
                                quantity--;
                                DatabaseReference c1v2= FirebaseDatabase.getInstance().getReference().child("Item").child(id).child("quantity");
                                c1v2.setValue(String.valueOf(quantity));

                                create();





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

    public void create()
    {
        ref.child("Item").addValueEventListener(new ValueEventListener() {
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
                            String quantity = "1";
                            String keyid = dbref.push().getKey();
                            String price = contract.getPrice();
                            String itemurl = keyid;
                            String itemid = contract.getItemid();
                            String userid = uid;


                            Item basket = new Item(category, manufacturer, title, quantity, price, itemurl, itemid,userid);


                            dbref.child(keyid).setValue(basket);

                            //String keyId = dbRef.push().getKey();
                            //dbRef.child(keyId).setValue(contract);

                            if(admin==1)
                            {
                                Intent intent = new Intent(ViewItems.this, adminhome.class);
                                startActivity(intent);

                            }
                            else {
                                Intent intent = new Intent(ViewItems.this, UserHomeActivity.class);
                                startActivity(intent);
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