package com.example.ecommerce;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private RecyclerView cartRecyclerView;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItemList;
    private Button placeOrderButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);
        placeOrderButton = findViewById(R.id.place_order_button);


        // Initialize RecyclerView
        cartRecyclerView = findViewById(R.id.cart_recycler_view);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize cart list and adapter
        cartItemList = new ArrayList<>();
        cartAdapter = new CartAdapter(this, cartItemList);
        cartRecyclerView.setAdapter(cartAdapter);

        // Fetch data from SharedPreferences
        fetchCartData();

        // Set window insets listener
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Highlight the current item
        bottomNavigationView.setSelectedItemId(R.id.nav_cart);

        // Set navigation item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_home) {
                    startActivity(new Intent(CartActivity.this, homepage.class));
                    overridePendingTransition(0, 0);
                    finish(); // Optional to prevent stacking activities
                    return true;
                } else if (itemId == R.id.nav_products) {
                    startActivity(new Intent(CartActivity.this, ProductActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                } else if (itemId == R.id.nav_cart) {
                    // Already on the cart page, do nothing
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    startActivity(new Intent(CartActivity.this, ProfileActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                } else {
                    return false;
                }
            }
        });
        placeOrderButton.setOnClickListener(v -> {
            Intent intent = new Intent(CartActivity.this, OrderSummaryActivity.class);
            startActivity(intent);
        });


    }

    private void fetchCartData() {
        SharedPreferences sharedPreferences = getSharedPreferences("cart_prefs", Context.MODE_PRIVATE);
        String cartJson = sharedPreferences.getString("cart_data", "[]");
        Gson gson = new Gson();
        Type type = new TypeToken<List<CartItem>>() {}.getType();
        List<CartItem> items = gson.fromJson(cartJson, type);

        if (items != null && !items.isEmpty()) {
            cartItemList.clear();
            cartItemList.addAll(items);
            cartAdapter.notifyDataSetChanged();
            placeOrderButton.setVisibility(View.VISIBLE); // Show button if cart is not empty
        } else {
            cartItemList.clear();
            cartAdapter.notifyDataSetChanged();
            placeOrderButton.setVisibility(View.GONE); // Hide button if cart is empty
            Toast.makeText(this, "Your cart is empty.", Toast.LENGTH_SHORT).show();
        }
    }
    public void togglePlaceOrderButton(boolean show) {
        if (placeOrderButton != null) {
            placeOrderButton.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }





    private List<CartItem> getCartItems() {
        SharedPreferences sharedPreferences = getSharedPreferences("cart_prefs", MODE_PRIVATE);
        String cartJson = sharedPreferences.getString("cart_data", "[]");

        Gson gson = new Gson();
        Type type = new TypeToken<List<CartItem>>() {}.getType();

        return gson.fromJson(cartJson, type);
    }
}
