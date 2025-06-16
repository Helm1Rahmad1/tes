package view.components;

import java.awt.*;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;
import utils.GameConstants;

/**
 * Magical Lasso - Enhanced visual component with magical effects
 * Part of View layer in MVVM pattern
 */
public class Lasso {
    private int startX, startY;
    private int endX, endY;
    private int currentLength;
    private boolean extending;
    private boolean active;
    private Ball caughtBall;
    
    // Magical enhancement properties
    private float animationTimer = 0.0f;
    private List<MagicalParticle> particles;
    private Color[] magicalColors;
    private float glowIntensity = 0.0f;
    private boolean sparkleEffect = false;
    private int chargeLevel = 0; // 0-100, builds up when extending
    
    // Lasso chain effect
    private List<ChainLink> chainLinks;
    private static final int CHAIN_SEGMENT_SIZE = 8;

    public Lasso() {
        this.currentLength = 0;
        this.extending = false;
        this.active = false;
        this.caughtBall = null;
        this.particles = new ArrayList<>();
        this.chainLinks = new ArrayList<>();
        
        // Initialize magical colors
        this.magicalColors = new Color[] {
            new Color(255, 215, 0, 200),    // Gold
            new Color(138, 43, 226, 200),   // Purple
            new Color(0, 191, 255, 200),    // Deep Sky Blue
            new Color(255, 20, 147, 200),   // Deep Pink
            new Color(50, 205, 50, 200)     // Lime Green
        };
    }

    /**
     * Start magical lasso from character position to target
     */
    public void start(int characterX, int characterY, int targetX, int targetY) {
        this.startX = characterX;
        this.startY = characterY;
        
        // Calculate direction with magical precision
        int dx = targetX - characterX;
        int dy = targetY - characterY;
        double distance = Math.sqrt(dx * dx + dy * dy);

        System.out.println("🔮 Magical Lasso Cast! Distance: " + distance);

        if (distance > 0) {
            this.endX = targetX;
            this.endY = targetY;
            System.out.println("✨ Target locked: (" + endX + ", " + endY + ")");
        } else {
            this.endX = characterX;
            this.endY = characterY;
        }
        
        this.currentLength = 0;
        this.extending = true;
        this.active = true;
        this.caughtBall = null;
        this.animationTimer = 0.0f;
        this.glowIntensity = 1.0f;
        this.sparkleEffect = true;
        this.chargeLevel = 0;
        
        // Initialize chain links
        initializeChainLinks();
        
        // Create initial magical particles
        createInitialParticles();
    }

    /**
     * Initialize chain links for the lasso rope effect
     */
    private void initializeChainLinks() {
        chainLinks.clear();
        int dx = endX - startX;
        int dy = endY - startY;
        double totalDistance = Math.sqrt(dx * dx + dy * dy);
        int numLinks = (int)(totalDistance / CHAIN_SEGMENT_SIZE) + 1;
        
        for (int i = 0; i < numLinks; i++) {
            chainLinks.add(new ChainLink());
        }
    }

    /**
     * Create initial magical particles
     */
    private void createInitialParticles() {
        particles.clear();
        for (int i = 0; i < 5; i++) {
            particles.add(new MagicalParticle(startX, startY));
        }
    }

    /**
     * Update start position based on character movement
     */
    public void updateStartPosition(int characterX, int characterY) {
        if (active) {
            this.startX = characterX;
            this.startY = characterY;
        }
    }

    /**
     * Enhanced lasso update with magical effects
     */
    public void update() {
        if (!active) return;

        animationTimer += 0.15f;
        
        if (extending) {
            currentLength += GameConstants.LASSO_SPEED;
            chargeLevel = Math.min(100, chargeLevel + 2);
            
            // Calculate max length to target
            int dx = endX - startX;
            int dy = endY - startY;
            int maxLength = (int)Math.sqrt(dx * dx + dy * dy);
            
            if (currentLength >= maxLength) {
                currentLength = maxLength;
                extending = false;
                sparkleEffect = false;
            }
            
            // Add particles while extending
            if (Math.random() < 0.3) {
                int tipX = getCurrentTipX();
                int tipY = getCurrentTipY();
                particles.add(new MagicalParticle(tipX, tipY));
            }
        } else {
            // Retracting with magical effect
            currentLength -= GameConstants.LASSO_SPEED * 1.5; // Faster retraction
            chargeLevel = Math.max(0, chargeLevel - 3);
            
            if (currentLength <= 0) {
                currentLength = 0;
                active = false;
                glowIntensity = 0.0f;
                
                // Magical collection effect
                if (caughtBall != null) {
                    createCollectionEffect();
                    caughtBall.startTransitionFromCharacterToBasket();
                    caughtBall = null;
                }
            }
        }
        
        // Update magical particles
        updateParticles();
        
        // Update chain links
        updateChainLinks();
        
        // Update glow intensity
        glowIntensity = 0.7f + 0.3f * (float)Math.sin(animationTimer * 3);
    }

