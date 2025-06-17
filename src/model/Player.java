package model;

/**
 * Player - Model class untuk data player
 * Bagian dari Model layer dalam MVVM pattern
 */
public class Player {
    private String username;
    private int score;
    private int count;

    public Player(String username, int score, int count) {
        this.username = username;
        this.score = score;
        this.count = count;
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public int getScore() {
        return score;
    }

    public int getCount() {
        return count;
    }

    // Setters
    public void setUsername(String username) {
        this.username = username;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void addScore(int scoreToAdd) {
        this.score += scoreToAdd;
    }

    public void addCount(int countToAdd) {
        this.count += countToAdd;
    }

    @Override
    public String toString() {
        return String.format("Player{username='%s', score=%d, count=%d}", 
                           username, score, count);
    }
}