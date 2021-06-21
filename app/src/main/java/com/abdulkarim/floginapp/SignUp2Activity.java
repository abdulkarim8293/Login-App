package com.abdulkarim.floginapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.textfield.TextInputLayout;
import com.hbb20.CountryCodePicker;

public class SignUp2Activity extends AppCompatActivity {

    TextInputLayout phoneNumber;
    CountryCodePicker countryCodePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2);

        countryCodePicker = findViewById(R.id.country_code_picker);
        phoneNumber = findViewById(R.id.sign_up_phone_number);
    }

    public void callVerifyActivity(View view) {
        /*if (!validatePhoneNumber()){
            return;
        }*/

        String _fullName = getIntent().getStringExtra("fullName");
        String _email = getIntent().getStringExtra("email");
        String _password = getIntent().getStringExtra("password");

        String _getUserEnteredPhoneNumber = phoneNumber.getEditText().getText().toString().trim();
        String _phoneNo = "+"+countryCodePicker.getFullNumber()+_getUserEnteredPhoneNumber;

        //Log.i("TAG" ," "+_fullName + " "+_email+" " +_password+ " "+_phoneNo);

        Intent intent = new Intent(SignUp2Activity.this,VerifyOTPActivity.class);

        intent.putExtra("fullName",_fullName);
        intent.putExtra("email",_email);
        intent.putExtra("password",_password);
        intent.putExtra("phoneNo",_phoneNo);

        intent.putExtra("whatToDo","createNewUser");
        startActivity(intent);
    }

    private boolean validatePhoneNumber() {
        String val = phoneNumber.getEditText().getText().toString().trim();
        String checkspaces = "Aw{1,20}z";
        if (val.isEmpty()) {
            phoneNumber.setError("Enter valid phone number");
            return false;
        } else if (!val.matches(checkspaces)) {
            phoneNumber.setError("No White spaces are allowed!");
            return false;
        } else {
            phoneNumber.setError(null);
            phoneNumber.setErrorEnabled(false);
            return true;
        }
    }
}