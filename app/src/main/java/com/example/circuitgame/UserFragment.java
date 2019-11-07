package com.example.circuitgame;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnUserFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class UserFragment extends Fragment {
    private NavController navController;
    private EditText username;
    private List<User> users;
    private Spinner spinner;
    private ArrayAdapter<User> adapter;

    private OnUserFragmentInteractionListener mListener;

    public UserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Context context = getContext();
        if (context == null) return;

        users = new ArrayList<>();
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, users);
        spinner = view.findViewById(R.id.userSpinner);
        spinner.setAdapter(adapter);

        navController = Navigation.findNavController(view);
        username = view.findViewById(R.id.userNameText);
        Button addUserButton = view.findViewById(R.id.addUserButton);
        addUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User(username.getText().toString());
                addUser(user);
            }
        });
        Button selectUserButton = view.findViewById(R.id.selectButton);
        selectUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = (User) spinner.getSelectedItem();
                onUserSelected(user);
            }
        });
        Button loadProfileButton = view.findViewById(R.id.loadButton);
        loadProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                users.clear();
                users.addAll(UserFile.readFromFile(context));
                adapter.notifyDataSetChanged();
            }
        });
        Button wipeProfileButton = view.findViewById(R.id.deleteButton);
        wipeProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                users.clear();
                UserFile.saveToFile(context, users);
                adapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    private void addUser(User user){
        user.setID(users.size());
        users.add(user);
        adapter.notifyDataSetChanged();
        UserFile.saveToFile(getContext(), users);
        onUserSelected(user);
    }

    private void onUserSelected(User user) {
        if (mListener != null) {
            mListener.onUserFragmentInteraction(user);
        }
        navController.navigate(R.id.action_userFragment_to_gameFragment);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnUserFragmentInteractionListener) {
            mListener = (OnUserFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnUserFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnUserFragmentInteractionListener {
        void onUserFragmentInteraction(User user);
    }
}
