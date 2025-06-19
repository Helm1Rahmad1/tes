package view.components.animations;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Merepresentasikan objek partikel individual untuk efek visual
 * Mengelola posisi, kecepatan, ukuran, transparansi, umur, dan warna partikel
 * Bertanggung jawab untuk memperbarui status dan merender partikel
 */
public class Particle {
    private float x, y, vx, vy, size, alpha, life; 
    private Color color; 

    public Particle() {
        reset(); 
    }

    private void reset() {
        // Mengatur ulang posisi, kecepatan, ukuran, transparansi, umur, dan warna partikel
        x = (float) (Math.random() * 1400); 
        y = (float) (Math.random() * 900); 
        vx = (float) (Math.random() - 0.5) * 0.5f; 
        vy = (float) (Math.random() - 0.5) * 0.5f; 
        size = (float) (Math.random() * 3 + 1); 
        alpha = (float) (Math.random() * 0.3 + 0.1); 
        life = 1.0f; 

        // Warna acak dari daftar warna
        Color[] colors = {Color.BLUE, Color.MAGENTA, Color.GREEN, Color.ORANGE};
        color = colors[(int) (Math.random() * colors.length)];
    }

    public void update() {
        // Memperbarui posisi partikel berdasarkan kecepatan
        x += vx;
        y += vy;
        life -= 0.001f; 

        // Mengatur ulang partikel jika keluar dari layar atau umur habis
        if (life <= 0 || x < -size || x > 1400 + size || y < -size || y > 900 + size) {
            reset();
        }
    }

    public void draw(Graphics2D g2d) {
        // Menggambar partikel dengan warna dan transparansi yang sesuai
        g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) (alpha * life * 255)));
        g2d.fillOval((int) x, (int) y, (int) size, (int) size); 
    }
}