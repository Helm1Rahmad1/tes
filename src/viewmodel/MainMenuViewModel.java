package viewmodel;

import model.Player;
import model.Database;
import model.GameData;

import java.util.List;

/**
 * MainMenuViewModel - ViewModel untuk main menu
 * Menghandle logic dan data untuk main menu view
 * Bagian dari ViewModel layer dalam MVVM pattern
 */
public class MainMenuViewModel {
    private GameData gameData;
    private List<Player> players;

    public MainMenuViewModel() {
        this.gameData = new GameData();
        loadPlayers();
    }

    /**
     * Load semua players dari database
     */
    public void loadPlayers() {
        this.players = Database.getAllPlayers();
    }

    /**
     * Get list of all players
     */
    public List<Player> getPlayers() {
        return players;
    }

    
    public boolean startGame(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        
        gameData.setCurrentUsername(username.trim());
        gameData.resetGame();
        return true;
    }

    /**
     * Get current game data
     */
    public GameData getGameData() {
        return gameData;
    }

    /**
     * Menyimpan hasil game ke database.
     * @param username Username pemain.
     * @param score Skor yang diperoleh.
     * @param count Jumlah bola yang ditangkap.
     */
    public void saveGameResult(String username, int score, int count) {
        if (username != null && !username.trim().isEmpty() && score > 0) {
            Database.insertOrUpdatePlayer(username.trim(), score, count);
            loadPlayers(); // Refresh data pemain
        }
    }

    /**
     * Get player by username
     */
    public Player getPlayerByUsername(String username) {
        if (players == null) return null;
        
        return players.stream()
                .filter(player -> player.getUsername().equalsIgnoreCase(username))
                .findFirst()
                .orElse(null);
    }

    /**
     * Check if username exists in database
     */
    public boolean usernameExists(String username) {
        return getPlayerByUsername(username) != null;
    }

    /**
     * Get total number of players
     */
    public int getTotalPlayers() {
        return players != null ? players.size() : 0;
    }

    /**
     * Get top player (highest score)
     */
    public Player getTopPlayer() {
        if (players == null || players.isEmpty()) {
            return null;
        }
        return players.get(0); // Already sorted by score DESC
    }

    /**
     * Refresh data from database
     */
    public void refreshData() {
        loadPlayers();
    }
}