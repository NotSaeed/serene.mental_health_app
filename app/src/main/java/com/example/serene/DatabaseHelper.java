package com.example.serene;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "serene.db";
    public static final String MOOD_TABLE = "mood";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + MOOD_TABLE + " (id INTEGER PRIMARY KEY AUTOINCREMENT, mood INT, entry TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS " + MOOD_TABLE);
        onCreate(db);
    }

    public void saveMoodEntry(int mood, String entry) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("mood", mood);
        cv.put("entry", entry);
        db.insert(MOOD_TABLE, null, cv);
        db.close();
    }

    public List<String> getMoodEntries() {
        List<String> entries = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + MOOD_TABLE, null);
        while (cursor.moveToNext()) {
            int mood = cursor.getInt(cursor.getColumnIndexOrThrow("mood"));
            String entry = cursor.getString(cursor.getColumnIndexOrThrow("entry"));
            entries.add("Mood: " + mood + "\nNote: " + entry);
        }
        cursor.close();
        db.close();
        return entries;
    }
}
