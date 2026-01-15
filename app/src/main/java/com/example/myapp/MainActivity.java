package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

//    public void openToast(View view) {
//        startActivity(new Intent(this, ToastActivity.class));
//    }
//
//    public void openForm(View view) {
//        startActivity(new Intent(this, FormActivity.class));
//    }

    public void openLogin(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }

}
