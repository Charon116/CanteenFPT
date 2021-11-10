package com.example.canteenfpt.customerFoodPanel;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.canteenfpt.R;

public class CustomerOrderFragment extends Fragment {
    TextView Pendingorder, Payableorder;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_customer_orders,null);
        getActivity().setTitle("Đơn hàng");

        Pendingorder = (TextView) v.findViewById(R.id.pendingorder);
        Payableorder = (TextView) v.findViewById(R.id.payableorder);

        Pendingorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), PendingOrders.class);
                startActivity(intent);
            }
        });


        Payableorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), PayableOrders.class);
                startActivity(i);
            }
        });
        return v;
    }
}
