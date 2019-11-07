package com.example.circuitgame;

class User{
    private String username;
    private int ID;
    private int score;

    User(String username) {
        this.username = username;
    }

    User(String[] fileLine) {
        this.ID = Integer.parseInt(fileLine[0]);
        this.username = fileLine[1];
        this.score = Integer.parseInt(fileLine[2]);
    }

    String toCSV(){
        return ID + UserFile.COMMA + username + UserFile.COMMA + score + UserFile.NEWLINE;
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
    @Override
    public String toString() {
        return username;
    }

}
