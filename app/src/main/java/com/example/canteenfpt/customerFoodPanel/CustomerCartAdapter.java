package com.example.canteenfpt.customerFoodPanel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.canteenfpt.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

public class CustomerCartAdapter extends RecyclerView.Adapter<CustomerCartAdapter.ViewHolder> {

    private Context mcontext;
    private List<Cart> cartModellist;
    static int total = 0;

    public CustomerCartAdapter(Context mcontext, List<Cart> cartModellist) {
        this.mcontext = mcontext;
        this.cartModellist = cartModellist;
        total = 0;
    }

    @NonNull
    @Override
    public CustomerCartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_placeorder, parent, false);
        return new CustomerCartAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerCartAdapter.ViewHolder holder, int position) {
        final Cart cart = cartModellist.get(position);
        holder.dishname.setText(cart.getDishName());
        holder.PriceRs.setText("Giá: " + cart.getPrice());
        holder.Qty.setText("x " + cart.getDishQuantity());
        holder.Totalrs.setText("Tổng tiền: " + cart.getTotalprice());

        total += Integer.parseInt(cart.getTotalprice());

        holder.elegantNumberButton.setNumber(cart.getDishQuantity());
        final int dishprice = Integer.parseInt(cart.getPrice());

        holder.elegantNumberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                int num = newValue;
                int totalprice = num * dishprice;
                if (num != 0) {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("DishID", cart.getDishID());
                    hashMap.put("DishName", cart.getDishName());
                    hashMap.put("DishQuantity", String.valueOf(num));
                    hashMap.put("Price", String.valueOf(dishprice));
                    hashMap.put("Totalprice", String.valueOf(totalprice));
                    hashMap.put("ChefId",cart.getChefId());

                    FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(cart.getDishID()).setValue(hashMap);
                } else {
                    FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(cart.getDishID()).removeValue();
                }
            }
        });
        CustomerCartFragment.grandt.setText("Tổng tiền:  " + total+ "đ");
        FirebaseDatabase.getInstance().getReference("Cart").child("GrandTotal").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("GrandTotal").setValue(String.valueOf(total));
    }

    @Override
    public int getItemCount() {
        return cartModellist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView dishname, PriceRs, Qty, Totalrs;
        ElegantNumberButton elegantNumberButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            dishname = itemView.findViewById(R.id.foodname);
            PriceRs = itemView.findViewById(R.id.prices);
            Qty = itemView.findViewById(R.id.qty);
            Totalrs = itemView.findViewById(R.id.totals);
            elegantNumberButton = itemView.findViewById(R.id.elegantbtn);
        }
    }
}
