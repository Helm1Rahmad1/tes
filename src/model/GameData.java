package model;

/**
 * GameData - Model untuk menyimpan state game saat ini
 * Bagian dari Model layer dalam MVVM pattern
 */
public class GameData {
    private String currentUsername;
    private int currentScore;
    private int currentCount;
    private boolean gameRunning;
    private boolean gamePaused;

    public GameData() {
        this.currentUsername = "";
        this.currentScore = 0;
        this.currentCount = 0;
        this.gameRunning = false;
        this.gamePaused = false;
    }

    public GameData(String username) {
        this.currentUsername = username;
        this.currentScore = 0;
        this.currentCount = 0;
        this.gameRunning = false;
        this.gamePaused = false;
    }

    // Getters
    public String getCurrentUsername() {
        return currentUsername;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public int getCurrentCount() {
        return currentCount;
    }

    public boolean isGameRunning() {
        return gameRunning;
    }

    public boolean isGamePaused() {
        return gamePaused;
    }

    // Setters
    public void setCurrentUsername(String currentUsername) {
        this.currentUsername = currentUsername;
    }

    public void setCurrentScore(int currentScore) {
        this.currentScore = currentScore;
    }

    public void setCurrentCount(int currentCount) {
        this.currentCount = currentCount;
    }

    public void setGameRunning(boolean gameRunning) {
        this.gameRunning = gameRunning;
    }

    public void setGamePaused(boolean gamePaused) {
        this.gamePaused = gamePaused;
    }

    // Game actions
    public void addScore(int points) {
        this.currentScore += points;
    }

    public void incrementCount() {
        this.currentCount++;
    }

    public void resetGame() {
        this.currentScore = 0;
        this.currentCount = 0;
        this.gameRunning = false;
        this.gamePaused = false;
    }

    public void startGame() {
        this.gameRunning = true;
        this.gamePaused = false;
    }

    public void pauseGame() {
        this.gamePaused = true;
    }

    public void stopGame() {
        this.gameRunning = false;
        this.gamePaused = false;
    }
}