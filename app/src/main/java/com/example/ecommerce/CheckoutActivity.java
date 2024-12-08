package com.example.ecommerce;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Random;

public class CheckoutActivity extends AppCompatActivity {

    private EditText addressLine1Field, addressLine2Field, cityField, provinceField, countryField, zipCodeField;
    private EditText nameOnCardField, cardNumberField, expiryDateField, cvvField;
    private TextView orderNumberTextView, totalPriceTextView;
    private Button placeOrderButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        // Initialize UI components for address details
        addressLine1Field = findViewById(R.id.address_line1_field);
        addressLine2Field = findViewById(R.id.address_line2_field);
        cityField = findViewById(R.id.city_field);
        provinceField = findViewById(R.id.province_field);
        countryField = findViewById(R.id.country_field);
        zipCodeField = findViewById(R.id.zipcode_field);

        // Initialize UI components for payment details
        nameOnCardField = findViewById(R.id.name_on_card_field);
        cardNumberField = findViewById(R.id.card_number_field);
        expiryDateField = findViewById(R.id.expiry_date_field);
        cvvField = findViewById(R.id.cvv_field);

        // Initialize TextViews for order number and total price
        orderNumberTextView = findViewById(R.id.order_number_text_view);
        totalPriceTextView = findViewById(R.id.total_price_text_view);

        // Initialize Place Order Button
        placeOrderButton = findViewById(R.id.place_order_button);

        // Receive total price from intent and display
        double totalPrice = getIntent().getDoubleExtra("TOTAL_PRICE", 0.0);
        totalPriceTextView.setText("Total Price: $" + String.format("%.2f", totalPrice));

        // Generate and display random order number
        String orderNumber = generateRandomOrderNumber();
        orderNumberTextView.setText("Order Number: " + orderNumber);

        // Handle Place Order Button Click
        placeOrderButton.setOnClickListener(v -> {
            if (validateInputs()) {
                clearCart(); // Clear the user's cart
                Intent intent = new Intent(CheckoutActivity.this, ThankYouActivity.class);
                startActivity(intent);
                finish(); // Close CheckoutActivity
            }
        });

    }
    private void clearCart() {
        SharedPreferences sharedPreferences = getSharedPreferences("cart_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("cart_data", "[]"); // Set cart data to an empty array
        editor.apply();
    }


    private boolean validateInputs() {
        // Address Line 1 validation (Mandatory)
        if (addressLine1Field.getText().toString().trim().isEmpty()) {
            addressLine1Field.setError("Address Line 1 is required");
            return false;
        }

        // City validation (Mandatory, Alphabets Only)
        if (!validateAlphabetic(cityField.getText().toString().trim())) {
            cityField.setError("City must contain only alphabets");
            return false;
        }

        // Province validation (Mandatory, Alphabets Only)
        if (!validateAlphabetic(provinceField.getText().toString().trim())) {
            provinceField.setError("Province/State must contain only alphabets");
            return false;
        }

        // Country validation (Mandatory, Alphabets Only)
        if (!validateAlphabetic(countryField.getText().toString().trim())) {
            countryField.setError("Country must contain only alphabets");
            return false;
        }

        // Postal Code validation (Mandatory, 6 Alphanumeric Characters)
        if (!validatePostalCode(zipCodeField.getText().toString().trim())) {
            zipCodeField.setError("Postal Code must be 6 alphanumeric characters");
            return false;
        }

        // Name on Card validation (Mandatory, Alphabets Only)
        if (!validateAlphabetic(nameOnCardField.getText().toString().trim())) {
            nameOnCardField.setError("Name on card must contain only alphabets");
            return false;
        }

        // Card Number validation (Mandatory, 16 Digits)
        if (!validateCardNumber(cardNumberField.getText().toString().trim())) {
            cardNumberField.setError("Card Number must be 16 digits");
            return false;
        }

        // Expiry Date validation (Mandatory, Valid Format and Future Date)
        if (!validateExpiryDate(expiryDateField.getText().toString().trim())) {
            expiryDateField.setError("Invalid expiry date. Ensure MM/YY format and future date");
            return false;
        }

        // CVV validation (Mandatory, 3 Digits)
        if (!validateCVV(cvvField.getText().toString().trim())) {
            cvvField.setError("CVV must be a 3-digit number");
            return false;
        }

        return true;
    }

    private boolean validateAlphabetic(String input) {
        return input.matches("[a-zA-Z ]+");
    }

    private boolean validatePostalCode(String input) {
        return input.matches("[a-zA-Z0-9]{6}");
    }

    private boolean validateCardNumber(String input) {
        return input.matches("\\d{16}");
    }

    private boolean validateExpiryDate(String input) {
        if (!input.matches("\\d{2}/\\d{2}")) { // MM/YY format
            return false;
        }

        String[] parts = input.split("/");
        int month = Integer.parseInt(parts[0]);
        int year = Integer.parseInt(parts[1]) + 2000; // Add 2000 to match the current century

        // Validate month range
        if (month < 1 || month > 12) {
            return false;
        }

        // Get current month and year
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1; // Month is 0-based

        // Validate year and month
        if (year < currentYear || (year == currentYear && month < currentMonth)) {
            return false;
        }

        return true;
    }

    private boolean validateCVV(String input) {
        return input.matches("\\d{3}");
    }

    private String generateRandomOrderNumber() {
        Random random = new Random();
        int randomNum = 100000 + random.nextInt(900000); // Generate a 6-digit random number
        return String.valueOf(randomNum);
    }
}
