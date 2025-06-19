package model;

/**
 * Untuk menyimpan status game saat ini
 * Bertanggung jawab untuk melacak username, skor, jumlah bola,
 * dan status permainan (berjalan, jeda, game over)
 */
public class GameData {
    private String currentUsername;
    private int currentScore;
    private int currentCount;
    private boolean gameRunning;
    private boolean gamePaused;
    private boolean gameOver; 

    public GameData() {
        this.currentUsername = "";
        this.currentScore = 0;
        this.currentCount = 0;
        this.gameRunning = false;
        this.gamePaused = false;
        this.gameOver = false; 
    }

    public GameData(String username) {
        this.currentUsername = username;
        this.currentScore = 0;
        this.currentCount = 0;
        this.gameRunning = false;
        this.gamePaused = false;
        this.gameOver = false; 
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

    public boolean isGameOver() { 
        return gameOver;
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

    public void setGameOver(boolean gameOver) { 
        this.gameOver = gameOver;
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
        this.gameOver = false; 
    }

    public void startGame() {
        this.gameRunning = true;
        this.gamePaused = false;
        this.gameOver = false; 
    }

    public void pauseGame() {
        this.gamePaused = true;
    }

    public void stopGame() {
        this.gameRunning = false;
        this.gamePaused = false;
        this.gameOver = true; 
    }
}