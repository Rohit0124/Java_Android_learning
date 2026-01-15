package com.example.myapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText user = findViewById(R.id.edtUser);
        EditText pass = findViewById(R.id.edtPass);
        Button btn = findViewById(R.id.btnRegister);

        btn.setOnClickListener(v -> {
            SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);
            sp.edit()
                    .putString("username", user.getText().toString())
                    .putString("password", pass.getText().toString())
                    .apply();

            Toast.makeText(this, "Registered Successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
        });
    }
}
