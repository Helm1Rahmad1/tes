package utils;

import java.awt.Color;

/**
 * Berisi konstanta untuk konfigurasi game
 * Mendefinisikan dimensi jendela, pengaturan karakter, bola, lasso, keranjang,
 * warna, mekanisme permainan, dan pengaturan spawn bola
 */
public class GameConstants {
    // Dimensi jendela
    public static final int WINDOW_WIDTH = 1400; 
    public static final int WINDOW_HEIGHT = 900; 
    public static final int GAME_AREA_HEIGHT = 500; 
    
    // Pengaturan karakter
    public static final int CHARACTER_SIZE = 64; 
    public static final int CHARACTER_SPEED = 5; 
    public static final int CHARACTER_START_X = WINDOW_WIDTH / 2; 
    public static final int CHARACTER_START_Y = WINDOW_HEIGHT / 2; 
    
    // Pengaturan bola
    public static final int BALL_SIZE = 40; 
    public static final int BALL_SPEED = 1; 
    public static final int MAX_BALLS = 20; 
    public static final int INITIAL_BALL_COUNT = 5; 
    
    // Pengaturan lasso
    public static final int LASSO_SPEED = 8; 
    
    // Pengaturan keranjang
    public static final int BASKET_WIDTH = 80; 
    public static final int BASKET_HEIGHT = 40; 
    public static final int BASKET_X = WINDOW_WIDTH - BASKET_WIDTH - 20; 
    public static final int BASKET_Y = WINDOW_HEIGHT - BASKET_HEIGHT - 100; 

    // Warna
    public static final Color CHARACTER_COLOR = Color.RED; 
    
    // Mekanisme permainan
    public static final int FRAME_RATE = 60; 
    public static final int FRAME_DELAY = 1000 / FRAME_RATE; 
    
    // Pengaturan spawn bola
    public static final int BALL_SPAWN_DELAY = 100; 
    public static final int INITIAL_TIME = 60; 
    public static final int[] BALL_VALUES = {
        10, 10, 10, 10, 20, 20, 20, 30, 30, 40, 50, 60, 70, 80, 90 
    };
}