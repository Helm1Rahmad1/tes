package view.components;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import utils.GameConstants;
import utils.AssetLoader; // Tambahkan import ini

/**
 * Merepresentasikan komponen visual untuk karakter utama game.
 * Bertanggung jawab untuk mengelola posisi, pergerakan, dan animasi sprite karakter.
 */
public class Character {
    private int x, y;
    private int targetX, targetY;
    private boolean moving;
    
    // Variabel animasi
    private BufferedImage[] downSprites = new BufferedImage[4];  
    private BufferedImage[] upSprites = new BufferedImage[4];    
    private BufferedImage[] leftSprites = new BufferedImage[4];   
    private BufferedImage[] rightSprites = new BufferedImage[4];  
    
    private int currentFrame = 0;
    private int animationCounter = 0;
    private static final int ANIMATION_SPEED = 8; // Mengontrol kecepatan animasi
    
    // Konstanta arah
    private static final int DIRECTION_DOWN = 0;
    private static final int DIRECTION_UP = 1;
    private static final int DIRECTION_LEFT = 2;
    private static final int DIRECTION_RIGHT = 3;
    
    private int currentDirection = DIRECTION_DOWN;
    private int lastDirection = DIRECTION_DOWN;

    public Character() {
        this.x = GameConstants.CHARACTER_START_X;
        this.y = GameConstants.CHARACTER_START_Y;
        this.targetX = x;
        this.targetY = y;
        this.moving = false;
        
        loadSprites();
    }

    /**
     * Memuat semua sprite karakter
     */
    private void loadSprites() {
        // Ganti string literal dengan konstanta dari AssetLoader
        downSprites[0] = AssetLoader.loadImage(AssetLoader.CHAR_DOWN_F1);
        downSprites[1] = AssetLoader.loadImage(AssetLoader.CHAR_DOWN_F2);
        downSprites[2] = AssetLoader.loadImage(AssetLoader.CHAR_DOWN_F3);
        downSprites[3] = AssetLoader.loadImage(AssetLoader.CHAR_DOWN_F4);
        
        upSprites[0] = AssetLoader.loadImage(AssetLoader.CHAR_UP_F1);
        upSprites[1] = AssetLoader.loadImage(AssetLoader.CHAR_UP_F2);
        upSprites[2] = AssetLoader.loadImage(AssetLoader.CHAR_UP_F3);
        upSprites[3] = AssetLoader.loadImage(AssetLoader.CHAR_UP_F4);
        
        leftSprites[0] = AssetLoader.loadImage(AssetLoader.CHAR_LEFT_F1);
        leftSprites[1] = AssetLoader.loadImage(AssetLoader.CHAR_LEFT_F2);
        leftSprites[2] = AssetLoader.loadImage(AssetLoader.CHAR_LEFT_F3);
        leftSprites[3] = AssetLoader.loadImage(AssetLoader.CHAR_LEFT_F4);
        
        rightSprites[0] = AssetLoader.loadImage(AssetLoader.CHAR_RIGHT_F1);
        rightSprites[1] = AssetLoader.loadImage(AssetLoader.CHAR_RIGHT_F2);
        rightSprites[2] = AssetLoader.loadImage(AssetLoader.CHAR_RIGHT_F3);
        rightSprites[3] = AssetLoader.loadImage(AssetLoader.CHAR_RIGHT_F4);
        
        boolean allLoaded = true;
        for (BufferedImage sprite : downSprites) if (sprite == null) allLoaded = false;
        for (BufferedImage sprite : upSprites) if (sprite == null) allLoaded = false;
        for (BufferedImage sprite : leftSprites) if (sprite == null) allLoaded = false;
        for (BufferedImage sprite : rightSprites) if (sprite == null) allLoaded = false;

        if (allLoaded) {
            System.out.println("Sprite karakter berhasil dimuat!");
        } else {
            System.err.println("Error: Beberapa sprite karakter gagal dimuat.");
        }
    }

    /**
     * Memperbarui posisi karakter dengan pergerakan halus dan animasi
     */
    public void update() {
        // Selalu perbarui penghitung animasi untuk animasi yang halus
        updateAnimation();
        
        if (moving) {
            // Pergerakan halus menuju target
            int dx = targetX - x;
            int dy = targetY - y;
            
            // Menentukan arah pergerakan untuk animasi
            if (Math.abs(dx) > Math.abs(dy)) {
                // Pergerakan horizontal lebih dominan
                if (dx > 0) {
                    currentDirection = DIRECTION_RIGHT;
                } else {
                    currentDirection = DIRECTION_LEFT;
                }
            } else if (Math.abs(dy) > 0) {
                // Pergerakan vertikal lebih dominan
                if (dy > 0) {
                    currentDirection = DIRECTION_DOWN;
                } else {
                    currentDirection = DIRECTION_UP;
                }
            }
            
            // Pergerakan halus dengan langkah yang lebih kecil
            int moveSpeed = GameConstants.CHARACTER_SPEED;
            
            if (Math.abs(dx) > moveSpeed) {
                x += dx > 0 ? moveSpeed : -moveSpeed;
            } else if (dx != 0) {
                x = targetX;
            }
            
            if (Math.abs(dy) > moveSpeed) {
                y += dy > 0 ? moveSpeed : -moveSpeed;
            } else if (dy != 0) {
                y = targetY;
            }
            
            // Berhenti bergerak saat target tercapai
            if (x == targetX && y == targetY) {
                moving = false;
                lastDirection = currentDirection; // Simpan arah terakhir
            }
        } else {
            // Saat tidak bergerak, gunakan arah terakhir untuk animasi diam
            currentDirection = lastDirection;
            currentFrame = 0; // Gunakan frame pertama sebagai diam
        }
    }

