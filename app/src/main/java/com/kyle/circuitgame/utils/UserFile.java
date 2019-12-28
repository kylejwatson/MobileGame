package com.kyle.circuitgame.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.kyle.circuitgame.activities.UserActivity;
import com.kyle.circuitgame.models.User;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserFile {
    public static final char NEWLINE = '\n';
    public static final String COMMA = ",";
    private static final String FILE_NAME = "mUsers.csv";
    private List<User> mUsers;
    private static UserFile sInstance;
    private User currentUser;

    private UserFile(Context context) {
        mUsers = new ArrayList<>();
        readFromFile(context);
        currentUser = getUser(context, mUsers.size() == 0 ? 0 : mUsers.size() - 1);
    }

    public static UserFile getInstance(Context context) {
        if (sInstance == null) sInstance = new UserFile(context);
        return sInstance;
    }

    public class FileRunnable implements Runnable {
        private Context context;

        FileRunnable(Context context) {
            this.context = context;
        }

        @Override
        public void run() {
            StringBuilder builder = new StringBuilder();

            for (User user : mUsers) builder.append(user.toCSV());
            if (builder.length() > 0 && builder.charAt(builder.length() - 1) == NEWLINE) {
                builder.deleteCharAt(builder.length() - 1);
            }
            try {
                File fp = new File(context.getFilesDir(), FILE_NAME);
                FileWriter out = new FileWriter(fp);
                out.write(builder.toString());
                out.close();
            } catch (IOException e) {
                Log.d("Me", "file error:" + e);
            }
        }
    }

    public void saveToFile(Context context) {
        FileRunnable fileRunnable = new FileRunnable(context);
        new Thread(fileRunnable).start();
    }

    private void readFromFile(Context context) {
        try {
            File fp = new File(context.getFilesDir(), FILE_NAME);
            BufferedReader in = new BufferedReader(new FileReader(fp));
            String line;
            while ((line = in.readLine()) != null) {
                mUsers.add(new User(line.split("" + COMMA)));
            }
        } catch (FileNotFoundException e) {
            Log.d("ID", e.getMessage() + "");
        } catch (IOException e) {
            Log.d("ID", e.getMessage() + "");
        }
    }

    private User getUser(Context context, int i) {
        if (mUsers.size() <= i) {
            context.startActivity(new Intent(context, UserActivity.class));
            return new User("No Profile");
        }
        return mUsers.get(i);
    }

    public void addUser(User user) {
        int max = 0;
        for (User u : mUsers) if (u.getID() >= max) max = u.getID() + 1;
        user.setID(max);
        mUsers.add(user);
        currentUser = user;
    }

    public void selectCurrentUser(int i) {
        currentUser = mUsers.get(i);
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public List<User> getUserList() {
        return mUsers;
    }

    public void editUser(User user) {
        for (int i = 0; i < mUsers.size(); i++) {
            if (mUsers.get(i).getID() == user.getID()) mUsers.set(i, user);
        }
    }

    private void wipeFile(Context context) {
        File fp = new File(context.getFilesDir(), FILE_NAME);
        try {
            FileWriter out = new FileWriter(fp);
            out.write("");
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
