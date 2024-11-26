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

public class ProfileActivity extends AppCompatActivity {

    private int selectedItemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        // Set window insets listener
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set the selected item to highlight the current screen
        selectedItemId = R.id.nav_profile; // Since we're in ProfileActivity
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
                    // Already on the profile page
                    return true;
                } else {
                    return false;
                }
            }
        });
    }
}
