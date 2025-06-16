package utils;

import java.awt.Color;

/**
 * GameConstants - Konstanta untuk konfigurasi game
 */
public class GameConstants {
    // Dimensi jendela
    public static final int WINDOW_WIDTH = 1400; // Lebar jendela
    public static final int WINDOW_HEIGHT = 900; // Tinggi jendela
    public static final int GAME_AREA_HEIGHT = 500; // Tinggi area permainan
    
    // Pengaturan karakter
    public static final int CHARACTER_SIZE = 64; // Ukuran karakter
    public static final int CHARACTER_SPEED = 5; // Kecepatan karakter
    public static final int CHARACTER_START_X = WINDOW_WIDTH / 2; // Posisi awal karakter (X)
    public static final int CHARACTER_START_Y = WINDOW_HEIGHT / 2; // Posisi awal karakter (Y)
    
    // Pengaturan bola
    public static final int BALL_SIZE = 40; // Ukuran bola
    public static final int BALL_SPEED = 1; // Kecepatan bola
    public static final int MAX_BALLS = 20; // Jumlah maksimum bola
    public static final int INITIAL_BALL_COUNT = 5; // Jumlah bola saat permainan dimulai
    
    // Pengaturan lasso
    public static final int LASSO_SPEED = 8; // Kecepatan lasso
    
    // Pengaturan keranjang
    public static final int BASKET_WIDTH = 80; // Lebar keranjang
    public static final int BASKET_HEIGHT = 40; // Tinggi keranjang
    public static final int BASKET_X = WINDOW_WIDTH - BASKET_WIDTH - 20; // Posisi keranjang (X)
    public static final int BASKET_Y = WINDOW_HEIGHT - BASKET_HEIGHT - 100; // Posisi keranjang (Y)

    // Warna
    public static final Color CHARACTER_COLOR = Color.RED; // Warna karakter
    
    // Mekanisme permainan
    public static final int FRAME_RATE = 60; // Frame per detik
    public static final int FRAME_DELAY = 1000 / FRAME_RATE; // Delay per frame dalam milidetik
    
    // Pengaturan spawn bola
    public static final int BALL_SPAWN_DELAY = 100; // Delay antar bola yang muncul
    public static final int INITIAL_TIME = 60; // Waktu awal permainan dalam detik
    public static final int[] BALL_VALUES = {
        10, 10, 10, 10, 20, 20, 20, 30, 30, 40, 50, 60, 70, 80, 90 // Nilai bola (gems)
    };
}