package view.components.dialogs;

import view.constants.ColorConstants;
import view.constants.FontConstants;

import javax.swing.*;
import java.awt.*;

public class NotificationDialog {

    public enum NotificationType {
        SUCCESS, WARNING, ERROR // Jenis notifikasi: sukses, peringatan, atau kesalahan
    }

    public static void showModernNotification(JFrame parent, String message, NotificationType type) {
        // Membuat dialog notifikasi mengambang
        JDialog notification = new JDialog(parent, false);
        notification.setUndecorated(true); // Menghapus dekorasi jendela
        notification.setOpacity(0.95f); // Mengatur transparansi

        // Panel untuk menampilkan notifikasi
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // Mengaktifkan anti-aliasing
                
                // Mengatur warna latar belakang berdasarkan jenis notifikasi
                Color bgColor = type == NotificationType.SUCCESS ? ColorConstants.ACCENT_GREEN :
                               type == NotificationType.WARNING ? ColorConstants.ACCENT_ORANGE : ColorConstants.ACCENT_RED;
                
                g2d.setColor(bgColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15); // Membuat sudut panel melengkung
                
                // Menambahkan efek border transparan
                g2d.setColor(new Color(255, 255, 255, 50));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                
                g2d.dispose();
            }
        };
        
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 15)); // Mengatur tata letak panel
        
        // Menentukan ikon berdasarkan jenis notifikasi
        String icon = type == NotificationType.SUCCESS ? "✅" :
                     type == NotificationType.WARNING ? "⚠️" : "❌";
        
        // Label untuk menampilkan pesan
        JLabel label = new JLabel(icon + " " + message);
        label.setForeground(Color.WHITE); // Warna teks putih
        label.setFont(FontConstants.FONT_BODY); // Menggunakan font yang telah ditentukan
        
        panel.add(label); // Menambahkan label ke panel
        notification.add(panel); // Menambahkan panel ke dialog
        notification.pack(); // Menyesuaikan ukuran dialog
        
        // Mengatur posisi dialog agar muncul di tengah atas jendela parent
        Point parentLocation = parent.getLocationOnScreen();
        notification.setLocation(
            parentLocation.x + (parent.getWidth() - notification.getWidth()) / 2,
            parentLocation.y + 100
        );
        
        notification.setVisible(true); // Menampilkan dialog
        
        // Menutup dialog secara otomatis setelah 3 detik
        Timer hideTimer = new Timer(3000, e -> {
            notification.dispose();
        });
        hideTimer.setRepeats(false); // Timer hanya berjalan sekali
        hideTimer.start();
    }
}