package viewmodel;

import model.Player;
import model.Database;
import model.GameData;

import java.util.List;

/**
 * MainMenuViewModel - ViewModel untuk main menu
 * Mengelola logika dan data untuk tampilan main menu
 * Bagian dari lapisan ViewModel dalam pola MVVM
 */
public class MainMenuViewModel {
    private GameData gameData;
    private List<Player> players;

    public MainMenuViewModel() {
        this.gameData = new GameData();
        loadPlayers();
    }

    /**
     * Memuat semua pemain dari database
     */
    public void loadPlayers() {
        this.players = Database.getAllPlayers();
    }

    /**
     * Mendapatkan daftar semua pemain
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Memulai permainan dengan username tertentu
     */
    public boolean startGame(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        
        gameData.setCurrentUsername(username.trim());
        gameData.resetGame();
        return true;
    }

    /**
     * Mendapatkan data permainan saat ini
     */
    public GameData getGameData() {
        return gameData;
    }

    /**
     * Menyimpan hasil permainan ke database.
     * @param username Username pemain.
     * @param score Skor yang diperoleh.
     * @param count Jumlah bola yang ditangkap.
     */
    public void saveGameResult(String username, int score, int count) {
        if (username != null && !username.trim().isEmpty() && score > 0) {
            Database.insertOrUpdatePlayer(username.trim(), score, count);
            loadPlayers(); 
        }
    }

    /**
     * Mendapatkan pemain berdasarkan username
     */
    public Player getPlayerByUsername(String username) {
        if (players == null) return null;
        
        return players.stream()
                .filter(player -> player.getUsername().equalsIgnoreCase(username))
                .findFirst()
                .orElse(null);
    }

    /**
     * Memeriksa apakah username sudah ada di database
     */
    public boolean usernameExists(String username) {
        return getPlayerByUsername(username) != null;
    }

    /**
     * Mendapatkan total jumlah pemain
     */
    public int getTotalPlayers() {
        return players != null ? players.size() : 0;
    }

    /**
     * Mendapatkan pemain dengan skor tertinggi
     */
    public Player getTopPlayer() {
        if (players == null || players.isEmpty()) {
            return null;
        }
        return players.get(0); 
    }

    /**
     * Memperbarui data dari database
     */
    public void refreshData() {
        loadPlayers();
    }
}