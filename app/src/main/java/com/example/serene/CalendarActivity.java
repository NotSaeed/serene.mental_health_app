package com.example.serene;

import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;


public class CalendarActivity extends AppCompatActivity {
    private static final int CELL_HEIGHT_DP = 160;
    private GridLayout calendarGrid;
    private TextView tvMotivation;
    private Map<LocalDate, Integer> quizScores = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Calendar");

        calendarGrid = findViewById(R.id.calendarGrid);
        tvMotivation = findViewById(R.id.tvMotivation);

        LocalDate today = LocalDate.now();
        String username = getIntent().getStringExtra("username"); // ← Make sure username is passed

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("quiz_results").child(username);
        dbRef.get().addOnSuccessListener(snapshot -> {
            for (DataSnapshot dateSnapshot : snapshot.getChildren()) {
                String dateStr = dateSnapshot.getKey(); // e.g. "2025-06-06"
                LocalDate date = LocalDate.parse(dateStr);


                Integer maxScore = dateSnapshot.child("highest_score").getValue(Integer.class);
                if (maxScore != null) {
                    quizScores.put(date, maxScore);
                }
            }

            generateCalendar(today);
            updateMotivationText();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void generateCalendar(LocalDate date) {
        calendarGrid.removeAllViews();

        YearMonth yearMonth = YearMonth.from(date);
        LocalDate firstDay = yearMonth.atDay(1);
        int daysInMonth = yearMonth.lengthOfMonth();
        int dayOfWeekOffset = firstDay.getDayOfWeek().getValue() % 7;


        String[] weekDays = {"S", "M", "T", "W", "T", "F", "S"};
        int[] colors = {
                Color.parseColor("#FFCDD2"), // Sunday - red-ish
                Color.parseColor("#C8E6C9"), // Monday - green
                Color.parseColor("#BBDEFB"), // Tuesday - blue
                Color.parseColor("#FFF9C4"), // Wednesday - yellow
                Color.parseColor("#D1C4E9"), // Thursday - purple
                Color.parseColor("#FFE0B2"), // Friday - orange
                Color.parseColor("#E0E0E0")  // Saturday - gray
        };

        for (int i = 0; i < 7; i++) {
            TextView header = new TextView(this);
            header.setText(weekDays[i]);
            header.setGravity(Gravity.CENTER);
            header.setTextSize(16);
            header.setTextColor(Color.BLACK);
            header.setBackgroundColor(colors[i]);
            header.setPadding(4, 8, 4, 8);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            header.setLayoutParams(params);
            calendarGrid.addView(header);
        }


        for (int i = 0; i < dayOfWeekOffset; i++) {
            calendarGrid.addView(new TextView(this));
        }


        for (int i = 1; i <= daysInMonth; i++) {
            LocalDate currentDate = yearMonth.atDay(i);
            Integer score = quizScores.get(currentDate);
            calendarGrid.addView(createDayCell(currentDate.getDayOfWeek().toString().substring(0,1), String.valueOf(i), score));
        }
    }


    private LinearLayout createDayCell(String weekday, String dayNum, Integer score) {

        LinearLayout cell = new LinearLayout(this);
        cell.setOrientation(LinearLayout.VERTICAL);
        cell.setGravity(Gravity.CENTER);
        cell.setPadding(6, 6, 6, 6);
        cell.setBackgroundResource(R.drawable.day_cell_background);


        int cellHeightPx = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                CELL_HEIGHT_DP,
                getResources().getDisplayMetrics()
        );


        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = 0;
        params.height = cellHeightPx;
        params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        cell.setLayoutParams(params);


        TextView weekLabel = new TextView(this);
        weekLabel.setText(weekday);
        weekLabel.setTextSize(14);
        weekLabel.setGravity(Gravity.CENTER);
        weekLabel.setTextColor(Color.DKGRAY);


        TextView dayLabel = new TextView(this);
        dayLabel.setText(dayNum);
        dayLabel.setTextSize(18);
        dayLabel.setGravity(Gravity.CENTER);
        dayLabel.setTextColor(Color.BLACK);


        LinearLayout scoreLayout = new LinearLayout(this);
        scoreLayout.setOrientation(LinearLayout.HORIZONTAL);
        scoreLayout.setGravity(Gravity.CENTER);

        if (score != null) {

            TextView trophy = new TextView(this);
            trophy.setText(" ");
            trophy.setTextSize(14);

            TextView scoreText = new TextView(this);
            scoreText.setText(score + "/40");
            scoreText.setTextSize(14);
            scoreText.setTextColor(Color.parseColor("#512DA8"));
            scoreLayout.addView(trophy);
            scoreLayout.addView(scoreText);


            cell.setBackgroundColor(Color.parseColor("#D1C4E9"));
        } else {

            scoreLayout.setMinimumHeight(40);
        }


        cell.addView(weekLabel);
        cell.addView(dayLabel);
        cell.addView(scoreLayout);


        cell.setOnClickListener(v ->
                Toast.makeText(this, "Clicked day " + dayNum, Toast.LENGTH_SHORT).show());

        return cell;
    }
    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics()
        );
    }

    private void updateMotivationText() {
        int streak = getStreakCount();
        if (streak >= 3) {
            tvMotivation.setText(" Great job! You're on a " + streak + "-day quiz streak!");
        } else {
            tvMotivation.setText("Take your quiz today and start your wellness streak ");
        }
    }

    private int getStreakCount() {
        int streak = 0;
        LocalDate today = LocalDate.now();
        while (quizScores.containsKey(today.minusDays(streak))) {
            streak++;
        }
        return streak;
    }
}
