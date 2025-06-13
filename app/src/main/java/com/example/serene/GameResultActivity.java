package com.example.serene;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.view.Gravity;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameResultActivity extends AppCompatActivity {

    private LinearLayout scoreListContainer;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(generateLayout());

        getSupportActionBar().setTitle("Game Score History");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        username = getIntent().getStringExtra("username");
        if (username == null) username = "guest";

        loadScoreHistory();
    }

    private void loadScoreHistory() {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("game_scores").child(username);
        dbRef.get().addOnSuccessListener(snapshot -> {
            List<String> records = new ArrayList<>();

            for (DataSnapshot dateSnap : snapshot.getChildren()) {
                String date = dateSnap.getKey();
                int high = 0;
                String badge = "";

                for (DataSnapshot entry : dateSnap.getChildren()) {
                    Integer score = entry.child("score").getValue(Integer.class);
                    String currentBadge = entry.child("badge").getValue(String.class);
                    if (score != null && score > high) {
                        high = score;
                        badge = currentBadge != null ? currentBadge : "";
                    }
                }

                String line = date + " - Highest Score: " + high;
                if (!badge.isEmpty()) line += " 🏅 " + badge;
                line += " " + getFeedback(high);
                records.add(line);
            }

            Collections.sort(records);

            for (String line : records) {
                TextView tv = new TextView(this);
                tv.setText(line);
                tv.setTextSize(18f);
                tv.setTextColor(getResources().getColor(android.R.color.black));
                tv.setGravity(Gravity.CENTER);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(20, 20, 20, 80);
                params.gravity = Gravity.CENTER_HORIZONTAL;
                tv.setLayoutParams(params);

                scoreListContainer.addView(tv);
            }
        });
    }

    private String getFeedback(int score) {
        if (score >= 15) return "✨ Great focus!";
        else if (score >= 10) return "👍 Nice work!";
        else if (score >= 5) return "-> Keep practicing!";
        else return "! Try again tomorrow!";
    }

    private ScrollView generateLayout() {
        ScrollView scrollView = new ScrollView(this);
        scrollView.setFillViewport(true); // Ensures the layout fills screen height

        scoreListContainer = new LinearLayout(this);
        scoreListContainer.setOrientation(LinearLayout.VERTICAL);
        scoreListContainer.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        scoreListContainer.setPadding(32, 48, 32, 64); // top, bottom padding

        scrollView.addView(scoreListContainer, new ScrollView.LayoutParams(
                ScrollView.LayoutParams.MATCH_PARENT,
                ScrollView.LayoutParams.MATCH_PARENT
        ));
        return scrollView;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
