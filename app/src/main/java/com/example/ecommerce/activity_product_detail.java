package com.example.ecommerce;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

public class activity_product_detail extends AppCompatActivity {

    // Declare UI components
    private ImageView productImage;
    private TextView productName, productPrice, productDescription;
    private Button addToCartButton;
    private LinearLayout quantityLayout;
    private TextView quantityText;
    private Button minusButton, plusButton;
    private int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_detail);

        // Ensure system bars are handled
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Enable the back button in the ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Initialize UI components
        productImage = findViewById(R.id.detail_product_image);
        productName = findViewById(R.id.detail_product_name);
        productPrice = findViewById(R.id.detail_product_price);
        productDescription = findViewById(R.id.detail_product_description);
        addToCartButton = findViewById(R.id.add_to_cart_button);
        quantityLayout = findViewById(R.id.quantity_layout);
        quantityText = findViewById(R.id.quantity_text);
        minusButton = findViewById(R.id.minus_button);
        plusButton = findViewById(R.id.plus_button);
        plusButton.setText("+");
        minusButton.setText("-");

        // Receive product data from Intent
        String name = getIntent().getStringExtra("product_name");
        String price = getIntent().getStringExtra("product_price");
        String description = getIntent().getStringExtra("product_description");
        String imageUrl = getIntent().getStringExtra("product_image");

        // Populate the UI components with received data
        productName.setText(name);
        productPrice.setText(price);
        productDescription.setText(description);
        Glide.with(this).load(imageUrl).into(productImage);

        // Check if the product is already in the cart
        SharedPreferences sharedPreferences = getSharedPreferences("cart_prefs", MODE_PRIVATE);
        String cartJson = sharedPreferences.getString("cart_data", "[]");
        Gson gson = new Gson();
        Type type = new TypeToken<List<CartItem>>() {}.getType();
        List<CartItem> cartItems = gson.fromJson(cartJson, type);

        if (cartItems != null) {
            for (CartItem item : cartItems) {
                if (item.getName().equals(name)) {
                    // Product already in cart, update UI
                    addToCartButton.setVisibility(View.GONE);
                    quantityLayout.setVisibility(View.VISIBLE);
                    quantity = item.getQuantity(); // Set the existing quantity
                    quantityText.setText(String.valueOf(quantity));
                    break;
                }
            }
        }

        // Set up BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_products); // Highlight current item

        // Handle navigation item selection
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    startActivity(new Intent(activity_product_detail.this, homepage.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                } else if (itemId == R.id.nav_products) {
                    // Already on this page
                    return true;
                } else if (itemId == R.id.nav_cart) {
                    startActivity(new Intent(activity_product_detail.this, CartActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    startActivity(new Intent(activity_product_detail.this, ProfileActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                }
                return false;
            }
        });

        // Set click listener for "Add to Cart" button
        addToCartButton.setOnClickListener(view -> {
            CartItem cartItem = new CartItem(
                    productName.getText().toString(),
                    imageUrl,
                    Double.parseDouble(price.replace("$", "")),
                    quantity
            );

            // Save locally
            saveCartItemLocally(cartItem);

            Toast.makeText(activity_product_detail.this, "Item added to cart!", Toast.LENGTH_SHORT).show();
            addToCartButton.setVisibility(View.GONE);
            quantityLayout.setVisibility(View.VISIBLE);
        });

        // Handle the "+" button click
        plusButton.setOnClickListener(view -> {
            if (quantity < 9) {
                quantity++;
                quantityText.setText(String.valueOf(quantity));
                updateCartQuantityInSharedPreferences();
            } else {
                Toast.makeText(activity_product_detail.this, "Maximum quantity allowed is 9.", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle the "-" button click
        minusButton.setOnClickListener(view -> {
            if (quantity > 1) {
                quantity--;
                quantityText.setText(String.valueOf(quantity));
                updateCartQuantityInSharedPreferences();
            } else {
                removeFromCart();
                quantityLayout.setVisibility(View.INVISIBLE);
                addToCartButton.setVisibility(View.VISIBLE);
                quantity = 1;
                updateCartQuantityInSharedPreferences();
            }
        });
    }

    private void saveCartItemLocally(CartItem cartItem) {
        SharedPreferences sharedPreferences = getSharedPreferences("cart_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Retrieve existing cart data
        String cartJson = sharedPreferences.getString("cart_data", "[]");
        Gson gson = new Gson();
        Type type = new TypeToken<List<CartItem>>() {}.getType();
        List<CartItem> cartItems = gson.fromJson(cartJson, type);

        if (cartItems == null) {
            cartItems = new ArrayList<>();
        }

        // Check if item already exists in the cart
        boolean itemExists = false;
        for (CartItem item : cartItems) {
            if (item.getName().equals(cartItem.getName())) {
                int newQuantity = item.getQuantity() + cartItem.getQuantity();
                item.setQuantity(Math.min(newQuantity, 9)); // Enforce max of 9
                itemExists = true;
                break;
            }
        }

        if (!itemExists) {
            cartItems.add(cartItem);
        }

        String updatedCartJson = gson.toJson(cartItems);
        editor.putString("cart_data", updatedCartJson);
        editor.apply();
    }

    private void updateCartQuantityInSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("cart_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Retrieve existing cart data
        String cartJson = sharedPreferences.getString("cart_data", "[]");
        Gson gson = new Gson();
        Type type = new TypeToken<List<CartItem>>() {}.getType();
        List<CartItem> cartItems = gson.fromJson(cartJson, type);

        if (cartItems != null) {
            for (CartItem item : cartItems) {
                if (item.getName().equals(productName.getText().toString())) {
                    item.setQuantity(quantity); // Update the quantity
                    break;
                }
            }

            // Save back the updated cart
            String updatedCartJson = gson.toJson(cartItems);
            editor.putString("cart_data", updatedCartJson);
            editor.apply();
        }
    }

    private void removeFromCart() {
        SharedPreferences sharedPreferences = getSharedPreferences("cart_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Retrieve existing cart data
        String cartJson = sharedPreferences.getString("cart_data", "[]");
        Gson gson = new Gson();
        Type type = new TypeToken<List<CartItem>>() {}.getType();
        List<CartItem> cartItems = gson.fromJson(cartJson, type);

        if (cartItems != null) {
            // Remove the item matching the current product
            for (int i = 0; i < cartItems.size(); i++) {
                if (cartItems.get(i).getName().equals(productName.getText().toString())) {
                    cartItems.remove(i);
                    break;
                }

            }

            // Save the updated cart data back to SharedPreferences
            String updatedCartJson = gson.toJson(cartItems);
            editor.putString("cart_data", updatedCartJson);
            editor.apply();
        }
    }

}
