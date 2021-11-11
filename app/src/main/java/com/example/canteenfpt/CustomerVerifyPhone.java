package com.example.canteenfpt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class CustomerVerifyPhone extends AppCompatActivity {
    String verificationId;
    FirebaseAuth FAuth;
    Button verify, resend;
    TextView txt;
    EditText enterCode;
    String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_verify_phone);

        phone = getIntent().getStringExtra("phonenumber").trim();

        enterCode = findViewById(R.id.code);
        txt = findViewById(R.id.text);
        resend = findViewById(R.id.Resendotp);

        verify =  findViewById(R.id.Verify);
        FAuth = FirebaseAuth.getInstance();

        resend.setVisibility(View.INVISIBLE);
        txt.setVisibility(View.INVISIBLE);

        sendverificationcode(phone);

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = enterCode.getText().toString().trim();
                resend.setVisibility(View.INVISIBLE);
                if (code.isEmpty() && code.length()<6){
                    enterCode.setError("Enter code");
                    enterCode.requestFocus();
                    return;
                }
                verifyCode(code);
            }
        });

        new CountDownTimer(60000,1000){
            @Override
            public void onTick(long millisUntilFinished) {
                txt.setVisibility(View.VISIBLE);
                txt.setText("Resend Code Within"+millisUntilFinished/1000+"Seconds");
            }
            /**
             * Callback fired when the time is up.
             */
            @Override
            public void onFinish() {
                resend.setVisibility(View.VISIBLE);
                txt.setVisibility(View.INVISIBLE);
            }
        }.start();

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resend.setVisibility(View.INVISIBLE);
                Resendotp(phone);

                new CountDownTimer(60000,1000){

                    @Override
                    public void onTick(long millisUntilFinished) {

                        txt.setVisibility(View.VISIBLE);
                        txt.setText("Resend Code Within"+millisUntilFinished/1000+"Seconds");

                    }

                    /**
                     * Callback fired when the time is up.
                     */
                    @Override
                    public void onFinish() {
                        resend.setVisibility(View.VISIBLE);
                        txt.setVisibility(View.INVISIBLE);

                    }
                }.start();
            }
        });

    }

    private void Resendotp(String phone) {
        sendverificationcode(phone);
    }

    private void sendverificationcode(String number) {
        PhoneAuthProvider.verifyPhoneNumber(
                PhoneAuthOptions
                        .newBuilder(FirebaseAuth.getInstance())
                        .setActivity(CustomerVerifyPhone.this)
                        .setPhoneNumber(number)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                String code = phoneAuthCredential.getSmsCode();
                                if(code != null){
                                    enterCode.setText(code);  // Auto Verification
                                    verifyCode(code);
                                }
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(CustomerVerifyPhone.this , e.getMessage(),Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCodeSent(String s , PhoneAuthProvider.ForceResendingToken forceResendingToken){
                                super.onCodeSent(s,forceResendingToken);
                                verificationId = s;
                            }
                        })
                        .build());
        FAuth.setLanguageCode("vi");
    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId , code);
        linkCredential(credential);
    }

    private void linkCredential(PhoneAuthCredential credential) {
        FAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(CustomerVerifyPhone.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(CustomerVerifyPhone.this , MainMenu.class);
                            startActivity(intent);
                            finish();
                        }else{
                            ReusableCodeForAll.ShowAlert(CustomerVerifyPhone.this,"Error",task.getException().getMessage());
                        }
                    }
                });
    }
}