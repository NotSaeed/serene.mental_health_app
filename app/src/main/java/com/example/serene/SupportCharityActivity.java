package com.example.serene;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class SupportCharityActivity extends AppCompatActivity {

    private LinearLayout orgContainer;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_charity);
        getSupportActionBar().setTitle("Support a Charity");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // This shows the back arrow

        username = getIntent().getStringExtra("username");
        orgContainer = findViewById(R.id.orgContainer);

        loadStaticCharityList();
    }

    private void loadStaticCharityList() {
        addCharity("SimplyGiving: Sabah Mental Health",
                "https://legacy.simplygiving.com/Home/Step1?NonProfitId=44abc0ef-ed88-4a57-bac5-78acebf0dd2a",
                "Support mental health causes in East Malaysia");

        addCharity("Malaysian Care",
                "https://malaysiancare.org/donation/",
                "Community mental health & youth empowerment");

        addCharity("Amitabha Malaysia",
                "https://m.amitabhamalaysia.org/en/donation",
                "Provides care & mental wellness programs");
    }


    private void addCharity(String name, String url, String desc) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_charity, orgContainer, false);
        ((TextView) view.findViewById(R.id.txtOrgName)).setText(name);
        ((TextView) view.findViewById(R.id.txtOrgDesc)).setText(desc);
        view.findViewById(R.id.btnVisit).setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
        });
        orgContainer.addView(view);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
