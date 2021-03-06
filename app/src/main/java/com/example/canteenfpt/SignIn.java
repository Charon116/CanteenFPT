package com.example.canteenfpt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignIn extends AppCompatActivity {
    EditText edtEmail, edtPassword;
    Button btnSignin;
    TextView Forgotpassword, signup, signinchef;
    FirebaseAuth Fauth;
    String emailid,password;

    Application context;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        try{
            edtEmail = findViewById(R.id.editEmail);
            edtPassword = findViewById(R.id.editPassword);
            btnSignin = findViewById(R.id.btnsignin);
            Forgotpassword = findViewById(R.id.forgotpass);
            signup = findViewById(R.id.txtSignUp);
            signinchef =findViewById(R.id.txtsigninchef);

            Fauth = FirebaseAuth.getInstance();

            btnSignin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    emailid = edtEmail.getText().toString().trim();
                    password = edtPassword.getText().toString().trim();

                    if(isValid()){
                        final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                        mDialog.setCanceledOnTouchOutside(false);
                        mDialog.setCancelable(false);
                        mDialog.setMessage("??ang ????ng nh???p.......");
                        mDialog.show();

                        Fauth.signInWithEmailAndPassword(emailid,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    mDialog.dismiss();
                                    if(Fauth.getCurrentUser().isEmailVerified()) {
                                        mDialog.dismiss();
                                        Toast.makeText(SignIn.this, "????ng nh???p th??nh c??ng", Toast.LENGTH_SHORT).show();
                                        Intent Z = new Intent(SignIn.this, CustomerFoodPanel_BottomNavigation.class);
                                        //ReusableCodeForAll.ShowAlert(SignIn.this,"Verification Failed",Fauth.toString());
                                        startActivity(Z);
                                        finish();

                                    }else{
                                        ReusableCodeForAll.ShowAlert(SignIn.this,"Ch??a x??c nh???n","B???n ch??a x??c nh???n t??i kho???n qua Email");

                                    }
                                }else{
                                    mDialog.dismiss();
                                    ReusableCodeForAll.ShowAlert(SignIn.this,"L???i sai m???t kh???u","C???n nh???p l???i m???t kh???u");
                                }
                            }
                        });
                    }
                }
            });

            signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(SignIn.this,SignUpCustomer.class));
                    finish();
                }
            });
            Forgotpassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(SignIn.this,CustomerForgotPassword.class));
                    finish();
                }
            });
            signinchef.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(SignIn.this,SignInChef.class));
                    finish();
                }
            });
        }catch (Exception e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
    String emailpattern  = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    public boolean isValid(){
        edtEmail.setError("");
        edtPassword.setError("");
        boolean isvalid=false,isvalidemail=false,isvalidpassword=false;
        if(emailid.isEmpty()){
            edtEmail.setError("C???n nh???p v??o ?? Email");
        }else{
            if(emailid.matches(emailpattern)){
                isvalidemail=true;
            }else{
                edtEmail.setError("Email sai ?????a ch???");
            }
        }
        if(password.isEmpty()){
            edtPassword.setError("C???n nh???p v??o ?? m???t kh???u");
        }else{
            isvalidpassword=true;
        }
        isvalid=(isvalidemail && isvalidpassword)?true:false;
        return isvalid;
    }
}