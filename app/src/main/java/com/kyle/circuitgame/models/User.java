package com.kyle.circuitgame.models;

import android.net.Uri;

import com.kyle.circuitgame.utils.UserFile;


public class User{
    private String username;
    private int ID;
    private int score;
    private Uri uri;

    public User(String username) {
        this.username = username;
        uri = Uri.EMPTY;
        ID = -1;
    }

    public User(String[] fileLine) {
        this.ID = Integer.parseInt(fileLine[0]);
        this.username = fileLine[1];
        this.score = Integer.parseInt(fileLine[2]);

        this.uri = fileLine.length > 3 ? Uri.parse(fileLine[3]) : Uri.EMPTY;
    }

    public String toCSV(){
        return ID + UserFile.COMMA + username + UserFile.COMMA + score + UserFile.COMMA + uri.toString() + UserFile.NEWLINE;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getID() {
        return ID;
    }

    public void setID(int id) {
        this.ID = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return username;
    }
}
