package view.components.panels;

import view.constants.ColorConstants;

import javax.swing.*;
import java.awt.*;

public class GlassmorphismCard extends JPanel {
    public GlassmorphismCard() {
        setOpaque(false); // Panel ini transparan
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Latar belakang kaca
        g2d.setColor(ColorConstants.SURFACE_GLASS);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

        // Border highlight
        g2d.setColor(ColorConstants.SURFACE_HIGHLIGHT);
        g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);

        // Inner glow (efek cahaya di dalam)
        g2d.setColor(new Color(255, 255, 255, 5));
        g2d.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 18, 18);

        g2d.dispose(); 
    }
}