package com.kyle.circuitgame.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.kyle.circuitgame.R;
import com.kyle.circuitgame.utils.UserFile;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStop() {
        UserFile.getInstance(this).saveToFile(this);
        super.onStop();
    }
}
