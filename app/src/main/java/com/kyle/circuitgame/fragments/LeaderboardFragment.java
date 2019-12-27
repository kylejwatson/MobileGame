package com.kyle.circuitgame.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.kyle.circuitgame.R;
import com.kyle.circuitgame.adapters.ScoreRecyclerViewAdapter;
import com.kyle.circuitgame.utils.UserFile;

public class LeaderboardFragment extends Fragment {
    static final String TEXT_ARG = "win_lose_text";
    static final String POINTS_ARG = "win_lose_points";

    public LeaderboardFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_leaderboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();
        if (args == null) return;
        TextView winLabel = view.findViewById(R.id.fragment_leader_tv_end);
        winLabel.setText(getArguments().getString(TEXT_ARG));
        TextView pointLabel = view.findViewById(R.id.fragment_leader_tv_score);
        pointLabel.setText(getString(R.string.end_score_label, args.getInt(POINTS_ARG)));

        Context context = view.getContext();
        RecyclerView recyclerView = view.findViewById(R.id.fragment_leader_rv_user);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new ScoreRecyclerViewAdapter(UserFile.getInstance(context).getUserList(), null));
        
        final NavController navController = Navigation.findNavController(view);
        Button playAgain = view.findViewById(R.id.fragment_leader_btn_again);
        playAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_leaderboardFragment_to_gameFragment);
            }
        });

        Button mainMenu = view.findViewById(R.id.fragment_leader_btn_menu);
        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_leaderboardFragment_to_titleFragment);
            }
        });
    }
}
