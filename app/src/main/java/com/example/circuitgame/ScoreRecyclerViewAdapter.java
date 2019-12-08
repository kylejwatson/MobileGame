package com.example.circuitgame;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link User} and makes a call to the.
 */
public class ScoreRecyclerViewAdapter extends RecyclerView.Adapter<ScoreRecyclerViewAdapter.ViewHolder> {

    private final List<User> users;
    private int currentID;

    public ScoreRecyclerViewAdapter(List<User> users) {
        this.users = users;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_score, parent, false);
        currentID = UserFile.getInstance(parent.getContext()).getCurrentUser().getID();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        User user = users.get(position);
        holder.username.setText(user.getUsername());
        holder.score.setText("PB: " + user.getScore());
        if(currentID == user.getID()) {
            holder.view.setBackgroundResource(R.color.colorSelect);
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView username;
        public final TextView score;
        public final ImageView profile;
        public final View view;


        public ViewHolder(View view) {
            super(view);
            this.view = view;
            username = view.findViewById(R.id.userName);
            score = view.findViewById(R.id.score);
            profile = view.findViewById(R.id.userProfileImage);
        }
    }
}
