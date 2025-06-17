package view.components.ui;

import view.constants.ColorConstants;
import view.constants.FontConstants;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Renderer sel tabel dengan gaya ultra-modern
 */
public class UltraModernTableRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {
            
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        setOpaque(false); // Membuat latar belakang transparan
        setFont(FontConstants.FONT_BODY); // Mengatur font

        if (isSelected) {
            setForeground(ColorConstants.TEXT_PRIMARY); // Warna teks saat dipilih
            setBackground(new Color(ColorConstants.ACCENT_BLUE.getRed(), ColorConstants.ACCENT_BLUE.getGreen(), ColorConstants.ACCENT_BLUE.getBlue(), 60)); // Highlight dengan alpha
        } else {
            setForeground(row % 2 == 0 ? ColorConstants.TEXT_PRIMARY : ColorConstants.TEXT_SECONDARY); // Warna teks bergantian
            setBackground(new Color(0, 0, 0, 0)); // Latar belakang transparan
        }
        
        setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15)); // Padding dalam sel
        setHorizontalAlignment(SwingConstants.LEFT); // Rata kiri semua kolom
        
        return this;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        // Menggambar latar belakang hanya jika memiliki alpha
        if (getBackground().getAlpha() > 0) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(getBackground());
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8); // Sudut membulat
            g2d.dispose();
        }
        super.paintComponent(g); // Memanggil super untuk menggambar teks dan ikon
    }
}