package com.example.canteenfpt.chefFoodPanel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.canteenfpt.R;

import java.util.List;

public class ChefOrderFoodsAdapter extends RecyclerView.Adapter<ChefOrderFoodsAdapter.ViewHolder> {
    private Context mcontext;
    private List<ChefPendingOrders> chefPendingOrderslist;

    public ChefOrderFoodsAdapter(Context mcontext, List<ChefPendingOrders> chefPendingOrderslist) {
        this.mcontext = mcontext;
        this.chefPendingOrderslist = chefPendingOrderslist;
    }

    @NonNull
    @Override
    public ChefOrderFoodsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.chef_order_foods, parent, false);
        return new ChefOrderFoodsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChefOrderFoodsAdapter.ViewHolder holder, int position) {
        final ChefPendingOrders chefPendingOrders = chefPendingOrderslist.get(position);
        holder.dishname.setText(chefPendingOrders.getDishName());
        holder.price.setText("Giá: " + chefPendingOrders.getPrice()+"đ");
        holder.quantity.setText("× " + chefPendingOrders.getDishQuantity());
        holder.totalprice.setText("Tổng tiền: " + chefPendingOrders.getTotalPrice()+"đ");
    }

    @Override
    public int getItemCount() {
        return chefPendingOrderslist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView dishname, price, totalprice, quantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dishname = itemView.findViewById(R.id.DN);
            price = itemView.findViewById(R.id.PR);
            totalprice = itemView.findViewById(R.id.TR);
            quantity = itemView.findViewById(R.id.QY);
        }
    }
}
