package com.example.circuitgame;

import android.content.Context;
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
    static void saveToFile(Context context, List<User> users) {
        StringBuilder builder = new StringBuilder();

        for (User user: users)
            builder.append(user.toCSV());
        if (builder.length() > 0 && builder.charAt(builder.length()-1) == NEWLINE)
            builder.deleteCharAt(builder.length()-1);
        Log.d("ID","file dir = " + context.getFilesDir());
        Log.d("ID","file contents = " + builder);
        try {
            File fp = new File(context.getFilesDir(), FILE_NAME);
            FileWriter out = new FileWriter(fp);
            out.write(builder.toString());
            out.close();
        } catch (IOException e) {
            Log.d("Me","file error:" + e);
        }
    }
    static List<User> readFromFile(Context context) {
        BufferedReader in;
        List<User> users = new ArrayList<>();
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
        return users;
    }
}
