package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Player;

/**
 * Untuk operasi database MySQL
 * Menggunakan pola Singleton untuk mengelola koneksi database
 * Bertanggung jawab untuk inisialisasi database, pembuatan tabel,
 * penyisipan/pembaruan data pemain, dan pengambilan data skor
 */
public class Database {
    // Konfigurasi database MySQL
    private static final String DB_HOST = "localhost"; 
    private static final String DB_PORT = "3306"; 
    private static final String DB_NAME = "game_scores_db"; 
    private static final String DB_USERNAME = "root"; 
    private static final String DB_PASSWORD = ""; 

    private static final String DB_URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME 
                                        + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    
    private static Connection connection;

    /**
     * Inisialisasi database dan buat tabel jika belum ada
     */
    public static synchronized void initializeDatabase() {
        if (connection != null) {
            return; 
        }

        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            System.out.println("Mencoba koneksi ke MySQL...");
            System.out.println("URL: " + DB_URL);
            System.out.println("Username: " + DB_USERNAME);
            
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            createTableIfNotExists(); 
            insertSampleData(); 
            
            System.out.println("Database MySQL berhasil diinisialisasi");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver tidak ditemukan: " + e.getMessage());
            System.err.println("Pastikan MySQL Connector/J sudah ditambahkan ke classpath");
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
            System.err.println("Kemungkinan penyebab:");
            System.err.println("1. MySQL server tidak berjalan");
            System.err.println("2. Username/password salah");
            System.err.println("3. Database '" + DB_NAME + "' belum dibuat");
            System.err.println("4. Permission denied untuk user");
        }
    }

    /**
     * Buat tabel thasil jika belum ada
     */
    private static void createTableIfNotExists() throws SQLException {
        // SQL untuk membuat tabel jika belum ada
        String sql = "CREATE TABLE IF NOT EXISTS thasil (" +
                     "id INT AUTO_INCREMENT PRIMARY KEY, " +
                     "username VARCHAR(50) NOT NULL UNIQUE, " +
                     "skor INT NOT NULL DEFAULT 0, " +
                     "count INT NOT NULL DEFAULT 0, " +
                     "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                     "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP" +
                     ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
            System.out.println("Tabel 'thasil' berhasil dibuat atau sudah ada");
        }
    }

    /**
     * Insert data contoh untuk demo
     */
    private static void insertSampleData() throws SQLException {
        String checkSql = "SELECT COUNT(*) FROM thasil";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(checkSql)) {

            if (rs.next() && rs.getInt(1) == 0) {
                // Insert data contoh jika tabel kosong
                insertOrUpdatePlayer("NalarJalan", 1000, 100);
                insertOrUpdatePlayer("UseYourLogic", 800, 80);
                insertOrUpdatePlayer("NoJudgement", 700, 40);
                insertOrUpdatePlayer("EkoPrasetyo", 950, 95);
                insertOrUpdatePlayer("Fitriani", 880, 85);
                insertOrUpdatePlayer("GatotKaca", 750, 70);
                insertOrUpdatePlayer("DewiSartika", 650, 60);
                insertOrUpdatePlayer("Cahya", 500, 50);
                insertOrUpdatePlayer("BayuSamudra", 450, 45);
                insertOrUpdatePlayer("AnisaRahma", 400, 40);
                System.out.println("Sample data berhasil diinsert");
            }
        }
    }

    /**
     * Insert player baru atau update jika sudah ada
     */
    public static void insertOrUpdatePlayer(String username, int score, int count) {
        if (connection == null) {
            System.err.println("Database connection is not initialized.");
            return;
        }

        String selectSql = "SELECT skor, count FROM thasil WHERE username = ?";
        String updateSql = "UPDATE thasil SET skor = ?, count = ?, updated_at = CURRENT_TIMESTAMP WHERE username = ?";
        String insertSql = "INSERT INTO thasil (username, skor, count) VALUES (?, ?, ?)";

        try (PreparedStatement selectStmt = connection.prepareStatement(selectSql)) {
            selectStmt.setString(1, username);
            try (ResultSet rs = selectStmt.executeQuery()) {
                if (rs.next()) {
                    // Player sudah ada, update dengan nilai baru
                    int existingScore = rs.getInt("skor");
                    int existingCount = rs.getInt("count");

                    try (PreparedStatement updateStmt = connection.prepareStatement(updateSql)) {
                        updateStmt.setInt(1, existingScore + score);
                        updateStmt.setInt(2, existingCount + count);
                        updateStmt.setString(3, username);
                        int rowsAffected = updateStmt.executeUpdate();
                        if (rowsAffected > 0) {
                            System.out.println("Player " + username + " berhasil diupdate");
                        }
                    }
                } else {
                    // Player belum ada, insert baru
                    try (PreparedStatement insertStmt = connection.prepareStatement(insertSql)) {
                        insertStmt.setString(1, username);
                        insertStmt.setInt(2, score);
                        insertStmt.setInt(3, count);
                        int rowsAffected = insertStmt.executeUpdate();
                        if (rowsAffected > 0) {
                            System.out.println("Player " + username + " berhasil ditambahkan");
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error inserting/updating player: " + e.getMessage());
        }
    }

    /**
     * Get semua player, diurutkan berdasarkan skor (descending)
     */
    public static List<Player> getAllPlayers() {
        List<Player> players = new ArrayList<>();
        if (connection == null) {
            System.err.println("Database connection is not initialized.");
            return players;
        }

        String sql = "SELECT username, skor, count FROM thasil ORDER BY skor DESC";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                players.add(new Player(
                    rs.getString("username"),
                    rs.getInt("skor"),
                    rs.getInt("count")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all players: " + e.getMessage());
        }

        return players;
    }

    /**
     * Get player berdasarkan username
     */
    public static Player getPlayerByUsername(String username) {
        if (connection == null) {
            System.err.println("Database connection is not initialized.");
            return null;
        }

        String sql = "SELECT username, skor, count FROM thasil WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Player(
                        rs.getString("username"),
                        rs.getInt("skor"),
                        rs.getInt("count")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting player by username: " + e.getMessage());
        }
        return null;
    }

    /**
     * Test koneksi database
     */
    public static boolean testConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                try (Statement stmt = connection.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT 1")) {
                    return rs.next();
                }
            }
        } catch (SQLException e) {
            System.err.println("Connection test failed: " + e.getMessage());
        }
        return false;
    }

    /**
     * Tutup koneksi database
     */
    public static void closeConnection() {
        if (connection == null) {
            return; // Tidak ada koneksi untuk ditutup
        }

        try {
            if (!connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed");
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        } finally {
            connection = null;
        }
    }
}