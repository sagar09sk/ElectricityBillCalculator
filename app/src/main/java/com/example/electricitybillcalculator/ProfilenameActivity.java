package com.example.electricitybillcalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ProfilenameActivity extends AppCompatActivity {

    ListView profilenamelistView;
    BillData billData;
    ArrayList<String> nameList;
    String name;

    public static final String nameIntent = "electricitybillcalculator.Profilename.name";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilename);


        profilenamelistView = findViewById(R.id.profilenamelistView);

        nameList = new ArrayList<>();


        billData = new BillData(this);
        storeDataInArray();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this , android.R.layout.simple_list_item_1,nameList);
        profilenamelistView.setAdapter(arrayAdapter);

        profilenamelistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                name = new String();
                name = ((TextView) view).getText().toString();

                Intent intent = new Intent(ProfilenameActivity.this , BillByProfileActivity.class);
                intent.putExtra(nameIntent, name);
                startActivity(intent);

                //For long press delete function
                registerForContextMenu(profilenamelistView);

            }
        });

    }

    void storeDataInArray(){
        Cursor cursor = billData.readNameFromBillTable();
        if(cursor.getCount() ==  0){
            Toast.makeText(this, "No Data found", Toast.LENGTH_SHORT).show();
        }
        else{
            while(cursor.moveToNext()){
                nameList.add(cursor.getString(0));
            }
        }

    }



//For delete
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.delete_menu,menu);

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.deleteItem:
                confirmDialog();
                return true;
            case R.id.cancelItem:
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onContextItemSelected(item);

        }

    }

    void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete ?");
        builder.setMessage("Are you sure you want delete ?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //delete operation

                long result = billData.deleteProfile(name);

                if(result == -1 ){
                    Toast.makeText(ProfilenameActivity.this, "Failed to delete", Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(ProfilenameActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                }

            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(ProfilenameActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
            }
        });
        builder.create().show();
    }



}