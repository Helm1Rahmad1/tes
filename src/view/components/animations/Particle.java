package view.components.animations;

import java.awt.Color;
import java.awt.Graphics2D;

import javax.swing.text.StyleConstants.ColorConstants;

public class Particle {
    private float x, y, vx, vy, size, alpha, life;
    private Color color;

    public Particle() {
        reset();
    }

    private void reset() {
        x = (float) (Math.random() * 1400); // Assuming GameConstants.WINDOW_WIDTH for now
        y = (float) (Math.random() * 900); // Assuming GameConstants.WINDOW_HEIGHT for now
        vx = (float) (Math.random() - 0.5) * 0.5f;
        vy = (float) (Math.random() - 0.5) * 0.5f;
        size = (float) (Math.random() * 3 + 1);
        alpha = (float) (Math.random() * 0.3 + 0.1);
        life = 1.0f;
        
        Color[] colors = {Color.BLUE, Color.MAGENTA, Color.GREEN, Color.ORANGE};
        color = colors[(int) (Math.random() * colors.length)];
    }

    public void update() {
        x += vx;
        y += vy;
        life -= 0.001f;
        
        // Reset if particle goes off-screen
        if (life <= 0 || x < -size || x > 1400 + size || y < -size || y > 900 + size) {
            reset();
        }
    }

    public void draw(Graphics2D g2d) {
        g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) (alpha * life * 255)));
        g2d.fillOval((int) x, (int) y, (int) size, (int) size);
    }
}