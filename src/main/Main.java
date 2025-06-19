package main;

import view.MainMenuView;
import model.Database;

/**
 *  Saya Muhammad Helmi Rahmadi mengerjakan evaluasi Tugas Masa Depan dalam mata kuliah
    Desain dan Pemrograman Berorientasi Objek untuk keberkahanNya maka saya
    tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.
 *
  
 * Main class - Entry point aplikasi Collect The Skill Balls
 * Menggunakan konsep MVVM (Model-View-ViewModel)
 */

public class Main {
    public static void main(String[] args) {
        try {
            // Inisialisasi database
            // Memastikan bahwa koneksi ke database atau setup awal database dilakukan sebelum aplikasi berjalan
            Database.initializeDatabase();
            
            // Memulai aplikasi dengan menampilkan menu utama
            // Menggunakan SwingUtilities.invokeLater untuk memastikan GUI berjalan di thread event-dispatching
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




