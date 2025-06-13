package com.example.serene;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity {

    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_main);




        username = getIntent().getStringExtra("username");

        TextView welcomeText = findViewById(R.id.welcomeText);
        welcomeText.setText("Welcome, " + username + "!");


        findViewById(R.id.btnCalendar).setOnClickListener(v -> goTo(CalendarActivity.class));
        findViewById(R.id.btnMoodTracker).setOnClickListener(v -> goTo(MoodTrackerActivity.class));
        findViewById(R.id.btnSelfCare).setOnClickListener(v -> goTo(SelfCareChallengeActivity.class));
        findViewById(R.id.btnAudioPlayer).setOnClickListener(v -> goTo(AudioPlayerActivity.class));
        findViewById(R.id.btnGallery).setOnClickListener(v -> goTo(GalleryActivity.class));
        findViewById(R.id.btnChillSpace).setOnClickListener(v -> goTo(ChillSpaceActivity.class));
        findViewById(R.id.btnChatRoom).setOnClickListener(v -> goTo(ChatRoomActivity.class));
        findViewById(R.id.btnQuiz).setOnClickListener(v -> goTo(QuizActivity.class));
        findViewById(R.id.btnSOS).setOnClickListener(v -> goTo(SOSActivity.class));

        findViewById(R.id.btnGame).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PopGameActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });

        findViewById(R.id.btnGameResult).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GameResultActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });

        findViewById(R.id.btnQuizResult).setOnClickListener(v -> {
            if (username != null) {
                Intent intent = new Intent(MainActivity.this, QuizResultActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Please login again", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });


        FloatingActionButton fabDonate = findViewById(R.id.fabDonate);
        fabDonate.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DonationMenuActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });
    }

    private void goTo(Class<?> cls) {
        Intent intent = new Intent(MainActivity.this, cls);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            intent.putExtra("username", username); // Pass current user
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
