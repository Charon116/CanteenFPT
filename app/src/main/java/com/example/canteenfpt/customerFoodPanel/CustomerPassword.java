package com.example.canteenfpt.customerFoodPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.canteenfpt.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CustomerPassword extends AppCompatActivity {

    EditText current, newpw, confirm;
    Button change_pw;
    String cur, newps, conf, email, password;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_password);

        current =  findViewById(R.id.current_pw);
        newpw =  findViewById(R.id.new_pw);
        confirm =  findViewById(R.id.confirm_pw);
        change_pw = (Button) findViewById(R.id.change);

        final String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Customer").child(userid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Customer customer = snapshot.getValue(Customer.class);
                email = customer.getEmailId();

                change_pw.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cur = current.getText().toString().trim();
                        newps = newpw.getText().toString().trim();
                        conf = confirm.getText().toString().trim();

                        if (isvalid()) {
                            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            AuthCredential credential = EmailAuthProvider.getCredential(email, cur);

                            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        user.updatePassword(newps).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {

                                                    String userid=FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                    FirebaseDatabase.getInstance().getReference("Customer").child(userid).child("Password").setValue(newps);
                                                    FirebaseDatabase.getInstance().getReference("Customer").child(userid).child("ConfirmPassword").setValue(conf);

                                                    Toast.makeText(CustomerPassword.this, "???? thay ?????i m???t kh???u", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(CustomerPassword.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    } else {
                                        Toast.makeText(CustomerPassword.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public boolean isvalid(){
        newpw.setError("");
        confirm.setError("");
        boolean isValidnewpassword = false, isValidconfirmpasswoord = false, isvalid = false;

        if (newps.isEmpty()) {
            newpw.setError("C???n nh???p v??o ?? m???t kh???u m???i");

        } else {
            if (newps.length() < 6) {
                newpw.setError("M???t kh???u qu?? y???u d??? b??? m???t");
                confirm.setError("M???t kh???u qu?? y???u d??? b??? m???t");
            } else {
                isValidnewpassword = true;
            }
        }

        if (conf.isEmpty()) {
            confirm.setError("C???n nh???p v??o ?? x??c th???c m???t kh???u");
        } else {
            if (!newps.equals(conf)) {
                newpw.setError("Kh??ng ????ng m???t kh???u x??c th???c");
                confirm.setError("Kh??ng ????ng m???t kh???u m???i");
            } else {
                isValidconfirmpasswoord = true;
            }
        }
        isvalid = (isValidnewpassword && isValidconfirmpasswoord) ? true : false;
        return isvalid;
    }
}