package com.example.myapp;
import com.example.myapp.model.Song;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.os.Bundle;
import android.os.Handler;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MusicActivity extends AppCompatActivity {

    TextView btnBack, btnFav, btnPlaylist, btnMore;
    Button btnPlay, btnNext, btnPrev, btnShuffle, btnRepeat;
    TextView songName;
    ImageView albumImage;
    SeekBar seekBar;
    ListView songList;
    LinearLayout songListLayout;
    MediaPlayer mediaPlayer;
    List<Song> songs;

    boolean isListVisible = false;
    int current = 0;
    boolean isFav = false;
    boolean isShuffle = false;
    boolean isRepeat = false;

    Handler handler = new Handler();
    ApiService apiService;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        //  Bind Views
        btnBack = findViewById(R.id.btnBack);
        btnFav = findViewById(R.id.btnFav);
        btnMore = findViewById(R.id.btnMore);
        btnPlaylist = findViewById(R.id.btnPlaylist);

        btnPlay = findViewById(R.id.btnPlay);
        btnNext = findViewById(R.id.btnNext);
        btnPrev = findViewById(R.id.btnPrev);
        btnShuffle = findViewById(R.id.btnShuffle);
        btnRepeat = findViewById(R.id.btnRepeat);

        songName = findViewById(R.id.songName);
        albumImage = findViewById(R.id.albumImage);
        seekBar = findViewById(R.id.seekBar);
        songList = findViewById(R.id.songList);

        apiService = ApiClient.getInstance().create(ApiService.class);

        loadSongsFromApi();

        //  ← Back
        btnBack.setOnClickListener(v -> finish());

        //  ⋮ Show Playlist 2nd method
//        btnPlaylist.setOnClickListener(v -> {
//            isListVisible = !isListVisible;
//            songListLayout.setVisibility(isListVisible ? View.VISIBLE : View.GONE);
//        });

        btnPlaylist.setOnClickListener(v -> {
            if (songListLayout.getVisibility() == View.GONE) {
                songListLayout.setVisibility(View.VISIBLE);
            } else {
                songListLayout.setVisibility(View.GONE);
            }
        });

        //  Fav
        btnFav.setOnClickListener(v -> {
            isFav = !isFav;

            if (isFav) {
                btnFav.setText("❤️");
                Toast.makeText(this, "Added to Favourite", Toast.LENGTH_SHORT).show();
            } else {
                btnFav.setText("🤍");
                Toast.makeText(this, "Removed from Favourite", Toast.LENGTH_SHORT).show();
            }
        });

        //  music menu
        btnMore.setOnClickListener(v -> {

            Context wrapper = new ContextThemeWrapper(MusicActivity.this, R.style.PopupMenuStyle);

            PopupMenu popup = new PopupMenu(wrapper, btnMore);

            popup.getMenuInflater().inflate(R.menu.music_menu, popup.getMenu());

            popup.setOnMenuItemClickListener(item -> {

                int id = item.getItemId();

                if (id == R.id.menu_share) {
                    Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                    return true;

                } else if (id == R.id.menu_download) {
                    Toast.makeText(this, "Download", Toast.LENGTH_SHORT).show();
                    return true;

                } else if (id == R.id.menu_info) {
                    Toast.makeText(this, "Info", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            });

            popup.show();
        });

        //  ▶ Play / Pause
        btnPlay.setOnClickListener(v -> {
            if (mediaPlayer == null) return;

            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                btnPlay.setText(R.drawable.play);
            } else {
                mediaPlayer.start();
                btnPlay.setText(R.drawable.pause);
                updateSeekBar();
            }
        });

        // ⏭ Next
        btnNext.setOnClickListener(v -> {
            if (songs == null) return;

            if (isShuffle)
                current = (int) (Math.random() * songs.size());
            else
                current = (current + 1) % songs.size();

            playSong();
        });

        //  ⏮ Previous
        btnPrev.setOnClickListener(v -> {
            if (songs == null) return;

            current = (current - 1 + songs.size()) % songs.size();
            playSong();
        });

        //  Shuffle
        btnShuffle.setOnClickListener(v -> {
            isShuffle = !isShuffle;
            Toast.makeText(this,
                    isShuffle ? "Shuffle ON" : "Shuffle OFF",
                    Toast.LENGTH_SHORT).show();
        });

        //  Repeat
        btnRepeat.setOnClickListener(v -> {
            isRepeat = !isRepeat;
            Toast.makeText(this,
                    isRepeat ? "Repeat ON" : "Repeat OFF",
                    Toast.LENGTH_SHORT).show();
        });

        //  List click
        songList.setOnItemClickListener((a, b, i, l) -> {
            current = i;
            playSong();
        });

        //  Seekbar
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar s, int p, boolean fromUser) {
                if (fromUser && mediaPlayer != null)
                    mediaPlayer.seekTo(p);
            }
            public void onStartTrackingTouch(SeekBar s) {}
            public void onStopTrackingTouch(SeekBar s) {}
        });
    }

    // ================= LOAD SONGS =================
    void loadSongsFromApi() {
        apiService.getSongs().enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {

                songs = response.body();

                String[] names = new String[songs.size()];
                for (int i = 0; i < songs.size(); i++)
                    names[i] = songs.get(i).getTitle();

                songList.setAdapter(new ArrayAdapter<>(
                        MusicActivity.this,
                        android.R.layout.simple_list_item_1,
                        names
                ));

                playSong();
            }

            @Override
            public void onFailure(Call<List<Song>> call, Throwable t) {
                Toast.makeText(MusicActivity.this,
                        "API Error: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    // ================= PLAY SONG =================
    @SuppressLint("ResourceType")
    void playSong() {
        try {
            if (mediaPlayer != null)
                mediaPlayer.release();

            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(songs.get(current).getSongUrl());
            mediaPlayer.prepare();
            mediaPlayer.start();

            songName.setText(songs.get(current).getTitle());
            seekBar.setMax(mediaPlayer.getDuration());
            btnPlay.setText(R.drawable.pause2);

            mediaPlayer.setOnCompletionListener(mp -> {
                if (isRepeat) {
                    playSong();
                } else {
                    btnNext.performClick();
                }
            });

            updateSeekBar();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= SEEK =================
    void updateSeekBar() {
        seekBar.setProgress(mediaPlayer.getCurrentPosition());

        handler.postDelayed(() -> {
            if (mediaPlayer != null && mediaPlayer.isPlaying())
                updateSeekBar();
        }, 500);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
