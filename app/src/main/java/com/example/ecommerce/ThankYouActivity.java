package com.example.ecommerce;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ThankYouActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you);

        TextView thankYouMessage = findViewById(R.id.thank_you_message);
        Button continueShoppingButton = findViewById(R.id.continue_shopping_button);

        // Set "Thank You" message
        thankYouMessage.setText("Thank You for Your Order!");

        // Handle "Continue Shopping" button click
        continueShoppingButton.setOnClickListener(v -> {
            Intent intent = new Intent(ThankYouActivity.this, ProductActivity.class);
            startActivity(intent);
            finish(); // Close ThankYouActivity
        });
    }
}
