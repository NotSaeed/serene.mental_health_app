package com.example.serene;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.util.*;

public class PopGameActivity extends AppCompatActivity {

    private RelativeLayout gameLayout;
    private TextView tvTimer, tvScore;
    private int score = 0;
    private int bubbleSpeed = 3000;
    private Random random = new Random();
    private CountDownTimer gameTimer;
    private MediaPlayer popSound, bgMusic;
    private String username;

    private final List<String> positive = Arrays.asList(
            "I am capable", "I’m doing my best", "I matter", "I choose peace", "I am strong",
            "Every step counts", "I’ve overcome before", "I can improve", "I deserve happiness", "I am growing"
    );

    private final List<String> negative = Arrays.asList(
            "I’m a failure", "I can’t do it", "No one cares", "I’m not enough", "Everything is wrong",
            "I’m useless", "It’s hopeless", "I’m always anxious", "Nothing works", "I give up"
    );

    private final List<String> allThoughts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_game);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Positive Pop Game");

        gameLayout = findViewById(R.id.gameLayout);
        tvTimer = findViewById(R.id.tvTimer);
        tvScore = findViewById(R.id.tvScore);

        username = getIntent().getStringExtra("username");
        if (username == null) username = "guest";

        popSound = MediaPlayer.create(this, R.raw.pop);
        bgMusic = MediaPlayer.create(this, R.raw.bgmusic); // Add bgmusic.mp3 to res/raw
        bgMusic.setLooping(true);
        bgMusic.start();

        allThoughts.addAll(positive);
        allThoughts.addAll(negative);

        startGame();
    }

    private void startGame() {
        gameTimer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                tvTimer.setText((millisUntilFinished / 1000) + "s");

                long secondsLeft = millisUntilFinished / 1000;
                if (secondsLeft <= 45 && secondsLeft > 30) bubbleSpeed = 4000;
                else if (secondsLeft <= 30 && secondsLeft > 15) bubbleSpeed = 3000;
                else if (secondsLeft <= 15) bubbleSpeed = 2000;

                spawnThought();
            }

            public void onFinish() {
                tvTimer.setText("Game Over\nScore: " + score);
                Toast.makeText(PopGameActivity.this, "Final Score: " + score, Toast.LENGTH_LONG).show();

                String date = LocalDate.now().toString();
                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("game_scores");
                String id = dbRef.push().getKey();

                Map<String, Object> data = new HashMap<>();
                data.put("score", score);
                data.put("timestamp", System.currentTimeMillis());

                if (score >= 15) {
                    data.put("badge", "Focus Master");
                }

                if (id != null) {
                    dbRef.child(username).child(date).child(id).setValue(data);
                }

                Intent intent = new Intent(PopGameActivity.this, GameResultActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
                finish();
            }
        }.start();
    }

    private void spawnThought() {
        final TextView bubble = new TextView(this);
        bubble.setText(allThoughts.get(random.nextInt(allThoughts.size())));
        bubble.setTextSize(16f);
        bubble.setPadding(20, 10, 20, 10);
        bubble.setGravity(Gravity.CENTER);
        bubble.setBackgroundResource(R.drawable.bubble_background);
        bubble.setTextColor(getResources().getColor(android.R.color.black));

        int maxX = gameLayout.getWidth() - 200;
        int x = random.nextInt(Math.max(100, maxX));
        bubble.setX(x);
        bubble.setY(gameLayout.getHeight());

        bubble.setOnClickListener(v -> {
            String text = bubble.getText().toString();
            if (positive.contains(text)) score += 1;
            else score -= 1;

            tvScore.setText("Score: " + score);
            if (popSound != null) popSound.start();
            gameLayout.removeView(bubble);
        });

        gameLayout.addView(bubble);

        ObjectAnimator animator = ObjectAnimator.ofFloat(bubble, "translationY", -200f);
        animator.setDuration(bubbleSpeed);
        animator.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (gameTimer != null) gameTimer.cancel();
        if (popSound != null) popSound.release();
        if (bgMusic != null) {
            bgMusic.stop();
            bgMusic.release();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
