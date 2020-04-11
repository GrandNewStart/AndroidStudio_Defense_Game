package com.jinwoo.defensegame;

public class Score {

    int rank;
    int score;
    String id;

    public Score (int rank, String id, int score) {
        this.rank = rank;
        this.id = id;
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
