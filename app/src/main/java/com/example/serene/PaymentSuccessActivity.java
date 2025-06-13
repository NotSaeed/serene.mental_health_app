package com.example.serene;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.FirebaseDatabase;

public class PaymentSuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_success);
        getSupportActionBar().setTitle("Payment Successful");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String username = getIntent().getStringExtra("username");
        String storyId = getIntent().getStringExtra("storyId");
        String personName = getIntent().getStringExtra("personName");
        String storyText = getIntent().getStringExtra("storyText");
        double amount = getIntent().getDoubleExtra("amount", 0);
        boolean anonymous = getIntent().getBooleanExtra("anonymous", false);
        String method = getIntent().getStringExtra("method");

        double appFee = Math.round(amount * 0.10 * 100.0) / 100.0;
        double donated = Math.round((amount - appFee) * 100.0) / 100.0;
        long timestamp = System.currentTimeMillis();


        DonateToPersonActivity.DonationRecord donation = new DonateToPersonActivity.DonationRecord(
                storyId, personName, amount, appFee, donated, anonymous, timestamp
        );

        FirebaseDatabase.getInstance().getReference("donations")
                .child(username)
                .push()
                .setValue(donation);


        TextView text = findViewById(R.id.txtPaymentSuccess);
        text.setText("🎉 Payment via " + method + " was successful!\n\nRM" + donated + " will go to support " + personName);

        Toast.makeText(this, "Thank you! Your donation was received.", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
