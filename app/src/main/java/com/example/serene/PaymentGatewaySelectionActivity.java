package com.example.serene;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class PaymentGatewaySelectionActivity extends AppCompatActivity {

    double amount;
    boolean anonymous;
    String username, storyId, personName, storyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_selection);
        getSupportActionBar().setTitle("Choose Payment Method");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        amount = getIntent().getDoubleExtra("amount", 0);
        anonymous = getIntent().getBooleanExtra("anonymous", false);
        username = getIntent().getStringExtra("username");
        storyId = getIntent().getStringExtra("storyId");
        personName = getIntent().getStringExtra("personName");
        storyText = getIntent().getStringExtra("storyText");

        Button btnCreditCard = findViewById(R.id.btnCreditCard);
        Button btnOnlineBanking = findViewById(R.id.btnOnlineBanking);

        btnCreditCard.setOnClickListener(v -> {
            Intent intent = new Intent(PaymentGatewaySelectionActivity.this, VisaPaymentActivity.class);
            passData(intent);
        });

        btnOnlineBanking.setOnClickListener(v -> {
            Intent intent = new Intent(PaymentGatewaySelectionActivity.this, OnlineBankingActivity.class);
            passData(intent);
        });
    }

    private void passData(Intent intent) {
        intent.putExtra("amount", amount);
        intent.putExtra("anonymous", anonymous);
        intent.putExtra("username", username);
        intent.putExtra("storyId", storyId);
        intent.putExtra("personName", personName);
        intent.putExtra("storyText", storyText);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
