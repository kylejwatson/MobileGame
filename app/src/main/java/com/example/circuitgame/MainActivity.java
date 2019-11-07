package com.example.circuitgame;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements UserFragment.OnUserFragmentInteractionListener, LeaderboardFragment.OnLeaderboardFragmentInteractionListener, GameFragment.OnGameFragmentInteractionListener {

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onUserFragmentInteraction(User user) {
        this.user = user;
    }

    @Override
    public void onLeaderboardFragmentInteraction(User user) {

    }

    @Override
    public void onGameFragmentInteraction(Uri uri) {

    }
}
