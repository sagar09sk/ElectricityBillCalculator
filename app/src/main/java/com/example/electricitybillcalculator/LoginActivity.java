package com.example.electricitybillcalculator;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText editTextTextEmailAddress,editTextTextPassword;
    Button buttonLoginAccount;
    FirebaseAuth firebaseAuth;
    TextView dontHaveAccount , forgetPassword;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextTextEmailAddress = findViewById(R.id.editTextTextEmailAddress);
        editTextTextPassword = findViewById(R.id.editTextTextPassword);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        buttonLoginAccount = findViewById(R.id.buttonLoginAccount);
        firebaseAuth = FirebaseAuth.getInstance();

        //for already have account
        dontHaveAccount = findViewById(R.id.dontHaveAccount);
        dontHaveAccount.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(),SigninActivity.class));
            finish();
        });

        //forget password
        forgetPassword = findViewById(R.id.forgetPassword);
        forgetPassword.setOnClickListener(view -> {
            final EditText editTextEmail = new EditText(view.getContext());
            final AlertDialog.Builder passwordDialog = new AlertDialog.Builder(view.getContext());
            passwordDialog.setTitle(" Get Email to reset password ");
            passwordDialog.setMessage(" Enter Your Email ");
            passwordDialog.setView(editTextEmail);

            passwordDialog.setPositiveButton(" Yes ", (dialogInterface, i) -> {
                String email = editTextEmail.getText().toString();
                firebaseAuth.sendPasswordResetEmail(email).addOnSuccessListener(unused -> {
                    Toast.makeText(LoginActivity.this, "Reset Link sent to your Email", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finish();
                }).addOnFailureListener(e ->
                        Toast.makeText(LoginActivity.this, "Error ! "+ e , Toast.LENGTH_SHORT).show()
                );
            });
            passwordDialog.setNegativeButton(" Cancel ", (dialogInterface, i) -> {
                //close alert dialog
            });
            passwordDialog.create().show();
        });

        //login process
        buttonLoginAccount.setOnClickListener(view -> {
            String email = editTextTextEmailAddress.getText().toString().trim();
            String password = editTextTextPassword.getText().toString().trim();

            if(TextUtils.isEmpty(email)){
                editTextTextEmailAddress.setError("Email is Required");
                return;
            }
            if(TextUtils.isEmpty(password)){
                editTextTextEmailAddress.setError("Password is Required");
                return;
            }
            if(password.length() < 6){
                editTextTextPassword.setError("Password is must be more than 6 Characters");
                return;
            }

            //authenticate account
            firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                progressBar.setVisibility(View.VISIBLE);
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Logged in  SuccessFully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finish();
                }else{
                    Toast.makeText(LoginActivity.this, "Login failed "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);

                }

            });

        });


    }
}