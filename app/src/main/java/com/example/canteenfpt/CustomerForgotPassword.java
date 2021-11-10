package com.example.canteenfpt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class CustomerForgotPassword extends AppCompatActivity {
    EditText emailEditText;
    Button resetPasswordButton;
    ProgressBar progressBar;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_forgot_password);

        emailEditText = (EditText) findViewById(R.id.editEmail);
        resetPasswordButton = (Button) findViewById(R.id.resetPassword);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        auth = FirebaseAuth.getInstance();



        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog mDialog = new ProgressDialog(CustomerForgotPassword.this);
                mDialog.setCanceledOnTouchOutside(false);
                mDialog.setCancelable(false);
                mDialog.setMessage("Logging in...");
                mDialog.show();

                auth.sendPasswordResetEmail(emailEditText.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            mDialog.dismiss();
                            ReusableCodeForAll.ShowAlert(CustomerForgotPassword.this,"","Kiểm tra email của bạn để reset mật khẩu!");

                        }else{
                            mDialog.dismiss();
                            ReusableCodeForAll.ShowAlert(CustomerForgotPassword.this,"Lỗi",task.getException().getMessage());
                        }
                    }
                });
            }
        });
    }

}