package com.kyle.circuitgame.models;

import android.net.Uri;

import com.kyle.circuitgame.utils.UserFile;


public class User{
    private String mUsername;
    private int mID;
    private int mScore;
    private Uri mUri;

    public User(String username) {
        mUsername = username;
        mUri = Uri.EMPTY;
        mID = -1;
    }

    public User(String[] fileLine) {
        mID = Integer.parseInt(fileLine[0]);
        mUsername = fileLine[1];
        mScore = Integer.parseInt(fileLine[2]);
        mUri = fileLine.length > 3 ? Uri.parse(fileLine[3]) : Uri.EMPTY;
    }

    @Override
    public String toString() {
        return mUsername;
    }

    public String toCSV(){
        return mID + UserFile.COMMA + mUsername + UserFile.COMMA + mScore + UserFile.COMMA +
                mUri.toString() + UserFile.NEWLINE;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public int getID() {
        return mID;
    }

    public void setID(int id) {
        mID = id;
    }

    public int getScore() {
        return mScore;
    }

    public void setScore(int score) {
        mScore = score;
    }

    public Uri getUri() {
        return mUri;
    }

    public void setUri(Uri uri) {
        mUri = uri;
    }
}
