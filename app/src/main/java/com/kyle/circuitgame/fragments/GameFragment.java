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
    private int mCurrentScore;
    private TextView mScoreLabel;
    private TextView mObjectiveLabel;
    private NavController mNavController;
    private Objective mCurrentObjective;
    private SoundPool mSoundPool;
    private int mWinSound;
    private int mLoseSound;

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
        Button helpButton = view.findViewById(R.id.fragment_game_btn_help);
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), HelpActivity.class));
            }
        });
        mNavController = Navigation.findNavController(view);
        Button quitButton = view.findViewById(R.id.fragment_game_btn_quit);
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNavController.navigate(R.id.action_gameFragment_to_titleFragment);
            }
        });
        mScoreLabel = view.findViewById(R.id.fragment_game_tv_score);
        mObjectiveLabel = view.findViewById(R.id.fragment_game_tv_next);
        mCurrentScore = 0;
        loadSounds();
        GameView gameView = GameView.getInstance(getContext(), new GameView.ObjectiveListener() {
            @Override
            public void objectiveReached(Objective objective) {
                updateObjective(objective);
            }
        }, mSoundPool);

        FrameLayout frameLayout = view.findViewById(R.id.fragment_game_frame);
        frameLayout.addView(gameView);
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

    private void updateObjective(Objective objective) {
        if (mCurrentObjective == null) {
            mCurrentObjective = objective;
            updateUI();
            return;
        }
        if (!objective.equals(mCurrentObjective)) {
            end(false);
            return;
        }
        if (objective.getNextObjective() == null) {
            end(true);
            return;
        }
        mCurrentScore += 100;
        mCurrentObjective = objective.getNextObjective();
        updateUI();
    }

    private void updateUI() {
        mObjectiveLabel.setText(getString(R.string.collect_label, mCurrentObjective.getName()));
        mScoreLabel.setText(getString(R.string.score_label, mCurrentScore));
    }

    private void loadSounds() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .build();
            mSoundPool = new SoundPool.Builder()
                    .setMaxStreams(2)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else mSoundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 1);

        mWinSound = mSoundPool.load(getContext(), R.raw.win, 1);
        mLoseSound = mSoundPool.load(getContext(), R.raw.losew, 1);
    }

    @Override
    public void onPause() {
        super.onPause();
        GameView.getInstance().pause();
    }

    private void end(boolean win) {
        int sound = win ? mWinSound : mLoseSound;
        mSoundPool.play(sound, 1.0f, 1.0f, 1, 0, 1);
        User currentUser = UserFile.getInstance(getContext()).getCurrentUser();
        if (currentUser.getScore() < mCurrentScore) currentUser.setScore(mCurrentScore);
        Bundle bundle = new Bundle();
        bundle.putString(LeaderboardFragment.TEXT_ARG,
                getString(win ? R.string.win_status : R.string.lose_status));
        bundle.putInt(LeaderboardFragment.POINTS_ARG, mCurrentScore);
        mNavController.navigate(R.id.action_gameFragment_to_leaderboardFragment, bundle);
    }
}
