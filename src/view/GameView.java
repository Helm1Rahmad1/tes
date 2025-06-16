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
 * GameView - Tampilan game utama dengan tema sihir
 * Bagian dari View layer dalam MVVM pattern
 */
public class GameView extends JFrame {
    private GameViewModel viewModel;
    private MainMenuView mainMenuView;
    private GamePanel gamePanel;
    private Timer gameTimer;
    private boolean gameRunning;
    private Clip gameMusic;
    
    // Magic theme elements
    private float magicParticleTimer = 0.0f;
    private BufferedImage backgroundImage;

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
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    viewModel.stopGame(); // Memanggil stopGame di ViewModel untuk menangani logika game over dan saving
                    gameRunning = false; // Menghentikan loop di View
                    gamePanel.repaint();
                    SwingUtilities.invokeLater(() -> {
                        drawGameOverOverlay(gamePanel.getGraphics());
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
                viewModel.handleKeyRelease(e.getKeyCode());
            }
        });

        gamePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    System.out.println("Magic spell cast at: (" + e.getX() + ", " + e.getY() + ")");
                    viewModel.handleMouseClick(e.getX(), e.getY());
                }
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                returnToMainMenu();
            }
        });

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
        gameTimer = new Timer(GameConstants.FRAME_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameRunning) {
                    if (viewModel.getGameData().isGameOver()) {
                        gameRunning = false;
                        gamePanel.repaint();
                        SwingUtilities.invokeLater(() -> {
                            drawGameOverOverlay(gamePanel.getGraphics());
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

    private void saveGameResult() {
        // Metode ini sekarang kosong karena saving sudah dipindahkan ke ViewModel.stopGame()
        // dan dipicu saat game berakhir.
    }

    private void playGameMusic() {
        try {
            System.out.println("Playing magical music...");
            // Ganti string literal dengan konstanta dari AssetLoader
            AudioInputStream audioStream = AssetLoader.loadAudio(AssetLoader.AUDIO_GAME_MUSIC);
            
            if (audioStream != null) {
                gameMusic = AudioSystem.getClip();
                gameMusic.open(audioStream);
                gameMusic.loop(Clip.LOOP_CONTINUOUSLY);
            } else {
                System.err.println("Error playing magical music: Audio stream is null.");
            }
        } catch (Exception e) {
            System.err.println("Error playing magical music: " + e.getMessage());
        }
    }

    private void stopGameMusic() {
        if (gameMusic != null && gameMusic.isRunning()) {
            gameMusic.stop();
            gameMusic.close();
        }
    }

    private void returnToMainMenu() {
        stopGameMusic();
        gameRunning = false;
        if (gameTimer != null) {
            gameTimer.stop();
        }
        
        viewModel.resetGame();
        
        mainMenuView.showMainMenu();
        dispose();
    }

    private void drawGameOverOverlay(Graphics graphics) {
        Graphics2D g2d = (Graphics2D) graphics.create();

        // Magical game over overlay
        g2d.setColor(new Color(20, 20, 60, 180));
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Magical border effect
        g2d.setColor(new Color(255, 215, 0, 100));
        g2d.setStroke(new BasicStroke(5));
        g2d.drawRect(50, 50, getWidth() - 100, getHeight() - 100);

        g2d.setColor(Color.YELLOW);
        g2d.setFont(new Font("Serif", Font.BOLD, 36));
        String gameOverText = "🔮 Magical Journey Ends 🔮";
        FontMetrics fm = g2d.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(gameOverText)) / 2;
        int y = getHeight() / 2 - 50;
        g2d.drawString(gameOverText, x, y);

        g2d.setFont(new Font("Serif", Font.BOLD, 24));
        String scoreText = "✨ Final Score: " + viewModel.getCurrentScore() + " ✨";
        fm = g2d.getFontMetrics();
        x = (getWidth() - fm.stringWidth(scoreText)) / 2;
        g2d.drawString(scoreText, x, y + 60);

        g2d.dispose();
    }

    /**
     * Game Panel dengan tema sihir
     */
    private class GamePanel extends JPanel {
        public GamePanel() {
            setBackground(new Color(25, 25, 50)); // Dark magical background
            setPreferredSize(new Dimension(GameConstants.WINDOW_WIDTH, GameConstants.WINDOW_HEIGHT));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            drawMagicalBackground(g2d);
            drawGame(g2d);
            
            g2d.dispose();
        }

        private void drawMagicalBackground(Graphics2D g2d) {
            // Gradient background
            GradientPaint bgGradient = new GradientPaint(
                0, 0, new Color(15, 15, 40),
                0, getHeight(), new Color(40, 15, 60)
            );
            g2d.setPaint(bgGradient);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            
            // Floating magical particles
            drawMagicalParticles(g2d);
            
            // Magical border
            g2d.setColor(new Color(100, 50, 200, 100));
            g2d.setStroke(new BasicStroke(3));
            g2d.drawRect(10, 10, getWidth() - 20, getHeight() - 20);
        }

        private void drawMagicalParticles(Graphics2D g2d) {
            g2d.setColor(new Color(255, 255, 255, 50));
            
            for (int i = 0; i < 20; i++) {
                int x = (int)(Math.sin(magicParticleTimer + i) * 50 + getWidth() / 2 + i * 30);
                int y = (int)(Math.cos(magicParticleTimer * 0.7 + i * 0.5) * 30 + getHeight() / 2 + i * 20);
                
                x = Math.max(0, Math.min(getWidth(), x));
                y = Math.max(0, Math.min(getHeight(), y));
                
                g2d.fillOval(x, y, 3, 3);
                
                // Sparkle effect
                g2d.setColor(new Color(255, 255, 255, 30));
                g2d.drawLine(x - 5, y, x + 5, y);
                g2d.drawLine(x, y - 5, x, y + 5);
                g2d.setColor(new Color(255, 255, 255, 50));
            }
        }

        private void drawGame(Graphics2D g2d) {
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
            // Magical basket with glow effect
            int basketX = GameConstants.BASKET_X;
            int basketY = GameConstants.BASKET_Y;
            int basketW = GameConstants.BASKET_WIDTH;
            int basketH = GameConstants.BASKET_HEIGHT;
            
            // Glow effect
            g2d.setColor(new Color(255, 215, 0, 50));
            g2d.fillRect(basketX - 10, basketY - 10, basketW + 20, basketH + 20);
            
            // Main basket with gradient
            GradientPaint basketGradient = new GradientPaint(
                basketX, basketY, new Color(139, 69, 19),
                basketX + basketW, basketY + basketH, new Color(160, 82, 45)
            );
            g2d.setPaint(basketGradient);
            g2d.fillRect(basketX, basketY, basketW, basketH);

            // Magical border
            g2d.setColor(new Color(255, 215, 0));
            g2d.setStroke(new BasicStroke(3));
            g2d.drawRect(basketX, basketY, basketW, basketH);
            
            // Magical symbol on basket
            g2d.setColor(new Color(255, 215, 0, 150));
            g2d.setFont(new Font("Serif", Font.BOLD, 16));
            g2d.drawString("✨", basketX + basketW/2 - 8, basketY + basketH/2 + 5);
        }

        private void drawMagicalUI(Graphics2D g2d) {
            // Magical UI panel
            g2d.setColor(new Color(0, 0, 0, 100));
            g2d.fillRoundRect(10, 10, 250, 80, 15, 15);
            
            g2d.setColor(new Color(255, 215, 0));
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRoundRect(10, 10, 250, 80, 15, 15);
            
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Serif", Font.BOLD, 18));
            
            String scoreText = "✨ Score: " + viewModel.getCurrentScore();
            g2d.drawString(scoreText, 20, 35);
            
            String timeText = "⏰ Time: " + viewModel.getTimeRemaining() + "s";
            g2d.drawString(timeText, 20, 60);
            
            String gemText = "💎 Gems: " + viewModel.getCurrentCount();
            g2d.drawString(gemText, 20, 80);
        }

        private void drawGemLeaderboard(Graphics2D g2d) {
            // Gem leaderboard panel
            int panelX = getWidth() - 200;
            int panelY = 10;
            int panelW = 180;
            int panelH = 300; // Adjust height to fit images

            g2d.setColor(new Color(0, 0, 0, 120));
            g2d.fillRoundRect(panelX, panelY, panelW, panelH, 15, 15);

            g2d.setColor(new Color(255, 215, 0));
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRoundRect(panelX, panelY, panelW, panelH, 15, 15);

            // Title
            g2d.setColor(Color.YELLOW);
            g2d.setFont(new Font("Serif", Font.BOLD, 14));
            g2d.drawString("🏆 Gem Values 🏆", panelX + 10, panelY + 20);

            // Gem rankings with images
            g2d.setFont(new Font("Serif", Font.PLAIN, 12));
            Object[][] gemInfo = {
                {AssetLoader.GOLDEN_GEM, "Golden Gem: 90"}, // Ganti string literal dengan konstanta
                {AssetLoader.FROST_GEM, "Frost Gem: 80pts"},
                {AssetLoader.PURPLE_GEM, "Purple Gem: 70pts"},
                {AssetLoader.RUBY_GEM, "Ruby Gem: 60pts"},
                {AssetLoader.CYAN_GEM, "Cyan Gem: 50pts"},
                {AssetLoader.SAPPHIRE_GEM, "Sapphire Gem: 40pts"},
                {AssetLoader.EMERALD_GEM, "Emerald Gem: 30pts"},
                {AssetLoader.ORANGE_GEM, "Orange Gem: 20pts"},
                {AssetLoader.BLACK_GEM, "Black Gem: Bomb"}
            };

            int yPos = panelY + 40;
            for (Object[] info : gemInfo) {
                if (yPos > panelY + panelH - 15) break; // Prevent overflow
                BufferedImage gemImage = AssetLoader.loadImage((String) info[0]);
                
                if (gemImage != null) {
                    g2d.drawImage(gemImage, panelX + 10, yPos, 20, 20, null);
                } else {
                    System.err.println("Failed to load gem image for: " + info[1]);
                }
                g2d.drawString((String) info[1], panelX + 40, yPos + 15);
                yPos += 30; // Adjust spacing for images
            }
        }

        private void drawMagicalPauseOverlay(Graphics2D g2d) {
            // Semi-transparent overlay
            g2d.setColor(new Color(0, 0, 0, 150));
            g2d.fillRect(0, 0, getWidth(), getHeight());
            
            // Magical pause border
            g2d.setColor(new Color(255, 215, 0, 100));
            g2d.setStroke(new BasicStroke(5));
            g2d.drawRect(100, 100, getWidth() - 200, getHeight() - 200);
            
            // Pause text with magical styling
            g2d.setColor(Color.YELLOW);
            g2d.setFont(new Font("Serif", Font.BOLD, 48));
            String pauseText = "⏸️ PAUSED ⏸️";
            FontMetrics fm = g2d.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(pauseText)) / 2;
            int y = getHeight() / 2;
            
            // Text shadow effect
            g2d.setColor(new Color(0, 0, 0, 100));
            g2d.drawString(pauseText, x + 3, y + 3);
            
            // Main text
            g2d.setColor(Color.YELLOW);
            g2d.drawString(pauseText, x, y);
            
            // Subtitle
            g2d.setFont(new Font("Serif", Font.BOLD, 20));
            String subtitleText = "✨ Click to resume your magical journey ✨";
            fm = g2d.getFontMetrics();
            x = (getWidth() - fm.stringWidth(subtitleText)) / 2;
            g2d.drawString(subtitleText, x, y + 60);
        }

        private void drawMagicalGameOverOverlay(Graphics2D g2d) {
            // Semi-transparent magical overlay
            g2d.setColor(new Color(20, 20, 60, 180));
            g2d.fillRect(0, 0, getWidth(), getHeight());

            // Magical border effect with animation
            g2d.setColor(new Color(255, 215, 0, (int)(100 + 50 * Math.sin(magicParticleTimer))));
            g2d.setStroke(new BasicStroke(8));
            g2d.drawRect(50, 50, getWidth() - 100, getHeight() - 100);
            
            // Inner magical border
            g2d.setColor(new Color(138, 43, 226, 150));
            g2d.setStroke(new BasicStroke(4));
            g2d.drawRect(70, 70, getWidth() - 140, getHeight() - 140);

            // Game Over title with glow effect
            g2d.setColor(new Color(255, 255, 255, 100));
            g2d.setFont(new Font("Serif", Font.BOLD, 42));
            String gameOverText = "🔮 Magical Journey Ends 🔮";
            FontMetrics fm = g2d.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(gameOverText)) / 2;
            int y = getHeight() / 2 - 80;
            
            // Glow effect
            g2d.drawString(gameOverText, x + 2, y + 2);
            
            // Main title
            g2d.setColor(Color.YELLOW);
            g2d.drawString(gameOverText, x, y);

            // Score display
            g2d.setFont(new Font("Serif", Font.BOLD, 28));
            String scoreText = "✨ Final Score: " + viewModel.getCurrentScore() + " ✨";
            fm = g2d.getFontMetrics();
            x = (getWidth() - fm.stringWidth(scoreText)) / 2;
            g2d.setColor(new Color(255, 215, 0));
            g2d.drawString(scoreText, x, y + 70);

            // Gems collected display
            g2d.setFont(new Font("Serif", Font.BOLD, 24));
            String gemsText = "💎 Magical Gems Collected: " + viewModel.getCurrentCount() + " 💎";
            fm = g2d.getFontMetrics();
            x = (getWidth() - fm.stringWidth(gemsText)) / 2;
            g2d.setColor(new Color(255, 192, 203));
            g2d.drawString(gemsText, x, y + 110);

            // Return message
            g2d.setFont(new Font("Serif", Font.ITALIC, 18));
            String returnText = "🌟 Returning to the Magical Realm... 🌟";
            fm = g2d.getFontMetrics();
            x = (getWidth() - fm.stringWidth(returnText)) / 2;
            g2d.setColor(Color.WHITE);
            g2d.drawString(returnText, x, y + 160);
            
            // Animated sparkles around the text
            drawGameOverSparkles(g2d, x, y);
        }
        
        private void drawGameOverSparkles(Graphics2D g2d, int centerX, int centerY) {
            g2d.setColor(new Color(255, 255, 255, (int)(150 + 100 * Math.sin(magicParticleTimer * 2))));
            
            for (int i = 0; i < 12; i++) {
                double angle = (magicParticleTimer + i * 0.5) * 0.8;
                int sparkleX = (int)(centerX + Math.cos(angle) * (80 + 20 * Math.sin(magicParticleTimer + i)));
                int sparkleY = (int)(centerY + Math.sin(angle) * (60 + 15 * Math.cos(magicParticleTimer + i)));
                
                // Ensure sparkles stay within bounds
                sparkleX = Math.max(20, Math.min(getWidth() - 20, sparkleX));
                sparkleY = Math.max(20, Math.min(getHeight() - 20, sparkleY));
                
                // Draw sparkle
                g2d.fillOval(sparkleX - 2, sparkleY - 2, 4, 4);
                
                // Draw sparkle rays
                g2d.setStroke(new BasicStroke(1));
                g2d.drawLine(sparkleX - 6, sparkleY, sparkleX + 6, sparkleY);
                g2d.drawLine(sparkleX, sparkleY - 6, sparkleX, sparkleY + 6);
                g2d.drawLine(sparkleX - 4, sparkleY - 4, sparkleX + 4, sparkleY + 4);
                g2d.drawLine(sparkleX - 4, sparkleY + 4, sparkleX + 4, sparkleY - 4);
            }
        }
    }

    // Public methods for external access
    public void pauseGame() {
        if (gameRunning) {
            viewModel.pauseGame();
            gamePanel.repaint();
        }
    }

    public void resumeGame() {
        if (gameRunning) {
            viewModel.resumeGame();
            requestFocus(); // Ensure the game window has focus
        }
    }

    public boolean isGameRunning() {
        return gameRunning;
    }

    public GameViewModel getViewModel() {
        return viewModel;
    }

    // Clean up resources when game ends
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