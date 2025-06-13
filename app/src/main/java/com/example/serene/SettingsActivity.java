package com.example.serene;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingsActivity extends AppCompatActivity {

    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        username = getIntent().getStringExtra("username");

        LinearLayout btnUpdateInfo = findViewById(R.id.btnUpdateInfoContainer);
        LinearLayout btnDeleteAccount = findViewById(R.id.btnDeleteAccountContainer);
        LinearLayout btnLogout = findViewById(R.id.btnLogoutContainer);

        btnUpdateInfo.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, UpdateInfoActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });
        btnDeleteAccount.setOnClickListener(v -> {
            new AlertDialog.Builder(SettingsActivity.this)
                    .setTitle("Delete Account")
                    .setMessage("Are you sure you want to permanently delete your account?")
                    .setPositiveButton("Yes", (dialog, which) -> {


                        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");


                        usersRef.orderByChild("username").equalTo(username)
                                .addListenerForSingleValueEvent(new ValueEventListener() {

                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            for (DataSnapshot userSnap : snapshot.getChildren()) {
                                                userSnap.getRef().removeValue();
                                            }
                                        }


                                        FirebaseDatabase.getInstance().getReference("moods").child(username).removeValue();
                                        FirebaseDatabase.getInstance().getReference("quiz_results").child(username).removeValue();
                                        FirebaseDatabase.getInstance().getReference("game_scores").child(username).removeValue();


                                        DBHelper dbHelper = new DBHelper(SettingsActivity.this);
                                        dbHelper.deleteUser(username);

                                        Toast.makeText(SettingsActivity.this, "Account deleted successfully", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }


                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(SettingsActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
