package com.example.serene;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class VisaPaymentActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visa_payment);
        getSupportActionBar().setTitle("Visa Payment");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView resultText = findViewById(R.id.textPaymentResult);
        resultText.setText("Payment Successful via Visa!");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
