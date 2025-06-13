package com.example.serene;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    EditText editTextUsername, editTextPassword;
    Button buttonRegister;
    ImageView togglePasswordVisibility;
    DBHelper dbHelper;
    DatabaseReference firebaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Serene");
        }

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonRegister = findViewById(R.id.buttonRegister);
        togglePasswordVisibility = findViewById(R.id.togglePasswordVisibility);

        dbHelper = new DBHelper(this);
        firebaseRef = FirebaseDatabase.getInstance().getReference("users");

        togglePasswordVisibility.setOnClickListener(v -> {
            if (editTextPassword.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                togglePasswordVisibility.setImageResource(android.R.drawable.ic_menu_view);
            } else {
                editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                togglePasswordVisibility.setImageResource(android.R.drawable.ic_menu_view);
            }
            editTextPassword.setSelection(editTextPassword.getText().length());
        });

        buttonRegister.setOnClickListener(v -> {
            String username = editTextUsername.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (username.contains(" ") || password.contains(" ")) {
                Toast.makeText(this, "No spaces allowed in username or password", Toast.LENGTH_SHORT).show();
                return;
            }

            if (username.length() < 4 || password.length() < 4) {
                Toast.makeText(this, "Username and password must be at least 4 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean inserted = dbHelper.registerUser(username, password);
            if (inserted) {
                String id = firebaseRef.push().getKey();
                firebaseRef.child(id).setValue(new User(username, password));
                Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Registration failed. Try a different username.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
