package com.abdulkarim.floginapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SetNewPasswordActivity extends AppCompatActivity {
    private TextInputLayout newPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_password);

        newPassword = findViewById(R.id.new_password);
    }

    public void setNewPasswordBtn(View view) {
        String _newPassword = newPassword.getEditText().getText().toString().trim();
        String _phoneNumber = getIntent().getStringExtra("phoneNo");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        reference.child(_phoneNumber).child("password").setValue(_newPassword);

        Intent intent = new Intent(getApplicationContext(),ForgetPasswordSuccessActivity.class);
        startActivity(intent);
        finish();

    }

}