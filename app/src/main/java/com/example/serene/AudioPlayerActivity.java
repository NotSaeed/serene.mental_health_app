package com.example.serene;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class AudioPlayerActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private Handler handler = new Handler();

    private String[] audioTitles = {
            "Quran" ,"Calm Waves", "Relaxing Piano", "Nature Forest",
            "Rain", "Relax", "Lofi", "Breathe"
    };

    private int[] audioResources = {
            R.raw.quran, R.raw.calm_waves, R.raw.relaxing_piano, R.raw.nature_forest,
            R.raw.rain, R.raw.relax, R.raw.lofi, R.raw.breathe
    };

    private int currentTrack = -1;
    private boolean isLooping = false;
    private boolean isShuffling = false;

    private TextView nowPlayingText;
    private SeekBar seekBar, volumeSeekBar;
    private ImageButton playBtn, pauseBtn, stopBtn, forwardBtn, rewindBtn;
    private CheckBox loopCheckBox, shuffleCheckBox;
    private Runnable updateSeekbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_player);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Audio Player");


        ListView listView = findViewById(R.id.audioListView);
        nowPlayingText = findViewById(R.id.nowPlayingText);
        seekBar = findViewById(R.id.seekBar);
        volumeSeekBar = findViewById(R.id.volumeSeekBar);

        playBtn = findViewById(R.id.btnPlay);
        pauseBtn = findViewById(R.id.btnPause);
        stopBtn = findViewById(R.id.btnStop);
        forwardBtn = findViewById(R.id.btnForward);
        rewindBtn = findViewById(R.id.btnBackward);

        loopCheckBox = findViewById(R.id.loopCheckBox);
        shuffleCheckBox = findViewById(R.id.shuffleCheckBox);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, audioTitles);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((adapterView, view, position, id) -> {
            currentTrack = position;
            playAudio(position);
        });

        playBtn.setOnClickListener(v -> {
            if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                mediaPlayer.start();
                nowPlayingText.setText("Now playing: " + audioTitles[currentTrack]);
                handler.post(updateSeekbar);
            }
        });

        pauseBtn.setOnClickListener(v -> {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                nowPlayingText.setText("Paused: " + audioTitles[currentTrack]);
            }
        });

        stopBtn.setOnClickListener(v -> {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
                nowPlayingText.setText("Stopped");
                seekBar.setProgress(0);
            }
        });

        forwardBtn.setOnClickListener(v -> {
            if (mediaPlayer != null) {
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 5000);
            }
        });

        rewindBtn.setOnClickListener(v -> {
            if (mediaPlayer != null) {
                mediaPlayer.seekTo(Math.max(mediaPlayer.getCurrentPosition() - 5000, 0));
            }
        });

        loopCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> isLooping = isChecked);
        shuffleCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> isShuffling = isChecked);

        volumeSeekBar.setMax(100);
        volumeSeekBar.setProgress(50);
        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float volume = progress / 100f;
                if (mediaPlayer != null) {
                    mediaPlayer.setVolume(volume, volume);
                }
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null && fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        updateSeekbar = new Runnable() {
            @Override public void run() {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    handler.postDelayed(this, 1000);
                }
            }
        };
    }

    private void playAudio(int position) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        mediaPlayer = MediaPlayer.create(this, audioResources[position]);
        mediaPlayer.setLooping(isLooping);

        nowPlayingText.setText("Now playing: " + audioTitles[position]);
        seekBar.setMax(mediaPlayer.getDuration());
        mediaPlayer.start();
        handler.post(updateSeekbar);

        mediaPlayer.setOnCompletionListener(mp -> {
            if (isShuffling) {
                Random rand = new Random();
                currentTrack = rand.nextInt(audioResources.length);
                playAudio(currentTrack);
            } else if (!isLooping) {
                nowPlayingText.setText("Stopped");
                seekBar.setProgress(0);
            }
        });

        float volume = volumeSeekBar.getProgress() / 100f;
        mediaPlayer.setVolume(volume, volume);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        handler.removeCallbacks(updateSeekbar);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
