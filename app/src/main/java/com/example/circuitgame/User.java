package com.example.circuitgame;

import android.graphics.Bitmap;
import android.net.Uri;

class User{
    private String username;
    private int ID;
    private int score;
    private Uri uri;

    User(String username) {
        this.username = username;
        uri = Uri.EMPTY;
        ID = -1;
    }

    User(String[] fileLine) {
        this.ID = Integer.parseInt(fileLine[0]);
        this.username = fileLine[1];
        this.score = Integer.parseInt(fileLine[2]);

        this.uri = fileLine.length > 3 ? Uri.parse(fileLine[3]) : Uri.EMPTY;
    }

    String toCSV(){
        return ID + UserFile.COMMA + username + UserFile.COMMA + score + UserFile.COMMA + uri.toString() + UserFile.NEWLINE;
    }

    String getUsername() {
        return username;
    }

    void setUsername(String username) {
        this.username = username;
    }

    int getID() {
        return ID;
    }

    void setID(int id) {
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
