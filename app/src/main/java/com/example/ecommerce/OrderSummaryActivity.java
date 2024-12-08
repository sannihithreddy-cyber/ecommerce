package com.example.ecommerce;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class OrderSummaryActivity extends AppCompatActivity {

    private RecyclerView orderSummaryRecyclerView;
    private TextView grandTotalTextView;
    private List<CartItem> cartItems;
    private double taxRate = 0.13; // 13% tax rate

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ordersummaryactivity);

        // Initialize UI components
        orderSummaryRecyclerView = findViewById(R.id.order_summary_recycler_view);
        grandTotalTextView = findViewById(R.id.grand_total_value_text_view);
        Button checkoutButton = findViewById(R.id.checkout_button);


        // Fetch cart data
        SharedPreferences sharedPreferences = getSharedPreferences("cart_prefs", MODE_PRIVATE);
        String cartJson = sharedPreferences.getString("cart_data", "[]");
        Gson gson = new Gson();
        Type type = new TypeToken<List<CartItem>>() {}.getType();
        cartItems = gson.fromJson(cartJson, type);

        // Setup RecyclerView
        OrderSummaryAdapter adapter = new OrderSummaryAdapter(this, cartItems);
        orderSummaryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderSummaryRecyclerView.setAdapter(adapter);

        // Calculate and display grand total
        displayGrandTotal();
        // Handle Checkout Button Click
        checkoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(OrderSummaryActivity.this, CheckoutActivity.class);

            // Calculate total price including tax
            double totalPrice = 0.0;
            for (CartItem item : cartItems) {
                totalPrice += item.getPrice() * item.getQuantity();
            }
            double tax = totalPrice * taxRate;
            double finalPrice = totalPrice + tax;

            // Pass the total price to CheckoutActivity
            intent.putExtra("TOTAL_PRICE", finalPrice);
            startActivity(intent);
        });

    }

    private void displayGrandTotal() {
        double totalPrice = 0.0;

        for (CartItem item : cartItems) {
            totalPrice += item.getPrice() * item.getQuantity();
        }

        double tax = totalPrice * taxRate;
        double finalPrice = totalPrice + tax;

        grandTotalTextView.setText("$" + String.format("%.2f", finalPrice));
    }
}
