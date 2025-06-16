package view.components.ui;

import view.constants.ColorConstants;
import view.constants.FontConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class ModernTextField extends JTextField {
    private String placeholder;
    private boolean focused = false;
    private Color focusColor = ColorConstants.ACCENT_BLUE;
    private Timer focusTimer;
    private float focusAnimation = 0.0f;

    public ModernTextField(String placeholder) {
        this.placeholder = placeholder;
        setPreferredSize(new Dimension(300, 45));
        setFont(FontConstants.FONT_BODY);
        setForeground(ColorConstants.TEXT_PRIMARY);
        setCaretColor(ColorConstants.ACCENT_BLUE);
        setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        setOpaque(false);
        
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                focused = true;
                startFocusAnimation(true);
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                focused = false;
                startFocusAnimation(false);
            }
        });
    }
    
    private void startFocusAnimation(boolean gaining) {
        if (focusTimer != null) focusTimer.stop();
        
        focusTimer = new Timer(16, e -> {
            if (gaining) {
                focusAnimation = Math.min(1.0f, focusAnimation + 0.1f);
            } else {
                focusAnimation = Math.max(0.0f, focusAnimation - 0.1f);
            }
            
            if ((gaining && focusAnimation >= 1.0f) || (!gaining && focusAnimation <= 0.0f)) {
                focusTimer.stop();
            }
            repaint();
        });
        focusTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Background with glass effect
        Color bgColor = new Color(255, 255, 255, (int)(10 + focusAnimation * 15));
        g2d.setColor(bgColor);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
        
        // Animated border
        int borderAlpha = (int)(100 + focusAnimation * 155);
        Color borderColor = new Color(focusColor.getRed(), focusColor.getGreen(), focusColor.getBlue(), borderAlpha);
        g2d.setColor(borderColor);
        g2d.setStroke(new BasicStroke(2.0f));
        g2d.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 12, 12);
        
        g2d.dispose();
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        // Custom border is painted in paintComponent
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        
        // Draw placeholder text
        if (getText().isEmpty() && !focused) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2d.setColor(ColorConstants.TEXT_SECONDARY);
            g2d.setFont(getFont());
            
            FontMetrics fm = g2d.getFontMetrics();
            int x = getInsets().left;
            int y = (getHeight() + fm.getAscent()) / 2 - 2;
            g2d.drawString(placeholder, x, y);
            g2d.dispose();
        }
    }
    
    public String getCleanText() {
        return getText().trim();
    }
}