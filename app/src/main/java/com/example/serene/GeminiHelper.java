package com.example.serene;

import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class GeminiHelper {

    public interface GeminiCallback {
        void onReply(String reply);
    }

    public static void getAIReply(String input, GeminiCallback callback) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL url = new URL("https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=AIzaSyCYSvelMrwczLgwvYHJKOzqu4pxZoaW8Fk");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setDoOutput(true);

                    JSONObject prompt = new JSONObject();
                    JSONArray contents = new JSONArray();
                    JSONObject part = new JSONObject();
                    part.put("text", input);
                    JSONObject partsObj = new JSONObject();
                    partsObj.put("parts", new JSONArray().put(part));
                    contents.put(partsObj);
                    prompt.put("contents", contents);

                    OutputStream os = conn.getOutputStream();
                    os.write(prompt.toString().getBytes());
                    os.flush();

                    Scanner scanner = new Scanner(conn.getInputStream());
                    StringBuilder result = new StringBuilder();
                    while (scanner.hasNext()) {
                        result.append(scanner.nextLine());
                    }
                    scanner.close();

                    JSONObject response = new JSONObject(result.toString());
                    String reply = response
                            .getJSONArray("candidates")
                            .getJSONObject(0)
                            .getJSONObject("content")
                            .getJSONArray("parts")
                            .getJSONObject(0)
                            .getString("text");

                    return reply;

                } catch (Exception e) {
                    return "Error: " + e.getMessage();
                }
            }

            @Override
            protected void onPostExecute(String result) {
                callback.onReply("SereneGPT: " + result);
            }
        }.execute();
    }
}
