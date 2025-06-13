package com.example.serene;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class OnlineBankingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_banking);
        getSupportActionBar().setTitle("Online Banking");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView resultText = findViewById(R.id.textPaymentResult);
        resultText.setText("Payment Successful via Online Banking!");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
