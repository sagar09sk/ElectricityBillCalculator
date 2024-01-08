package com.example.electricitybillcalculator;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {

    Button getTotalBillButton;
    LinearLayout linearLayout;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    RecyclerView recyclerView;
    ArrayList<String> profileNameList, dateList, amountList;
    ImageButton createProfileButton;
    String currentdate;

    ConstraintLayout constraintLayout;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDate todayDate = LocalDate.now();
            currentdate = todayDate.toString();
        }


        //if user is login
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null ){
            //if user is not verify
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

            String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            firebaseFirestore = FirebaseFirestore.getInstance();
            profileNameList = new ArrayList<>();
            dateList = new ArrayList<>();
            amountList = new ArrayList<>();


            //get and set all last bills to View
            recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            CustomAdapterForLastBill customAdapterForLastBill = new CustomAdapterForLastBill(this,profileNameList,dateList,amountList);
            firebaseFirestore.collection("Profiles of "+userID).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        amountList.add(""+document.getString("Amount"));
                        profileNameList.add(""+document.getString("Profile Name"));
                        dateList.add(""+document.getString("Date"));
                        customAdapterForLastBill.notifyDataSetChanged();
                    }
                } else {
                    Log.w(TAG, "Error getting documents.", task.getException());
                }
            });
            recyclerView.setAdapter(customAdapterForLastBill);


            linearLayout = findViewById(R.id.linearLayout);
            linearLayout.setVisibility(View.VISIBLE);


            //get total bill Amount
            getTotalBillButton = findViewById(R.id.getTotalBillButton);
            getTotalBillButton.setVisibility(View.VISIBLE);
            getTotalBillButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            // create profile
            createProfileButton = findViewById(R.id.createProfileButton);
            createProfileButton.setOnClickListener(view -> {
                EditText editTextCreate = new EditText(view.getContext());
                AlertDialog.Builder createDialog = new AlertDialog.Builder(view.getContext());
                createDialog.setTitle(" Enter Profile Name ");
                createDialog.setMessage(" profile Name must be Unique ");
                createDialog.setView(editTextCreate);
                createDialog.setPositiveButton(" Create ", (dialogInterface, i) -> {
                    String createName = editTextCreate.getText().toString();
                    FireBaseFireStoreHelper fireBaseFireStoreHelper = new FireBaseFireStoreHelper();
                    fireBaseFireStoreHelper.createProfileInFirebase(MainActivity.this,createName,currentdate);
                    startActivity(new Intent(MainActivity.this,MainActivity.class));
                });
                createDialog.setNegativeButton("Cancel",(dialogInterface, i) -> {

                });
            createDialog.create().show();
            });


        }

        // if user is not login
        else{
            constraintLayout = findViewById(R.id.constraintLayout);
            constraintLayout.setVisibility(View.VISIBLE);
        }

    }

    public void LoginButtonClick(View view){
        Intent intent = new Intent( MainActivity.this,LoginActivity.class);
        startActivity(intent);
    }
    public void SignInButtonClick(View view){
        Intent intent = new Intent( MainActivity.this,LoginActivity.class);
        startActivity(intent);
    }

}

