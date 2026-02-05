package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;

public class MusicHomeActivity extends AppCompatActivity {

    ImageView btnMenu;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_home);

        btnMenu = findViewById(R.id.btnMenu);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);

        // OPEN DRAWER
        btnMenu.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        // MENU CLICK
        navigationView.setNavigationItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.nav_home) {
                Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
            }
            else if (id == R.id.nav_music) {
                startActivity(new Intent(this, MusicActivity.class));
            }
            else if (id == R.id.nav_playlist) {
                Toast.makeText(this, "Empty Playlist", Toast.LENGTH_SHORT).show();
            }
            else if (id == R.id.nav_settings) {
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }
}
