package com.example.circuitgame;

import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.util.Log;
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
    private UserClickListener userClickListener;

    public ScoreRecyclerViewAdapter(List<User> users, UserClickListener userClickListener) {
        this.users = users;
        this.userClickListener = userClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell_score, parent, false);
        return new ViewHolder(view, userClickListener);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Log.d("ADAPTER", "bind" + position);
        User user = users.get(position);
        holder.username.setText(user.getUsername());
        holder.score.setText("PB: " + user.getScore());
        int currentID = UserFile.getInstance(holder.view.getContext()).getCurrentUser().getID();
        if (!user.getUri().equals(Uri.EMPTY)) {
            holder.profile.setImageURI(user.getUri());
        }
        holder.view.setBackgroundResource(currentID == user.getID() ? R.color.colorSelect : 0);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView username;
        public final TextView score;
        public final ImageView profile;
        public final View view;
        private final UserClickListener userClickListener;


        public ViewHolder(View view, UserClickListener userClickListener) {
            super(view);
            this.view = view;
            username = view.findViewById(R.id.userName);
            score = view.findViewById(R.id.score);
            profile = view.findViewById(R.id.userProfileImage);
            this.userClickListener = userClickListener;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (userClickListener != null) userClickListener.userOnClick(getAdapterPosition());
        }
    }

    public interface UserClickListener {
        void userOnClick(int i);
    }
}
