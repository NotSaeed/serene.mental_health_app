package com.example.serene;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;

public class DonationHistoryActivity extends AppCompatActivity {

    TextView totalDonatedText;
    Spinner filterSpinner;
    RecyclerView recyclerDonations;

    String username;
    ArrayList<DonationRecord> allDonations = new ArrayList<>();
    ArrayList<String> filterOptions = new ArrayList<>();
    String currentFilter = "All";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_history);
        getSupportActionBar().setTitle("Your Donations");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        username = getIntent().getStringExtra("username");


        totalDonatedText = findViewById(R.id.totalDonatedText);
        filterSpinner = findViewById(R.id.filterSpinner);
        recyclerDonations = findViewById(R.id.recyclerDonations);

        recyclerDonations.setLayoutManager(new LinearLayoutManager(this));

        loadDonationHistory();
    }

    private void loadDonationHistory() {
        FirebaseDatabase.getInstance().getReference("donations")
                .child(username)
                .get()
                .addOnSuccessListener(snapshot -> {
                    allDonations.clear();
                    filterOptions.clear();
                    filterOptions.add("All");

                    HashSet<String> months = new HashSet<>();
                    SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());

                    for (DataSnapshot snap : snapshot.getChildren()) {
                        DonationRecord d = snap.getValue(DonationRecord.class);
                        if (d != null) {
                            if (d.timestamp == 0) {
                                d.timestamp = System.currentTimeMillis();
                            }
                            allDonations.add(d);

                            String month = monthFormat.format(new Date(d.timestamp));
                            months.add(month);
                        }
                    }

                    ArrayList<String> sortedMonths = new ArrayList<>(months);
                    Collections.sort(sortedMonths);
                    filterOptions.addAll(sortedMonths);

                    setupFilterSpinner();
                    applyFilter(currentFilter);
                });
    }

    private void setupFilterSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, filterOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner.setAdapter(adapter);

        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentFilter = filterOptions.get(position);
                applyFilter(currentFilter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void applyFilter(String filter) {
        double total = 0;
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());

        ArrayList<DonationRecord> filteredList = new ArrayList<>();
        for (DonationRecord d : allDonations) {
            String month = monthFormat.format(new Date(d.timestamp));
            if (filter.equals("All") || month.equals(filter)) {
                filteredList.add(d);
                total += d.amount;
            }
        }

        Collections.sort(filteredList, Comparator.comparingDouble(a -> -a.amount));


        totalDonatedText.setText(String.format("Total Donated: RM%.2f", total));
        DonationAdapter adapter = new DonationAdapter(filteredList, username);
        recyclerDonations.setAdapter(adapter);
    }

    public static class DonationRecord {
        public String storyId, recipient;
        public double amount, appFee, donated;
        public boolean anonymous;
        public long timestamp;

        public DonationRecord() {
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
