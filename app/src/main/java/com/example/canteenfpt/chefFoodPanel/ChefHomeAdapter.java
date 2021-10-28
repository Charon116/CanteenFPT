package com.example.canteenfpt.chefFoodPanel;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.canteenfpt.R;
import com.example.canteenfpt.UpdateFoodModel;

import java.util.ArrayList;
import java.util.List;

public class ChefHomeAdapter extends RecyclerView.Adapter<ChefHomeAdapter.ViewHolder> {
    private Context context;
    private ArrayList<UpdateFoodModel> updateFoodModelList;

    public ChefHomeAdapter(Context context, ArrayList<UpdateFoodModel> updateFoodModelList) {
        this.context = context;
        this.updateFoodModelList = updateFoodModelList;
    }

    @NonNull
    @Override
    public ChefHomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chefmenu_update_delete,parent,false);
        return new ChefHomeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChefHomeAdapter.ViewHolder holder, int position) {
        final UpdateFoodModel updateFoodModel = updateFoodModelList.get(position);
        holder.foodName.setText(updateFoodModel.getName());
        updateFoodModel.getRandomUID();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,UpdateDelete_Food.class);
                intent.putExtra("updatedeletedish",updateFoodModel.getRandomUID());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return updateFoodModelList.size();
    }

    public class  ViewHolder extends RecyclerView.ViewHolder{
        TextView foodName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.foodName);
        }
    }
}
