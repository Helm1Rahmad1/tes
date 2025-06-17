package viewmodel;

import model.Database;
import model.GameData;
import view.components.Ball;
import view.components.Lasso;
import view.components.Character;

import utils.GameConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * GameViewModel - ViewModel untuk game logic dengan sprite animation support
 * Menghandle semua logic gameplay
 * Bagian dari ViewModel layer dalam MVVM pattern
 */
public class GameViewModel {
    private GameData gameData;
    private Character character;
    private Lasso lasso;
    private List<Ball> balls;
    private Random random;
    private int ballSpawnTimer;
    private boolean[] keyPressed;
    private int currentScore;
    private int timeRemaining;
    private boolean gamePaused;
    private double timeAccumulator = 0;
    
    // Movement tracking for smooth animation
    private boolean isMovingHorizontally = false;
    private boolean isMovingVertically = false;
    private long lastMoveTime = 0;
    private static final long MOVE_DELAY = 50; // Milliseconds between moves for smooth animation

    public GameViewModel(String username) {
        this.gameData = new GameData(username);
        this.random = new Random();
        this.ballSpawnTimer = 0;
        this.keyPressed = new boolean[256];
        
        initializeGameElements();
        gameData.startGame();
    }

    private void initializeGameElements() {
        // Initialize balls
        balls = new ArrayList<>();
        // Inisialisasi awal bola juga perlu disesuaikan dengan logika spawn baru
        for (int i = 0; i < GameConstants.INITIAL_BALL_COUNT; i++) {
            spawnSingleBall(); // Gunakan metode spawn tunggal yang sudah diatur
        }

        // Initialize character
        character = new Character();

        // Initialize lasso
        lasso = new Lasso();

        // Initialize other game state variables
        currentScore = 0;
        timeRemaining = GameConstants.INITIAL_TIME;
        gamePaused = false;
    }

    /**
     * Update game state - main game loop logic
     */
    public void update() {
        if (!gameData.isGameRunning() || gameData.isGamePaused() || gameData.isGameOver()) { // Tambahkan gameData.isGameOver()
            return;
        }

        // Update character (this will handle sprite animation)
        character.update();

        // Perbarui posisi awal lasso berdasarkan pergerakan karakter
        lasso.updateStartPosition(character.getCenterX(), character.getCenterY());

        // Tangani input dengan pergerakan yang lebih halus
        handleInputSmooth();

        // Perbarui lasso
        lasso.update();

        // Periksa tabrakan lasso dengan bola
        Ball caughtBall = lasso.checkCollision(balls.toArray(new Ball[0]));
        if (caughtBall != null) {
            if (caughtBall.getValue() == 10) { // Jika bola adalah bom
                gameData.setGameOver(true); // Atur status game over
            } else {
                // Tambahkan skor dan jumlah bola jika bukan bom
                currentScore += caughtBall.getValue();
                gameData.addScore(caughtBall.getValue());
                gameData.incrementCount();
            }
        }

        // Perbarui bola
        updateBalls();

        // Spawn bola baru
        spawnBalls();

        // Perbarui waktu yang tersisa
        timeAccumulator += GameConstants.FRAME_DELAY / 1000.0;
        if (timeAccumulator >= 1.0) {
            timeRemaining--;
            timeAccumulator = 0;
        }

        if (timeRemaining <= 0) {
            timeRemaining = 0;
            stopGame(); // stopGame akan mengatur gameData.setGameOver(true)
        }
    }

    /**
     * Tangani input keyboard dengan pergerakan langsung untuk animasi yang lebih halus
     */
    private void handleInputSmooth() {
        int moveSpeed = GameConstants.CHARACTER_SPEED;
        boolean moved = false;
        int dx = 0, dy = 0;
        
        // Akumulasi pergerakan
        if (keyPressed[37]) { // Kiri
            dx -= moveSpeed;
            moved = true;
        }
        if (keyPressed[39]) { // Kanan
            dx += moveSpeed;
            moved = true;
        }
        if (keyPressed[38]) { // Atas
            dy -= moveSpeed;
            moved = true;
        }
        if (keyPressed[40]) { // Bawah
            dy += moveSpeed;
            moved = true;
        }
        
        // Terapkan pergerakan jika ada tombol yang ditekan
        if (moved) {
            character.moveDirectly(dx, dy);
        }
    }


    /**
     * Update all balls
     */
    private void updateBalls() {
        balls.removeIf(ball -> !ball.isActive());
        
        for (Ball ball : balls) {
            ball.update();
        }
    }

    /**
     * Spawn new balls
     */
    private void spawnBalls() {
        ballSpawnTimer++;
        
        if (ballSpawnTimer >= GameConstants.BALL_SPAWN_DELAY && balls.size() < GameConstants.MAX_BALLS) {
            spawnSingleBall();
            ballSpawnTimer = 0;
        }
    }

