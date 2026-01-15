package com.example.myapp;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class MusicActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    SeekBar seekBar;
    TextView songName;
    ImageView albumImage;
    ListView songList;

    int[] songs = {R.raw.krishna, R.raw.santoor};
    int[] images = {R.drawable.music2, R.drawable.music2};
    String[] titles = {"Krishna", "Santoor"};

    int current = 0;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_music);

        Button play = findViewById(R.id.btnPlay);
        Button next = findViewById(R.id.btnNext);
        Button prev = findViewById(R.id.btnPrev);

        seekBar = findViewById(R.id.seekBar);
        songName = findViewById(R.id.songName);
        albumImage = findViewById(R.id.albumImage);
        songList = findViewById(R.id.songList);

        songList.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, titles));

        loadSong();

        play.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                play.setText("▶");
            } else {
                mediaPlayer.start();
                play.setText("⏸");
            }
        });

        next.setOnClickListener(v -> {
            current = (current + 1) % songs.length;
            changeSong();
        });

        prev.setOnClickListener(v -> {
            current = (current - 1 + songs.length) % songs.length;
            changeSong();
        });

        songList.setOnItemClickListener((a,q,i,l)->{
            current = i;
            changeSong();
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar s,int p,boolean f){
                if(f) mediaPlayer.seekTo(p);
            }
            public void onStartTrackingTouch(SeekBar s){}
            public void onStopTrackingTouch(SeekBar s){}
        });

        updateSeek();
    }

    void loadSong(){
        if(mediaPlayer!=null) mediaPlayer.release();
        mediaPlayer = MediaPlayer.create(this,songs[current]);
        songName.setText(titles[current]);
        albumImage.setImageResource(images[current]);
        seekBar.setMax(mediaPlayer.getDuration());
    }

    void changeSong(){
        mediaPlayer.stop();
        loadSong();
        mediaPlayer.start();
    }

    void updateSeek(){
        seekBar.setProgress(mediaPlayer.getCurrentPosition());
        handler.postDelayed(this::updateSeek,500);
    }
}
