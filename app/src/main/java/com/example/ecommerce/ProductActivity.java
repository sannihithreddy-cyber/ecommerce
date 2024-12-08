// ProductActivity.java
package com.example.ecommerce;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.GridLayoutManager;
import android.content.Intent;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
// Import ValueEventListener
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private List<Product> productList;
    private ProductAdapter adapter;

    private int selectedItemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2); // 2 columns
        recyclerView.setLayoutManager(gridLayoutManager);

        // Initialize product list and adapter
        productList = new ArrayList<>();
        adapter = new ProductAdapter(this, productList, product -> {
            // Handle product click
            Intent intent = new Intent(ProductActivity.this, activity_product_detail.class);
            intent.putExtra("product_name", product.getName());
            intent.putExtra("product_price", "$" + product.getPrice());
            intent.putExtra("product_description", product.getDescription());
            intent.putExtra("product_image", product.getImg());

            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);


        // Set window insets listener
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Fetch data from Firebase
        fetchProductsFromFirebase();

        // Initialize BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set the selected item to highlight the current screen
        selectedItemId = R.id.nav_products; // Since we're in ProductActivity
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
                    startActivity(new Intent(ProductActivity.this, homepage.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                } else if (itemId == R.id.nav_products) {
                    // Already on the products page
                    return true;
                } else if (itemId == R.id.nav_cart) {
                    startActivity(new Intent(ProductActivity.this, CartActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    startActivity(new Intent(ProductActivity.this, ProfileActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    private void fetchProductsFromFirebase() {
        // Initialize Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference("products");

        // Add listener to read data
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // Clear the list before adding new data
                productList.clear();

                // Loop through the data snapshots
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    // Get Product object
                    Product product = postSnapshot.getValue(Product.class);
                    // Add product to the list
                    productList.add(product);
                }

                // Notify adapter about data changes
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle possible errors.
                Toast.makeText(ProductActivity.this, "Failed to load products.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
