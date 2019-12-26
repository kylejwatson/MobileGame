package com.kyle.circuitgame.fragments;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.kyle.circuitgame.R;
import com.kyle.circuitgame.activities.HelpActivity;
import com.kyle.circuitgame.game.GameView;
import com.kyle.circuitgame.game.Objective;
import com.kyle.circuitgame.models.User;
import com.kyle.circuitgame.utils.UserFile;

public class GameFragment extends Fragment {
    private int currentScore;
    private TextView scoreLabel;
    private TextView objectiveLabel;
    private NavController navController;
    private Objective currentObjective;
    private SoundPool soundPool;
    private int winSound;
    private int loseSound;

    public GameFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);
        FrameLayout frameLayout = view.findViewById(R.id.fragment_game_frame);
        frameLayout.removeAllViews();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(2)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 1);
        }
        winSound = soundPool.load(getContext(), R.raw.win, 1);
        loseSound = soundPool.load(getContext(), R.raw.losew, 1);
        Button helpButton = view.findViewById(R.id.fragment_game_btn_help);
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), HelpActivity.class));
            }
        });
        Button quitButton = view.findViewById(R.id.fragment_game_btn_quit);
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_gameFragment_to_titleFragment);
            }
        });
        currentScore = 0;
        scoreLabel = view.findViewById(R.id.fragment_game_tv_score);
        objectiveLabel = view.findViewById(R.id.fragment_game_tv_next);
        navController = Navigation.findNavController(view);
        GameView gameView = GameView.getInstance(getContext(), new GameView.ObjectiveListener() {
            @Override
            public void objectiveReached(Objective objective) {
                if (currentObjective == null) {
                    currentObjective = objective;
                    objectiveLabel.setText(getString(R.string.collect_label, currentObjective.getName()));
                    scoreLabel.setText(getString(R.string.score_label, currentScore));
                    return;
                }
                if (!objective.equals(currentObjective)) {
                    lose();
                    return;
                }
                currentScore += 100;
                scoreLabel.setText(getString(R.string.score_label, currentScore));
                if (objective.getNextObjective() == null) {
                    win();
                    return;
                }
                currentObjective = objective.getNextObjective();
                objectiveLabel.setText(getString(R.string.collect_label, currentObjective.getName()));
            }
        }, soundPool);

        FrameLayout frameLayout = view.findViewById(R.id.fragment_game_frame);
        frameLayout.addView(gameView);
    }

    @Override
    public void onPause() {
        super.onPause();
        GameView.getInstance().pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        GameView.getInstance().play();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (!GameView.getInstance().getPaused()) GameView.getInstance().stop();
    }

    private void lose() {
        soundPool.play (loseSound, 1.0f, 1.0f, 1, 0, 1);
        User currentUser = UserFile.getInstance(getContext()).getCurrentUser();
        if (currentUser.getScore() < currentScore) currentUser.setScore(currentScore);
        Bundle bundle = new Bundle();
        bundle.putString(LeaderboardFragment.TEXT_ARG, "You Lose");
        bundle.putInt(LeaderboardFragment.POINTS_ARG, currentScore);
        navController.navigate(R.id.action_gameFragment_to_leaderboardFragment, bundle);
    }

    private void win() {
        soundPool.play (winSound, 0.5f, 0.5f, 1, 0, 1);

        User currentUser = UserFile.getInstance(getContext()).getCurrentUser();
        if (currentUser.getScore() < currentScore) currentUser.setScore(currentScore);
        Bundle bundle = new Bundle();
        bundle.putString(LeaderboardFragment.TEXT_ARG, "You Win");
        bundle.putInt(LeaderboardFragment.POINTS_ARG, currentScore);
        navController.navigate(R.id.action_gameFragment_to_leaderboardFragment, bundle);
    }
}
