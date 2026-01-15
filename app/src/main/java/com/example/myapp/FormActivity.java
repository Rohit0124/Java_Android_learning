package com.example.myapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class FormActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText name = findViewById(R.id.edtName);
        EditText email = findViewById(R.id.edtEmail);
        Button submit = findViewById(R.id.btnSubmit);

        submit.setOnClickListener(v -> {

            String userName = name.getText().toString();
            String userEmail = email.getText().toString();

            if (userName.isEmpty() || userEmail.isEmpty()) {
                Toast.makeText(this, "All fields required", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(
                        this,
                        "Name: " + userName + "\nEmail: " + userEmail,
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }
}
