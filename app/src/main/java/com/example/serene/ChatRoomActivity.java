package com.example.serene;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class ChatRoomActivity extends AppCompatActivity {

    private EditText editTextMessage;
    private Button btnSend;
    private ListView listViewMessages;
    private ArrayList<String> messageList;
    private ArrayAdapter<String> adapter;

    private static final String GEMINI_API_KEY = "AIzaSyCYSvelMrwczLgwvYHJKOzqu4pxZoaW8Fk";
    private static final String GEMINI_MODEL_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Serene");
        }

        editTextMessage = findViewById(R.id.editTextMessage);
        btnSend = findViewById(R.id.btnSend);
        listViewMessages = findViewById(R.id.listViewMessages);

        messageList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, messageList);
        listViewMessages.setAdapter(adapter);

        btnSend.setOnClickListener(v -> {
            String userInput = editTextMessage.getText().toString().trim();
            if (!userInput.isEmpty()) {
                messageList.add("You: " + userInput);
                adapter.notifyDataSetChanged();
                editTextMessage.setText("");

                new Thread(() -> {
                    try {
                        String reply = callGeminiAPI(userInput);
                        runOnUiThread(() -> {
                            messageList.add("SereneGPT: " + reply);
                            adapter.notifyDataSetChanged();
                        });
                    } catch (Exception e) {
                        runOnUiThread(() -> {
                            messageList.add("SereneGPT error: " + e.getMessage());
                            adapter.notifyDataSetChanged();
                        });
                    }
                }).start();
            }
        });
    }

    private String callGeminiAPI(String prompt) throws Exception {
        URL url = new URL(GEMINI_MODEL_URL + "?key=" + GEMINI_API_KEY);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/json");

        JSONObject content = new JSONObject()
                .put("parts", new JSONArray().put(new JSONObject().put("text", prompt)));

        JSONObject body = new JSONObject()
                .put("contents", new JSONArray().put(content));

        try (OutputStream os = conn.getOutputStream()) {
            os.write(body.toString().getBytes());
        }

        Scanner scanner = new Scanner(conn.getInputStream());
        StringBuilder response = new StringBuilder();
        while (scanner.hasNext()) response.append(scanner.nextLine());
        scanner.close();

        JSONObject json = new JSONObject(response.toString());
        return json.getJSONArray("candidates")
                .getJSONObject(0)
                .getJSONObject("content")
                .getJSONArray("parts")
                .getJSONObject(0)
                .getString("text");
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
