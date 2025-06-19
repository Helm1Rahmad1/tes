package view.components.ui;

import view.constants.ColorConstants;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

/**
 * Untuk tampilan dan nuansa scroll bar 
 * Ini menyesuaikan warna, menghilangkan tombol panah, dan merender
 */
public class UltraModernScrollBarUI extends BasicScrollBarUI {
    @Override
    protected void configureScrollBarColors() {
        // Mengatur warna thumb (bagian yang bisa digeser)
        thumbColor = new Color(ColorConstants.ACCENT_BLUE.getRed(), ColorConstants.ACCENT_BLUE.getGreen(), ColorConstants.ACCENT_BLUE.getBlue(), 100);
        thumbHighlightColor = new Color(ColorConstants.ACCENT_BLUE.getRed(), ColorConstants.ACCENT_BLUE.getGreen(), ColorConstants.ACCENT_BLUE.getBlue(), 150);
        thumbLightShadowColor = new Color(ColorConstants.ACCENT_BLUE.getRed(), ColorConstants.ACCENT_BLUE.getGreen(), ColorConstants.ACCENT_BLUE.getBlue(), 50);
        thumbDarkShadowColor = new Color(ColorConstants.ACCENT_BLUE.getRed(), ColorConstants.ACCENT_BLUE.getGreen(), ColorConstants.ACCENT_BLUE.getBlue(), 30);
        trackColor = new Color(0, 0, 0, 0); // Track transparan
        trackHighlightColor = new Color(0, 0, 0, 0);
    }
    
    @Override
    protected JButton createDecreaseButton(int orientation) {
        return createInvisibleButton(); 
    }
    
    @Override
    protected JButton createIncreaseButton(int orientation) {
        return createInvisibleButton(); 
    }
    
    private JButton createInvisibleButton() {
        // Membuat tombol yang tidak terlihat
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(0, 0));
        button.setMinimumSize(new Dimension(0, 0));
        button.setMaximumSize(new Dimension(0, 0));
        return button;
    }
    
    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        // Menggambar thumb dengan sudut membulat
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2d.setColor(thumbColor);
        g2d.fillRoundRect(thumbBounds.x + 2, thumbBounds.y + 2, 
                        thumbBounds.width - 4, thumbBounds.height - 4, 6, 6);
        
        g2d.dispose();
    }
    
    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
    }
}