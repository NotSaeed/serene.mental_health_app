package com.example.serene;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class QuizActivity extends AppCompatActivity {

    private TextView questionText, questionNumberText, timerText;
    private Button option1, option2, option3, option4, nextButton;
    private int currentQuestion = 0;
    private int score = 0;
    private String username;
    private DBHelper dbHelper;
    private CountDownTimer countDownTimer;
    private boolean answered = false;
    private int selectedIndex = -1;

    private final String[] questions = {
            "Mental health includes emotional, psychological, and ____ well-being.",
            "Which of the following is a symptom of depression?",
            "Which practice helps reduce stress and anxiety?",
            "What is a common sign of anxiety?",
            "What should you do if a friend is showing signs of suicidal thoughts?",
            "What is one benefit of physical activity on mental health?",
            "Which is a healthy coping mechanism?",
            "Which type of therapy is commonly used to treat depression?",
            "How many hours of sleep are generally recommended for good mental health?",
            "What is mindfulness?"
    };

    private final String[][] options = {
            {"Spiritual", "Physical", "Financial", "Political"},
            {"Increased energy", "Feeling hopeless", "Better sleep", "Increased appetite"},
            {"Multitasking", "Rumination", "Mindfulness meditation", "Avoiding problems"},
            {"Joyfulness", "Stable mood", "Racing thoughts", "Increased confidence"},
            {"Ignore it", "Change the subject", "Encourage them to seek help", "Tell them to get over it"},
            {"Increases blood pressure", "Improves sleep", "Decreases social interaction", "Makes you tired"},
            {"Substance use", "Meditation", "Overworking", "Avoidance"},
            {"Homeopathy", "Diet therapy", "Surgery", "Cognitive Behavioral Therapy (CBT)"},
            {"3–4 hours", "Less than 5 hours", "10–12 hours", "7–9 hours"},
            {"Judging your thoughts", "Always being happy", "Avoiding emotions", "Being present and aware"}
    };


    private final int[][] optionPoints = {
            {2, 4, 1, 0},
            {1, 4, 2, 3},
            {2, 1, 4, 0},
            {1, 2, 4, 3},
            {0, 1, 4, 2},
            {0, 4, 2, 1},
            {0, 4, 1, 2},
            {1, 2, 0, 4},
            {1, 2, 3, 4},
            {0, 1, 2, 4}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Quiz");

        questionText = findViewById(R.id.questionText);
        questionNumberText = findViewById(R.id.questionNumberText);
        timerText = findViewById(R.id.timerText);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        nextButton = findViewById(R.id.nextButton);

        dbHelper = new DBHelper(this);
        username = getIntent().getStringExtra("username");
        if (username == null) username = "guest";

        loadQuestion();

        View.OnClickListener answerClickListener = view -> {
            if (answered) return;

            answered = true;
            Button clicked = (Button) view;
            selectedIndex = getOptionIndex(clicked);

            int points = optionPoints[currentQuestion][selectedIndex];
            score += points;

            clicked.setBackgroundColor(Color.parseColor("#4CAF50")); // green
            disableOptions();
            countDownTimer.cancel();
        };

        option1.setOnClickListener(answerClickListener);
        option2.setOnClickListener(answerClickListener);
        option3.setOnClickListener(answerClickListener);
        option4.setOnClickListener(answerClickListener);

        nextButton.setOnClickListener(v -> nextOrSubmit());
    }

    private void loadQuestion() {
        answered = false;
        selectedIndex = -1;

        questionText.setText(questions[currentQuestion]);
        questionNumberText.setText("Question " + (currentQuestion + 1) + " of " + questions.length);
        option1.setText(options[currentQuestion][0]);
        option2.setText(options[currentQuestion][1]);
        option3.setText(options[currentQuestion][2]);
        option4.setText(options[currentQuestion][3]);

        resetOptionColors();
        enableOptions();

        countDownTimer = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                timerText.setText("Time left: " + millisUntilFinished / 1000 + "s");
            }

            public void onFinish() {
                timerText.setText("Time's up!");
                if (!answered) {
                    answered = true;
                    disableOptions(); // no points
                    nextOrSubmit();   // auto next
                }
            }
        }.start();

        nextButton.setText(currentQuestion == questions.length - 1 ? "Submit" : "Next");
    }

    private void nextOrSubmit() {
        if (currentQuestion < questions.length - 1) {
            currentQuestion++;
            loadQuestion();
        } else {

            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("quiz_results");
            String date = LocalDate.now().toString();

            Map<String, Object> data = new HashMap<>();
            data.put("score", score);
            data.put("timestamp", System.currentTimeMillis());

            String uniqueId = dbRef.push().getKey();
            dbRef.child(username).child(date).child(uniqueId).setValue(data);


            DatabaseReference bestRef = dbRef.child(username).child(date).child("highest_score");
            bestRef.get().addOnSuccessListener(bestSnap -> {
                Integer best = bestSnap.getValue(Integer.class);
                if (best == null || score > best) {
                    bestRef.setValue(score);
                }
            });


            dbHelper.insertQuizResult(username, score);


            Intent intent = new Intent(QuizActivity.this, QuizResultActivity.class);
            intent.putExtra("username", username);
            intent.putExtra("score", score);
            startActivity(intent);
            finish();
        }
    }

    private void resetOptionColors() {
        int gray = Color.LTGRAY;
        option1.setBackgroundColor(gray);
        option2.setBackgroundColor(gray);
        option3.setBackgroundColor(gray);
        option4.setBackgroundColor(gray);
    }

    private void disableOptions() {
        option1.setEnabled(false);
        option2.setEnabled(false);
        option3.setEnabled(false);
        option4.setEnabled(false);
    }

    private void enableOptions() {
        option1.setEnabled(true);
        option2.setEnabled(true);
        option3.setEnabled(true);
        option4.setEnabled(true);
    }

    private int getOptionIndex(Button b) {
        if (b == option1) return 0;
        if (b == option2) return 1;
        if (b == option3) return 2;
        return 3;
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
