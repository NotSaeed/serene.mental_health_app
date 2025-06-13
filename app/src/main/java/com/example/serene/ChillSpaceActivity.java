package com.example.serene;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class ChillSpaceActivity extends AppCompatActivity {

    private View breathCircle;
    private TextView breathText, countdownText, affirmationText;

    private MediaPlayer inhalePlayer, exhalePlayer;
    private Handler handler = new Handler();
    private Vibrator vibrator;

    private int phase = 0; // 0 = Inhale, 1 = Hold, 2 = Exhale

    private final int INHALE_DURATION = 4000;
    private final int HOLD_DURATION = 7000;
    private final int EXHALE_DURATION = 8000;

    private final String[] affirmations = {
            "You are safe. You are healing.",
            "Inhale calm, exhale tension.",
            "You are enough.",
            "This moment is yours.",
            "Feel the peace in your breath."
    };

    private Runnable exhaleLoop;
    private Runnable breathCycle;

    private void animateCircle(float from, float to, int duration) {
        ScaleAnimation scale = new ScaleAnimation(
                from, to,
                from, to,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        scale.setDuration(duration);
        scale.setFillAfter(true);
        breathCircle.startAnimation(scale);
    }

    private void startCountdown(int durationMillis) {
        final int totalSeconds = durationMillis / 1000;
        countdownText.setText(String.valueOf(totalSeconds));

        for (int i = 1; i <= totalSeconds; i++) {
            final int second = totalSeconds - i;
            handler.postDelayed(() -> countdownText.setText(String.valueOf(second)), i * 1000L);
        }
    }

    private void stopSounds() {
        if (inhalePlayer != null && inhalePlayer.isPlaying()) inhalePlayer.pause();
        if (exhalePlayer != null && exhalePlayer.isPlaying()) exhalePlayer.pause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chill_space);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Chill Space");

        breathCircle = findViewById(R.id.breathCircle);
        breathText = findViewById(R.id.breathText);
        countdownText = findViewById(R.id.countdownText);
        affirmationText = findViewById(R.id.affirmationText);

        inhalePlayer = MediaPlayer.create(this, R.raw.inhale);
        exhalePlayer = MediaPlayer.create(this, R.raw.exhale);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        int rand = (int) (Math.random() * affirmations.length);
        affirmationText.setText(affirmations[rand]);

        exhaleLoop = new Runnable() {
            @Override
            public void run() {
                if (exhalePlayer != null) {
                    exhalePlayer.seekTo(0);
                    exhalePlayer.start();
                }
                handler.postDelayed(this, 3000);
            }
        };

        breathCycle = new Runnable() {
            @Override
            public void run() {
                stopSounds();

                if (vibrator != null && vibrator.hasVibrator()) {
                    vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
                }

                switch (phase) {
                    case 0: // Inhale
                        breathText.setText("Breathe In");
                        breathCircle.setBackgroundColor(ContextCompat.getColor(ChillSpaceActivity.this, R.color.inhaleColor));
                        animateCircle(1f, 1.5f, INHALE_DURATION);
                        if (inhalePlayer != null) {
                            inhalePlayer.seekTo(0);
                            inhalePlayer.start();
                        }
                        startCountdown(INHALE_DURATION);
                        handler.postDelayed(this, INHALE_DURATION);
                        break;

                    case 1: // Hold
                        breathText.setText("Hold");
                        breathCircle.setBackgroundColor(ContextCompat.getColor(ChillSpaceActivity.this, R.color.holdColor));
                        startCountdown(HOLD_DURATION);
                        handler.postDelayed(this, HOLD_DURATION);
                        break;

                    case 2: // Exhale
                        breathText.setText("Breathe Out");
                        breathCircle.setBackgroundColor(ContextCompat.getColor(ChillSpaceActivity.this, R.color.exhaleColor));
                        animateCircle(1.5f, 1f, EXHALE_DURATION);
                        handler.post(exhaleLoop);
                        handler.postDelayed(() -> handler.removeCallbacks(exhaleLoop), EXHALE_DURATION);
                        startCountdown(EXHALE_DURATION);
                        handler.postDelayed(this, EXHALE_DURATION);
                        break;
                }

                phase = (phase + 1) % 3;
            }
        };

        handler.post(breathCycle);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(breathCycle);
        handler.removeCallbacks(exhaleLoop);
        stopSounds();
        if (inhalePlayer != null) inhalePlayer.release();
        if (exhalePlayer != null) exhalePlayer.release();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
