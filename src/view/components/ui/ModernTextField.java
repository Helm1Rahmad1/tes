package view.components.ui;

import view.constants.ColorConstants;
import view.constants.FontConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

// Kelas ModernTextField adalah komponen UI yang merupakan turunan dari JTextField
// dengan tampilan modern dan animasi fokus.
public class ModernTextField extends JTextField {
    private String placeholder;
    private boolean focused = false;
    private Color focusColor = ColorConstants.ACCENT_BLUE;
    private Timer focusTimer;
    private float focusAnimation = 0.0f;

    // Konstruktor untuk membuat ModernTextField dengan placeholder.
    public ModernTextField(String placeholder) {
        this.placeholder = placeholder;
        // Mengatur ukuran preferensi, font, warna teks, warna kursor, dan border.
        setPreferredSize(new Dimension(300, 45));
        setFont(FontConstants.FONT_BODY);
        setForeground(ColorConstants.TEXT_PRIMARY);
        setCaretColor(ColorConstants.ACCENT_BLUE);
        setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        setOpaque(false);
        
        // Menambahkan listener untuk mendeteksi fokus pada field.
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                // Mengatur status fokus menjadi true dan memulai animasi fokus.
                focused = true;
                startFocusAnimation(true);
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                // Mengatur status fokus menjadi false dan memulai animasi fokus.
                focused = false;
                startFocusAnimation(false);
            }
        });
    }
    
    // Memulai animasi fokus, baik saat mendapatkan fokus maupun kehilangan fokus.
    private void startFocusAnimation(boolean gaining) {
        // Jika timer sudah berjalan, hentikan.
        if (focusTimer != null) focusTimer.stop();
        
        // Membuat timer untuk mengatur perubahan nilai animasi fokus.
        focusTimer = new Timer(16, e -> {
            if (gaining) {
                // Menambah nilai animasi fokus.
                focusAnimation = Math.min(1.0f, focusAnimation + 0.1f);
            } else {
                // Mengurangi nilai animasi fokus.
                focusAnimation = Math.max(0.0f, focusAnimation - 0.1f);
            }
            
            // Hentikan timer jika animasi selesai.
            if ((gaining && focusAnimation >= 1.0f) || (!gaining && focusAnimation <= 0.0f)) {
                focusTimer.stop();
            }
            // Meminta komponen untuk menggambar ulang.
            repaint();
        });
        focusTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Menggambar komponen dengan efek visual.
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Menggambar latar belakang dengan efek kaca.
        Color bgColor = new Color(255, 255, 255, (int)(10 + focusAnimation * 15));
        g2d.setColor(bgColor);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
        
        // Menggambar border dengan animasi.
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
        // Border custom sudah digambar di paintComponent.
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        
        // Menggambar teks placeholder jika field kosong dan tidak fokus.
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
    
    // Mengembalikan teks yang bersih (tanpa spasi di awal dan akhir).
    public String getCleanText() {
        return getText().trim();
    }
}