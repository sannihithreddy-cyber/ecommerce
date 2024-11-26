package com.example.ecommerce;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class loginpage extends AppCompatActivity {
    // Fields for login
    EditText loginEmail, loginPassword;

    // Fields for sign-up
    EditText signUpEmail, signUpPassword, confirmPassword, firstName, lastName;

    // Buttons
    Button loginButton, signUpButton;

    // Firebase Authentication
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpage);

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Bind login fields
        loginEmail = findViewById(R.id.EmailAddress);
        loginPassword = findViewById(R.id.password);

        // Bind sign-up fields
        signUpEmail = findViewById(R.id.emailsignup);
        signUpPassword = findViewById(R.id.signuppassword);
        confirmPassword = findViewById(R.id.confirmpassword);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastname);

        // Bind buttons
        loginButton = findViewById(R.id.loginbutton);
        signUpButton = findViewById(R.id.signupbutton);

        // Login button logic
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = loginEmail.getText().toString().trim();
                String password = loginPassword.getText().toString().trim();

                // Validate login input
                if (email.isEmpty()) {
                    loginEmail.setError("Enter email");
                    return;
                }
                if (password.isEmpty()) {
                    loginPassword.setError("Enter password");
                    return;
                }

                // Log in with Firebase
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Toast.makeText(loginpage.this, "Welcome back, " + user.getEmail(), Toast.LENGTH_SHORT).show();

                                    // Redirect to homepage
                                    Intent intent = new Intent(loginpage.this, homepage.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(loginpage.this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        // Sign-up button logic
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = signUpEmail.getText().toString().trim();
                String password = signUpPassword.getText().toString().trim();
                String confirmPass = confirmPassword.getText().toString().trim();

                // Validate sign-up input
                if (email.isEmpty()) {
                    signUpEmail.setError("Enter email");
                    return;
                }
                if (password.isEmpty()) {
                    signUpPassword.setError("Enter password");
                    return;
                }
                if (!password.equals(confirmPass)) {
                    confirmPassword.setError("Passwords do not match");
                    return;
                }

                // Optional: Validate other fields
                if (firstName.getText().toString().trim().isEmpty()) {
                    firstName.setError("Enter your first name");
                    return;
                }
                if (lastName.getText().toString().trim().isEmpty()) {
                    lastName.setError("Enter your last name");
                    return;
                }

                // Create user with Firebase
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Toast.makeText(loginpage.this, "Sign-up successful. Welcome, " + user.getEmail(), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(loginpage.this, "Sign-up failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}