    /**
     * Memperbarui frame animasi karakter
     */
    private void updateAnimation() {
        if (moving) {
            // Animasi hanya berjalan saat karakter bergerak
            animationCounter++;
            if (animationCounter >= ANIMATION_SPEED) {
                currentFrame = (currentFrame + 1) % 4; 
                animationCounter = 0;
            }
        } else {
            // Reset animasi saat karakter tidak bergerak
            currentFrame = 0;
            animationCounter = 0;
        }
    }

    /**
     * Memindahkan karakter ke arah tertentu dengan pergerakan langsung
     */
    public void moveDirectly(int dx, int dy) {
        int newX = x + dx;
        int newY = y + dy;

        // Pemeriksaan batas
        newX = Math.max(0, Math.min(GameConstants.WINDOW_WIDTH - GameConstants.CHARACTER_SIZE, newX));
        newY = Math.max(0, Math.min(GameConstants.WINDOW_HEIGHT - GameConstants.CHARACTER_SIZE, newY));

        // Atur posisi langsung untuk pergerakan yang lebih halus
        x = newX;
        y = newY;
        targetX = x;
        targetY = y;
        
        // Atur arah untuk animasi
        if (Math.abs(dx) > Math.abs(dy)) {
            if (dx > 0) {
                currentDirection = DIRECTION_RIGHT;
            } else if (dx < 0) {
                currentDirection = DIRECTION_LEFT;
            }
        } else if (dy != 0) {
            if (dy > 0) {
                currentDirection = DIRECTION_DOWN;
            } else {
                currentDirection = DIRECTION_UP;
            }
        }
        
        // Atur status bergerak untuk animasi
        moving = (dx != 0 || dy != 0);
        if (moving) {
            lastDirection = currentDirection;
        }
    }

    /**
     * Mengatur posisi target untuk pergerakan halus
     */
    public void setTarget(int targetX, int targetY) {
        System.out.println("Mengatur posisi target: (" + targetX + ", " + targetY + ")");
        this.targetX = targetX;
        this.targetY = targetY;
        this.moving = true;
        
        // Simpan arah terakhir saat mulai bergerak
        if (!moving) {
            lastDirection = currentDirection;
        }
    }

    /**
     * Memindahkan karakter langsung ke posisi tertentu
     */
    public void setPosition(int x, int y) {
        this.x = Math.max(0, Math.min(GameConstants.WINDOW_WIDTH - GameConstants.CHARACTER_SIZE, x));
        this.y = Math.max(0, Math.min(GameConstants.GAME_AREA_HEIGHT - GameConstants.CHARACTER_SIZE, y));
        this.targetX = this.x;
        this.targetY = this.y;
        this.moving = false;
        this.currentFrame = 0;
    }

    /**
     * Merender karakter dengan animasi sprite
     * @param g2d Graphics2D untuk menggambar
     */
    public void render(Graphics2D g2d) {
        BufferedImage currentSprite = getCurrentSprite();
        
        if (currentSprite != null) {
            // Gambar sprite pada posisi karakter
            g2d.drawImage(currentSprite, x, y, GameConstants.CHARACTER_SIZE, GameConstants.CHARACTER_SIZE, null);
        } else {
            // Fallback ke persegi sederhana jika sprite gagal dimuat
            g2d.setColor(GameConstants.CHARACTER_COLOR);
            g2d.fillRect(x, y, GameConstants.CHARACTER_SIZE, GameConstants.CHARACTER_SIZE);
        }
    }

    /**
     * Mendapatkan sprite saat ini berdasarkan arah dan frame animasi
     */
    private BufferedImage getCurrentSprite() {
        try {
            switch (currentDirection) {
                case DIRECTION_DOWN:
                    return downSprites[currentFrame];
                case DIRECTION_UP:
                    return upSprites[currentFrame];
                case DIRECTION_LEFT:
                    return leftSprites[currentFrame];
                case DIRECTION_RIGHT:
                    return rightSprites[currentFrame];
                default:
                    return downSprites[0]; // Default ke menghadap bawah
            }
        } catch (Exception e) {
            System.err.println("Error mendapatkan sprite saat ini: " + e.getMessage());
            return null;
        }
    }

    /**
     * Mendapatkan batas tabrakan
     */
    public Rectangle getBounds() {
        return new Rectangle(x, y, GameConstants.CHARACTER_SIZE, GameConstants.CHARACTER_SIZE);
    }

    /**
     * Mendapatkan titik tengah karakter
     */
    public int getCenterX() {
        return x + GameConstants.CHARACTER_SIZE / 2;
    }

    public int getCenterY() {
        return y + GameConstants.CHARACTER_SIZE / 2;
    }

    // Getter
    public int getX() { return x; }
    public int getY() { return y; }
    public boolean isMoving() { return moving; }

    // Mengatur ulang karakter ke posisi awal
    public void reset() {
        setPosition(GameConstants.CHARACTER_START_X, GameConstants.CHARACTER_START_Y);
        currentDirection = DIRECTION_DOWN;
        lastDirection = DIRECTION_DOWN;
        currentFrame = 0;
        animationCounter = 0;
    }

}