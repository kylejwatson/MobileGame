package com.example.circuitgame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        final EditText username = findViewById(R.id.usernameText);

        Button addButton = findViewById(R.id.saveButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserFile.getInstance(UserActivity.this).addUser(new User(username.getText().toString()));
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        UserFile.getInstance(this).saveToFile(this);
        super.onDestroy();
    }
}
