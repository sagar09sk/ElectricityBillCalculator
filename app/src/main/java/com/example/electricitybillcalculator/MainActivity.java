package com.example.electricitybillcalculator;

import static android.content.ContentValues.TAG;
import androidx.appcompat.app.AppCompatActivity;
import java.time.LocalDate;
import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class MainActivity extends AppCompatActivity {
    TextView textViewTotalAmount;
    LinearLayout linearLayout;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FireBaseFireStoreHelper fireBaseFireStoreHelper;
    RecyclerView recyclerView;
    ArrayList<String> profileNameList, dateList, amountList;
    ImageView createProfileButton;
    String currentDate;
    @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LocalDate todayDate = LocalDate.now();
        currentDate = todayDate.toString();
        firebaseAuth = FirebaseAuth.getInstance();

        // if user not login
        if(firebaseAuth.getCurrentUser() == null ){
            Intent intent = new Intent( MainActivity.this,LoginActivity.class);
            startActivity(intent);
        }
        //if user is login
        if(firebaseAuth.getCurrentUser() != null ){
            //if user is not verify
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if(!user.isEmailVerified()){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Your email is not verified");
                builder.setMessage("Please verify your Email then login");
                builder.setPositiveButton(" Resend ", (dialogInterface, i) ->
                        user.sendEmailVerification().addOnSuccessListener(unused -> {
                            Toast.makeText(MainActivity.this, "Verification Email has been sent. ", Toast.LENGTH_SHORT).show();
                            firebaseAuth.signOut();
                            finish();
                        }));
                builder.setNegativeButton(" OK ", (dialogInterface, i) -> {
                    firebaseAuth.signOut();
                    finish();
                });
                builder.create().show();
            }

            //declare variables
            String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            firebaseFirestore = FirebaseFirestore.getInstance();
            fireBaseFireStoreHelper = new FireBaseFireStoreHelper();
            profileNameList = new ArrayList<>();
            dateList = new ArrayList<>();
            amountList = new ArrayList<>();
            textViewTotalAmount = findViewById(R.id.textViewTotalAmount);

            //get and set all last bills to RecyclerView
            recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            CustomAdapterForLastBill customAdapterForLastBill = new CustomAdapterForLastBill(this,userID,profileNameList,dateList,amountList);
            firebaseFirestore.collection("Profiles of "+userID).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        amountList.add(document.getString("Amount"));
                        profileNameList.add(document.getString("Profile Name"));
                        dateList.add(document.getString("Date"));
                        customAdapterForLastBill.notifyDataSetChanged();
                    }
                } else {
                    Log.w(TAG, "Error getting documents.", task.getException());
                }
                //get total
                int total = 0;
                for(String i : amountList){
                    int a = Integer.parseInt(i);
                    total = total + a;
                }
                textViewTotalAmount.setText("Total Amount = " + total  +"Rs" );
            });

            recyclerView.setAdapter(customAdapterForLastBill);

            linearLayout = findViewById(R.id.linearLayout);
            linearLayout.setVisibility(View.VISIBLE);

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
                    fireBaseFireStoreHelper.createProfileInFirebaseNew(MainActivity.this,createName,currentDate);
                    startActivity(new Intent(MainActivity.this,MainActivity.class));
                });
                createDialog.setNeutralButton("Cancel",(dialogInterface, i) -> {

                });
            createDialog.create().show();
            });


        }

    }

}

