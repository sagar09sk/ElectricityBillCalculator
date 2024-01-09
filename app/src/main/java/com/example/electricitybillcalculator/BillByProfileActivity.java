package com.example.electricitybillcalculator;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class BillByProfileActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView textViewdetail;
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

        dateList = new ArrayList<>();
        currentList = new ArrayList<>();
        amountList = new ArrayList<>();

        customAdapterForBillhistory = new CustomAdapterForBillhistory(BillByProfileActivity.this,dateList,currentList,amountList);
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Bills of "+ profileName +" of " +userID).get().addOnCompleteListener(task -> {
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