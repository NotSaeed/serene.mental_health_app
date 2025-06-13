package com.example.serene;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CustomMoodChartView extends View {

    private List<MoodTrendActivity.MoodEntry> moodEntries = new ArrayList<>();
    private List<String> dateLabels = new ArrayList<>();

    private Paint linePaint;
    private Paint pointPaint;
    private Paint textPaint;
    private Paint legendPaint;

    public CustomMoodChartView(Context context) {
        super(context);
        init();
    }

    public CustomMoodChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        linePaint = new Paint();
        linePaint.setColor(Color.DKGRAY);
        linePaint.setStrokeWidth(5f);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setAntiAlias(true);

        pointPaint = new Paint();
        pointPaint.setStyle(Paint.Style.FILL);
        pointPaint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(30f);
        textPaint.setTextAlign(Paint.Align.CENTER);

        legendPaint = new Paint();
        legendPaint.setColor(Color.BLACK);
        legendPaint.setTextSize(28f);
        legendPaint.setTextAlign(Paint.Align.LEFT);
    }

    public void setMoodEntries(List<MoodTrendActivity.MoodEntry> entries) {
        this.moodEntries = entries;
        invalidate();
    }

    public void setDateLabels(List<String> labels) {
        this.dateLabels = labels;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (moodEntries == null || moodEntries.size() < 2) return;

        float width = getWidth();
        float height = getHeight();
        float padding = 100;

        int pointCount = moodEntries.size();
        float spacing = (width - 2 * padding) / (pointCount - 1);

        Path path = new Path();

        for (int i = 0; i < moodEntries.size(); i++) {
            MoodTrendActivity.MoodEntry entry = moodEntries.get(i);
            float x = padding + i * spacing;
            float y = height - padding - (entry.score * 100);

            if (i == 0) {
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }

            pointPaint.setColor(getColorForMood(entry.score));
            canvas.drawCircle(x, y, 10f, pointPaint);

            if (i < dateLabels.size()) {
                canvas.drawText(dateLabels.get(i), x, height - 30, textPaint);
            }
        }

        canvas.drawPath(path, linePaint);

        // Optional: draw axis line
        canvas.drawLine(padding, height - padding, width - padding, height - padding, linePaint);
    }

    private int getColorForMood(int score) {
        switch (score) {
            case 0: return Color.RED;           // Angry
            case 1: return Color.rgb(255, 140, 0); // Sad - orange
            case 2: return Color.rgb(255, 165, 0); // Anxious - light orange
            case 3: return Color.YELLOW;
            case 4: return Color.rgb(144, 238, 144); // Okay - light green
            case 5: return Color.GREEN;
            default: return Color.GRAY;
        }
    }
}
