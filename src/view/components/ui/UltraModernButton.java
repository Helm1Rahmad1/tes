package view.components.ui;

import view.constants.ButtonStyle;
import view.constants.ColorConstants;
import view.constants.FontConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Tombol yang sangat modern dengan berbagai gaya dan animasi
 * Mendukung efek hover, loading, dan spinning untuk memberikan pengalaman pengguna yang dinamis
 */
public class UltraModernButton extends JButton {
    private Color baseColor;
    private ButtonStyle style;
    private boolean isHovered = false;
    private boolean isPressed = false;
    private boolean isLoading = false;
    private boolean isSpinning = false;
    private Timer hoverTimer;
    private Timer loadingTimer;
    private Timer spinTimer;
    private float hoverAnimation = 0.0f;
    private float loadingAnimation = 0.0f;
    private float spinAnimation = 0.0f;

    
    public UltraModernButton(String text, Color baseColor, ButtonStyle style) {
        super(text);
        this.baseColor = baseColor;
        this.style = style;
        
        // Mengatur font, warna, dan properti tombol
        setFont(style == ButtonStyle.ICON ? FontConstants.FONT_HEADING : FontConstants.FONT_SUBHEADING);
        setForeground(ColorConstants.TEXT_PRIMARY);
        setBorder(BorderFactory.createEmptyBorder());
        setContentAreaFilled(false);
        setFocusPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Menambahkan listener untuk event mouse
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!isLoading) {
                    isHovered = true;
                    startHoverAnimation(true);
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                startHoverAnimation(false);
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                if (!isLoading) {
                    isPressed = true;
                    repaint();
                }
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                isPressed = false;
                repaint();
            }
        });
    }
    
    /**
     * Memulai animasi hover
     * @param entering Apakah mouse masuk ke area tombol
     */
    private void startHoverAnimation(boolean entering) {
        if (hoverTimer != null) hoverTimer.stop();
        
        hoverTimer = new Timer(16, e -> {
            if (entering) {
                hoverAnimation = Math.min(1.0f, hoverAnimation + 0.15f);
            } else {
                hoverAnimation = Math.max(0.0f, hoverAnimation - 0.15f);
            }
            
            if ((entering && hoverAnimation >= 1.0f) || (!entering && hoverAnimation <= 0.0f)) {
                hoverTimer.stop();
            }
            repaint();
        });
        hoverTimer.start();
    }
    
    /**
     * Mengatur status loading tombol
     * @param loading Apakah tombol dalam status loading
     */
    public void setLoadingState(boolean loading) {
        this.isLoading = loading;
        if (loading) {
            startLoadingAnimation();
        } else {
            stopLoadingAnimation();
        }
    }
    
    /**
     * Memulai animasi loading
     */
    private void startLoadingAnimation() {
        loadingTimer = new Timer(50, e -> {
            loadingAnimation += 0.2f;
            if (loadingAnimation > 1.0f) loadingAnimation = 0.0f;
            repaint();
        });
        loadingTimer.start();
    }
    
    /**
     * Menghentikan animasi loading
     */
    private void stopLoadingAnimation() {
        if (loadingTimer != null) {
            loadingTimer.stop();
            loadingAnimation = 0.0f;
            repaint();
        }
    }
    
    /**
     * Memulai animasi spinning
     */
    public void startSpinAnimation() {
        isSpinning = true;
        spinTimer = new Timer(16, e -> {
            spinAnimation += 0.1f;
            if (spinAnimation > 2 * Math.PI) spinAnimation = 0.0f;
            repaint();
        });
        spinTimer.start();
    }
    
    /**
     * Menghentikan animasi spinning
     */
    public void stopSpinAnimation() {
        isSpinning = false;
        if (spinTimer != null) {
            spinTimer.stop();
            spinAnimation = 0.0f;
            repaint();
        }
    }

    /**
     * Melukis komponen tombol
     * @param g Objek grafis
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        int width = getWidth();
        int height = getHeight();
        
        // Menghitung efek animasi
        float scale = isPressed ? 0.95f : (1.0f + hoverAnimation * 0.05f);
        float opacity = isLoading ? 0.7f : 1.0f;
        
        // Menerapkan transformasi skala
        g2d.translate(width / 2, height / 2);
        g2d.scale(scale, scale);
        g2d.translate(-width / 2, -height / 2);
        
        // Latar belakang dengan gradien dan efek glow
        if (style != ButtonStyle.ICON) {
            // Efek glow
            if (hoverAnimation > 0) {
                Color glowColor = new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), 
                                         (int)(hoverAnimation * 100));
                for (int i = 0; i < 10; i++) {
                    g2d.setColor(new Color(glowColor.getRed(), glowColor.getGreen(), glowColor.getBlue(), 
                                         glowColor.getAlpha() / (i + 1)));
                    g2d.fillRoundRect(-i, -i, width + 2*i, height + 2*i, 15 + i, 15 + i);
                }
            }
            
            // Latar belakang utama
            GradientPaint gradient = new GradientPaint(
                0, 0, new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), 
                               (int)(opacity * (style == ButtonStyle.PRIMARY ? 255 : 100))),
                0, height, new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), 
                                    (int)(opacity * (style == ButtonStyle.PRIMARY ? 200 : 50)))
            );
            g2d.setPaint(gradient);
            g2d.fillRoundRect(0, 0, width, height, 15, 15);
            
            // Border
            g2d.setColor(new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), 
                                 (int)(opacity * 150)));
            g2d.drawRoundRect(0, 0, width - 1, height - 1, 15, 15);
        }
        
        // Latar belakang tombol ikon
        if (style == ButtonStyle.ICON) {
            Color iconBg = new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), 
                                   (int)(opacity * (50 + hoverAnimation * 100)));
            g2d.setColor(iconBg);
            g2d.fillRoundRect(0, 0, width, height, 12, 12);
        }
        
        // Animasi loading
        if (isLoading) {
            g2d.setColor(new Color(255, 255, 255, 100));
            int dotSize = 4;
            int spacing = 12;
            int startX = (width - 2 * spacing) / 2;
            int y = height / 2 - dotSize / 2;
            
            for (int i = 0; i < 3; i++) {
                float alpha = (float) Math.abs(Math.sin(loadingAnimation * Math.PI * 2 + i * Math.PI / 2));
                g2d.setColor(new Color(255, 255, 255, (int)(alpha * 255)));
                g2d.fillOval(startX + i * spacing, y, dotSize, dotSize);
            }
        }
        // Animasi spinning untuk tombol refresh
        else if (isSpinning && style == ButtonStyle.ICON) {
            g2d.translate(width / 2, height / 2);
            g2d.rotate(spinAnimation);
            g2d.translate(-width / 2, -height / 2);
        }
        
        // Teks tombol
        if (!isLoading) {
            g2d.setColor(getForeground());
            g2d.setFont(getFont());
            FontMetrics fm = g2d.getFontMetrics();
            int textX = (width - fm.stringWidth(getText())) / 2; 
            int textY = (height - fm.getHeight()) / 2 + fm.getAscent(); 
            
            g2d.drawString(getText(), textX, textY);
        }
        
        g2d.dispose();
    }
}