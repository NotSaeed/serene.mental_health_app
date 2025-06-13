package com.example.serene;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.FirebaseDatabase;

public class SubmitStoryActivity extends AppCompatActivity {

    EditText inputName, inputLocation, inputStory;
    Button btnSubmit;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_story);

        getSupportActionBar().setTitle("Submit Your Story");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        username = getIntent().getStringExtra("username");

        inputName = findViewById(R.id.inputName);
        inputLocation = findViewById(R.id.inputLocation);
        inputStory = findViewById(R.id.inputStory);
        btnSubmit = findViewById(R.id.btnSubmitStory);

        btnSubmit.setOnClickListener(v -> {
            String name = inputName.getText().toString().trim();
            String location = inputLocation.getText().toString().trim();
            String story = inputStory.getText().toString().trim();

            if (name.isEmpty() || location.isEmpty() || story.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            FirebaseDatabase.getInstance().getReference("donation_stories")
                    .push()
                    .setValue(new Story(name, location, story));

            Toast.makeText(this, "Thank you for sharing!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    public static class Story {
        public String name, location, story;

        public Story() {}

        public Story(String name, String location, String story) {
            this.name = name;
            this.location = location;
            this.story = story;
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
