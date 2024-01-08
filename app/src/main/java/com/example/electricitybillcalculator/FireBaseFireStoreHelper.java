package com.example.electricitybillcalculator;

import android.content.Context;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FireBaseFireStoreHelper {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    String userID = firebaseAuth.getCurrentUser().getUid();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();


    public void createProfileInFirebase(Context context, String profileName , String date ){
        DocumentReference ProfilesCollection = firebaseFirestore.collection("Profiles of "+userID ).document(profileName);
        Map<String,Object> profile = new HashMap<>();
        profile.put("Profile Name",profileName);
        profile.put("Amount", "0");
        profile.put("Date" , date);
        ProfilesCollection.set(profile).addOnSuccessListener(unused ->
                Toast.makeText(context, "Profile created", Toast.LENGTH_SHORT).show()
        ).addOnFailureListener(e ->
                Toast.makeText(context, "failed "+ e , Toast.LENGTH_SHORT).show()
        );

        DocumentReference billsCollection = firebaseFirestore.collection("Bills of "+ profileName +" of " +userID).document("date " +date);
        Map<String,Object> bill = new HashMap<>();
        bill.put("Previous Reading","0");
        bill.put("Current Reading","0");
        bill.put("Amount","0");
        bill.put("Date" , date);
        billsCollection.set(bill).addOnSuccessListener(unused ->
                Toast.makeText(context, "bill saved", Toast.LENGTH_SHORT).show()
        ).addOnFailureListener(e ->
                Toast.makeText(context, "failed "+ e , Toast.LENGTH_SHORT).show()
        );
    }


    public void addBillInFirebase(Context context,String profileName , String date ,String previousReading ,String currentReading,String amount){
        DocumentReference billsCollection = firebaseFirestore.collection("Bills of "+ profileName +" of " +userID).document("date " +date);
        Map<String,Object> bill = new HashMap<>();
        bill.put("Previous Reading",previousReading);
        bill.put("Current Reading",currentReading);
        bill.put("Amount",amount);
        bill.put("Date" , date);
        billsCollection.set(bill).addOnSuccessListener(unused ->
                Toast.makeText(context, "bill saved", Toast.LENGTH_SHORT).show()
        ).addOnFailureListener(e ->
                Toast.makeText(context, "failed "+ e ,Toast.LENGTH_SHORT).show()
        );
    }


    public void updatebillInprofile(Context context,String profileName , String date ,String amount){
        DocumentReference ProfilesCollection = firebaseFirestore.collection("Profiles of "+userID ).document(profileName);
        Map<String,Object> profile = new HashMap<>();
        profile.put("Profile Name",profileName);
        profile.put("Amount", amount);
        profile.put("Date" , date);
        ProfilesCollection.set(profile).addOnSuccessListener(unused ->
                Toast.makeText(context, "bill updated", Toast.LENGTH_SHORT).show()
        ).addOnFailureListener(e ->
                Toast.makeText(context, "failed "+ e , Toast.LENGTH_SHORT).show()
        );
    }



}
