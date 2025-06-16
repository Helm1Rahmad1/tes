package utils;

import java.awt.Color;

/**
 * GameConstants - Konstanta untuk konfigurasi game
 */
public class GameConstants {
    // Window dimensions
    public static final int WINDOW_WIDTH = 1400;
    public static final int WINDOW_HEIGHT = 900;
    public static final int GAME_AREA_HEIGHT = 500;
    
    
    // Character settings
    public static final int CHARACTER_SIZE = 64; // Increased size
    public static final int CHARACTER_SPEED = 5;
    public static final int CHARACTER_START_X = WINDOW_WIDTH / 2;
    public static final int CHARACTER_START_Y = WINDOW_HEIGHT / 2;
    
    // Ball settings
    public static final int BALL_SIZE = 40; // Increased size
    public static final int BALL_SPEED = 1; // Reduced speed
    public static final int MAX_BALLS = 20;
    public static final int INITIAL_BALL_COUNT = 5; // Number of balls at the start
    
    // Lasso settings
    public static final int LASSO_MAX_LENGTH = 100;
    public static final int LASSO_SPEED = 8;
    
    // Basket settings
    public static final int BASKET_WIDTH = 80;
    public static final int BASKET_HEIGHT = 40;
    public static final int BASKET_X = WINDOW_WIDTH - BASKET_WIDTH - 20; // Adjust if needed
    public static final int BASKET_Y = WINDOW_HEIGHT - BASKET_HEIGHT - 100; // Adjust if needed

    
    // Colors
    public static final Color BACKGROUND_COLOR = new Color(135, 206, 235); // Sky blue
    public static final Color CHARACTER_COLOR = Color.RED;
    public static final Color BALL_COLOR = Color.YELLOW;
    public static final Color BASKET_COLOR = new Color(139, 69, 19); // Brown
    public static final Color LASSO_COLOR = new Color(160, 82, 45); // Saddle brown
    public static final Color SCORE_COLOR = Color.BLACK;
    
    // Game mechanics
    public static final int FRAME_RATE = 60; // Frames per second
    public static final int FRAME_DELAY = 1000 / FRAME_RATE; // Delay per frame in milliseconds
    
    // Ball spawn settings
    public static final int BALL_SPAWN_DELAY = 100; // Delay between ball spawns
    public static final int INITIAL_TIME = 60; // Initial game time in seconds
    public static final int[] BALL_VALUES = {
        10, 20, 30, 50, 70, 90, 100 // Consistent values for gems
    };
    
    // Game state
    public static boolean GAME_OVER = false;
}