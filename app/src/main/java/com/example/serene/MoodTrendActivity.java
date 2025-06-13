package com.example.serene;

import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class MoodTrendActivity extends AppCompatActivity {

    private CustomMoodChartView chartView;
    private LinearLayout chartContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_trend);

        getSupportActionBar().setTitle("Mood Trend");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        chartContainer = findViewById(R.id.chartContainer);

        chartView = new CustomMoodChartView(this);
        chartView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                800
        ));
        chartContainer.addView(chartView);

        addMoodLegend();
        loadMoodData();
    }

    private void loadMoodData() {
        FirebaseDatabase.getInstance().getReference("moods").child("all")
                .get()
                .addOnSuccessListener(snapshot -> {
                    ArrayList<MoodEntry> moodList = new ArrayList<>();

                    for (DataSnapshot entrySnapshot : snapshot.getChildren()) {
                        HashMap<String, Object> map = (HashMap<String, Object>) entrySnapshot.getValue();
                        if (map != null && map.get("date") != null && map.get("score") != null) {
                            String date = String.valueOf(map.get("date"));
                            long scoreLong = (long) map.get("score");
                            moodList.add(new MoodEntry(date, (int) scoreLong));
                        }
                    }

                    if (moodList.isEmpty()) {
                        Toast.makeText(this, "No mood data found", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Collections.sort(moodList, Comparator.comparing(entry -> entry.date));
                    chartView.setMoodEntries(moodList);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load data", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                });
    }

    private void addMoodLegend() {
        TextView legendTitle = new TextView(this);
        legendTitle.setText("Mood Scale:");
        legendTitle.setTextSize(18f);
        legendTitle.setPadding(0, 24, 0, 12);
        chartContainer.addView(legendTitle);

        String[] labels = {
                "0 -  Angry (Red)",
                "1 -  Sad (Orange)",
                "2 -  Anxious (Light Orange)",
                "3 -  Neutral (Yellow)",
                "4 -  Okay (Light Green)",
                "5 -  Happy (Green)"
        };

        for (String label : labels) {
            TextView item = new TextView(this);
            item.setText(label);
            item.setTextSize(16f);
            item.setPadding(8, 4, 8, 4);
            item.setGravity(Gravity.START);
            chartContainer.addView(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public static class MoodEntry {
        String date;
        int score;

        public MoodEntry(String date, int score) {
            this.date = date;
            this.score = score;
        }
    }
}
