package com.example.circuitgame;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class UserFile {
    static final char NEWLINE = '\n';
    private static final String FILE_NAME = "users.csv";
    static final String COMMA = ",";
    private List<User> users;
    private static UserFile instance;
    private User currentUser;
    private boolean wipe = false;

    private UserFile(Context context) {
        if (wipe) {
            File fp = new File(context.getFilesDir(), FILE_NAME);
            try {
                FileWriter out = new FileWriter(fp);
                out.write("");
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        users = new ArrayList<>();
        readFromFile(context);
        currentUser = getUser(context, users.size() == 0 ? 0 : users.size() - 1);
    }

    static UserFile getInstance(Context context) {
        if (instance == null) instance = new UserFile(context);
        return instance;
    }

    void saveToFile(Context context) {
        StringBuilder builder = new StringBuilder();

        for (User user : users)
            builder.append(user.toCSV());
        if (builder.length() > 0 && builder.charAt(builder.length() - 1) == NEWLINE)
            builder.deleteCharAt(builder.length() - 1);
        Log.d("ID", "file dir = " + context.getFilesDir());
        Log.d("ID", "file contents = " + builder);
        try {
            File fp = new File(context.getFilesDir(), FILE_NAME);
            FileWriter out = new FileWriter(fp);
            out.write(builder.toString());
            out.close();
        } catch (IOException e) {
            Log.d("Me", "file error:" + e);
        }
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
            Log.d("ID", e.getMessage());
        } catch (IOException e) {
            Log.d("ID", e.getMessage());
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

    void addUser(User user) {
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

    User getCurrentUser() {
        return currentUser;
    }

    List<User> getUserList() {
        return users;
    }
}
