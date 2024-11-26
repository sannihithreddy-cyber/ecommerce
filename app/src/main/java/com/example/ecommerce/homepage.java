package com.example.ecommerce;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.annotation.NonNull;
import android.view.MenuItem;

public class homepage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_homepage);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Initialize BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

// Set the selected item to highlight the current screen
        int selectedItemId = R.id.nav_home; // Since we're on the homepage
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
                    // Navigate to homepage (current activity)
                    startActivity(new Intent(homepage.this, homepage.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (itemId == R.id.nav_products) {
                    // Navigate to ProductActivity
                    startActivity(new Intent(homepage.this, ProductActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (itemId == R.id.nav_cart) {
                    // Navigate to CartActivity
                    startActivity(new Intent(homepage.this, CartActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    // Navigate to ProfileActivity
                    startActivity(new Intent(homepage.this, ProfileActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else {
                    return false;
                }
            }
        });
    }
}