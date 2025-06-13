package com.example.serene;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.*;

import java.text.SimpleDateFormat;
import java.util.*;

public class SelfCareChallengeActivity extends AppCompatActivity {

    private LinearLayout challengeContainer;
    private DatabaseReference databaseReference;
    private String userId, today, username;
    private TextView tvStreakBadge;

    private final List<String> challengeList = Arrays.asList(
            " Drink 8 glasses of water",
            " Walk outside for 15 minutes",
            " Read for 15 minutes",
            " Meditate for 5 minutes",
            "️ Write 3 things you’re grateful for",
            " No phone for 1 hour",
            " Compliment yourself",
            " Call or text a friend"
    );

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_care_challenge);

        getSupportActionBar().setTitle("Self-Care Challenges");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        challengeContainer = findViewById(R.id.challengeContainer);
        tvStreakBadge = findViewById(R.id.tvStreakBadge);

        username = getIntent().getStringExtra("username");

        if (username == null || username.isEmpty()) {
            Toast.makeText(this, "Username not found. Please log in again.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        userId = username;
        today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        databaseReference = FirebaseDatabase.getInstance()
                .getReference("SelfCareProgress")
                .child(userId)
                .child(today);

        scheduleDailyReminder();
        loadSavedProgress();
    }

    private void loadSavedProgress() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Set<String> completed = new HashSet<>();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    if (Boolean.TRUE.equals(snap.getValue(Boolean.class))) {
                        completed.add(snap.getKey());
                    }
                }
                buildChallengeList(completed);
                checkStreakCondition();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(SelfCareChallengeActivity.this, "Error loading progress", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void buildChallengeList(Set<String> completed) {
        for (String challenge : challengeList) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(challenge);
            checkBox.setTextSize(18f);
            checkBox.setPadding(10, 20, 10, 20);
            checkBox.setChecked(completed.contains(challenge));

            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                databaseReference.child(challenge).setValue(isChecked).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        checkStreakCondition();
                    }
                });

                Toast.makeText(SelfCareChallengeActivity.this,
                        isChecked ? "Challenge marked done!" : "Challenge unchecked",
                        Toast.LENGTH_SHORT).show();
            });

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 10, 0, 10);
            checkBox.setLayoutParams(params);
            challengeContainer.addView(checkBox);
        }
    }

    private void checkStreakCondition() {
        DatabaseReference streakRef = FirebaseDatabase.getInstance()
                .getReference("SelfCareStreaks")
                .child(userId);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean isAllDone = true;
                for (String challenge : challengeList) {
                    Boolean completed = snapshot.child(challenge).getValue(Boolean.class);
                    if (completed == null || !completed) {
                        isAllDone = false;
                        break;
                    }
                }

                final boolean finalAllDone = isAllDone;

                streakRef.child("streakCount").get().addOnSuccessListener(task -> {
                    Long count = task.getValue(Long.class);
                    long currentStreak = (count == null) ? 0 : count;


                    tvStreakBadge.setText(" Current Streak: " + currentStreak + " Day" + (currentStreak == 1 ? "" : "s"));


                    if (finalAllDone) {
                        streakRef.child("lastCompleted").setValue(today);
                        streakRef.child("streakCount").setValue(currentStreak + 1);

                        tvStreakBadge.setText(" Current Streak: " + (currentStreak + 1) + " Days");

                        if (currentStreak + 1 >= 5) {
                            Toast.makeText(SelfCareChallengeActivity.this,
                                    "🎉 You’ve maintained a " + (currentStreak + 1) + "-day streak! Keep going!",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(SelfCareChallengeActivity.this, "Failed to check streak", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void scheduleDailyReminder() {
        Intent intent = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
        }

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,
                pendingIntent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}