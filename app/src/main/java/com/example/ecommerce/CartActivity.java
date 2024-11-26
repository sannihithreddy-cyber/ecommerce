package com.example.ecommerce;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CartActivity extends AppCompatActivity {

    private int selectedItemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);

        // Set window insets listener
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set the selected item to highlight the current screen
        selectedItemId = R.id.nav_cart; // Since we're in CartActivity
        bottomNavigationView.setSelectedItemId(selectedItemId);

        // Set navigation item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == selectedItemId) {
                    // Do nothing if the selected item is the current screen
                    return true;
                }

                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    startActivity(new Intent(CartActivity.this, homepage.class));
                    overridePendingTransition(0, 0);
                    finish(); // Optional, to prevent stacking activities
                    return true;
                } else if (itemId == R.id.nav_products) {
                    startActivity(new Intent(CartActivity.this, ProductActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                } else if (itemId == R.id.nav_cart) {
                    // Already on the cart page
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
    }
}
