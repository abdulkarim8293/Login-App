package com.abdulkarim.floginapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.textfield.TextInputLayout;

public class SignUpActivity extends AppCompatActivity {

    TextInputLayout fullName,email,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        fullName = findViewById(R.id.sign_up_fullName);
        email = findViewById(R.id.sign_up_email);
        password = findViewById(R.id.sign_up_password);
    }

    public void callSignUp2Activity(View view) {

        if (!validateFullName() | !validateEmail() | !validatePassword()){
            return;
        }

        String _fullName = fullName.getEditText().getText().toString().trim();
        String _email = email.getEditText().getText().toString().trim();
        String _password = password.getEditText().getText().toString();

        Intent intent = new Intent(SignUpActivity.this,SignUp2Activity.class);
        intent.putExtra("fullName",_fullName);
        intent.putExtra("email",_email);
        intent.putExtra("password",_password);
        startActivity(intent);
    }

    // validation function
    private boolean validateFullName(){
        String value = fullName.getEditText().getText().toString().trim();
        if (value.isEmpty()){
            fullName.setError("Field can not be empty");
            return false;
        }else {
            fullName.setError(null);
            fullName.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validateEmail() {
        String val = email.getEditText().getText().toString().trim();
        String checkEmail = "[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+";

        if (val.isEmpty()) {
            email.setError("Field can not be empty");
            return false;
        } else if (!val.matches(checkEmail)) {
            email.setError("Invalid Email!");
            return false;
        } else {
            email.setError(null);
            email.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validatePassword() {
        String val = password.getEditText().getText().toString().trim();
        String checkPassword = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";

        if (val.isEmpty()) {
            password.setError("Field can not be empty");
            return false;
        } /*else if (!val.matches(checkPassword)) {
            password.setError("Password should contain 4 characters!");
            return false;
        }*/ else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }
}