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
            // Initialize database
            Database.initializeDatabase();
            
            // Start application with main menu
            javax.swing.SwingUtilities.invokeLater(() -> {
                new MainMenuView().setVisible(true);
            });
            
        } catch (Exception e) {
            System.err.println("Error starting application: " + e.getMessage());
            e.printStackTrace();
        }
    }
}



//      * Assets Character - https://opengameart.org/content/witchmagicianmagemagi#
//      * Assets Lasso - https://opengameart.org/content/icicle-spell

//      * Assets Ball Cristal - https://opengameart.org/content/gem-jewel-diamond-glass

//      * Assets Basket - 
//      * Assets Cloud - 
//      * Assets Background - 
//      * Assets Sound Effects Menu - https://opengameart.org/content/magic-space
//      * Assets Music Game - https://opengameart.org/content/mystical-caverns
//      * Assets Music Menu - 
//      * Assets Font - 