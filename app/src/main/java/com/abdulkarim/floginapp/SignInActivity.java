package com.abdulkarim.floginapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

public class SignInActivity extends AppCompatActivity {
    CountryCodePicker countryCodePicker;
    TextInputLayout phoneNumber,password;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        countryCodePicker = findViewById(R.id.sign_in_country_code_picker);
        phoneNumber = findViewById(R.id.sign_in_phone_number);
        password = findViewById(R.id.sign_in_password);
        progressBar = findViewById(R.id.sign_in_progress_bar);

        progressBar.setVisibility(View.GONE);


    }

    public void goToHomePage(View view) {
        if (!validateFields()){
            return;
        }
        String _phoneNumber = phoneNumber.getEditText().getText().toString().trim();
        String _password = password.getEditText().getText().toString().trim();
        if (_phoneNumber.charAt(0)=='0'){
            _phoneNumber = _phoneNumber.substring(1);
        }
        String _completePhoneNumber = "+"+countryCodePicker.getFullNumber()+_phoneNumber;

        Query checkUser = FirebaseDatabase.getInstance().getReference("users").orderByChild("phoneNo").equalTo(_completePhoneNumber);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    phoneNumber.setError(null);
                    phoneNumber.setErrorEnabled(false);
                    String systemPassword = dataSnapshot.child(_completePhoneNumber).child("password").getValue(String.class);
                    if (systemPassword.equals(_password)){
                        password.setError(null);
                        password.setErrorEnabled(false);

                        String _fullName = dataSnapshot.child(_completePhoneNumber).child("fullName").getValue(String.class);
                        String _email = dataSnapshot.child(_completePhoneNumber).child("email").getValue(String.class);
                        String _phoneNo = dataSnapshot.child(_completePhoneNumber).child("phoneNo").getValue(String.class);

                        Intent intent = new Intent(SignInActivity.this,MainActivity.class);
                        startActivity(intent);

                        Toast.makeText(SignInActivity.this, ""+_fullName + " "+_email+" " +_phoneNo, Toast.LENGTH_SHORT).show();


                    }else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(SignInActivity.this, "Password does not match", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(SignInActivity.this, "No such user exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SignInActivity.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }

    private boolean validateFields(){
        String _phoneNumber = phoneNumber.getEditText().getText().toString().trim();
        String _password = password.getEditText().getText().toString().trim();
        if (_phoneNumber.isEmpty()){
            phoneNumber.setError("Phone number can not be empty");
            phoneNumber.requestFocus();
            return false;
        }else if (_password.isEmpty()){
            password.setError("password can not be empty");
            password.requestFocus();
            return false;
        }else {
            return true;
        }
    }

    public void callSignUpActivity(View view) {
        Intent intent = new Intent(SignInActivity.this,SignUpActivity.class);
        startActivity(intent);
    }


    public void callForgetPasswordActivity(View view) {
        Intent intent = new Intent(SignInActivity.this,ForgetPasswordActivity.class);
        startActivity(intent);
    }
}