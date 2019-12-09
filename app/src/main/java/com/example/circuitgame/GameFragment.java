package com.example.circuitgame;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
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
    private TextView objectiveLabel;
    private NavController navController;
    private Objective currentObjective;

    public GameFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_game, container, false);

        FrameLayout frameLayout = view.findViewById(R.id.gameFrame);
        frameLayout.removeAllViews();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        currentScore = 0;
        scoreLabel = view.findViewById(R.id.scoreLabel);
        objectiveLabel = view.findViewById(R.id.nextText);
        navController = Navigation.findNavController(view);
        GameView gameView = GameView.getInstance(getContext(), new GameView.ObjectiveListener() {
            @Override
            public void objectiveReached(Objective objective) {
                if (currentObjective == null){
                    currentObjective = objective;
                    objectiveLabel.setText(currentObjective.getName());
                    return;
                }
                if (!objective.equals(currentObjective)) {
                    lose();
                    return;
                }
                currentScore += 100;
                scoreLabel.setText("Score: " + currentScore);
                if (objective.getNextObjective() == null) {
                    win();
                    return;
                }
                currentObjective = objective.getNextObjective();
                objectiveLabel.setText(currentObjective.getName());
            }
        });

        FrameLayout frameLayout = view.findViewById(R.id.gameFrame);
        frameLayout.addView(gameView);
    }

    private void lose() {
        // TODO make proper lose screen/make leaderboard show lose or win
        objectiveLabel.setText("LOSE");
    }

    private void win() {
        User currentUser = UserFile.getInstance(getContext()).getCurrentUser();
        if (currentUser.getScore() < currentScore) currentUser.setScore(currentScore);
        navController.navigate(R.id.action_gameFragment_to_leaderboardFragment);
    }
}
