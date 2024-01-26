package com.example.electricitybillcalculator;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class BillByProfileActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView textViewdetail;
    ImageView deleteView;
    ArrayList<String> dateList,currentList,amountList;
    CustomAdapterForBillhistory customAdapterForBillhistory;

    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_by_profile);

        Intent intent = getIntent();
        String profileName = intent.getStringExtra("name");
        String userID = intent.getStringExtra("userID");
        recyclerView = findViewById(R.id.recyclerView);
        textViewdetail = findViewById(R.id.textViewdetail);
        textViewdetail.setText("Bill History of "+profileName.toUpperCase());
        deleteView = findViewById(R.id.deleteView);
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        dateList = new ArrayList<>();
        currentList = new ArrayList<>();
        amountList = new ArrayList<>();

        deleteView.setOnClickListener(v -> {
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

        });



        customAdapterForBillhistory = new CustomAdapterForBillhistory(BillByProfileActivity.this,dateList,currentList,amountList);
        firebaseFirestore
                .collection("Profile Bills Data of " +userID)
                .document("Bill Data")
                .collection(profileName).get().addOnCompleteListener(task -> {
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

}