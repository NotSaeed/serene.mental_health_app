package com.example.serene;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.FirebaseDatabase;

public class DonateToPersonActivity extends AppCompatActivity {

    String username, storyId, personName, storyText;
    TextView txtPerson, txtStory;
    EditText inputAmount;
    Button btnConfirm;
    CheckBox checkboxAnonymous;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate_to_person);
        getSupportActionBar().setTitle("Confirm Donation");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        username = getIntent().getStringExtra("username");
        storyId = getIntent().getStringExtra("storyId");
        personName = getIntent().getStringExtra("personName");
        storyText = getIntent().getStringExtra("storyText");


        txtPerson = findViewById(R.id.txtPersonName);
        txtStory = findViewById(R.id.txtPersonStory);
        inputAmount = findViewById(R.id.inputAmount);
        btnConfirm = findViewById(R.id.btnConfirmDonation);
        checkboxAnonymous = findViewById(R.id.checkboxAnonymous);


        txtPerson.setText(personName);
        txtStory.setText(storyText);

        btnConfirm.setOnClickListener(v -> {
            String amountStr = inputAmount.getText().toString().trim();
            if (amountStr.isEmpty()) {
                inputAmount.setError("Please enter an amount");
                return;
            }

            double amount = Double.parseDouble(amountStr);
            boolean anonymous = checkboxAnonymous.isChecked();


            Intent intent = new Intent(DonateToPersonActivity.this, PaymentMethodActivity.class);
            intent.putExtra("username", username);
            intent.putExtra("storyId", storyId);
            intent.putExtra("personName", personName);
            intent.putExtra("storyText", storyText);
            intent.putExtra("amount", amount);
            intent.putExtra("anonymous", anonymous);
            startActivity(intent);
        });
    }


    public static class DonationRecord {
        public String storyId, recipient;
        public double amount, appFee, donated;
        public boolean anonymous;
        public long timestamp;

        public DonationRecord() {
        }

        public DonationRecord(String storyId, String recipient, double amount, double appFee,
                              double donated, boolean anonymous, long timestamp) {
            this.storyId = storyId;
            this.recipient = recipient;
            this.amount = amount;
            this.appFee = appFee;
            this.donated = donated;
            this.anonymous = anonymous;
            this.timestamp = timestamp;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
