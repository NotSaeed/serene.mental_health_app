package com.example.serene;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "Serene.db";
    public static final int DB_VERSION = 1;

    // Table for users
    public static final String TABLE_USERS = "users";
    public static final String COL_ID = "id";
    public static final String COL_USERNAME = "username";
    public static final String COL_PASSWORD = "password";

    // Table for quiz results
    public static final String TABLE_QUIZ = "quiz_results";
    public static final String COL_SCORE = "score";
    public static final String COL_DATE = "date";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_USERS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USERNAME + " TEXT, " +
                COL_PASSWORD + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_QUIZ + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USERNAME + " TEXT, " +
                COL_SCORE + " INTEGER, " +
                COL_DATE + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZ);
        onCreate(db);
    }

    // Register
    public boolean registerUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_USERNAME, username);
        cv.put(COL_PASSWORD, password);
        long result = db.insert(TABLE_USERS, null, cv);
        return result != -1;
    }


    public boolean loginUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS +
                        " WHERE " + COL_USERNAME + "=? AND " + COL_PASSWORD + "=?",
                new String[]{username, password});
        return cursor.getCount() > 0;
    }


    public boolean checkLogin(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS +
                        " WHERE " + COL_USERNAME + "=? AND " + COL_PASSWORD + "=?",
                new String[]{email, password});
        boolean result = cursor.getCount() > 0;
        cursor.close();
        return result;
    }


    public String getUsernameByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COL_USERNAME + " FROM " + TABLE_USERS +
                " WHERE " + COL_USERNAME + "=?", new String[]{email});
        if (cursor.moveToFirst()) {
            String username = cursor.getString(0);
            cursor.close();
            return username;
        }
        return null;
    }


    public void insertQuizResult(String username, int score) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_USERNAME, username);
        cv.put(COL_SCORE, score);
        cv.put(COL_DATE, new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
        db.insert(TABLE_QUIZ, null, cv);
    }


    public int getLastQuizScore(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COL_SCORE + " FROM " + TABLE_QUIZ +
                " WHERE " + COL_USERNAME + "=? ORDER BY " + COL_ID + " DESC LIMIT 1", new String[]{username});
        if (cursor.moveToFirst()) {
            return cursor.getInt(0);
        }
        return 0;
    }


    public List<String> getAllQuizDates() {
        List<String> dates = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT " + COL_DATE + " FROM " + TABLE_QUIZ, null);
        while (cursor.moveToNext()) {
            dates.add(cursor.getString(0));
        }
        cursor.close();
        return dates;
    }


    public int getStreakCount() {
        List<String> dates = getAllQuizDates();
        int streak = 1;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            for (int i = dates.size() - 1; i > 0; i--) {
                Date today = sdf.parse(dates.get(i));
                Date yesterday = sdf.parse(dates.get(i - 1));
                long diff = today.getTime() - yesterday.getTime();
                if (diff == 86400000L) {
                    streak++;
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return streak;
    }
    public boolean deleteUser(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        int deletedRows = db.delete("users", "username = ?", new String[]{username});
        return deletedRows > 0;
    }
    public boolean updateUser(String oldUsername, String newUsername, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", newUsername);
        values.put("password", newPassword);
        int rows = db.update("users", values, "username = ?", new String[]{oldUsername});
        return rows > 0;
    }
}
