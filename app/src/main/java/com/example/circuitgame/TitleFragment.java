package com.example.circuitgame;


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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TitleFragment extends Fragment {

    private List<User> singleUser;
    private ScoreRecyclerViewAdapter adapter;
    public TitleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_title, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Context context = getContext();
        if (context == null) return;
        singleUser = new ArrayList<>();
        RecyclerView recyclerView = view.findViewById(R.id.singleUserList);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new ScoreRecyclerViewAdapter(singleUser, null);
        recyclerView.setAdapter(adapter);
        displayCurrentUser();

        final NavController navController = Navigation.findNavController(view);
        Button playButton = view.findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_titleFragment_to_gameFragment);
            }
        });

        Button changeButton = view.findViewById(R.id.changeUser);
        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ChangeUserActivity.class));
            }
        });

        Button helpButton = view.findViewById(R.id.helpButton);
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

    private void displayCurrentUser(){
        User currentUser = UserFile.getInstance(getContext()).getCurrentUser();
        singleUser.clear();
        singleUser.add(currentUser);
        adapter.notifyDataSetChanged();
    }
}