    /**
     * Logic untuk membuat satu bola baru dengan arah yang ditentukan
     */
    private void spawnSingleBall() {
        int x, y, direction;
        
        // Tentukan apakah muncul dari atas atau bawah (50/50 chance)
        if (random.nextBoolean()) { // Muncul dari atas (kanan ke kiri), di atas posisi Y karakter
            x = GameConstants.WINDOW_WIDTH; // Mulai dari sisi kanan
            // Y acak di area di atas karakter, tapi masih dalam GAME_AREA_HEIGHT
            // Batas atas: 0
            // Batas bawah: CHARACTER_START_Y - (GAME_AREA_HEIGHT / 4)
            // Menggunakan CHARACTER_START_Y sebagai referensi tengah
            y = random.nextInt(GameConstants.CHARACTER_START_Y - GameConstants.BALL_SIZE - 50); // Agar tidak terlalu dekat
            direction = -1; // Bergerak ke kiri
        } else { // Muncul dari bawah (kiri ke kanan), di bawah posisi Y karakter
            x = -GameConstants.BALL_SIZE; // Mulai dari sisi kiri (di luar layar)
            // Y acak di area di bawah karakter, sampai batas bawah layar
            // Batas atas: CHARACTER_START_Y + CHARACTER_SIZE + 50
            // Batas bawah: WINDOW_HEIGHT - BALL_SIZE
            y = GameConstants.CHARACTER_START_Y + GameConstants.CHARACTER_SIZE + 50 +
                random.nextInt(GameConstants.WINDOW_HEIGHT - (GameConstants.CHARACTER_START_Y + GameConstants.CHARACTER_SIZE + 50) - GameConstants.BALL_SIZE);
            direction = 1; // Bergerak ke kanan
        }
        balls.add(new Ball(x, y, direction));
    }


    /**
     * Handle mouse click for lasso
     */
    public void handleMouseClick(int mouseX, int mouseY) {
        if (!gameData.isGameRunning() || gameData.isGamePaused() || gameData.isGameOver() || lasso.isActive()) { // Tambahkan gameData.isGameOver()
            return;
        }

        // Start lasso from character to mouse position
        lasso.start(character.getCenterX(), character.getCenterY(), mouseX, mouseY);
    }

    /**
     * Tangani event ketika tombol keyboard ditekan.
     * @param keyCode Kode tombol yang ditekan.
     */
    public void handleKeyPress(int keyCode) {
        if (keyCode < keyPressed.length) {
            keyPressed[keyCode] = true;
        }

        // Tombol spasi untuk pause/unpause game
        if (keyCode == 32) { // Spasi
            if (gameData.isGamePaused()) {
                gameData.startGame(); // Melanjutkan game (juga mengatur gameData.setGameOver(false))
            } else {
                gameData.pauseGame(); // Pause game
            }
        }
    }

    /**
     * Tangani event ketika tombol keyboard dilepas.
     * @param keyCode Kode tombol yang dilepas.
     */
    public void handleKeyRelease(int keyCode) {
        if (keyCode < keyPressed.length) {
            keyPressed[keyCode] = false;
        }
    }

    /**
     * Stop game and save result
     */
    public void stopGame() {
        gameData.stopGame();
        
        // Save to database
        if (gameData.getCurrentScore() > 0) {
            Database.insertOrUpdatePlayer(
                gameData.getCurrentUsername(),
                gameData.getCurrentScore(),
                gameData.getCurrentCount()
            );
        }
    }

    /**
     * Pause game
     */
    public void pauseGame() {
        gamePaused = true;
        gameData.pauseGame();
    }

    /**
     * Resume game
     */
    public void resumeGame() {
        gamePaused = false;
        gameData.startGame();
    }

    /**
     * Reset game
     */
    public void resetGame() {
        gameData.resetGame(); // Reset data game, termasuk game over
        initializeGameElements(); // Inisialisasi ulang elemen game
        ballSpawnTimer = 0; // Reset timer spawn bola
        keyPressed = new boolean[256]; // Reset input
        currentScore = 0; // Reset skor
        timeRemaining = GameConstants.INITIAL_TIME; // Reset waktu
        gamePaused = false; // Pastikan game tidak dalam keadaan pause
    }

    /**
     * Save game result to database
     */
    public void saveGameResult(String username, int score, int count) {
        if (username != null && !username.trim().isEmpty() && score >= 0) { // Perbaiki validasi skor
            Database.insertOrUpdatePlayer(username.trim(), score, count);
            System.out.println("Game result saved: Username = " + username + ", Score = " + score + ", Count = " + count);
        } else {
            System.err.println("Failed to save game result: Invalid username or score.");
        }
    }

    // Getters
    public GameData getGameData() { return gameData; }
    public Lasso getLasso() { return lasso; }
    public List<Ball> getBalls() { return balls; }
    
    public boolean isGameRunning() { return gameData.isGameRunning(); }
    public boolean isGamePaused() { return gamePaused; }
    public int getCurrentScore() { return gameData.getCurrentScore(); }
    public int getCurrentCount() { return gameData.getCurrentCount(); }
    public String getCurrentUsername() { return gameData.getCurrentUsername(); }

    public Character getCharacter() {
        return character;
    }

    public String getTimeRemaining() {
        return String.valueOf(timeRemaining);
    }
    
    // Additional getters for movement state (useful for debugging)
    public boolean isCharacterMovingHorizontally() { return isMovingHorizontally; }
    public boolean isCharacterMovingVertically() { return isMovingVertically; }
}