package com.example.canteenfpt.customerFoodPanel;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.canteenfpt.R;
import com.example.canteenfpt.UpdateFoodModel;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class CustomerHomeAdapter extends RecyclerView.Adapter<CustomerHomeAdapter.ViewHolder> {
    private Context mcontext;
    private List<UpdateFoodModel> updateDishModellist;
    DatabaseReference databaseReference;

    public CustomerHomeAdapter(Context context , List<UpdateFoodModel>updateDishModelslist){
        this.updateDishModellist = updateDishModelslist;
        this.mcontext = context;
    }

    @NonNull
    @Override
    public CustomerHomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_menufood,parent,false);
        return new CustomerHomeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerHomeAdapter.ViewHolder holder, int position) {
        final UpdateFoodModel updateFoodModel = updateDishModellist.get(position);
        Glide.with(mcontext).load(updateFoodModel.getImageURL()).into(holder.imageView);
        holder.foodname.setText(updateFoodModel.getName());
        updateFoodModel.getRandomUID();
        updateFoodModel.getChefid();
        holder.Price.setText("Giá: "+updateFoodModel.getPrice()+"đ");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mcontext,OrderFood.class);
                intent.putExtra("FoodMenu",updateFoodModel.getRandomUID());
                intent.putExtra("ChefId",updateFoodModel.getChefid());

                mcontext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return updateDishModellist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView foodname,Price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.menu_image);
            foodname = itemView.findViewById(R.id.foodname);
            Price = itemView.findViewById(R.id.foodprice);
        }
    }
}