    /**
     * Update magical particles
     */
    private void updateParticles() {
        particles.removeIf(particle -> {
            particle.update();
            return particle.isDead();
        });
    }

    /**
     * Update chain links for rope animation
     */
    private void updateChainLinks() {
        if (chainLinks.isEmpty()) return;
        
        int dx = endX - startX;
        int dy = endY - startY;
        double totalLength = Math.sqrt(dx * dx + dy * dy);
        
        for (int i = 0; i < chainLinks.size(); i++) {
            ChainLink link = chainLinks.get(i);
            double ratio = (double)i / chainLinks.size();
            double currentRatio = Math.min(1.0, (double)currentLength / totalLength);
            
            if (ratio <= currentRatio) {
                link.x = startX + (int)(dx * ratio);
                link.y = startY + (int)(dy * ratio);
                link.active = true;
                link.sway = (float)Math.sin(animationTimer * 2 + i * 0.5) * 2;
            } else {
                link.active = false;
            }
        }
    }

    /**
     * Create magical collection effect
     */
    private void createCollectionEffect() {
        int tipX = getCurrentTipX();
        int tipY = getCurrentTipY();
        
        for (int i = 0; i < 15; i++) {
            MagicalParticle particle = new MagicalParticle(tipX, tipY);
            particle.setCollectionEffect(true);
            particles.add(particle);
        }
    }

    /**
     * Enhanced collision detection
     */
    public Ball checkCollision(Ball[] balls) {
        if (!active || !extending || caughtBall != null) return null;
        
        int tipX = getCurrentTipX();
        int tipY = getCurrentTipY();
        
        for (Ball ball : balls) {
            if (ball != null && ball.isActive() && !ball.isTransitioningToBasket()) {
                // Enhanced collision detection with magical radius
                int ballCenterX = ball.getX() + GameConstants.BALL_SIZE / 2;
                int ballCenterY = ball.getY() + GameConstants.BALL_SIZE / 2;
                double distance = Math.sqrt(Math.pow(tipX - ballCenterX, 2) + Math.pow(tipY - ballCenterY, 2));
                
                if (distance <= GameConstants.BALL_SIZE / 2 + 5) { // +5 for magical reach
                    caughtBall = ball;
                    extending = false;
                    
                    // Create capture effect
                    createCaptureEffect(tipX, tipY);
                    
                    System.out.println("✨ Magical gem captured! ✨");
                    return ball;
                }
            }
        }
        
        return null;
    }

    /**
     * Create magical capture effect
     */
    private void createCaptureEffect(int x, int y) {
        for (int i = 0; i < 20; i++) {
            MagicalParticle particle = new MagicalParticle(x, y);
            particle.setCaptureEffect(true);
            particles.add(particle);
        }
    }

    /**
     * Get current tip position
     */
    public int getCurrentTipX() {
        if (currentLength == 0) return startX;
        
        int dx = endX - startX;
        int dy = endY - startY;
        double totalLength = Math.sqrt(dx * dx + dy * dy);
        
        if (totalLength == 0) return startX;
        
        double ratio = currentLength / totalLength;
        return startX + (int)(dx * ratio);
    }

    public int getCurrentTipY() {
        if (currentLength == 0) return startY;
        
        int dx = endX - startX;
        int dy = endY - startY;
        double totalLength = Math.sqrt(dx * dx + dy * dy);
        
        if (totalLength == 0) return startY;
        
        double ratio = currentLength / totalLength;
        return startY + (int)(dy * ratio);
    }

    /**
     * Enhanced magical rendering
     */
    public void render(Graphics2D g2d) {
        if (!active || currentLength <= 0) return;
        
        // Enable antialiasing for smooth rendering
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Render magical chain
        renderMagicalChain(g2d);
        
        // Render main lasso line with glow effect
        renderMainLasso(g2d);
        
        // Render magical tip
        renderMagicalTip(g2d);
        
        // Render caught ball if any
        renderCaughtBall(g2d);
        
        // Render magical particles
        renderParticles(g2d);
        
        // Render charge indicator
        if (extending && chargeLevel > 20) {
            renderChargeIndicator(g2d);
        }
    }

