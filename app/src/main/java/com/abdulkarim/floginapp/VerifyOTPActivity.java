package com.abdulkarim.floginapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.abdulkarim.floginapp.model.User;
import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class VerifyOTPActivity extends AppCompatActivity {

    PinView pinView;
    String codeBySystem;

    String fullName,phoneNo,email,password;
    String whatToDo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_o_t_p);

        pinView = findViewById(R.id.pin_view);

        fullName = getIntent().getStringExtra("fullName");
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        phoneNo = getIntent().getStringExtra("phoneNo");
        whatToDo = getIntent().getStringExtra("whatToDo");

        sendVerificationCodeToUser(phoneNo);
    }


    private void sendVerificationCodeToUser(String phoneNo) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNo,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                TaskExecutors.MAIN_THREAD,// Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    codeBySystem = s;
                }

                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                    String code = phoneAuthCredential.getSmsCode();
                    if (code!= null){
                        pinView.setText(code);
                        verifyCode(code);
                    }
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    Toast.makeText(VerifyOTPActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            };

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeBySystem,code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Verification completed successfully here Either
                            // store the data or do whatever desire
                           if (whatToDo.equals("updateData")) {
                                updateOldUsersData();

                            } else if (whatToDo.equals("createNewUser")) {

                               storeNewUsersData();
                            }
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(VerifyOTPActivity.this, "Verification Not Completed! Try again.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void updateOldUsersData() {
        Intent intent = new Intent(getApplicationContext(),SetNewPasswordActivity.class);
        intent.putExtra("phoneNo",phoneNo);
        startActivity(intent);
        finish();
    }

    private void storeNewUsersData() {

        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("users");

        User addNewUser = new User(fullName,email,phoneNo,password);

        reference.child(phoneNo).setValue(addNewUser);
    }


    public void callNextScreenFromOTP(View view) {

        String code = pinView.getText().toString();
        if (!code.isEmpty()){
            verifyCode(code);
        }
    }
}