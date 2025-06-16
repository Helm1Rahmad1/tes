package view.components;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import utils.GameConstants;

/**
 * Character - Komponen visual untuk karakter utama game dengan sprite animation
 * Bagian dari View layer dalam MVVM pattern
 */
public class Character {
    private int x, y;
    private int targetX, targetY;
    private boolean moving;
    
    // Animation variables
    private BufferedImage[] downSprites = new BufferedImage[4];   // Frames 1-4 (down)
    private BufferedImage[] upSprites = new BufferedImage[4];     // Frames 5-8 (up)
    private BufferedImage[] leftSprites = new BufferedImage[4];   // Frames 9-12 (left)
    private BufferedImage[] rightSprites = new BufferedImage[4];  // Frames 13-16 (right)
    
    private int currentFrame = 0;
    private int animationCounter = 0;
    private static final int ANIMATION_SPEED = 8; // Controls animation speed
    
    // Direction constants
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
     * Load all character sprites
     */
    private void loadSprites() {
        try {
            // Load down sprites (frames 1-4)
            downSprites[0] = ImageIO.read(getClass().getResource("/assets/images/Character/MainCharacter_LW_F1.png"));
            downSprites[1] = ImageIO.read(getClass().getResource("/assets/images/Character/MainCharacter_LW_F2.png"));
            downSprites[2] = ImageIO.read(getClass().getResource("/assets/images/Character/MainCharacter_LW_F3.png"));
            downSprites[3] = ImageIO.read(getClass().getResource("/assets/images/Character/MainCharacter_LW_F4.png"));
            
            // Load up sprites (frames 5-8)
            upSprites[0] = ImageIO.read(getClass().getResource("/assets/images/Character/MainCharacter_LW_F5.png"));
            upSprites[1] = ImageIO.read(getClass().getResource("/assets/images/Character/MainCharacter_LW_F6.png"));
            upSprites[2] = ImageIO.read(getClass().getResource("/assets/images/Character/MainCharacter_LW_F7.png"));
            upSprites[3] = ImageIO.read(getClass().getResource("/assets/images/Character/MainCharacter_LW_F8.png"));
            
            // Load left sprites (frames 9-12)
            leftSprites[0] = ImageIO.read(getClass().getResource("/assets/images/Character/MainCharacter_LW_F9.png"));
            leftSprites[1] = ImageIO.read(getClass().getResource("/assets/images/Character/MainCharacter_LW_F10.png"));
            leftSprites[2] = ImageIO.read(getClass().getResource("/assets/images/Character/MainCharacter_LW_F11.png"));
            leftSprites[3] = ImageIO.read(getClass().getResource("/assets/images/Character/MainCharacter_LW_F12.png"));
            
            // Load right sprites (frames 13-16)
            rightSprites[0] = ImageIO.read(getClass().getResource("/assets/images/Character/MainCharacter_LW_F13.png"));
            rightSprites[1] = ImageIO.read(getClass().getResource("/assets/images/Character/MainCharacter_LW_F14.png"));
            rightSprites[2] = ImageIO.read(getClass().getResource("/assets/images/Character/MainCharacter_LW_F15.png"));
            rightSprites[3] = ImageIO.read(getClass().getResource("/assets/images/Character/MainCharacter_LW_F16.png"));
            
            System.out.println("Character sprites loaded successfully!");
            
        } catch (Exception e) {
            System.err.println("Error loading character sprites: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Update posisi karakter dengan smooth movement dan animasi
     */
    public void update() {
        // Always update animation counter for smooth animation
        updateAnimation();
        
        if (moving) {
            // Smooth movement towards target
            int dx = targetX - x;
            int dy = targetY - y;
            
            // Determine movement direction for animation
            if (Math.abs(dx) > Math.abs(dy)) {
                // Horizontal movement is dominant
                if (dx > 0) {
                    currentDirection = DIRECTION_RIGHT;
                } else {
                    currentDirection = DIRECTION_LEFT;
                }
            } else if (Math.abs(dy) > 0) {
                // Vertical movement is dominant
                if (dy > 0) {
                    currentDirection = DIRECTION_DOWN;
                } else {
                    currentDirection = DIRECTION_UP;
                }
            }
            
            // Smooth movement with smaller steps
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
            
            // Stop moving when target reached
            if (x == targetX && y == targetY) {
                moving = false;
                lastDirection = currentDirection; // Store last direction
            }
        } else {
            // When not moving, use the last direction for idle animation
            currentDirection = lastDirection;
            currentFrame = 0; // Use first frame as idle
        }
    }

    /**
     * Update animation frame
     */
    private void updateAnimation() {
        if (moving) {
            // Only animate when actually moving
            animationCounter++;
            if (animationCounter >= ANIMATION_SPEED) {
                currentFrame = (currentFrame + 1) % 4; // Cycle through 4 frames
                animationCounter = 0;
            }
        } else {
            // Reset animation when not moving
            currentFrame = 0;
            animationCounter = 0;
        }
    }

    /**
     * Move karakter ke arah tertentu dengan direct movement
     */
    public void moveDirectly(int dx, int dy) {
        int newX = x + dx;
        int newY = y + dy;

        // Boundary checking
        newX = Math.max(0, Math.min(GameConstants.WINDOW_WIDTH - GameConstants.CHARACTER_SIZE, newX));
        newY = Math.max(0, Math.min(GameConstants.WINDOW_HEIGHT - GameConstants.CHARACTER_SIZE, newY));

        // Set position directly for smoother movement
        x = newX;
        y = newY;
        targetX = x;
        targetY = y;
        
        // Set direction for animation
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
        
        // Set moving state for animation
        moving = (dx != 0 || dy != 0);
        if (moving) {
            lastDirection = currentDirection;
        }
    }

    /**
     * Set target position untuk smooth movement
     */
    public void setTarget(int targetX, int targetY) {
        System.out.println("Setting target position: (" + targetX + ", " + targetY + ")");
        this.targetX = targetX;
        this.targetY = targetY;
        this.moving = true;
        
        // Store the last direction when starting to move
        if (!moving) {
            lastDirection = currentDirection;
        }
    }

    /**
     * Move karakter langsung ke posisi tertentu
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
     * Render karakter dengan sprite animation
     */
    public void render(Graphics2D g2d) {
        BufferedImage currentSprite = getCurrentSprite();
        
        if (currentSprite != null) {
            // Draw the sprite at the character's position
            g2d.drawImage(currentSprite, x, y, GameConstants.CHARACTER_SIZE, GameConstants.CHARACTER_SIZE, null);
        } else {
            // Fallback to simple rectangle if sprites fail to load
            g2d.setColor(GameConstants.CHARACTER_COLOR);
            g2d.fillRect(x, y, GameConstants.CHARACTER_SIZE, GameConstants.CHARACTER_SIZE);
        }
    }

    /**
     * Get current sprite based on direction and animation frame
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
                    return downSprites[0]; // Default to down facing
            }
        } catch (Exception e) {
            System.err.println("Error getting current sprite: " + e.getMessage());
            return null;
        }
    }

    /**
     * Get collision bounds
     */
    public Rectangle getBounds() {
        return new Rectangle(x, y, GameConstants.CHARACTER_SIZE, GameConstants.CHARACTER_SIZE);
    }

    /**
     * Get center point of character
     */
    public int getCenterX() {
        return x + GameConstants.CHARACTER_SIZE / 2;
    }

    public int getCenterY() {
        return y + GameConstants.CHARACTER_SIZE / 2;
    }

    // Getters
    public int getX() { return x; }
    public int getY() { return y; }
    public boolean isMoving() { return moving; }

    // Reset character to starting position
    public void reset() {
        setPosition(GameConstants.CHARACTER_START_X, GameConstants.CHARACTER_START_Y);
        currentDirection = DIRECTION_DOWN;
        lastDirection = DIRECTION_DOWN;
        currentFrame = 0;
        animationCounter = 0;
    }

    public void move(int i, int j) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'move'");
    }
}