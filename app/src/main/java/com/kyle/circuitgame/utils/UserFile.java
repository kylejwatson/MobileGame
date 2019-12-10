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
    private static final String FILE_NAME = "users.csv";
    private List<User> users;
    private static UserFile instance;
    private User currentUser;

    private UserFile(Context context) {
        users = new ArrayList<>();
        readFromFile(context);
        currentUser = getUser(context, users.size() == 0 ? 0 : users.size() - 1);
    }

    public static UserFile getInstance(Context context) {
        if (instance == null) instance = new UserFile(context);
        return instance;
    }

    public class FileRunnable implements Runnable {
        private Context context;

        FileRunnable(Context context) {
            this.context = context;
        }

        @Override
        public void run() {
            StringBuilder builder = new StringBuilder();

            for (User user : users)
                builder.append(user.toCSV());
            if (builder.length() > 0 && builder.charAt(builder.length() - 1) == NEWLINE)
                builder.deleteCharAt(builder.length() - 1);
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
        BufferedReader in;
        try {
            File fp = new File(context.getFilesDir(), FILE_NAME);
            in = new BufferedReader(new FileReader(fp));
            String line;
            while ((line = in.readLine()) != null)
                users.add(new User(line.split("" + COMMA)));

        } catch (FileNotFoundException e) {
            Log.d("ID", e.getMessage() + "");
        } catch (IOException e) {
            Log.d("ID", e.getMessage() + "");
        }
    }

    private User getUser(Context context, int i) {
        if (users.size() <= i) {
            //start create user page
            context.startActivity(new Intent(context, UserActivity.class));
            return new User("No Profile");
        }
        return users.get(i);
    }

    public void addUser(User user) {
        int max = 0;
        for (User u : users) {
            if (u.getID() >= max) {
                max = u.getID() + 1;
            }
        }
        user.setID(max);
        users.add(user);
        currentUser = user;
    }

    public void selectCurrentUser(int i) {
        currentUser = users.get(i);
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public List<User> getUserList() {
        return users;
    }

    public void editUser(User user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getID() == user.getID()) {
                users.set(i, user);
            }
        }
    }

    private void wipeFile(Context context){
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