package com.example.electricitybillcalculator;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.util.ArrayList;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    Button buttonSign ,buttonLog ,getTotalBillButton, billshistoryButton;
    LinearLayout linearLayout;
    FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //if user is login
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null ){

            // if user is not verified
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if(!user.isEmailVerified()){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Your email is not verified");
                builder.setMessage("Please verify your Email then login");
                builder.setPositiveButton(" Resend ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(MainActivity.this, "Verification Email has been sent. ", Toast.LENGTH_SHORT).show();
                                firebaseAuth.signOut();
                                finish();
                            }
                        });

                    }
                });
                builder.setNegativeButton(" OK ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //close alert dialog
                        firebaseAuth.signOut();
                        finish();
                    }
                });
                builder.create().show();
            }

            linearLayout = findViewById(R.id.linearLayout);
            linearLayout.setVisibility(View.VISIBLE);
            billshistoryButton = findViewById(R.id.billshistoryButton);
            billshistoryButton.setVisibility(View.VISIBLE);
            //get total bill Amount
            TextView totalBillTextView = findViewById(R.id.totalBillTextView);
            getTotalBillButton = findViewById(R.id.getTotalBillButton);
            getTotalBillButton.setVisibility(View.VISIBLE);
            getTotalBillButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String totalAmountText = getTotalAmount();
                    totalBillTextView.setText("Rs "+totalAmountText);
                    getTotalBillButton.setText("Update");

                }
            });

        }
        // if user is not login
        else{

            buttonLog = findViewById(R.id.buttonLog);
            buttonLog.setVisibility(View.VISIBLE);
            buttonLog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MainActivity.this,LoginActivity.class) );
                }
            });

            buttonSign = findViewById(R.id.buttonSign);
            buttonSign.setVisibility(View.VISIBLE);
            buttonSign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MainActivity.this,SigninActivity.class) );
                }
            });

        }


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

