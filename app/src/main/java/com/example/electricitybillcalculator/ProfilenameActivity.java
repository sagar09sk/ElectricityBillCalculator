package com.example.electricitybillcalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class ProfilenameActivity extends AppCompatActivity {

    ListView profilenamelistView;
    FireBaseFireStoreHelper fireBaseFireStoreHelper;

    public static final String nameIntent = "electricitybillcalculator.Profilename.name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilename);


        profilenamelistView = findViewById(R.id.profilenamelistView);
        fireBaseFireStoreHelper = new FireBaseFireStoreHelper();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String userID = firebaseAuth.getCurrentUser().getUid();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        ArrayList<String> nameList = new ArrayList<>();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this , android.R.layout.simple_list_item_1,nameList);

        firebaseFirestore.collection("Profiles of "+userID).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                for(QueryDocumentSnapshot document: task.getResult()){
                    nameList.add(""+document.getString("Profile Name"));
                    arrayAdapter.notifyDataSetChanged();
                }
            }
        });
        profilenamelistView.setAdapter(arrayAdapter);


        profilenamelistView.setOnItemClickListener((adapterView, view, i, l) -> {
            String name = ((TextView) view).getText().toString();
            Intent intent = new Intent(ProfilenameActivity.this , BillByProfileActivity.class);
            intent.putExtra(nameIntent, name);
            startActivity(intent);

            //For long press delete function
            registerForContextMenu(profilenamelistView);

        });

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
                return true;
            default:
                return super.onContextItemSelected(item);

        }

    }

    void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete ?");
        builder.setMessage("Are you sure you want delete ?");

        builder.setPositiveButton("Yes", (dialogInterface, i) -> {
            //delete operation

        });
        builder.setNegativeButton("No", (dialogInterface, i) ->
                Toast.makeText(ProfilenameActivity.this, "Canceled", Toast.LENGTH_SHORT).show()
        );
        builder.create().show();
    }



}