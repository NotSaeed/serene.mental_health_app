package com.example.serene;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DonationMenuActivity extends AppCompatActivity {

    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_menu);
        getSupportActionBar().setTitle("Donation Options");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        username = getIntent().getStringExtra("username");


        TextView welcomeText = findViewById(R.id.donationWelcomeText);
        welcomeText.setText("Hello, " + username + "! Choose how you'd like to help:");


        LinearLayout btnSupportPerson = findViewById(R.id.btnSupportPerson);
        LinearLayout btnSupportCharity = findViewById(R.id.btnSupportCharity);
        LinearLayout btnDonationHistory = findViewById(R.id.btnDonationHistory);

        btnSupportPerson.setOnClickListener(v -> {
            Intent intent = new Intent(DonationMenuActivity.this, SupportPersonActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });

        btnSupportCharity.setOnClickListener(v -> {
            Intent intent = new Intent(DonationMenuActivity.this, SupportCharityActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });

        btnDonationHistory.setOnClickListener(v -> {
            Intent intent = new Intent(DonationMenuActivity.this, DonationHistoryActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
