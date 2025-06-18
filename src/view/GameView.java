package view;

import viewmodel.GameViewModel;
import view.components.Ball;

import utils.GameConstants;

import javax.swing.*;
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import utils.AssetLoader; // Tambahkan import ini

/**
 * GameView - Tampilan utama game dengan tema sihir
 * Bagian dari lapisan View dalam pola MVVM
 */
public class GameView extends JFrame {
    // Deklarasi variabel
    private GameViewModel viewModel;
    private MainMenuView mainMenuView;
    private GamePanel gamePanel;
    private Timer gameTimer;
    private boolean gameRunning;

    // Elemen tema sihir
    private float magicParticleTimer = 0.0f;
    private BufferedImage backgroundImage;
    private Clip gameMusic; 

    public GameView(String username, MainMenuView mainMenuView) {
        this.viewModel = new GameViewModel(username);
        this.mainMenuView = mainMenuView;
        this.gameRunning = true;

        initializeComponents();
        setupEventHandlers();
        playGameMusic();
        startGameLoop();
    }

    private void initializeComponents() {
        // Inisialisasi komponen GUI
        setTitle("🔮 Collect The Magical Gems - " + viewModel.getCurrentUsername() + " 🔮");
        setSize(GameConstants.WINDOW_WIDTH, GameConstants.WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        gamePanel = new GamePanel();
        add(gamePanel);

        setFocusable(true);
        requestFocus();
    }

    private void setupEventHandlers() {
        // Menambahkan event handler untuk input keyboard
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // Tangani tombol spasi untuk mengakhiri permainan
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    viewModel.stopGame(); 
                    gameRunning = false; 
                    gamePanel.repaint();
                    SwingUtilities.invokeLater(() -> {

                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException ex) {
                            Thread.currentThread().interrupt();
                        }
                        returnToMainMenu();
                    });
                } else {
                    viewModel.handleKeyPress(e.getKeyCode());
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // Tangani pelepasan tombol
                viewModel.handleKeyRelease(e.getKeyCode());
            }
        });

        // Menambahkan event handler untuk klik mouse
        gamePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    System.out.println("Sihir dilempar ke: (" + e.getX() + ", " + e.getY() + ")");
                    viewModel.handleMouseClick(e.getX(), e.getY());
                }
            }
        });

        // Menambahkan event handler untuk penutupan jendela
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                returnToMainMenu();
            }
        });

        // Menambahkan event handler untuk fokus jendela
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (gameRunning) {
                    viewModel.resumeGame();
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (gameRunning) {
                    viewModel.pauseGame();
                }
            }
        });
    }

    private void startGameLoop() {
        // Memulai loop permainan
        gameTimer = new Timer(GameConstants.FRAME_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameRunning) {
                    if (viewModel.getGameData().isGameOver()) {
                        gameRunning = false;
                        gamePanel.repaint();
                        SwingUtilities.invokeLater(() -> {
                            // Tidak perlu memanggil drawGameOverOverlay secara manual di sini lagi
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException ex) {
                                Thread.currentThread().interrupt();
                            }
                            returnToMainMenu();
                        });
                    } else {
                        viewModel.update();
                        magicParticleTimer += 0.1f;
                        gamePanel.repaint();
                    }
                }
            }
        });
        gameTimer.start();
    }

    private void playGameMusic() {
        // Memutar musik latar permainan
        try {
            System.out.println("Memutar musik sihir...");
            AudioInputStream audioStream = AssetLoader.loadAudio(AssetLoader.AUDIO_GAME_MUSIC);

            if (audioStream != null) {
                gameMusic = AudioSystem.getClip();
                gameMusic.open(audioStream);
                gameMusic.loop(Clip.LOOP_CONTINUOUSLY);
            } else {
                System.err.println("Kesalahan memutar musik sihir: Audio stream kosong.");
            }
        } catch (Exception e) {
            System.err.println("Kesalahan memutar musik sihir: " + e.getMessage());
        }
    }

    private void stopGameMusic() {
        // Menghentikan musik latar permainan
        if (gameMusic != null && gameMusic.isRunning()) {
            gameMusic.stop();
            gameMusic.close();
        }
    }

    private void returnToMainMenu() {
        // Kembali ke menu utama
        stopGameMusic();
        gameRunning = false;
        if (gameTimer != null) {
            gameTimer.stop();
        }

        viewModel.resetGame();

        mainMenuView.showMainMenu();
        dispose();
    }

    /**
     * Panel permainan dengan tema sihir
     */
    private class GamePanel extends JPanel {
        public GamePanel() {
            // Mengatur latar belakang panel
            setBackground(new Color(25, 25, 50)); 
            setPreferredSize(new Dimension(GameConstants.WINDOW_WIDTH, GameConstants.WINDOW_HEIGHT));
        }

        @Override
        protected void paintComponent(Graphics g) {
            // Menggambar komponen panel
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            drawMagicalBackground(g2d);
            drawGame(g2d);

            g2d.dispose();
        }

        private void drawMagicalBackground(Graphics2D g2d) {
            // Menggambar latar belakang dengan efek sihir
            GradientPaint bgGradient = new GradientPaint(
                0, 0, new Color(15, 15, 40),
                0, getHeight(), new Color(40, 15, 60)
            );
            g2d.setPaint(bgGradient);
            g2d.fillRect(0, 0, getWidth(), getHeight());

            // Partikel sihir melayang
            drawMagicalParticles(g2d);

            // Bingkai sihir
            g2d.setColor(new Color(100, 50, 200, 100));
            g2d.setStroke(new BasicStroke(3));
            g2d.drawRect(10, 10, getWidth() - 20, getHeight() - 20);
        }

        private void drawMagicalParticles(Graphics2D g2d) {
            // Menggambar partikel sihir
            g2d.setColor(new Color(255, 255, 255, 50));

            for (int i = 0; i < 20; i++) {
                int x = (int)(Math.sin(magicParticleTimer + i) * 50 + getWidth() / 2 + i * 30);
                int y = (int)(Math.cos(magicParticleTimer * 0.7 + i * 0.5) * 30 + getHeight() / 2 + i * 20);

                x = Math.max(0, Math.min(getWidth(), x));
                y = Math.max(0, Math.min(getHeight(), y));

                g2d.fillOval(x, y, 3, 3);

                // Efek kilauan
                g2d.setColor(new Color(255, 255, 255, 30));
                g2d.drawLine(x - 5, y, x + 5, y);
                g2d.drawLine(x, y - 5, x, y + 5);
                g2d.setColor(new Color(255, 255, 255, 50));
            }
        }

        private void drawGame(Graphics2D g2d) {
            // Menggambar elemen permainan
            List<Ball> balls = viewModel.getBalls();
            for (Ball ball : balls) {
                ball.render(g2d);
            }

            viewModel.getCharacter().render(g2d);
            viewModel.getLasso().render(g2d);

            drawMagicalBasket(g2d);
            drawMagicalUI(g2d);
            drawGemLeaderboard(g2d);

            if (viewModel.isGamePaused()) {
                drawMagicalPauseOverlay(g2d);
            }

            if (viewModel.getGameData().isGameOver()) {
                drawMagicalGameOverOverlay(g2d);
            }
        }

        private void drawMagicalBasket(Graphics2D g2d) {
            // Keranjang sihir dengan efek kilauan
            int basketX = GameConstants.BASKET_X;
            int basketY = GameConstants.BASKET_Y;
            int basketW = GameConstants.BASKET_WIDTH;
            int basketH = GameConstants.BASKET_HEIGHT;

            // Efek kilauan
            g2d.setColor(new Color(255, 215, 0, 50));
            g2d.fillRect(basketX - 10, basketY - 10, basketW + 20, basketH + 20);

            // Keranjang utama dengan gradien
            GradientPaint basketGradient = new GradientPaint(
                basketX, basketY, new Color(139, 69, 19),
                basketX + basketW, basketY + basketH, new Color(160, 82, 45)
            );
            g2d.setPaint(basketGradient);
            g2d.fillRect(basketX, basketY, basketW, basketH);

            // Bingkai sihir
            g2d.setColor(new Color(255, 215, 0));
            g2d.setStroke(new BasicStroke(3));
            g2d.drawRect(basketX, basketY, basketW, basketH);

            // Simbol sihir pada keranjang
            g2d.setColor(new Color(255, 215, 0, 150));
            g2d.setFont(new Font("Serif", Font.BOLD, 16));
            g2d.drawString("✨", basketX + basketW/2 - 8, basketY + basketH/2 + 5);
        }

        private void drawMagicalUI(Graphics2D g2d) {
            // Panel UI sihir
            g2d.setColor(new Color(0, 0, 0, 100));
            g2d.fillRoundRect(10, 10, 250, 80, 15, 15);

            g2d.setColor(new Color(255, 215, 0));
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRoundRect(10, 10, 250, 80, 15, 15);

            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Serif", Font.BOLD, 18));

            String scoreText = "✨ Skor: " + viewModel.getCurrentScore();
            g2d.drawString(scoreText, 20, 35);

            String timeText = "⏰ Waktu: " + viewModel.getTimeRemaining() + "s";
            g2d.drawString(timeText, 20, 60);

            String gemText = "💎 Permata: " + viewModel.getCurrentCount();
            g2d.drawString(gemText, 20, 80);
        }

        private void drawGemLeaderboard(Graphics2D g2d) {
            // Panel papan peringkat permata
            int panelX = getWidth() - 200;
            int panelY = 10;
            int panelW = 180;
            int panelH = 300; // Sesuaikan tinggi untuk gambar

            g2d.setColor(new Color(0, 0, 0, 120));
            g2d.fillRoundRect(panelX, panelY, panelW, panelH, 15, 15);

            g2d.setColor(new Color(255, 215, 0));
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRoundRect(panelX, panelY, panelW, panelH, 15, 15);

            // Judul
            g2d.setColor(Color.YELLOW);
            g2d.setFont(new Font("Serif", Font.BOLD, 14));
            g2d.drawString("🏆 Nilai Permata 🏆", panelX + 10, panelY + 20);

            // Peringkat permata dengan gambar
            g2d.setFont(new Font("Serif", Font.PLAIN, 12));
            Object[][] gemInfo = {
                {AssetLoader.GOLDEN_GEM, "Permata Emas: 90"}, 
                {AssetLoader.FROST_GEM, "Permata Es: 80pts"},
                {AssetLoader.PURPLE_GEM, "Permata Ungu: 70pts"},
                {AssetLoader.RUBY_GEM, "Permata Ruby: 60pts"},
                {AssetLoader.CYAN_GEM, "Permata Cyan: 50pts"},
                {AssetLoader.SAPPHIRE_GEM, "Permata Safir: 40pts"},
                {AssetLoader.EMERALD_GEM, "Permata Zamrud: 30pts"},
                {AssetLoader.ORANGE_GEM, "Permata Oranye: 20pts"},
                {AssetLoader.BLACK_GEM, "Permata Hitam: Bom"}
            };

            int yPos = panelY + 40;
            for (Object[] info : gemInfo) {
                if (yPos > panelY + panelH - 15) break; 
                BufferedImage gemImage = AssetLoader.loadImage((String) info[0]);

                if (gemImage != null) {
                    g2d.drawImage(gemImage, panelX + 10, yPos, 20, 20, null);
                } else {
                    System.err.println("Gagal memuat gambar permata untuk: " + info[1]);
                }
                g2d.drawString((String) info[1], panelX + 40, yPos + 15);
                yPos += 30; 
            }
        }

        private void drawMagicalPauseOverlay(Graphics2D g2d) {
            // Overlay semi-transparan
            g2d.setColor(new Color(0, 0, 0, 150));
            g2d.fillRect(0, 0, getWidth(), getHeight());

            // Bingkai sihir
            g2d.setColor(new Color(255, 215, 0, 100));
            g2d.setStroke(new BasicStroke(5));
            g2d.drawRect(100, 100, getWidth() - 200, getHeight() - 200);

            // Teks jeda dengan gaya sihir
            g2d.setColor(Color.YELLOW);
            g2d.setFont(new Font("Serif", Font.BOLD, 48));
            String pauseText = "⏸️ DIJEDA ⏸️";
            FontMetrics fm = g2d.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(pauseText)) / 2;
            int y = getHeight() / 2;

            // Efek bayangan teks
            g2d.setColor(new Color(0, 0, 0, 100));
            g2d.drawString(pauseText, x + 3, y + 3);

            // Teks utama
            g2d.setColor(Color.YELLOW);
            g2d.drawString(pauseText, x, y);

            // Subjudul
            g2d.setFont(new Font("Serif", Font.BOLD, 20));
            String subtitleText = "✨ Klik untuk melanjutkan perjalanan sihir Anda ✨";
            fm = g2d.getFontMetrics();
            x = (getWidth() - fm.stringWidth(subtitleText)) / 2;
            g2d.drawString(subtitleText, x, y + 60);
        }

        private void drawMagicalGameOverOverlay(Graphics2D g2d) {
            // Overlay semi-transparan sihir
            g2d.setColor(new Color(20, 20, 60, 180));
            g2d.fillRect(0, 0, getWidth(), getHeight());

            // Bingkai sihir dengan animasi
            g2d.setColor(new Color(255, 215, 0, (int)(100 + 50 * Math.sin(magicParticleTimer))));
            g2d.setStroke(new BasicStroke(8));
            g2d.drawRect(50, 50, getWidth() - 100, getHeight() - 100);

            // Bingkai sihir bagian dalam
            g2d.setColor(new Color(138, 43, 226, 150));
            g2d.setStroke(new BasicStroke(4));
            g2d.drawRect(70, 70, getWidth() - 140, getHeight() - 140);

            // Judul Game Over dengan efek kilauan
            g2d.setFont(new Font("Serif", Font.BOLD, 42));
            String gameOverText = "🔮 Perjalanan Sihir Berakhir 🔮";
            FontMetrics fm = g2d.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(gameOverText)) / 2;
            int y = getHeight() / 2 - 80;

            // Gambar bayangan terlebih dahulu
            g2d.setColor(new Color(0, 0, 0, 100));
            g2d.drawString(gameOverText, x + 3, y + 3);

            // Gambar teks utama di atas bayangan
            g2d.setColor(Color.YELLOW);
            g2d.drawString(gameOverText, x, y);

            // Tampilan skor
            g2d.setFont(new Font("Serif", Font.BOLD, 28));
            String scoreText = "✨ Skor Akhir: " + viewModel.getCurrentScore() + " ✨";
            fm = g2d.getFontMetrics();
            x = (getWidth() - fm.stringWidth(scoreText)) / 2;
            g2d.setColor(new Color(255, 215, 0));
            g2d.drawString(scoreText, x, y + 70);

            // Tampilan permata yang dikumpulkan
            g2d.setFont(new Font("Serif", Font.BOLD, 24));
            String gemsText = "💎 Permata Sihir yang Dikumpulkan: " + viewModel.getCurrentCount() + " 💎";
            fm = g2d.getFontMetrics();
            x = (getWidth() - fm.stringWidth(gemsText)) / 2;
            g2d.setColor(new Color(255, 192, 203));
            g2d.drawString(gemsText, x, y + 110);

            // Pesan kembali
            g2d.setFont(new Font("Serif", Font.ITALIC, 18));
            String returnText = "🌟 Kembali ke Alam Sihir... 🌟";
            fm = g2d.getFontMetrics();
            x = (getWidth() - fm.stringWidth(returnText)) / 2;
            g2d.setColor(Color.WHITE);
            g2d.drawString(returnText, x, y + 160);

            // Partikel kilauan animasi di sekitar teks
            drawGameOverSparkles(g2d, x, y);
        }

        private void drawGameOverSparkles(Graphics2D g2d, int centerX, int centerY) {
            // Menggambar kilauan animasi
            g2d.setColor(new Color(255, 255, 255, (int)(150 + 100 * Math.sin(magicParticleTimer * 2))));

            for (int i = 0; i < 12; i++) {
                double angle = (magicParticleTimer + i * 0.5) * 0.8;
                int sparkleX = (int)(centerX + Math.cos(angle) * (80 + 20 * Math.sin(magicParticleTimer + i)));
                int sparkleY = (int)(centerY + Math.sin(angle) * (60 + 15 * Math.cos(magicParticleTimer + i)));

                // Pastikan kilauan tetap dalam batas
                sparkleX = Math.max(20, Math.min(getWidth() - 20, sparkleX));
                sparkleY = Math.max(20, Math.min(getHeight() - 20, sparkleY));

                // Menggambar kilauan
                g2d.fillOval(sparkleX - 2, sparkleY - 2, 4, 4);

                // Menggambar sinar kilauan
                g2d.setStroke(new BasicStroke(1));
                g2d.drawLine(sparkleX - 6, sparkleY, sparkleX + 6, sparkleY);
                g2d.drawLine(sparkleX, sparkleY - 6, sparkleX, sparkleY + 6);
                g2d.drawLine(sparkleX - 4, sparkleY - 4, sparkleX + 4, sparkleY + 4);
                g2d.drawLine(sparkleX - 4, sparkleY + 4, sparkleX + 4, sparkleY - 4);
            }
        }
    }

    // Metode publik untuk akses eksternal
    public void pauseGame() {
        // Menjeda permainan
        if (gameRunning) {
            viewModel.pauseGame();
            gamePanel.repaint();
        }
    }

    public void resumeGame() {
        // Melanjutkan permainan
        if (gameRunning) {
            viewModel.resumeGame();
            requestFocus(); 
        }
    }

    public boolean isGameRunning() {
        return gameRunning;
    }

    public GameViewModel getViewModel() {
        return viewModel;
    }

    // Membersihkan sumber daya saat permainan berakhir
    @Override
    public void dispose() {
        stopGameMusic();
        if (gameTimer != null) {
            gameTimer.stop();
        }
        gameRunning = false;
        super.dispose();
    }
}