package com.example.circuitgame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChangeUserActivity extends AppCompatActivity {
    private ScoreRecyclerViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user);

        Button select = findViewById(R.id.selectButton);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button editUser = findViewById(R.id.editButton);
        editUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangeUserActivity.this, UserActivity.class);
                intent.putExtra(UserActivity.EDIT_USER_EXTRA, true);
                startActivity(intent);
            }
        });
        Button addUser = findViewById(R.id.addButton);
        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChangeUserActivity.this, UserActivity.class));
            }
        });

        RecyclerView recyclerView = findViewById(R.id.userList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ScoreRecyclerViewAdapter(UserFile.getInstance(this).getUserList(), new ScoreRecyclerViewAdapter.UserClickListener() {
            @Override
            public void userOnClick(int i) {
                UserFile.getInstance(ChangeUserActivity.this).selectCurrentUser(i);
                adapter.notifyDataSetChanged();
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onStop() {
        UserFile.getInstance(this).saveToFile(this);
        super.onStop();
    }
}
