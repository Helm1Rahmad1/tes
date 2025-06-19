package view.components.ui;

import view.constants.ColorConstants;
import view.constants.FontConstants;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Perender sel tabel kustom
 * dengan gaya modern. Ini menyesuaikan tampilan sel tabel termasuk
 */
public class UltraModernTableRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {
            
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        setOpaque(false); 
        setFont(FontConstants.FONT_BODY); 

        if (isSelected) {
            setForeground(ColorConstants.TEXT_PRIMARY); 
            setBackground(new Color(ColorConstants.ACCENT_BLUE.getRed(), ColorConstants.ACCENT_BLUE.getGreen(), ColorConstants.ACCENT_BLUE.getBlue(), 60)); 
        } else {
            setForeground(row % 2 == 0 ? ColorConstants.TEXT_PRIMARY : ColorConstants.TEXT_SECONDARY); 
            setBackground(new Color(0, 0, 0, 0)); 
        }
        
        setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15)); 
        setHorizontalAlignment(SwingConstants.LEFT); 
        
        return this;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        // Menggambar latar belakang hanya jika memiliki alpha
        if (getBackground().getAlpha() > 0) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(getBackground());
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8); 
            g2d.dispose();
        }
        super.paintComponent(g); 
    }
}