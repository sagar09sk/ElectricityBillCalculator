package com.example.electricitybillcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateProfileActivity extends AppCompatActivity {

    private BillData billData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        billData = new BillData(this );

        Button createButton = findViewById(R.id.createButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText nameText = findViewById(R.id.nameText);
                String name1 = String.valueOf(nameText.getText());
                SQLiteDatabase DB = billData.getWritableDatabase();
                billData.createProfileTable(DB,name1);
                billData.addProfileInBill(name1,"0");
                Toast.makeText(CreateProfileActivity.this, "Profile Successfully Created For "+name1, Toast.LENGTH_SHORT).show();
                finish();

            }
        });

    }
}