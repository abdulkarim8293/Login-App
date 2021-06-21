package com.abdulkarim.floginapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

public class ForgetPasswordActivity extends AppCompatActivity {
    CountryCodePicker countryCodePicker;
    TextInputLayout phoneNumberTextField;
    Button forgetPasswordNextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        countryCodePicker = findViewById(R.id.country_code_picker);
        phoneNumberTextField = findViewById(R.id.forget_password_phone_number);
        forgetPasswordNextButton = findViewById(R.id.forget_password_next_button);

    }

    public void verifyPhoneNumber(View view) {

        String _phoneNumber = phoneNumberTextField.getEditText().getText().toString().trim();

       /* if (_phoneNumber.charAt(0)=='0'){
            _phoneNumber = _phoneNumber.substring(1);
        }*/
        final String _completePhoneNumber = "+"+countryCodePicker.getFullNumber()+_phoneNumber;

        Query checkUser = FirebaseDatabase.getInstance().getReference("users").orderByChild("phoneNo").equalTo(_completePhoneNumber);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

               if (dataSnapshot.exists()){
                   Toast.makeText(ForgetPasswordActivity.this, ""+_completePhoneNumber, Toast.LENGTH_SHORT).show();
                   phoneNumberTextField.setError(null);
                   phoneNumberTextField.setErrorEnabled(false);
                   Intent intent = new Intent(getApplicationContext(),VerifyOTPActivity.class);
                   intent.putExtra("phoneNo",_completePhoneNumber);
                   intent.putExtra("whatToDo","updateData");
                   startActivity(intent);
                   finish();

               }else {
                   phoneNumberTextField.setError("No such users exist");
                   phoneNumberTextField.requestFocus();
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ForgetPasswordActivity.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}