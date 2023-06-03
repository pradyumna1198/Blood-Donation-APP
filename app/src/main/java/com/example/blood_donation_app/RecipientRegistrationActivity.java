package com.example.blood_donation_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RecipientRegistrationActivity extends AppCompatActivity {

    private TextView backButton;

    private TextInputEditText registerFullName,registerIDNumber,registerPhoneNumber,registerEmail,registerPassword;

    private Spinner bloodGroupSpinner;

    private Button registerButton;

    private ProgressDialog loader;

    private FirebaseAuth mAuth;
    private DatabaseReference userDatabaseRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipient_registration);

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecipientRegistrationActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });


        registerPassword = findViewById(R.id.registerPassword) ;
        registerEmail = findViewById(R.id.registerEmail);
        registerPhoneNumber = findViewById(R.id.registerPhoneNumber);
        registerIDNumber = findViewById(R.id.registerIDNumber);
        registerFullName = findViewById(R.id.registerFullName);
        bloodGroupSpinner =findViewById(R.id.bloodGroupSpinner);
        registerButton = findViewById(R.id.registerButton);
        loader = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email =  registerEmail.getText().toString().trim();
                final String password = registerPassword.getText().toString().trim();
                final String fullName = registerFullName.getText().toString().trim();
                final String idNumber = registerIDNumber.getText().toString().trim();
                final String bloodGroup = bloodGroupSpinner.getSelectedItem().toString();
                final String phoneNumber = registerPhoneNumber.getText().toString().trim();


                if (TextUtils.isEmpty(email)){
                    registerEmail.setError("Your Email is required!");
                    return ;
                }

                if (TextUtils.isEmpty(password)){
                    registerPassword.setError("Password is required!");
                    return ;
                }
                if (TextUtils.isEmpty(phoneNumber )){
                    registerPhoneNumber.setError("Your Phone Number is required!");
                    return ;
                }

                if (TextUtils.isEmpty(fullName)){
                    registerFullName.setError("Your Full Name is required!");
                    return ;
                }
                if (TextUtils.isEmpty(idNumber)){
                    registerIDNumber.setError("Id Number is required!");
                    return ;
                }
                if (bloodGroup.equals("Select your blood group")){
                    Toast.makeText(RecipientRegistrationActivity.this, "Select Blood Group", Toast.LENGTH_SHORT).show();
                    return ;
                }
                else{

                    loader.setMessage("Registering You....");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();

                    mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()){
                                String error = task.getException().toString();
                                Toast.makeText(RecipientRegistrationActivity.this, "Error" + error, Toast.LENGTH_SHORT).show();
                            }
                            else{
                                String currentUserId =  mAuth.getCurrentUser().getUid();
                                userDatabaseRef = FirebaseDatabase.getInstance().getReference()
                                        .child("users").child(currentUserId);
                                HashMap userInfo= new HashMap();
                                userInfo.put("id",currentUserId);
                                userInfo.put("name",fullName);
                                userInfo.put("email",email);
                                userInfo.put("phonenumber",phoneNumber);
                                userInfo.put("idnumber",idNumber);
                                userInfo.put("bloodgroup",bloodGroup);
                                userInfo.put("type","recipient");
                                userInfo.put("search","recipient"+bloodGroup);

                                userDatabaseRef.updateChildren(userInfo).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(RecipientRegistrationActivity.this, "Data set successfully", Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            Toast.makeText(RecipientRegistrationActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                        }

                                        finish();

                                    }
                                });

                                Intent intent = new Intent(RecipientRegistrationActivity.this,MainActivity.class);
                                startActivity(intent);
                                finish();
                                loader.dismiss();
                            }
                        }
                    });



                }
            }
        });

    }
}