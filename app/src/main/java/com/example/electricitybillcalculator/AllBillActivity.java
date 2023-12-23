package com.example.electricitybillcalculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

public class AllBillActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    BillData billData;
    ArrayList<String> profile_id, profile_name, profile_amount;
    CustomAdapterForLastBill customAdapterForLastBill;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_bill);

        recyclerView = findViewById(R.id.recyclerView);

        billData = new BillData(this);
        profile_id = new ArrayList<>();
        profile_name = new ArrayList<>();
        profile_amount = new ArrayList<>();

        storeDataInArray();


        customAdapterForLastBill = new CustomAdapterForLastBill(AllBillActivity.this,profile_id,profile_name,profile_amount);
        recyclerView.setAdapter(customAdapterForLastBill);

        recyclerView.setLayoutManager(new LinearLayoutManager(AllBillActivity.this));


    }
    void storeDataInArray(){
        Cursor cursor = billData.readDataFromBillTable();
        if(cursor.getCount() ==  0){
            Toast.makeText(this, "No Data found", Toast.LENGTH_SHORT).show();
        }
        else{
            while(cursor.moveToNext()){
                profile_id.add(cursor.getString(1));
                profile_name.add(cursor.getString(0));
                profile_amount.add(cursor.getString(2));
            }
        }

    }



}