package com.kyle.circuitgame.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.kyle.circuitgame.R;
import com.kyle.circuitgame.adapters.ScoreRecyclerViewAdapter;
import com.kyle.circuitgame.utils.UserFile;

public class ChangeUserActivity extends AppCompatActivity {
    private ScoreRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user);

        Button select = findViewById(R.id.activity_change_btn_select);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button editUser = findViewById(R.id.activity_change_btn_edit);
        editUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangeUserActivity.this, UserActivity.class);
                intent.putExtra(UserActivity.EDIT_USER_EXTRA, true);
                startActivity(intent);
            }
        });
        Button addUser = findViewById(R.id.activity_change_btn_add);
        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChangeUserActivity.this, UserActivity.class));
            }
        });

        RecyclerView recyclerView = findViewById(R.id.activity_change_rv_user);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ScoreRecyclerViewAdapter(UserFile.getInstance(this).getUserList(), new ScoreRecyclerViewAdapter.UserClickListener() {
            @Override
            public void userOnClick(int i) {
                UserFile.getInstance(ChangeUserActivity.this).selectCurrentUser(i);
                mAdapter.notifyDataSetChanged();
            }
        });
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onStop() {
        UserFile.getInstance(this).saveToFile(this);
        super.onStop();
    }
}
