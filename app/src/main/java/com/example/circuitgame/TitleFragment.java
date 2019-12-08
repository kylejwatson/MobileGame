package com.example.circuitgame;


import android.content.Context;
import android.content.Intent;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class TitleFragment extends Fragment {

    private TextView username;

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

        FrameLayout frameLayout = view.findViewById(R.id.userFrame);
        frameLayout.removeAllViews();
        LayoutInflater inflater =
                (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater == null) return;
        View userView = inflater.inflate(R.layout.fragment_score, frameLayout, true);
        username = userView.findViewById(R.id.userName);
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
                context.startActivity(new Intent(context, UserActivity.class));
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
        username.setText(currentUser.getUsername());
    }
}
