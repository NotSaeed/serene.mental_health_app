package com.example.serene;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MoodTrackerActivity extends AppCompatActivity {

    private final String[] moodLabels = {"Angry", "Sad", "Anxious", "Neutral", "Okay", "Happy"};
    private final int[] moodScores = {0, 1, 2, 3, 4, 5};
    private final int[] moodIconDrawables = {
            R.drawable.emoji_angry,
            R.drawable.emoji_sad,
            R.drawable.emoji_anxious,
            R.drawable.emoji_neutral,
            R.drawable.emoji_okay,
            R.drawable.emoji_happy
    };

    private EditText noteInput;
    private DatabaseReference databaseRef;
    private int selectedMoodIndex = -1;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_tracker);


        getSupportActionBar().setTitle("Mood Tracker");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        username = getIntent().getStringExtra("username");
        if (username == null) username = "guest";


        databaseRef = FirebaseDatabase.getInstance().getReference("moods");
        noteInput = findViewById(R.id.noteInput);


        int[] imageViewIds = {
                R.id.imageAngry, R.id.imageSad, R.id.imageAnxious,
                R.id.imageNeutral, R.id.imageOkay, R.id.imageHappy
        };

        for (int i = 0; i < imageViewIds.length; i++) {
            ImageView moodImg = findViewById(imageViewIds[i]);
            int index = i;
            moodImg.setOnClickListener(v -> {
                selectedMoodIndex = index;
                Toast.makeText(this, "Selected: " + moodLabels[index], Toast.LENGTH_SHORT).show();
            });
        }


        findViewById(R.id.saveMoodButton).setOnClickListener(v -> saveMood());


        findViewById(R.id.viewTrendButton).setOnClickListener(v -> {
            Intent intent = new Intent(MoodTrackerActivity.this, MoodTrendActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });


        findViewById(R.id.viewSelfTrendButton).setOnClickListener(v -> {
            Intent intent = new Intent(MoodTrackerActivity.this, SelfMoodTrendActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });
    }

    private void saveMood() {
        if (selectedMoodIndex == -1) {
            Toast.makeText(this, "Please select your mood", Toast.LENGTH_SHORT).show();
            return;
        }


        String note = noteInput.getText().toString().trim();
        int score = moodScores[selectedMoodIndex];
        String mood = moodLabels[selectedMoodIndex];
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        MoodEntry entry = new MoodEntry(username, mood, score, note, date);


        databaseRef.child(username).child(date).setValue(entry);


        databaseRef.child("all").push().setValue(entry);

        Toast.makeText(this, "Mood saved!", Toast.LENGTH_SHORT).show();
        noteInput.setText("");
        selectedMoodIndex = -1;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }


    public static class MoodEntry {
        public String username;
        public String mood;
        public int score;
        public String note;
        public String date;

        public MoodEntry() {} // Required for Firebase

        public MoodEntry(String username, String mood, int score, String note, String date) {
            this.username = username;
            this.mood = mood;
            this.score = score;
            this.note = note;
            this.date = date;
        }
    }
}
