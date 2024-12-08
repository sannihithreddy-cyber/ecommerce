package com.example.ecommerce;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize sections

        TextView logoutSection = findViewById(R.id.logout_section);

        // Set up BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav);
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);

        // Bottom navigation logic with if-else
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_home) {
                    startActivity(new Intent(ProfileActivity.this, homepage.class));
                    overridePendingTransition(0, 0);
                    finish(); // Optional, to prevent stacking activities
                    return true;
                } else if (itemId == R.id.nav_products) {
                    startActivity(new Intent(ProfileActivity.this, ProductActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                } else if (itemId == R.id.nav_cart) {
                    startActivity(new Intent(ProfileActivity.this, CartActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    // Already on this page
                    return true;
                }

                return false;
            }
        });


        logoutSection.setOnClickListener(v -> {
            // Clear user session or preferences and navigate to login
            startActivity(new Intent(ProfileActivity.this, loginpage.class));
            finish(); // Close ProfileActivity

        });
    }
}
