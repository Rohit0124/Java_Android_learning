package com.example.myapp;

import com.example.myapp.model.Song;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    @GET("/songs")
    Call<List<Song>> getSongs();
}
