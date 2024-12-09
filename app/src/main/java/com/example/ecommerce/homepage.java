package com.example.ecommerce;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class homepage extends AppCompatActivity {

    private VideoView videoView;
    private RecyclerView featuredProductsRecyclerView;
    private FeaturedProductsAdapter adapter;
    private List<Product> allProducts = new ArrayList<>();
    private List<Product> randomProducts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_homepage);

        // Handle window insets for edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        findViewById(R.id.view_all_products_button).setOnClickListener(v -> {
            Intent intent = new Intent(homepage.this, ProductActivity.class);
            startActivity(intent);
        });

        // Initialize VideoView
        videoView = findViewById(R.id.videoView);
        Uri videoUri = copyVideoToCache("5275266-uhd_4096_2160_25fps.mp4");
        if (videoUri != null) {
            videoView.setVideoURI(videoUri);

            // Add playback controls
//            MediaController mediaController = new MediaController(this);
//            mediaController.setAnchorView(videoView);
//            videoView.setMediaController(mediaController);

            // Set looping
            videoView.setOnCompletionListener(mp -> videoView.start());

            // Start the video
            videoView.start();
        }

        // Initialize RecyclerView
        featuredProductsRecyclerView = findViewById(R.id.featured_products_recyclerview);
        featuredProductsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Initialize Firebase Database reference
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("products");

        // Fetch data from Firebase
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                allProducts.clear();
                for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                    Product product = productSnapshot.getValue(Product.class);
                    if (product != null) {
                        allProducts.add(product);
                    }
                }

                if (allProducts.size() >= 3) {
                    Collections.shuffle(allProducts); // Shuffle the list to randomize
                    randomProducts = allProducts.subList(0, 3); // Pick the first 3 products
                } else {
                    randomProducts = allProducts; // Use all available products if less than 3
                }

                // Update the adapter with random products
                adapter = new FeaturedProductsAdapter(homepage.this, randomProducts);
                featuredProductsRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(homepage.this, "Failed to load products: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // BottomNavigationView logic
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) return true;
            else if (itemId == R.id.nav_products) {
                startActivity(new Intent(homepage.this, ProductActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_cart) {
                startActivity(new Intent(homepage.this, CartActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(homepage.this, ProfileActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
        });
    }

    private Uri copyVideoToCache(String fileName) {
        try {
            InputStream inputStream = getAssets().open(fileName);
            File tempFile = new File(getCacheDir(), fileName);
            FileOutputStream outputStream = new FileOutputStream(tempFile);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.close();
            inputStream.close();

            return Uri.fromFile(tempFile);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
