package com.example.canteenfpt.customerFoodPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.canteenfpt.CustomerFoodPanel_BottomNavigation;
import com.example.canteenfpt.R;
import com.example.canteenfpt.UpdateFoodModel;
import com.example.canteenfpt.chefFoodPanel.Chef;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class OrderFood extends AppCompatActivity {
    String RandomId, ChefID;
    ImageView imageView;
    ElegantNumberButton additem;
    TextView FoodName, ChefName, FoodQuantity, FoodPrice, FoodDescription;
    DatabaseReference databaseReference, dataaa, chefdata, reference, data, dataref;
    String mobileNo,foodname;
    int foodprice;
    String custID;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_food);

        FoodName = (TextView) findViewById(R.id.food_name);

        FoodQuantity = (TextView) findViewById(R.id.food_quantity);
        FoodPrice = (TextView) findViewById(R.id.food_price);
        FoodDescription = (TextView) findViewById(R.id.food_description);
        imageView = (ImageView) findViewById(R.id.image);
        additem = (ElegantNumberButton) findViewById(R.id.number_btn);




        final String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dataaa = FirebaseDatabase.getInstance().getReference("Customer").child(userid);
        dataaa.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Customer cust = snapshot.getValue(Customer.class);

                RandomId = getIntent().getStringExtra("FoodMenu");
                ChefID = getIntent().getStringExtra("ChefId");

                databaseReference = FirebaseDatabase.getInstance().getReference("FoodDetails").
                        child(ChefID).child(RandomId);
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UpdateFoodModel updateDishModel = snapshot.getValue(UpdateFoodModel.class);
                        FoodName.setText(updateDishModel.getName());
                        String qua = "<b>" + "Số lượng : " + "</b>" + updateDishModel.getQuantity();
                        FoodQuantity.setText(Html.fromHtml(qua));
                        String ss = "<b>" + "Mô tả : " + "</b>" + updateDishModel.getDescription();
                        FoodDescription.setText(Html.fromHtml(ss));
                        String pri = "<b>" + "Giá : " + "</b>" + updateDishModel.getPrice()+"đ";
                        FoodPrice.setText(Html.fromHtml(pri));
                        Glide.with(OrderFood.this).load(updateDishModel.getImageURL()).into(imageView);

                        chefdata = FirebaseDatabase.getInstance().getReference("Chef").child(ChefID);
                        chefdata.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Chef chef = snapshot.getValue(Chef.class);

                                custID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                databaseReference = FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(custID).child(RandomId);
                                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        Cart cart = snapshot.getValue(Cart.class);
                                        if (snapshot.exists()) {
                                            additem.setNumber(cart.getDishQuantity());
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                additem.setOnClickListener(new ElegantNumberButton.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dataref = FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        dataref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Cart cart1 = null;
                                if (snapshot.exists()) {
                                    int totalcount = 0;
                                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                        totalcount++;
                                    }
                                    int i = 0;
                                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                        i++;
                                        if (i == totalcount) {
                                            cart1 = snapshot.getValue(Cart.class);
                                        }
                                    }

                                    if (ChefID.equals(cart1.getChefId())) {
                                        data = FirebaseDatabase.getInstance().getReference("FoodDetails").child(ChefID).child(RandomId);
                                        data.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                UpdateFoodModel update = snapshot.getValue(UpdateFoodModel.class);
                                                foodname = update.getName();
                                                foodprice = Integer.parseInt(update.getPrice());

                                                int num = Integer.parseInt(additem.getNumber());
                                                int totalprice = num * foodprice;
                                                if (num != 0) {
                                                    HashMap<String, String> hashMap = new HashMap<>();
                                                    hashMap.put("DishName", foodname);
                                                    hashMap.put("DishID", RandomId);
                                                    hashMap.put("DishQuantity", String.valueOf(num));
                                                    hashMap.put("Price", String.valueOf(foodprice));
                                                    hashMap.put("Totalprice", String.valueOf(totalprice));
                                                    hashMap.put("ChefId", ChefID);
                                                    custID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                    reference = FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(custID).child(RandomId);
                                                    reference.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(@NonNull Void unused) {
                                                            Toast.makeText(OrderFood.this, "Thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                } else {
                                                    firebaseDatabase.getInstance().getReference("Cart").child(custID).child(RandomId).removeValue();
                                                }


                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }

                                            ;
                                        });
                                    } else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(OrderFood.this);
                                        builder.setMessage("Bạn không thể thêm sản phẩm mới vào giỏ hàng. " +
                                                "Vì số lượng có hạn cho học sinh,sinh viên, nên hãy đặt đơn hàng rồi đặt đơn mới.");
                                        builder.setCancelable(false);
                                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                                Intent intent = new Intent(OrderFood.this, CustomerFoodPanel_BottomNavigation.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });
                                        AlertDialog alert = builder.create();
                                        alert.show();
                                    }
                                } else{
                                    data = FirebaseDatabase.getInstance().getReference("FoodDetails").child(ChefID).child(RandomId);
                                    data.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            UpdateFoodModel update = snapshot.getValue(UpdateFoodModel.class);
                                            foodname = update.getName();
                                            foodprice = Integer.parseInt(update.getPrice());
                                            int num = Integer.parseInt(additem.getNumber());
                                            int totalprice = num * foodprice;
                                            if (num != 0) {
                                                HashMap<String, String> hashMap = new HashMap<>();
                                                hashMap.put("DishName", foodname);
                                                hashMap.put("DishID", RandomId);
                                                hashMap.put("DishQuantity", String.valueOf(num));
                                                hashMap.put("Price", String.valueOf(foodprice));
                                                hashMap.put("Totalprice", String.valueOf(totalprice));
                                                hashMap.put("ChefId", ChefID);
                                                custID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                reference = FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(custID).child(RandomId);
                                                reference.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(@NonNull Void unused) {
                                                        Toast.makeText(OrderFood.this, "Thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            } else {
                                                firebaseDatabase.getInstance().getReference("Cart").child(custID).child(RandomId).removeValue();
                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }

                                        ;
                                    });
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

}