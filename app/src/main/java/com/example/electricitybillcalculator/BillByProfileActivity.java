package com.example.electricitybillcalculator;

import static android.content.ContentValues.TAG;
import static android.text.InputType.TYPE_CLASS_TEXT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BillByProfileActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<String> dateList,currentList,amountList;
    CustomAdapterForBillhistory customAdapterForBillhistory;
    String profileName,date,userID;
    FirebaseFirestore firebaseFirestore;


    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_by_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        profileName = intent.getStringExtra("name");
        userID = intent.getStringExtra("userID");
        date = intent.getStringExtra("date");
        recyclerView = findViewById(R.id.recyclerView);
        toolbar.setTitle("Bills of "+profileName.toUpperCase());
        firebaseFirestore = FirebaseFirestore.getInstance();
        dateList = new ArrayList<>();
        currentList = new ArrayList<>();
        amountList = new ArrayList<>();

        customAdapterForBillhistory = new CustomAdapterForBillhistory(BillByProfileActivity.this,dateList,currentList,amountList);
        firebaseFirestore.collection("Profiles of "+userID ).document(profileName)
                .collection("Bill Data")
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    amountList.add(document.getString("Amount"));
                    currentList.add(document.getString("Current Reading"));
                    dateList.add(document.getString("Date"));
                    customAdapterForBillhistory.notifyDataSetChanged();
                }
            } else {
                Log.w(TAG, "Error getting documents.", task.getException());
            }
        });
        recyclerView.setAdapter(customAdapterForBillhistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(BillByProfileActivity.this));

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.add_new_bill){
            Intent intentAdd = new Intent(this,BillGenerateActivity.class);
            intentAdd.putExtra("Name" ,profileName);
            intentAdd.putExtra("Date",date);
            startActivity(intentAdd);
        }

        if(id == R.id.delete_profile){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Delete ?");
            builder.setMessage("Are you sure you want delete ?");
            builder.setPositiveButton("Yes", (dialogInterface, i) -> {
                //delete operation
                firebaseFirestore.collection("Profiles of "+userID ).document(profileName).delete().addOnSuccessListener(unused -> {
                    Toast.makeText(this, "deletion is successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this,MainActivity.class) );
                }).addOnFailureListener(e ->
                        Toast.makeText(this, " failed " +e , Toast.LENGTH_SHORT).show()
                );

            });
            builder.setNegativeButton("No", (dialogInterface, i) ->
                    Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show()
            );
            builder.create().show();

        }

        return super.onOptionsItemSelected(item);
    }






}