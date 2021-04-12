package com.example.electronicsstoreapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.strictmode.WebViewMethodCalledOnWrongThreadViolation;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

public class addItem extends AppCompatActivity {

    boolean startdateselected = true;
    private DatabaseReference dbRef,ref;
    private static final String Item = "Item";
    private StorageReference storageReference;
    private FirebaseStorage storage;
    private ImageView imgviewitem;
    private StorageReference profilepic;
    private FirebaseUser user;
    private FirebaseDatabase db;
    private Uri filepath;
    private String uid;
    private DatePicker dpStartdate,dpEnddate;
    private EditText etmanu,etcategory,etquantity,ettitle,etprice;
    private TextView tvstartdate,tvenddate,tvstarttime,tvendtime;
    private Button btnCreateContract,btnupload;
    private Admin currentadmin;
    private String globaltitle;
    private boolean starttimeistrue=true;
    private String picPath;
    private String globalid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        db = FirebaseDatabase.getInstance();

        dbRef = FirebaseDatabase.getInstance().getReference(Item);
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        ref = db.getReference();


        etcategory = findViewById(R.id.etcategory);
        etmanu = findViewById(R.id.etmanu);
        etprice = findViewById(R.id.etPrice);
        etquantity = findViewById(R.id.etQuantity);
        ettitle = findViewById(R.id.etTitle);
        imgviewitem = findViewById(R.id.imgviewitem);
        btnupload = findViewById(R.id.btnupload);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        profilepic = storage.getReference();

        btnCreateContract = findViewById(R.id.btnAddComment);

        imgviewitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
            }
        });


        btnCreateContract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref.child("Admin").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Iterable<DataSnapshot> children = snapshot.getChildren();
                        for (DataSnapshot child : children) {
                            if (child.getKey().equals(uid)) {
                                currentadmin = child.getValue(Admin.class);


                                String category = etcategory.getText().toString();
                                String manu = etmanu.getText().toString();
                                String title = ettitle.getText().toString();
                                int quantity = Integer.parseInt(etquantity.getText().toString());
                                String price = etprice.getText().toString();
                                String itemurl = "";
                                String itemid = dbRef.push().getKey();
                                String userid = "";
                                globaltitle = title;
                                globalid=itemid;

                                Item item = new Item(category, manu, title, quantity, price, itemurl, itemid,userid);

                                dbRef.child(itemid).setValue(item);

                                Toast.makeText(getApplicationContext(),"Item is created", Toast.LENGTH_LONG).show();

                                //String keyId = dbRef.push().getKey();
                                //dbRef.child(keyId).setValue(contract);




                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });
        btnupload.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(addItem.this, adminhome.class);
                startActivity(intent);

            }
        });


    }

    private void choosePicture()
    {
        Intent intent =new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null)
        {
            filepath = data.getData();



            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filepath);
                imgviewitem.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }


            uploadpicture();
        }

    }
    private void uploadpicture()
    {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Uplaoading Image..");
        pd.show();

        final String key = user.getUid();
        StorageReference ref = storageReference.child("images/item" + globaltitle);
        picPath = ref.getPath();
        DatabaseReference c1v2= FirebaseDatabase.getInstance().getReference().child("Item").child(globalid).child("itemurl");
        c1v2.setValue("gs://electronicstore-e3b89.appspot.com" + picPath);

        ref.putFile(filepath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Snackbar.make(findViewById(android.R.id.content),"Image Uploaded",Snackbar.LENGTH_LONG).show();


                        pd.dismiss();


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        pd.dismiss();
                        // ...
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progresspercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                pd.setMessage("Percentage: " + (int)progresspercent + "%");
            }
        });




    }








}