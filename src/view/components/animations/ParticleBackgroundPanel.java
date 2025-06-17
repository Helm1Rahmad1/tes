package view.components.animations;

import view.components.panels.TransparentPanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public class ParticleBackgroundPanel extends TransparentPanel {
    // Gambar latar belakang
    private BufferedImage backgroundImage;
    // Daftar partikel yang akan digambar
    private List<Particle> particles;
    // Supplier untuk mengecek apakah panel sudah diinisialisasi
    private BooleanSupplier isInitializedSupplier;
    // Supplier untuk mendapatkan nilai opacity global
    private Supplier<Float> globalOpacitySupplier;

    // Konstruktor untuk menginisialisasi panel dengan gambar latar belakang, partikel, dan supplier
    public ParticleBackgroundPanel(BufferedImage backgroundImage, List<Particle> particles, BooleanSupplier isInitializedSupplier, Supplier<Float> globalOpacitySupplier) {
        this.backgroundImage = backgroundImage;
        this.particles = particles;
        this.isInitializedSupplier = isInitializedSupplier;
        this.globalOpacitySupplier = globalOpacitySupplier;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        // Mengaktifkan anti-aliasing untuk kualitas gambar yang lebih baik
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Menggambar gambar latar belakang dengan opacity global
        if (backgroundImage != null) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, globalOpacitySupplier.get()));
            g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }

        // Menggambar partikel hanya jika panel sudah diinisialisasi
        if (isInitializedSupplier.getAsBoolean()) {
            g2d.setComposite(AlphaComposite.SrcOver); // Reset composite untuk partikel
            for (Particle particle : particles) {
                particle.draw(g2d); // Memanggil metode draw pada setiap partikel
            }
        }

        g2d.dispose(); // Membersihkan resource Graphics2D
    }
}