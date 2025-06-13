package com.example.serene;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

public class MoodLineChartView extends View {

    private List<Integer> moodScores;
    private List<String> labels;

    private Paint linePaint, pointPaint, textPaint, axisPaint;

    public MoodLineChartView(Context context) {
        super(context);
        init();
    }

    public MoodLineChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        linePaint = new Paint();
        linePaint.setColor(Color.parseColor("#6200EE")); // Deep purple line
        linePaint.setStrokeWidth(6f);
        linePaint.setStyle(Paint.Style.STROKE);

        pointPaint = new Paint();
        pointPaint.setColor(Color.MAGENTA); // Mood points
        pointPaint.setStyle(Paint.Style.FILL);

        textPaint = new Paint();
        textPaint.setColor(Color.DKGRAY);
        textPaint.setTextSize(28f);

        axisPaint = new Paint();
        axisPaint.setColor(Color.GRAY);
        axisPaint.setStrokeWidth(2f);
    }

    public void setData(List<Integer> moodScores, List<String> labels) {
        this.moodScores = moodScores;
        this.labels = labels;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (moodScores == null || moodScores.size() == 0 || labels == null || labels.size() == 0)
            return;

        int padding = 100;
        int width = getWidth();
        int height = getHeight();
        int chartWidth = width - padding * 2;
        int chartHeight = height - padding * 2;

        int count = moodScores.size();
        if (count < 1) return;

        float xInterval = (count > 1) ? chartWidth / (float) (count - 1) : 0;


        canvas.drawLine(padding, height - padding, width - padding, height - padding, axisPaint); // X axis
        canvas.drawLine(padding, padding, padding, height - padding, axisPaint); // Y axis


        for (int i = 0; i < count; i++) {
            float x = padding + i * xInterval;
            float y = padding + (5 - moodScores.get(i)) * (chartHeight / 5f);


            if (i > 0) {
                float prevX = padding + (i - 1) * xInterval;
                float prevY = padding + (5 - moodScores.get(i - 1)) * (chartHeight / 5f);
                canvas.drawLine(prevX, prevY, x, y, linePaint);
            }

            canvas.drawCircle(x, y, 10f, pointPaint);


            if (i < labels.size()) {
                canvas.drawText(labels.get(i), x - 30, height - padding + 40, textPaint);
            }
        }


        for (int i = 0; i <= 5; i++) {
            float y = padding + (5 - i) * (chartHeight / 5f);
            canvas.drawText(String.valueOf(i), padding - 50, y + 10, textPaint);
        }
    }
}
