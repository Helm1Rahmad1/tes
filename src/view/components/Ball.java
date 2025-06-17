package view.components;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.AlphaComposite;
import java.awt.RadialGradientPaint;
import java.awt.MultipleGradientPaint;
import java.awt.geom.Point2D;
import java.util.Random;
import utils.GameConstants;
import utils.AssetLoader;

/**
 * Ball - Komponen visual untuk bola dalam game dengan tema sihir
 * Bagian dari View layer dalam MVVM pattern
 */
public class Ball {
    private int x, y;
    private int value;
    private int direction; // 1 = left to right, -1 = right to left
    private Color color;
    private boolean active;
    private static Random random = new Random();
    private boolean transitioningToBasket = false;
    private boolean transitioningFromCharacterToBasket = false;
    private int basketX = GameConstants.BASKET_X + GameConstants.BASKET_WIDTH / 2 - GameConstants.BALL_SIZE / 2;
    private int basketY = GameConstants.BASKET_Y + GameConstants.BASKET_HEIGHT / 2 - GameConstants.BALL_SIZE / 2;

    // Magic effects
    private float glowIntensity = 0.0f;
    private boolean glowIncreasing = true;
    private int sparkleTimer = 0;
    private float rotation = 0.0f;
    private float pulseScale = 1.0f;
    private boolean pulseIncreasing = true;

    private static BufferedImage goldenGem, frostGem, purpleGem, rubyGem, cyanGem, sapphireGem, emeraldGem, orangeGem, bomb;

    static {
        goldenGem = AssetLoader.loadImage(AssetLoader.GOLDEN_GEM);
        frostGem = AssetLoader.loadImage(AssetLoader.FROST_GEM);
        purpleGem = AssetLoader.loadImage(AssetLoader.PURPLE_GEM);
        rubyGem = AssetLoader.loadImage(AssetLoader.RUBY_GEM);
        cyanGem = AssetLoader.loadImage(AssetLoader.CYAN_GEM);
        sapphireGem = AssetLoader.loadImage(AssetLoader.SAPPHIRE_GEM);
        emeraldGem = AssetLoader.loadImage(AssetLoader.EMERALD_GEM);
        orangeGem = AssetLoader.loadImage(AssetLoader.ORANGE_GEM);
        bomb = AssetLoader.loadImage(AssetLoader.BLACK_GEM);

        if (goldenGem == null || bomb == null) {
            System.err.println("Warning: Some essential gem images failed to load!");
        }
    }

    public Ball(int x, int y, int direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.value = GameConstants.BALL_VALUES[random.nextInt(GameConstants.BALL_VALUES.length)];
        this.active = true;
        
        this.color = generateColorByValue(value);
        this.glowIntensity = random.nextFloat() * 0.5f + 0.3f;
    }

    private Color generateColorByValue(int value) {
        switch (value) {
            case 10: return new Color(255, 69, 0);    // Red-Orange (bomb/danger)
            case 20: return new Color(34, 139, 34);   // Forest Green (low value)
            case 30: return new Color(30, 144, 255);  // Dodger Blue (medium-low)
            case 40: return new Color(138, 43, 226);  // Blue Violet (medium)
            case 50: return new Color(255, 140, 0);   // Dark Orange (medium-high)
            case 60: return new Color(220, 20, 60);   // Crimson (high value)
            case 70: return new Color(255, 215, 0);   // Gold (very high)
            case 80: return new Color(148, 0, 211);   // Dark Violet (premium)
            case 90: return new Color(255, 20, 147);  // Deep Pink (rare/special)
            default: return new Color(192, 192, 192); // Light Gray (neutral)
        }
    
    }

    private BufferedImage getGemImageByValue(int value) {
        switch (value) {
            case 10: return bomb; // Orange gem for 10 points
            case 20: return orangeGem; // Green gem
            case 30: return emeraldGem; // Blue gem
            case 40: return sapphireGem; // Cyan gem
            case 50: return cyanGem; // Red gem
            case 60: return rubyGem; // Frost gem
            case 70: return purpleGem; // Golden gem
            case 80: return frostGem; // Magical green gem
            case 90: return goldenGem; // Magical red gem
            default: return bomb; // Fallback to bomb image for undefined values
        }
    }

    public void update() {
        if (!active) return;

        // Update magical effects
        updateMagicalEffects();

        if (transitioningFromCharacterToBasket || transitioningToBasket) {
            int dx = basketX - x;
            int dy = basketY - y;
            double distance = Math.sqrt(dx * dx + dy * dy);

            if (distance > GameConstants.BALL_SPEED * 3) {
                double ratio = GameConstants.BALL_SPEED * 3 / distance;
                x += (int)(dx * ratio);
                y += (int)(dy * ratio);
            } else {
                x = basketX;
                y = basketY;
            }

            if (x == basketX && y == basketY) {
                active = false;
                transitioningFromCharacterToBasket = false;
                transitioningToBasket = false;
            }
        } else {
            x += direction * GameConstants.BALL_SPEED;

            // Periksa jika bola sudah keluar dari layar
            if (direction == 1 && x > GameConstants.WINDOW_WIDTH + GameConstants.BALL_SIZE) { // Bergerak ke kanan, keluar di kanan
                active = false;
            } else if (direction == -1 && x < -GameConstants.BALL_SIZE) { // Bergerak ke kiri, keluar di kiri
                active = false;
            }
        }
    }

