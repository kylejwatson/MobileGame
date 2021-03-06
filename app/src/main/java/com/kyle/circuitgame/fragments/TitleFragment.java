package com.kyle.circuitgame.fragments;

import android.content.Context;
import android.content.Intent;
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

import com.kyle.circuitgame.R;
import com.kyle.circuitgame.activities.ChangeUserActivity;
import com.kyle.circuitgame.activities.HelpActivity;
import com.kyle.circuitgame.adapters.ScoreRecyclerViewAdapter;
import com.kyle.circuitgame.models.User;
import com.kyle.circuitgame.utils.UserFile;

import java.util.ArrayList;
import java.util.List;

public class TitleFragment extends Fragment {

    private List<User> mSingleUser;
    private ScoreRecyclerViewAdapter mAdapter;

    public TitleFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_title, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Context context = getContext();
        if (context == null) return;
        mSingleUser = new ArrayList<>();
        RecyclerView recyclerView = view.findViewById(R.id.fragment_title_rv_user);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        mAdapter = new ScoreRecyclerViewAdapter(mSingleUser, null);
        recyclerView.setAdapter(mAdapter);
        displayCurrentUser();

        final NavController navController = Navigation.findNavController(view);
        Button playButton = view.findViewById(R.id.fragment_title_btn_play);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_titleFragment_to_gameFragment);
            }
        });

        Button changeButton = view.findViewById(R.id.fragment_title_btn_user);
        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ChangeUserActivity.class));
            }
        });

        Button helpButton = view.findViewById(R.id.fragment_title_btn_help);
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), HelpActivity.class));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        displayCurrentUser();
    }

    private void displayCurrentUser() {
        User currentUser = UserFile.getInstance(getContext()).getCurrentUser();
        mSingleUser.clear();
        mSingleUser.add(currentUser);
        mAdapter.notifyDataSetChanged();
    }
}
