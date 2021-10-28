package com.example.canteenfpt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.canteenfpt.chefFoodPanel.ChefHomeFragment;
import com.example.canteenfpt.chefFoodPanel.ChefOrderFragment;
import com.example.canteenfpt.chefFoodPanel.ChefPendingOrderFragment;
import com.example.canteenfpt.chefFoodPanel.ChefProfileFragment;
import com.example.canteenfpt.customerFoodPanel.CustomerCartFragment;
import com.example.canteenfpt.customerFoodPanel.CustomerHomeFragment;
import com.example.canteenfpt.customerFoodPanel.CustomerOrderFragment;
import com.example.canteenfpt.customerFoodPanel.CustomerProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CustomerFoodPanel_BottomNavigation extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_food_panel_bottom_navigation);
        BottomNavigationView navigationView = findViewById(R.id.customer_bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(this);

        String name = getIntent().getStringExtra("PAGE");
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(name != null){
            if(name.equalsIgnoreCase("Homepage")){
                loadcustomerfragment(new CustomerHomeFragment());
            }else if(name.equalsIgnoreCase("DeliveryOrderpage")){
                loadcustomerfragment(new CustomerOrderFragment());
            }else if(name.equalsIgnoreCase("Thankyoupage")) {
                loadcustomerfragment(new CustomerHomeFragment());
            }
        }else{
            loadcustomerfragment(new CustomerHomeFragment());
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()){
            case R.id.customerHome:
                fragment = new CustomerHomeFragment();
                break;
            case R.id.cart:
                fragment = new CustomerCartFragment();
                break;
            case R.id.detailOrders:
                fragment = new CustomerOrderFragment();
                break;
            case R.id.customerProfile:
                fragment = new CustomerProfileFragment();
                break;
        }
        return loadcustomerfragment(fragment);
    }

    private boolean loadcustomerfragment(Fragment fragment) {
        if(fragment != null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_customer,fragment).commit();
            return true;
        }
        return false;
    }
}