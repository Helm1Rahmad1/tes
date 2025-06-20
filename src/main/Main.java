package main;

import view.MainMenuView;
import model.Database;

/**
 *  Saya Muhammad Helmi Rahmadi dengan NIM 2311574 mengerjakan evaluasi Tugas Masa Depan dalam mata kuliah
    Desain dan Pemrograman Berorientasi Objek untuk keberkahanNya maka saya
    tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.
*
  
/**
 * Titik masuk utama (entry point) untuk aplikasi
 * permainan "Collect The Skill Balls". Kelas ini bertanggung jawab untuk
 * menginisialisasi database dan memulai tampilan menu utama permainan
 */
public class Main {
    public static void main(String[] args) {
        try {
            // Inisialisasi database
            Database.initializeDatabase();
            
            // Memulai aplikasi dengan menampilkan menu utama
            javax.swing.SwingUtilities.invokeLater(() -> {
                new MainMenuView().setVisible(true);
            });
            
        } catch (Exception e) {
            // Menangkap dan menampilkan error jika terjadi masalah saat memulai aplikasi
            System.err.println("Error starting application: " + e.getMessage());
            e.printStackTrace();
        }
    }
}




