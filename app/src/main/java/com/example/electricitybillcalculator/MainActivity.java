package com.example.electricitybillcalculator;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.util.ArrayList;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView totalBillTextView = findViewById(R.id.totalBillTextView);
        Button getTotalBillButton = findViewById(R.id.getTotalBillButton);

        getTotalBillButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String totalAmountText = getTotalAmount();
                totalBillTextView.setText("Rs "+totalAmountText);
                getTotalBillButton.setText("Update");

            }
        });

    }

    public void generateBill(View view){
        Intent intent = new Intent(MainActivity.this, BillGenerateActivity.class);
        startActivity(intent);
    }

    public void createProfile(View view){
        Intent intent = new Intent(MainActivity.this, CreateProfileActivity.class);
        startActivity(intent);
    }

    public void ShowAllLastBills(View view){
        Intent intent = new Intent(MainActivity.this, AllBillActivity.class);
        startActivity(intent);
    }

    public void showBillHistoryByProfile(View view){
        Intent intent = new Intent( MainActivity.this , ProfilenameActivity.class);
        startActivity(intent);
    }

    String getTotalAmount(){
        int totalAmount = 0;
        BillData billData = new BillData(this);
        Cursor cursor = billData.readAmountFromBillTable();
        if(cursor.getCount() ==  0){
            Toast.makeText(this, "No Data found", Toast.LENGTH_SHORT).show();
        }
        else{
            while(cursor.moveToNext()){
                String a = (cursor.getString(0));
                int x = Integer.parseInt(a);
                totalAmount = totalAmount + x ;
            }
        }
        return String.valueOf(totalAmount);
    }


}

