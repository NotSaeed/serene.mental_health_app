package com.example.serene;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class PaymentMethodActivity extends AppCompatActivity {

    String username, storyId, personName, storyText;
    double amount;
    boolean anonymous;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);
        getSupportActionBar().setTitle("Choose Payment Method");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        username = getIntent().getStringExtra("username");
        storyId = getIntent().getStringExtra("storyId");
        personName = getIntent().getStringExtra("personName");
        storyText = getIntent().getStringExtra("storyText");
        amount = getIntent().getDoubleExtra("amount", 0);
        anonymous = getIntent().getBooleanExtra("anonymous", false);

        findViewById(R.id.optionVisa).setOnClickListener(v -> launchSuccess("Visa"));
        findViewById(R.id.optionMaybank).setOnClickListener(v -> launchSuccess("Maybank2U"));
        findViewById(R.id.optionTnG).setOnClickListener(v -> launchSuccess("Touch 'n Go"));
    }

    private void launchSuccess(String method) {
        Intent intent = new Intent(PaymentMethodActivity.this, PaymentSuccessActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("storyId", storyId);
        intent.putExtra("personName", personName);
        intent.putExtra("storyText", storyText);
        intent.putExtra("amount", amount);
        intent.putExtra("anonymous", anonymous);
        intent.putExtra("method", method);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
