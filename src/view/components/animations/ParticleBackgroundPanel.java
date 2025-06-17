package view.components.animations;

import view.components.panels.TransparentPanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public class ParticleBackgroundPanel extends TransparentPanel {
    private BufferedImage backgroundImage;
    private List<Particle> particles;
    private BooleanSupplier isInitializedSupplier;
    private Supplier<Float> globalOpacitySupplier;

    public ParticleBackgroundPanel(BufferedImage backgroundImage, List<Particle> particles, BooleanSupplier isInitializedSupplier, Supplier<Float> globalOpacitySupplier) {
        this.backgroundImage = backgroundImage;
        this.particles = particles;
        this.isInitializedSupplier = isInitializedSupplier;
        this.globalOpacitySupplier = globalOpacitySupplier;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw background image with global opacity
        if (backgroundImage != null) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, globalOpacitySupplier.get()));
            g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }

        // Draw particles only when initialized
        if (isInitializedSupplier.getAsBoolean()) {
            g2d.setComposite(AlphaComposite.SrcOver); // Reset composite for particles
            for (Particle particle : particles) {
                particle.draw(g2d);
            }
        }

        g2d.dispose();
    }
}