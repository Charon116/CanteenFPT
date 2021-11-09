package com.example.canteenfpt.customerFoodPanel;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.canteenfpt.R;
import com.example.canteenfpt.ReusableCodeForAll;
import com.example.canteenfpt.SendNotification.APIService;
import com.example.canteenfpt.SendNotification.Data;
import com.example.canteenfpt.SendNotification.MyResponse;
import com.example.canteenfpt.SendNotification.NotificationSender;
import com.example.canteenfpt.chefFoodPanel.Chef;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import retrofit2.Callback;
import retrofit2.Call;
import retrofit2.Response;

public class CustomerCartFragment extends Fragment {
    RecyclerView recyclecart;
    private List<Cart> cartModelList;
    private CustomerCartAdapter adapter;
    private LinearLayout TotalBtns;
    DatabaseReference databaseReference,data, reference, ref, getRef, dataa;
    public static TextView total;
    Button remove, placeorder;
    String DishId, RandomUId, ChefId;
    private ProgressDialog progressDialog;
    private APIService apiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_customer_cart,null);
        getActivity().setTitle("Giỏ hàng");

        recyclecart = v.findViewById(R.id.recyclecart);
        recyclecart.setHasFixedSize(true);
        recyclecart.setLayoutManager(new LinearLayoutManager(getContext()));

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        cartModelList = new ArrayList<>();
        total = v.findViewById(R.id.GT);
        remove = v.findViewById(R.id.RM);
        placeorder = v.findViewById(R.id.PO);
        TotalBtns = v.findViewById(R.id.TotalBtns);
        customercart();
        return v;
    }

    private void customercart() {
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference("Cart")
                .child("CartItems").child(userID);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartModelList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Cart cart = snapshot1.getValue(Cart.class);
                    cartModelList.add(cart);
                }
                if(cartModelList.size() == 0){
                    TotalBtns.setVisibility(View.INVISIBLE);
                }else{
                    TotalBtns.setVisibility(View.VISIBLE);
                    remove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setMessage("Bạn muốn xóa món ăn này");
                            builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
                                    FirebaseDatabase.getInstance().getReference("Cart").child("GrandTotal").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
                                }
                            });
                            builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    });

                    //Duy lam nut order o day nha
                    String UserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    data = FirebaseDatabase.getInstance().getReference("Customer").child(UserID);
                    data.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            final Customer customer = snapshot.getValue(Customer.class);
                            final Cart cart = snapshot.getValue(Cart.class);
                            placeorder.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    FirebaseDatabase.getInstance().getReference("AlreadyOrdered").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("isOrdered").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                            progressDialog.setMessage("Xin chờ giây lát");
                                            progressDialog.show();
                                            reference = FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    RandomUId = UUID.randomUUID().toString();
                                                    for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                                                        final Cart cart1 = dataSnapshot1.getValue(Cart.class);
                                                        DishId = cart1.getDishID();
                                                        final HashMap<String, String> hashMap = new HashMap<>();
                                                        hashMap.put("ChefId", cart1.getChefId());
                                                        hashMap.put("DishID", cart1.getDishID());
                                                        hashMap.put("DishName", cart1.getDishName());
                                                        hashMap.put("DishQuantity", cart1.getDishQuantity());
                                                        hashMap.put("Price", cart1.getPrice());
                                                        hashMap.put("TotalPrice", cart1.getTotalprice());
                                                        FirebaseDatabase.getInstance().getReference("CustomerPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUId).child("Dishes").child(DishId).setValue(hashMap);

                                                    }
                                                    ref = FirebaseDatabase.getInstance().getReference("Cart").child("GrandTotal").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("GrandTotal");
                                                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            String grandtotal = snapshot.getValue(String.class);
                                                            HashMap<String, String> hashMap1 = new HashMap<>();
                                                            hashMap1.put("GrandTotalPrice", String.valueOf(grandtotal));
                                                            hashMap1.put("MobileNumber", customer.getMobileNo());
                                                            hashMap1.put("Name", cart.getDishName());
                                                            FirebaseDatabase.getInstance().getReference("CustomerPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUId).child("OtherInformation").setValue(hashMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(FirebaseAuth.getInstance().getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            FirebaseDatabase.getInstance().getReference("Cart").child("GrandTotal").child(FirebaseAuth.getInstance().getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    getRef = FirebaseDatabase.getInstance().getReference("CustomerPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUId).child("Dishes");
                                                                                    getRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                        @Override
                                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                            for (DataSnapshot dataSnapshot2 : snapshot.getChildren()) {
                                                                                                final CustomerPendingOrders customerPendingOrders = dataSnapshot2.getValue(CustomerPendingOrders.class);
                                                                                                String d = customerPendingOrders.getDishID();
                                                                                                String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                                                                ChefId = customerPendingOrders.getChefId();
                                                                                                final HashMap<String, String> hashMap2 = new HashMap<>();
                                                                                                hashMap2.put("ChefId", ChefId);
                                                                                                hashMap2.put("DishId", customerPendingOrders.getDishID());
                                                                                                hashMap2.put("DishName", customerPendingOrders.getDishName());
                                                                                                hashMap2.put("DishQuantity", customerPendingOrders.getDishQuantity());
                                                                                                hashMap2.put("Price", customerPendingOrders.getPrice());
                                                                                                hashMap2.put("RandomUID", RandomUId);
                                                                                                hashMap2.put("TotalPrice", customerPendingOrders.getTotalPrice());
                                                                                                hashMap2.put("UserId", userid);

                                                                                                FirebaseDatabase.getInstance().getReference("ChefPendingOrders").child(ChefId).child(RandomUId).child("Dishes").child(d).setValue(hashMap2);
                                                                                            }
                                                                                            dataa = FirebaseDatabase.getInstance().getReference("CustomerPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUId).child("OtherInformation");
                                                                                            dataa.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                @Override
                                                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                    CustomerPendingOrders1 customerPendingOrders1 = snapshot.getValue(CustomerPendingOrders1.class);
                                                                                                    HashMap<String, String> hashMap3 = new HashMap<>();
                                                                                                    hashMap3.put("GrandTotalPrice", customerPendingOrders1.getGrandTotalPrice());
                                                                                                    hashMap3.put("MobileNumber", customerPendingOrders1.getMobileNumber());
                                                                                                    hashMap3.put("Name", customerPendingOrders1.getName());
                                                                                                    hashMap3.put("RandomUID", RandomUId);

                                                                                                    FirebaseDatabase.getInstance().getReference("ChefPendingOrders").child(ChefId).child(RandomUId).child("OtherInformation").setValue(hashMap3).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                        @Override
                                                                                                        public void onSuccess(@NonNull Void unused) {
                                                                                                            FirebaseDatabase.getInstance().getReference("AlreadyOrdered").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("isOrdered").setValue("true").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                @Override
                                                                                                                public void onSuccess(@NonNull Void unused) {
                                                                                                                    FirebaseDatabase.getInstance().getReference().child("Tokens").child(ChefId).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                        @Override
                                                                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                                            String usertoken = snapshot.getValue(String.class);
                                                                                                                            //sendNotifications(usertoken, "Đơn hàng ", "Bạn có một đơn hàng ", "Order");
                                                                                                                            progressDialog.dismiss();
                                                                                                                            ReusableCodeForAll.ShowAlert(getContext(), "", "Đơn hàng của bạn đã được gửi qua bên nhà hàng, xin vui lòng chờ nhà hàng chấp nhận đơn hàng..");
                                                                                                                        }

                                                                                                                        @Override
                                                                                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                                                                                        }
                                                                                                                    });
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

                                                                                        @Override
                                                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                                                        }
                                                                                    });
                                                                                }
                                                                            });
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

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });

                                            //ReusableCodeForAll.ShowAlert(getContext(),"Lỗi", "Có vẻ như bạn đã đặt hàng rồi, vì vậy bạn không thể đặt một đơn hàng khác cho đến khi giao đơn hàng đầu tiên");
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
                adapter = new CustomerCartAdapter(getContext(),cartModelList);
                recyclecart.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendNotifications(String usertoken, String title, String message, String order) {

        Data data = new Data(title, message, order);
        NotificationSender sender = new NotificationSender(data, usertoken);
        apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                    }
                }

            }
            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }
}
