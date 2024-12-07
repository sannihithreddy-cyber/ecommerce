package com.example.ecommerce;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

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

        // Existing code: Ensure system bars are handled
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
        Glide.with(this)
                .load(imageUrl)
                .into(productImage);

        // Set click listener for "Add to Cart" button
        addToCartButton.setOnClickListener(view -> {
            addToCartButton.setVisibility(View.GONE); // Hide the "Add to Cart" button
            quantityLayout.setVisibility(View.VISIBLE); // Show the quantity layout
        });
        // Handle the "+" button click
        plusButton.setOnClickListener(view -> {
            if (quantity < 9) {
                quantity++; // Increment the quantity
                quantityText.setText(String.valueOf(quantity));
            } else {
                // Show a message when the limit is reached
                Toast.makeText(activity_product_detail.this, "Maximum quantity allowed is 9.", Toast.LENGTH_SHORT).show();
            }
        });

// Handle the "-" button click
        minusButton.setOnClickListener(view -> {
            if (quantity > 1) {
                quantity--;
                quantityText.setText(String.valueOf(quantity));
            } else {
                // Hide the quantity layout and show the "Add to Cart" button
                quantityLayout.setVisibility(View.INVISIBLE); // Use INVISIBLE instead of GONE
                addToCartButton.setVisibility(View.VISIBLE);
                quantity = 1; // Reset the quantity for future use
            }
        });



    }
}
