package com.example.serene;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateInfoActivity extends AppCompatActivity {

    EditText etNewUsername, etNewPassword;
    Button btnSave;
    String currentUsername;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_info);

        getSupportActionBar().setTitle("Update Info");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        currentUsername = getIntent().getStringExtra("username");

        etNewUsername = findViewById(R.id.etNewUsername);
        etNewPassword = findViewById(R.id.etNewPassword);
        btnSave = findViewById(R.id.btnSave);

        dbHelper = new DBHelper(this);


        FirebaseDatabase.getInstance().getReference("users")
                .orderByChild("username").equalTo(currentUsername)
                .addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            String name = snap.child("username").getValue(String.class);
                            String pass = snap.child("password").getValue(String.class);
                            etNewUsername.setText(name);
                            etNewPassword.setText(pass);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull com.google.firebase.database.DatabaseError error) {
                        Toast.makeText(UpdateInfoActivity.this, "Failed to load", Toast.LENGTH_SHORT).show();
                    }
                });

        btnSave.setOnClickListener(v -> {
            String newUsername = etNewUsername.getText().toString().trim();
            String newPassword = etNewPassword.getText().toString().trim();

            if (newUsername.isEmpty() || newPassword.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            FirebaseDatabase.getInstance().getReference("users")
                    .orderByChild("username").equalTo(currentUsername)
                    .addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot snap : snapshot.getChildren()) {
                                snap.getRef().child("username").setValue(newUsername);
                                snap.getRef().child("password").setValue(newPassword);
                            }

                            dbHelper.updateUser(currentUsername, newUsername, newPassword);

                            Toast.makeText(UpdateInfoActivity.this, "Info updated", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        @Override
                        public void onCancelled(@NonNull com.google.firebase.database.DatabaseError error) {
                            Toast.makeText(UpdateInfoActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