    /**
     * Render magical chain links
     */
    private void renderMagicalChain(Graphics2D g2d) {
        for (ChainLink link : chainLinks) {
            if (!link.active) continue;
            
            // Chain link with sway effect
            int linkX = link.x + (int)link.sway;
            int linkY = link.y;
            
            // Gradient for chain link
            Color chainColor = new Color(255, 215, 0, 180);
            g2d.setColor(chainColor);
            g2d.fillOval(linkX - 2, linkY - 2, 4, 4);
            
            // Small sparkle on each link
            if (Math.random() < 0.1) {
                g2d.setColor(Color.WHITE);
                g2d.fillOval(linkX - 1, linkY - 1, 2, 2);
            }
        }
    }

    /**
     * Render main lasso with glow effect
     */
    private void renderMainLasso(Graphics2D g2d) {
        int tipX = getCurrentTipX();
        int tipY = getCurrentTipY();
        
        // Glow effect layers
        for (int i = 6; i >= 1; i--) {
            float alpha = glowIntensity * (7 - i) / 6.0f * 0.3f;
            Color glowColor = new Color(255, 215, 0, (int)(alpha * 255));
            g2d.setColor(glowColor);
            g2d.setStroke(new BasicStroke(i * 1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2d.drawLine(startX, startY, tipX, tipY);
        }
        
        // Main lasso line with magical color cycling
        Color mainColor = magicalColors[(int)(animationTimer * 2) % magicalColors.length];
        g2d.setColor(mainColor);
        g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.drawLine(startX, startY, tipX, tipY);
    }

    /**
     * Render magical tip with enhanced effects
     */
    private void renderMagicalTip(Graphics2D g2d) {
        int tipX = getCurrentTipX();
        int tipY = getCurrentTipY();
        
        // Outer glow
        for (int i = 8; i >= 1; i--) {
            float alpha = glowIntensity * (9 - i) / 8.0f * 0.4f;
            g2d.setColor(new Color(255, 255, 255, (int)(alpha * 255)));
            g2d.fillOval(tipX - i, tipY - i, i * 2, i * 2);
        }
        
        // Core tip
        g2d.setColor(new Color(255, 255, 255, 220));
        g2d.fillOval(tipX - 4, tipY - 4, 8, 8);
        
        // Inner core
        g2d.setColor(new Color(255, 215, 0));
        g2d.fillOval(tipX - 2, tipY - 2, 4, 4);
        
        // Sparkle effect around tip
        if (sparkleEffect) {
            renderTipSparkles(g2d, tipX, tipY);
        }
    }

    /**
     * Render sparkles around the tip
     */
    private void renderTipSparkles(Graphics2D g2d, int tipX, int tipY) {
        g2d.setStroke(new BasicStroke(1));
        
        for (int i = 0; i < 6; i++) {
            float angle = animationTimer * 3 + i * (float)Math.PI / 3;
            int sparkleX = tipX + (int)(Math.cos(angle) * 10);
            int sparkleY = tipY + (int)(Math.sin(angle) * 10);
            
            g2d.setColor(new Color(255, 255, 255, 150));
            g2d.drawLine(sparkleX - 3, sparkleY, sparkleX + 3, sparkleY);
            g2d.drawLine(sparkleX, sparkleY - 3, sparkleX, sparkleY + 3);
        }
    }

    /**
     * Render caught ball with magical binding effect
     */
    private void renderCaughtBall(Graphics2D g2d) {
        if (caughtBall != null && !extending) {
            int tipX = getCurrentTipX();
            int tipY = getCurrentTipY();
            
            // Magical binding circle around ball
            g2d.setColor(new Color(255, 215, 0, 100));
            g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            float bindingRadius = 20 + 5 * (float)Math.sin(animationTimer * 4);
            g2d.drawOval(tipX - (int)bindingRadius, tipY - (int)bindingRadius, 
                        (int)bindingRadius * 2, (int)bindingRadius * 2);
            
            // Update ball position
            caughtBall.setX(tipX - GameConstants.BALL_SIZE / 2);
            caughtBall.setY(tipY - GameConstants.BALL_SIZE / 2);
            caughtBall.render(g2d);
        }
    }

    /**
     * Render magical particles
     */
    private void renderParticles(Graphics2D g2d) {
        for (MagicalParticle particle : particles) {
            particle.render(g2d);
        }
    }

    /**
     * Render charge indicator
     */
    private void renderChargeIndicator(Graphics2D g2d) {
        int barWidth = 60;
        int barHeight = 8;
        int barX = startX - barWidth / 2;
        int barY = startY - 20;
        
        // Background
        g2d.setColor(new Color(0, 0, 0, 100));
        g2d.fillRoundRect(barX, barY, barWidth, barHeight, 4, 4);
        
        // Charge fill
        int fillWidth = (int)(barWidth * chargeLevel / 100.0);
        Color chargeColor = chargeLevel > 80 ? Color.RED : 
                           chargeLevel > 50 ? Color.YELLOW : Color.GREEN;
        g2d.setColor(chargeColor);
        g2d.fillRoundRect(barX, barY, fillWidth, barHeight, 4, 4);
        
        // Border
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(1));
        g2d.drawRoundRect(barX, barY, barWidth, barHeight, 4, 4);
    }

    // Getters
    public boolean isActive() { return active; }
    public boolean isExtending() { return extending; }
    public Ball getCaughtBall() { return caughtBall; }
    public int getCurrentLength() { return currentLength; }

    /**
     * Force stop magical lasso
     */
    public void stop() {
        active = false;
        caughtBall = null;
        currentLength = 0;
        extending = false;
        sparkleEffect = false;
        glowIntensity = 0.0f;
        particles.clear();
        chainLinks.clear();
        System.out.println("🔮 Magical lasso dispelled");
    }

    /**
     * Check if lasso has completed its magical cycle
     */
    public boolean isComplete() {
        return !active && currentLength == 0;
    }

    /**
     * Inner class for magical particles
     */
    private class MagicalParticle {
        float x, y;
        float vx, vy;
        float life;
        float maxLife;
        Color color;
        boolean isCollectionEffect = false;
        boolean isCaptureEffect = false;
        
        public MagicalParticle(int startX, int startY) {
            this.x = startX + (float)(Math.random() - 0.5) * 10;
            this.y = startY + (float)(Math.random() - 0.5) * 10;
            this.vx = (float)(Math.random() - 0.5) * 4;
            this.vy = (float)(Math.random() - 0.5) * 4;
            this.maxLife = 30 + (float)Math.random() * 20;
            this.life = maxLife;
            this.color = magicalColors[(int)(Math.random() * magicalColors.length)];
        }
        
        public void setCollectionEffect(boolean collection) {
            this.isCollectionEffect = collection;
            if (collection) {
                this.vx *= 2;
                this.vy *= 2;
                this.maxLife *= 1.5f;
                this.life = maxLife;
            }
        }
        
        public void setCaptureEffect(boolean capture) {
            this.isCaptureEffect = capture;
            if (capture) {
                float angle = (float)(Math.random() * Math.PI * 2);
                float speed = 2 + (float)Math.random() * 3;
                this.vx = (float)Math.cos(angle) * speed;
                this.vy = (float)Math.sin(angle) * speed;
            }
        }
        
        public void update() {
            x += vx;
            y += vy;
            life--;
            
            // Gravity effect for some particles
            if (!isCollectionEffect && !isCaptureEffect) {
                vy += 0.1f;
            }
            
            // Fade velocity
            vx *= 0.98f;
            vy *= 0.98f;
        }
        
        public void render(Graphics2D g2d) {
            float alpha = Math.max(0, life / maxLife);
            Color renderColor = new Color(
                color.getRed(), 
                color.getGreen(), 
                color.getBlue(), 
                (int)(color.getAlpha() * alpha)
            );
            
            g2d.setColor(renderColor);
            int size = isCaptureEffect ? 4 : 2;
            g2d.fillOval((int)x - size/2, (int)y - size/2, size, size);
            
            // Sparkle effect for capture particles
            if (isCaptureEffect && Math.random() < 0.3) {
                g2d.setColor(Color.WHITE);
                g2d.drawLine((int)x - 3, (int)y, (int)x + 3, (int)y);
                g2d.drawLine((int)x, (int)y - 3, (int)x, (int)y + 3);
            }
        }
        
        public boolean isDead() {
            return life <= 0;
        }
    }

    /**
     * Inner class for chain links
     */
    private class ChainLink {
        int x, y;
        boolean active = false;
        float sway = 0.0f;
    }
}