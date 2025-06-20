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
 * ViewModel untuk logika game dengan dukungan animasi sprite
 * Menangani semua logika gameplay
 * Bagian dari lapisan ViewModel dalam pola MVVM
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
    
    // Pelacakan pergerakan untuk animasi yang halus
    private boolean isMovingHorizontally = false;
    private boolean isMovingVertically = false;
    private long lastMoveTime = 0;
    private static final long MOVE_DELAY = 50; 

    public GameViewModel(String username) {
        this.gameData = new GameData(username);
        this.random = new Random();
        this.ballSpawnTimer = 0;
        this.keyPressed = new boolean[256];
        
        initializeGameElements();
        gameData.startGame();
    }

    private void initializeGameElements() {
        // Inisialisasi bola
        balls = new ArrayList<>();
        for (int i = 0; i < GameConstants.INITIAL_BALL_COUNT; i++) {
            spawnSingleBall(); 
        }

        // Inisialisasi karakter
        character = new Character();

        // Inisialisasi lasso
        lasso = new Lasso();

        // Inisialisasi variabel status game lainnya
        currentScore = 0;
        timeRemaining = GameConstants.INITIAL_TIME;
        gamePaused = false;
    }

    /**
     * Status game logika loop game utama.
     */
    public void update() {
        if (!gameData.isGameRunning() || gameData.isGamePaused() || gameData.isGameOver()) { 
            return;
        }

        character.update();
        lasso.updateStartPosition(character.getCenterX(), character.getCenterY());
        handleInputSmooth();
        lasso.update();

        // Periksa tabrakan lasso dengan bola
        Ball caughtBall = lasso.checkCollision(balls.toArray(new Ball[0]));
        if (caughtBall != null) {
            if (caughtBall.getValue() == 10) { 
                gameData.setGameOver(true); 
                stopGame(); // 
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
            stopGame(); 
        }
    }

    /**
     * Tangani input keyboard dengan pergerakan langsung untuk animasi yang lebih halus.
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
        
        // Pergerakan jika ada tombol yang ditekan
        if (moved) {
            character.moveDirectly(dx, dy);
        }
    }


    /**
     * Perbarui semua bola.
     */
    private void updateBalls() {
        balls.removeIf(ball -> !ball.isActive());
        
        for (Ball ball : balls) {
            ball.update();
        }
    }

    /**
     * Hasilkan bola baru.
     */
    private void spawnBalls() {
        ballSpawnTimer++;
        
        if (ballSpawnTimer >= GameConstants.BALL_SPAWN_DELAY && balls.size() < GameConstants.MAX_BALLS) {
            spawnSingleBall();
            ballSpawnTimer = 0;
        }
    }

    /**
     * Logika untuk membuat satu bola baru dengan arah yang ditentukan.
     */
    private void spawnSingleBall() {
        int x, y, direction;
        
        // Tentukan apakah muncul dari atas atau bawah 
        if (random.nextBoolean()) { 
            x = GameConstants.WINDOW_WIDTH; // Mulai dari sisi kanan
            y = random.nextInt(GameConstants.CHARACTER_START_Y - GameConstants.BALL_SIZE - 50); 
            direction = -1; // Bergerak ke kiri
        } else { 
            x = -GameConstants.BALL_SIZE; // Mulai dari sisi kiri 
            y = GameConstants.CHARACTER_START_Y + GameConstants.CHARACTER_SIZE + 50 +
                random.nextInt(GameConstants.WINDOW_HEIGHT - (GameConstants.CHARACTER_START_Y + GameConstants.CHARACTER_SIZE + 50) - GameConstants.BALL_SIZE);
            direction = 1; // Bergerak ke kanan
        }
        balls.add(new Ball(x, y, direction));
    }


    /**
     * Tangani klik mouse untuk lasso.
     */
    public void handleMouseClick(int mouseX, int mouseY) {
        if (!gameData.isGameRunning() || gameData.isGamePaused() || gameData.isGameOver() || lasso.isActive()) { // Tambahkan gameData.isGameOver()
            return;
        }

        // Mulai lasso dari karakter ke posisi mouse
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
        if (keyCode == 32) { 
            if (gameData.isGamePaused()) {
                gameData.startGame(); 
            } else {
                gameData.pauseGame(); 
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
     * Hentikan game dan simpan hasilnya.
     */
    public void stopGame() {
        gameData.stopGame();
        
        
        if (gameData.getCurrentUsername() != null && !gameData.getCurrentUsername().trim().isEmpty()) {
            Database.insertOrUpdatePlayer(
                gameData.getCurrentUsername(),
                gameData.getCurrentScore(),
                gameData.getCurrentCount()
            );
        }
    }

    /**
     * Jeda game.
     */
    public void pauseGame() {
        gamePaused = true;
        gameData.pauseGame();
    }

    /**
     * Lanjutkan game.
     */
    public void resumeGame() {
        gamePaused = false;
        gameData.startGame();
    }

    /**
     * Setel ulang game.
     */
    public void resetGame() {
        gameData.resetGame(); 
        initializeGameElements(); 
        ballSpawnTimer = 0; 
        keyPressed = new boolean[256]; 
        currentScore = 0; 
        timeRemaining = GameConstants.INITIAL_TIME; 
        gamePaused = false; 
    }

    /**
     * Simpan hasil game ke database.
     */
    public void saveGameResult(String username, int score, int count) {
        if (username != null && !username.trim().isEmpty() && score >= 0) { 
            Database.insertOrUpdatePlayer(username.trim(), score, count);
            System.out.println("Hasil game disimpan: Username = " + username + ", Skor = " + score + ", Jumlah = " + count);
        } else {
            System.err.println("Gagal menyimpan hasil game: Username atau skor tidak valid.");
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
    
    // Getters untuk pergerakan karakter
    public boolean isCharacterMovingHorizontally() { return isMovingHorizontally; }
    public boolean isCharacterMovingVertically() { return isMovingVertically; }
}