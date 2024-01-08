package com.example.electricitybillcalculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

public class BillByProfileActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<String> bill_date,bill_current,bill_amount;
    CustomAdapterForBillhistory customAdapterForBillhistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_by_profile);

        Intent intent = getIntent();
        String name = intent.getStringExtra(ProfilenameActivity.nameIntent);

        recyclerView = findViewById(R.id.recyclerView);

        bill_date = new ArrayList<>();
        bill_current = new ArrayList<>();
        bill_amount = new ArrayList<>();


        customAdapterForBillhistory = new CustomAdapterForBillhistory(BillByProfileActivity.this,bill_date,bill_current,bill_amount);
        recyclerView.setAdapter(customAdapterForBillhistory);

        recyclerView.setLayoutManager(new LinearLayoutManager(BillByProfileActivity.this));

    }

}