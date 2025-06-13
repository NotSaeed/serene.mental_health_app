package com.example.serene;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;

public class QuizResultActivity extends AppCompatActivity {

    private TextView resultText, usernameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Quiz Result");

        resultText = findViewById(R.id.resultText);
        usernameText = findViewById(R.id.usernameText);

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        usernameText.setText("Hi " + username + "!");


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("quiz_results")
                .child(username)
                .child(LocalDate.now().toString());

        ref.get().addOnSuccessListener(snapshot -> {
            if (snapshot.exists()) {
                long latestTime = 0;
                int lastScore = 0;

                for (DataSnapshot attempt : snapshot.getChildren()) {
                    Long timestamp = attempt.child("timestamp").getValue(Long.class);
                    Integer score = attempt.child("score").getValue(Integer.class);

                    if (timestamp != null && score != null && timestamp > latestTime) {
                        latestTime = timestamp;
                        lastScore = score;
                    }
                }

                String message = getMessageForScore(lastScore);
                String summary = "\n\n You scored " + lastScore + " out of 40.";
                resultText.setText(message + summary);
            } else {
                resultText.setText("No quiz result found for today.");
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private String getMessageForScore(int score) {
        if (score <= 2) return " You should go now to a mental health doctor.";
        if (score <= 4) return " Your mental health might be suffering. Please seek support.";
        if (score <= 6) return " You're struggling. Talk to someone you trust.";
        if (score <= 8) return " You might need some rest and reflection.";
        if (score <= 10) return " Not bad, but there's room for self-care.";
        if (score <= 12) return " You're doing okay. Stay mindful.";
        if (score <= 14) return " Good balance. Keep it up!";
        if (score <= 16) return " Great mental resilience!";
        if (score <= 18) return " You're thriving emotionally!";
        if (score <= 20) return " Solid mental well-being. Stay consistent!";
        if (score <= 22) return " You're mentally strong and aware.";
        if (score <= 24) return " You're practicing great habits for your mental health.";
        if (score <= 26) return " You're above average in self-awareness and care.";
        if (score <= 28) return " Mental sharpness and emotional control are your strengths.";
        if (score <= 30) return " You're doing amazing! Just keep an eye on your balance.";
        if (score <= 32) return " High resilience and mindfulness. You're a role model!";
        if (score <= 34) return " You demonstrate excellent mental discipline and focus.";
        if (score <= 36) return " Balanced, confident, and mindful — you're glowing!";
        if (score <= 38) return " Elite level! You’re in control of your mind and emotions.";
        return " Perfect mental health! You're an inspiration to others!";
    }
}
