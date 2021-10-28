package com.example.canteenfpt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUpCustomer extends AppCompatActivity {
    TextView txtChangeChef, txtChangeSignIn;
    EditText edtPhone,edtEmail,edtPassword;
    Button btnSignUp;
    FirebaseAuth FAuth;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    String phone,emailid,password,role="Customer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_customer);

        edtPhone = findViewById(R.id.editTextPhone);
        edtEmail = findViewById(R.id.editTextEmail);
        edtPassword = findViewById(R.id.editTextPassword);

        txtChangeChef = findViewById(R.id.txtChangeChef);
        txtChangeSignIn = findViewById(R.id.txtChangeSignIn);

        btnSignUp = findViewById(R.id.btnSignUp);

        databaseReference = firebaseDatabase.getInstance().getReference("Customer");
        FAuth = FirebaseAuth.getInstance();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phone = edtPhone.getText().toString().trim();
                emailid = edtEmail.getText().toString().trim();
                password = edtPassword.getText().toString().trim();

                if(isValid()){
                    final ProgressDialog mDialog = new ProgressDialog(SignUpCustomer.this);
                    mDialog.setCancelable(false);
                    mDialog.setCanceledOnTouchOutside(false);
                    mDialog.setMessage("Registration in progress please wait......");
                    mDialog.show();

                    FAuth.createUserWithEmailAndPassword(emailid,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            String useridd = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            databaseReference = FirebaseDatabase.getInstance().getReference("Chef").child(useridd);
                            final HashMap<String , String> hashMap = new HashMap<>();
                            hashMap.put("Role",role);

                            databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    HashMap<String,String> hashMap1 = new HashMap<>();
                                    hashMap1.put("Phone Number",phone);
                                    hashMap1.put("EmailId",emailid);
                                    hashMap1.put("Password",password);

                                    firebaseDatabase.getInstance().getReference("Customer")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(hashMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            mDialog.dismiss();

                                            FAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpCustomer.this);
                                                        builder.setMessage("You Have Registered! Make Sure To Verify Your Email");
                                                        builder.setCancelable(false);
                                                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                dialogInterface.dismiss();

                                                                String phoneNumber = "+84" + phone ;
                                                                Intent b = new Intent(SignUpCustomer.this,CustomerVerifyPhone.class);
                                                                b.putExtra("phonenumber",phoneNumber);
                                                                startActivity(b);
                                                            }
                                                        });
                                                        AlertDialog alertDialog = builder.create();
                                                        alertDialog.show();
                                                    }else{
                                                        mDialog.dismiss();
                                                        ReusableCodeForAll.ShowAlert(SignUpCustomer.this,"Error",task.getException().getMessage());
                                                    }
                                                }
                                            });
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
            }
        });

        txtChangeChef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpCustomer.this,SignUpChef.class));
                finish();
            }
        });

        txtChangeSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpCustomer.this,SignIn.class));
                finish();
            }
        });
    }

    String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    public boolean isValid(){
        edtPhone.setError("");
        edtPassword.setError("");
        edtEmail.setError("");

        boolean isValid=false,isValidPhone=false,isValidEmail=false,isValidPassword=false;

        if(phone.isEmpty()){
            edtPhone.setError("Mobile Phone Number is Required");
            edtPhone.requestFocus();
        }else{
            isValidPhone = true;
        }

        if(emailid.isEmpty()){
            edtEmail.setError("Email Is Required");
            edtEmail.requestFocus();
        }else{
            if(emailid.matches(emailpattern)){
                isValidEmail = true;
            }else{
                edtEmail.setError("Enter a Valid Email Id");
            }
        }

        if(password.isEmpty()){
            edtPassword.setError("Enter Password");
            edtPassword.requestFocus();
        }else{
            if(password.length()<6){
                edtPassword.setError("Password is Weak");
            }else{
                isValidPassword=true;
            }
        }

        isValid = (isValidPhone && isValidEmail && isValidPassword) ? true : false;
        return isValid;
    };
}