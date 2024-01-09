package com.example.electricitybillcalculator;


import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;

public class BillGenerateActivity extends AppCompatActivity {

    private TextView previousinfo;
    private EditText currentinfo;
    private TextView unitinfo;
    private TextView amountinfo;
    private Button saveButton;
    String currentDate,previousReading,currentReading;

    @SuppressLint({"CutPasteId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_generate);

        //for get current date
        LocalDate todayDate;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            todayDate = LocalDate.now();
            currentDate = todayDate.toString();
            TextView dateinfo = findViewById(R.id.dateinfo);
            dateinfo.setText(String.valueOf(todayDate));
        }

        FireBaseFireStoreHelper fireBaseFireStoreHelper = new FireBaseFireStoreHelper();
        String name = getIntent().getStringExtra("Name");
        String date = getIntent().getStringExtra("Date");

        TextView nameinfo = findViewById(R.id.nameinfo);
        previousinfo = findViewById(R.id.previousinfo);
        nameinfo.setText(name);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String userID = firebaseAuth.getCurrentUser().getUid();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        // Edit bill
        if(date.equals(currentDate)){
            firebaseFirestore.collection("Bills of "+ name +" of " +userID).document("date " +date).get().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        previousinfo.setText(document.getString("Previous Reading"));
                        previousReading = document.getString("Previous Reading");
                    }
                }
            });
        //new bill
        }else{
            firebaseFirestore.collection("Bills of "+ name +" of " +userID).document("date " +date).get().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        previousinfo.setText(document.getString("Current Reading"));
                        previousReading = document.getString("Current Reading");
                    }
                }
            });
        }

        TextView rateinfo = findViewById(R.id.rateinfo);
        rateinfo.setText("Rs 5 ");

        //calculate Amount
        Button updateButton = findViewById(R.id.updateButton);
        updateButton.setOnClickListener(view -> {

            LinearLayout layoutUnit = findViewById(R.id.layoutUnit);
            LinearLayout layoutAmount = findViewById(R.id.layoutAmount);
            LinearLayout layoutButton = findViewById(R.id.layoutButton);
            layoutUnit.setVisibility(View.VISIBLE);
            layoutAmount.setVisibility(View.VISIBLE);
            layoutButton.setVisibility(View.VISIBLE);
            currentinfo = findViewById(R.id.currentinfo);
            currentReading = currentinfo.getText().toString();
            int pre = Integer.parseInt(previousReading);
            int cur = Integer.parseInt(currentReading);
            int unit = cur-pre;
            unitinfo = findViewById(R.id.unitinfo);
            unitinfo.setText(String.valueOf(unit));

            int amount = unit * 5 ;
            amountinfo = findViewById(R.id.amountinfo);
            amountinfo.setText("Rs " + amount);

            saveButton = findViewById(R.id.saveButton);
            saveButton.setOnClickListener(view1 -> {
                fireBaseFireStoreHelper.addBillInFirebase(this,name,currentDate,previousReading,currentReading,String.valueOf(amount));
                fireBaseFireStoreHelper.updatebillInprofile(this,name,date, String.valueOf(amount));
                startActivity(new Intent(BillGenerateActivity.this,MainActivity.class));
                finish();
            });
        });


    }

}