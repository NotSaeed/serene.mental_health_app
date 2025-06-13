package com.example.serene;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class StoryActivity extends AppCompatActivity {

    ImageView storyImageView;
    TextView storyTitle, storyContent, storyTakeaway;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Back button

        storyImageView = findViewById(R.id.storyImageView);
        storyTitle = findViewById(R.id.storyTitle);
        storyContent = findViewById(R.id.storyContent);
        storyTakeaway = findViewById(R.id.storyTakeaway);

        int imageResId = getIntent().getIntExtra("imageResId", -1);

        if (imageResId != -1) {
            storyImageView.setImageResource(imageResId);
        }

        if (imageResId == R.drawable.mind_1) {
            storyTitle.setText("The Art of Rising");
            storyContent.setText("When Layla was drowning in anxiety, she felt like the world was holding her down — expectations, mistakes, comparisons. One day, she stood at the edge of a canyon and watched a hot air balloon rise into the sky. It didn’t force itself upward. It just released what was heavy.\n\nThat moment changed her. She began letting go — not of dreams, but of the weight that held her back. Toxic friendships. Self-doubt. Unforgiveness. And as she did, she rose — not fast, but beautifully.");
            storyTakeaway.setText("“Letting go isn’t giving up. It’s making room for peace.”");
        } else if (imageResId == R.drawable.mind_2) {
            storyTitle.setText("The Path with No Map");
            storyContent.setText("Omar always needed a plan. A schedule. A destination. But life had recently thrown him into uncertainty — a breakup, job loss, and silence from the people he once leaned on.\n\nHe wandered into a forest trail, unsure why. There were no signs. No end in sight. But as he walked, the silence started speaking. The rustle of trees, the rhythm of steps, the cool air reminded him: some paths aren’t about arrival. They’re about becoming.\n\nHe didn't find answers that day — but he found himself still walking. That was enough.");
            storyTakeaway.setText("“You don’t need to know the destination to take the first step.”");
        } else if (imageResId == R.drawable.mind_3) {
            storyTitle.setText("The Power of Stillness");
            storyContent.setText("Nina always felt guilty for resting. She wore her busyness like a badge, afraid that stillness meant she wasn’t achieving enough. One evening, after burnout stole her energy, she sat by a quiet lake.\n\nThe boat in the water didn’t move, yet its reflection was perfect. She realized then — not every moment must be about action. Healing happens in stillness. Strength grows in silence.\n\nFor the first time in years, she let herself just be. No notifications. No productivity. Just peace.");
            storyTakeaway.setText("“Stillness is not emptiness — it’s where clarity lives.”");
        } else if (imageResId == R.drawable.mind_4) {
            storyTitle.setText("Built to Last");
            storyContent.setText("That scooter wasn’t shiny. It was scratched, dented, even mocked. But it carried generations. Rain or shine, it moved forward. It was never the fastest — but it never failed.\n\nLike the scooter, Rahman had been through a lot — divorce, illness, loneliness. But he kept showing up. At work. At home. For friends who never thanked him. He wasn’t perfect. But he was real. Strong. Reliable.\n\nWe often praise those who shine the most. But maybe we should honor those who stay the longest.");
            storyTakeaway.setText("“You’re not broken. You’re seasoned.”");
        } else if (imageResId == R.drawable.mind_5) {
            storyTitle.setText("The Richest Man in Rain");
            storyContent.setText("He had no mansion, no savings, no fame. Just a green jacket, a plastic bag of vegetables, and a smile that lit up rainy streets.\n\nEvery day, he walked the same path, greeting strangers like old friends. People wondered: why so happy?\n\nHe once lost everything — family, home, even hope. But then he found the secret: happiness isn’t in having. It’s in giving. A laugh, a hello, a handful of kindness.");
            storyTakeaway.setText("“Your joy isn’t what you own — it’s what you give away.”");
        } else if (imageResId == R.drawable.mind_6) {
            storyTitle.setText("In the Crowd");
            storyContent.setText("Among thousands, Aya still felt invisible. Everyone seemed to know where they were going — but she didn’t. She took photos to feel present. To prove she existed.\n\nShe wasn’t alone in being alone. Every face in the crowd had its own silence, its own battles. She began looking closer — behind the filters, beyond the noise. That’s when she found connection.\n\nSometimes, the loneliest place is in the middle of people. But connection doesn’t start outside. It starts within.");
            storyTakeaway.setText("“You don’t need the crowd to be seen. You just need to see yourself.”");
        } else if (imageResId == R.drawable.mind_7) {
            storyTitle.setText("The Sound of Rain");
            storyContent.setText("The room was full of chairs, but she was the only one sitting. Rain tapped on the windows like it was trying to comfort her. She felt like a ghost — not heard, not held, not seen.\n\nBut she remembered something her therapist once said: “Rain doesn’t ask for permission to fall. It just does.”\n\nSo she cried that day. Not because she was weak. But because she was brave enough to let it out. Brave enough to exist in her own story.");
            storyTakeaway.setText("“Tears are not weakness — they are your soul’s way of breathing.”");
        } else if (imageResId == R.drawable.mind_8) {
            storyTitle.setText("Becoming");
            storyContent.setText("The rosebud doesn't rush. It opens slowly — petal by petal. Not because it's weak, but because growth requires care. Every inch it stretches is proof of silent strength.\n\nLike this rose, you don’t have to bloom all at once. Your healing doesn’t need to impress anyone. It's enough that you’re still here — still trying, still reaching, still becoming.\n\nDon't compare your pace to someone already in full bloom. Trust your timing. You're not behind. You're unfolding.");
            storyTakeaway.setText("“You are becoming — and that is beautiful.”");
        } else {
            storyTitle.setText("Healing Story");
            storyContent.setText("The story you are looking for could not be found.");
            storyTakeaway.setText("“You are still the author of your story.”");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
