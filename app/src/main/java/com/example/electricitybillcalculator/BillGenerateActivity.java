package com.example.electricitybillcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;

public class BillGenerateActivity extends AppCompatActivity {


    private TextView nameTextView,nameinfo,previousTextView,previousinfo,currentTextView,currentinfo,unitTextView,unitinfo,rateTextView,rateinfo,amountTextView,amountinfo;
    private Button saveButton,updateButton;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_generate);

        //for date
        LocalDate todayDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            todayDate = LocalDate.now();
            date = todayDate.toString();
            TextView dateinfo = findViewById(R.id.dateinfo);
            dateinfo.setText(String.valueOf(todayDate));

        }

    }

    public void getDetailFromTable(View view){

        EditText numberinfo = findViewById(R.id.numberinfo);
        String id_number = numberinfo.getText().toString();
        int id = Integer.parseInt(id_number);

        BillData billData = new BillData(this);
        String name = billData.getNameFromTable(id);

        nameinfo = findViewById(R.id.nameinfo);
        nameinfo.setText(String.valueOf(name));

        String previousText = billData.getPreviousReading(name);

        previousinfo = findViewById(R.id.previousinfo);
        previousinfo.setText(String.valueOf(previousText));

        //set invisible text view to visible
        nameTextView = findViewById(R.id.nameTextView);
        nameTextView.setVisibility(View.VISIBLE);

        previousTextView = findViewById(R.id.previousTextView);
        previousTextView.setVisibility(View.VISIBLE);

        currentTextView = findViewById(R.id.currentTextView);
        currentTextView.setVisibility(View.VISIBLE);

        currentinfo = findViewById(R.id.currentinfo);
        currentinfo.setVisibility(View.VISIBLE);

        updateButton = (Button)findViewById(R.id.updateButton);
        updateButton.setVisibility(View.VISIBLE);


        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String currentText = currentinfo.getText().toString();
                int pre = Integer.parseInt(previousText);
                int cur = Integer.parseInt(currentText);
                int unit = cur-pre;
                unitinfo = findViewById(R.id.unitinfo);
                unitinfo.setText(String.valueOf(unit));

                int amount = unit * 5 ;
                String amountText = String.valueOf(amount);
                amountinfo = findViewById(R.id.amountinfo);
                amountinfo.setText("Rs " + String.valueOf(amount));

                // for set rate
                rateinfo = findViewById(R.id.rateinfo);
                rateinfo.setText("Rs 5 ");

                //set invisible text view to visible
                unitTextView = findViewById(R.id.unitTextView);
                unitTextView.setVisibility(View.VISIBLE);

                unitinfo.setVisibility(View.VISIBLE);

                rateTextView = findViewById(R.id.rateTextView);
                rateTextView.setVisibility(View.VISIBLE);

                rateinfo.setVisibility(View.VISIBLE);

                amountTextView = findViewById(R.id.amountTextView);
                amountTextView.setVisibility(View.VISIBLE);

                amountinfo.setVisibility(View.VISIBLE);

                saveButton = (Button)findViewById(R.id.saveButton);
                saveButton.setVisibility(View.VISIBLE);


                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        billData.addNewBill(name , date ,previousText, currentText,String.valueOf(amountText));
                        billData.updateAmount(name,amountText);
                        Toast.makeText(BillGenerateActivity.this, " successfully Save ", Toast.LENGTH_SHORT).show();

                        finish();

                    }
                });

            }
        });


    }


}