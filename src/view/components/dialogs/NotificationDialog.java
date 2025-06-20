package view.components.dialogs;

import view.constants.ColorConstants;
import view.constants.FontConstants;

import javax.swing.*;
import java.awt.*;

/**
 * Menyediakan fungsionalitas untuk menampilkan
 * notifikasi modern yang mengambang (floating notification) kepada pengguna
 * Notifikasi ini dapat disesuaikan tipenya (sukses, peringatan, error)
 * dengan efek visual dan penutupan otomatis
 */
public class NotificationDialog {

    public enum NotificationType {
        SUCCESS, WARNING, ERROR 
    }

    public static void showModernNotification(JFrame parent, String message, NotificationType type) {
        // Membuat dialog notifikasi mengambang
        JDialog notification = new JDialog(parent, false);
        notification.setUndecorated(true); 
        notification.setOpacity(0.95f); 

        // Panel untuk menampilkan notifikasi
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); 
                
                // Mengatur warna latar belakang berdasarkan jenis notifikasi
                Color bgColor = type == NotificationType.SUCCESS ? ColorConstants.ACCENT_GREEN :
                               type == NotificationType.WARNING ? ColorConstants.ACCENT_ORANGE : ColorConstants.ACCENT_RED;
                
                g2d.setColor(bgColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15); 
                
                // Menambahkan efek border transparan
                g2d.setColor(new Color(255, 255, 255, 50));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                
                g2d.dispose();
            }
        };
        
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 15)); 
        
        // Label untuk menampilkan pesan
        JLabel label = new JLabel( " " + message);
        label.setForeground(Color.WHITE); 
        label.setFont(FontConstants.FONT_BODY); 
        
        panel.add(label); 
        notification.add(panel); 
        notification.pack(); 
        
        // Mengatur posisi dialog agar muncul di tengah atas jendela parent
        Point parentLocation = parent.getLocationOnScreen();
        notification.setLocation(
            parentLocation.x + (parent.getWidth() - notification.getWidth()) / 2,
            parentLocation.y + 100
        );
        
        notification.setVisible(true); 
        
        // Menutup dialog secara otomatis setelah 3 detik
        Timer hideTimer = new Timer(3000, e -> {
            notification.dispose();
        });
        hideTimer.setRepeats(false); 
        hideTimer.start();
    }
}