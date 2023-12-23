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
    BillData billData;
    ArrayList<String> bill_date,bill_current,bill_amount;
    CustomAdapterForBillhistory customAdapterForBillhistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_by_profile);

        Intent intent = getIntent();
        String name = intent.getStringExtra(ProfilenameActivity.nameIntent);

        recyclerView = findViewById(R.id.recyclerView);

        billData = new BillData(this);
        bill_date = new ArrayList<>();
        bill_current = new ArrayList<>();
        bill_amount = new ArrayList<>();

        storeDataInArray(name);

        customAdapterForBillhistory = new CustomAdapterForBillhistory(BillByProfileActivity.this,bill_date,bill_current,bill_amount);
        recyclerView.setAdapter(customAdapterForBillhistory);

        recyclerView.setLayoutManager(new LinearLayoutManager(BillByProfileActivity.this));

    }

    void storeDataInArray(String name){

        Cursor cursor = billData.readDataFromProfileTable(name);
        if(cursor.getCount() == 0){
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        }
        else {
            while (cursor.moveToNext()){
                bill_date.add(cursor.getString(0));
                bill_current.add(cursor.getString(2));
                bill_amount.add(cursor.getString(3));
            }

        }


    }
}