    private void updateMagicalEffects() {
        // Glow effect
        if (glowIncreasing) {
            glowIntensity += 0.02f;
            if (glowIntensity >= 1.0f) {
                glowIntensity = 1.0f;
                glowIncreasing = false;
            }
        } else {
            glowIntensity -= 0.02f;
            if (glowIntensity <= 0.3f) {
                glowIntensity = 0.3f;
                glowIncreasing = true;
            }
        }

        // Rotation for magical spinning
        rotation += 2.0f;
        if (rotation >= 360.0f) {
            rotation = 0.0f;
        }

        // Pulse effect for high-value gems
        if (value >= 70) {
            if (pulseIncreasing) {
                pulseScale += 0.01f;
                if (pulseScale >= 1.2f) {
                    pulseScale = 1.2f;
                    pulseIncreasing = false;
                }
            } else {
                pulseScale -= 0.01f;
                if (pulseScale <= 1.0f) {
                    pulseScale = 1.0f;
                    pulseIncreasing = true;
                }
            }
        }

        sparkleTimer++;
    }

    public void render(Graphics2D g2d) {
        if (!active) return;
    
        Graphics2D g2dCopy = (Graphics2D) g2d.create();
        
        int ballSize = GameConstants.BALL_SIZE + (value == 100 ? 10 : 0);
        int renderSize = (int)(ballSize * pulseScale);
        int offsetX = (ballSize - renderSize) / 2;
        int offsetY = (ballSize - renderSize) / 2;
    
        // Draw magical glow effect
        drawMagicalGlow(g2dCopy, x + offsetX, y + offsetY, renderSize);
    
        // Draw sparkles for high-value gems
        if (value >= 50 && sparkleTimer % 10 == 0) {
            drawSparkles(g2dCopy, x + ballSize/2, y + ballSize/2);
        }
    
        // Apply rotation for magical effect
        if (value >= 70) {
            g2dCopy.rotate(Math.toRadians(rotation), x + ballSize/2.0, y + ballSize/2.0);
        }
    
        // Draw gem image
        BufferedImage gemImage = getGemImageByValue(value);
        if (gemImage != null) {
            // Add magical shimmer effect
            if (value >= 50) {
                g2dCopy.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f + glowIntensity * 0.2f));
            }
            g2dCopy.drawImage(gemImage, x + offsetX, y + offsetY, renderSize, renderSize, null);
            g2dCopy.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        } else {
            // Fallback magical circle
            drawMagicalCircle(g2dCopy, x + offsetX, y + offsetY, renderSize);
        }
    
        g2dCopy.dispose();
    }

    private void drawMagicalGlow(Graphics2D g2d, int centerX, int centerY, int size) {
        Color glowColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), 
                                   (int)(100 * glowIntensity));
        
        float[] fractions = {0.0f, 0.5f, 1.0f};
        Color[] colors = {
            new Color(glowColor.getRed(), glowColor.getGreen(), glowColor.getBlue(), 
                     (int)(150 * glowIntensity)),
            new Color(glowColor.getRed(), glowColor.getGreen(), glowColor.getBlue(), 
                     (int)(50 * glowIntensity)),
            new Color(glowColor.getRed(), glowColor.getGreen(), glowColor.getBlue(), 0)
        };

        RadialGradientPaint gradient = new RadialGradientPaint(
            new Point2D.Float(centerX + size/2, centerY + size/2),
            size/2 + 20,
            fractions, colors,
            MultipleGradientPaint.CycleMethod.NO_CYCLE
        );

        g2d.setPaint(gradient);
        g2d.fillOval(centerX - 10, centerY - 10, size + 20, size + 20);
    }

    private void drawSparkles(Graphics2D g2d, int centerX, int centerY) {
        g2d.setColor(Color.WHITE);
        for (int i = 0; i < 5; i++) {
            int sparkleX = centerX + random.nextInt(40) - 20;
            int sparkleY = centerY + random.nextInt(40) - 20;
            g2d.fillOval(sparkleX, sparkleY, 3, 3);
            
            // Draw cross sparkle
            g2d.drawLine(sparkleX - 3, sparkleY, sparkleX + 3, sparkleY);
            g2d.drawLine(sparkleX, sparkleY - 3, sparkleX, sparkleY + 3);
        }
    }

    private void drawMagicalCircle(Graphics2D g2d, int x, int y, int size) {
        // Outer magical ring
        g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 100));
        g2d.fillOval(x - 5, y - 5, size + 10, size + 10);
        
        // Inner gem
        g2d.setColor(color);
        g2d.fillOval(x, y, size, size);
        
        // Magical border
        g2d.setColor(Color.WHITE);
        g2d.drawOval(x, y, size, size);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, GameConstants.BALL_SIZE, GameConstants.BALL_SIZE);
    }

    public boolean contains(int px, int py) {
        return getBounds().contains(px, py);
    }

    // Getters
    public int getX() { return x; }
    public int getY() { return y; }
    public int getValue() { return value; }
    public boolean isActive() { return active; }
    public Color getColor() { return color; }

    public boolean isTransitioningToBasket() {
        return transitioningToBasket || transitioningFromCharacterToBasket;
    }

    // Setters
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public void setActive(boolean active) { this.active = active; }

    public int getCenterX() {
        return x + GameConstants.BALL_SIZE / 2;
    }

    public int getCenterY() {
        return y + GameConstants.BALL_SIZE / 2;
    }

    public void startTransitionToBasket() {
        transitioningToBasket = true;
    }

    public void startTransitionFromCharacterToBasket() {
        transitioningFromCharacterToBasket = true;
        transitioningToBasket = false;

    }
}