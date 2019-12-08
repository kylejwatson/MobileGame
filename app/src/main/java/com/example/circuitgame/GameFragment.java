package com.example.circuitgame;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * to handle interaction events.
 */
public class GameFragment extends Fragment {
    private int currentScore;
    private TextView scoreLabel;
    private NavController navController;

    public GameFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        GameView gameView = new GameView(getContext(), new GameView.ScoreChangeListener() {
            @Override
            public void scoreChanged(int difference) {
                currentScore += difference;
                scoreLabel.setText("Score: " + currentScore);
            }
        }, new GameView.GameEndListener() {
            @Override
            public void gameEnded(boolean win) {
                if (win) win();
            }
        });
        FrameLayout frameLayout = view.findViewById(R.id.gameFrame);
        frameLayout.addView(gameView);
        scoreLabel = view.findViewById(R.id.scoreLabel);
    }

    // TODO: Rename method, update argument and hook method into UI event
    private void win() {
        User currentUser = UserFile.getInstance(getContext()).getCurrentUser();
        if (currentUser.getScore() < currentScore) currentUser.setScore(currentScore);
        navController.navigate(R.id.action_gameFragment_to_leaderboardFragment);
    }
}
