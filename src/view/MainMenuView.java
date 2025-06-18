package view;

import viewmodel.MainMenuViewModel;
import model.Player;
import utils.GameConstants;
import view.components.animations.Particle;
import view.components.animations.ParticleBackgroundPanel;
import view.components.dialogs.ConfirmationDialog;
import view.components.dialogs.NotificationDialog;
import view.components.ui.ModernTextField;
import view.components.ui.UltraModernButton;
import view.components.ui.UltraModernScrollBarUI;
import view.components.ui.UltraModernTableRenderer;
import view.components.panels.GlassmorphismCard;
import view.components.panels.TransparentPanel;
import view.constants.ButtonStyle;
import view.constants.ColorConstants;
import view.constants.FontConstants;
import view.constants.UIConstants;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.sound.sampled.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import utils.AssetLoader;

/**
 * MainMenuView - Ultra Modern Game Menu Interface
 * Fitur: Desain Neumorphism, efek partikel, animasi canggih
 * * Peningkatan:
 * - Desain hybrid Neumorphism/Glassmorphism
 * - Sistem partikel animasi latar belakang
 * - Efek hover canggih dan mikro-interaksi
 * - Tata letak berbasis kartu modern
 * - Hierarki visual yang ditingkatkan
 * - Transisi halus dan animasi pegas
 */
public class MainMenuView extends JFrame {

    // Konstanta animasi
    private static final int PARTICLE_COUNT = 50;

    // Komponen inti
    private MainMenuViewModel viewModel;
    private JTable scoreTable;
    private DefaultTableModel tableModel;
    private ModernTextField usernameField;
    private UltraModernButton playButton;
    private UltraModernButton quitButton;
    private UltraModernButton refreshButton;
    private Clip backgroundMusic;
    private BufferedImage backgroundImage;

    // Animasi dan efek
    private Timer particleTimer;
    private java.util.List<Particle> particles;
    private Timer fadeTimer;
    private float globalOpacity = 0.0f;
    private boolean isInitialized = false;

    public MainMenuView() {
        this.viewModel = new MainMenuViewModel();
        initializeParticleSystem();
        loadResources();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadData();
        playBackgroundMusic();
        startEntranceAnimation();
    }

    /**
     * Inisialisasi sistem partikel untuk efek latar belakang
     */
    private void initializeParticleSystem() {
        particles = new ArrayList<>();
        for (int i = 0; i < PARTICLE_COUNT; i++) {
            particles.add(new Particle());
        }

        particleTimer = new Timer(32, e -> {
            for (Particle p : particles) {
                p.update();
            }
            repaint();
        });
        particleTimer.start();
    }

    /**
     * Memuat sumber daya dengan sistem cadangan
     */
    private void loadResources() {
        backgroundImage = AssetLoader.loadImage(AssetLoader.BG_MENU_IMAGE);
        
        if (backgroundImage != null) {
            System.out.println("✨ Gambar latar belakang berhasil dimuat");
        } else {
            System.err.println("⚠ Gagal memuat gambar latar belakang.");
        }
    }

    /**
     * Inisialisasi komponen dengan gaya ultra-modern
     */
    private void initializeComponents() {
        // Konfigurasi jendela
        setTitle("🔮 Collect The Magical Gems 🔮");
        setSize(GameConstants.WINDOW_WIDTH, GameConstants.WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Aktifkan grafik canggih
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");


        try {
        } catch (UnsupportedOperationException e) {
            System.err.println("Transparansi canggih tidak didukung.");
        }

        initializeTable();
        initializeInputComponents();
        initializeButtons();
    }

    /**
     * Inisialisasi tabel ultra-modern dengan gaya canggih
     */
    private void initializeTable() {
        String[] columnHeaders = {"🎯 Player", "🏆 High Score", "🎮 Games"};
        tableModel = new DefaultTableModel(columnHeaders, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? String.class : Integer.class;
            }
        };

        scoreTable = new JTable(tableModel);
        scoreTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scoreTable.setRowHeight(45);
        scoreTable.setShowGrid(false);
        scoreTable.setIntercellSpacing(new Dimension(0, 0));

        // Gaya tabel ultra-modern
        scoreTable.setBackground(new Color(0, 0, 0, 0));
        scoreTable.setOpaque(false);
        scoreTable.setForeground(ColorConstants.TEXT_PRIMARY);
        scoreTable.setFont(FontConstants.FONT_BODY);
        scoreTable.setSelectionBackground(new Color(ColorConstants.ACCENT_BLUE.getRed(), ColorConstants.ACCENT_BLUE.getGreen(), ColorConstants.ACCENT_BLUE.getBlue(), 40));
        scoreTable.setSelectionForeground(ColorConstants.TEXT_PRIMARY);

        // Setel UltraModernTableRenderer untuk setiap kolom secara eksplisit.
        for (int i = 0; i < columnHeaders.length; i++) {
            scoreTable.getColumnModel().getColumn(i).setCellRenderer(new UltraModernTableRenderer());
        }

        // Header modern kustom
        JTableHeader header = scoreTable.getTableHeader();
        header.setOpaque(false);
        header.setBackground(new Color(147, 0, 211, 90)); 
        header.setForeground(ColorConstants.TEXT_PRIMARY);
        header.setFont(FontConstants.FONT_SUBHEADING);
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(0, 55));
        header.setBorder(BorderFactory.createEmptyBorder());

        // Lebar kolom yang dioptimalkan
        scoreTable.getColumnModel().getColumn(0).setPreferredWidth(400);
        scoreTable.getColumnModel().getColumn(1).setPreferredWidth(250);
        scoreTable.getColumnModel().getColumn(2).setPreferredWidth(200);

    }

    /**
     * Inisialisasi komponen input modern
     */
    private void initializeInputComponents() {
        usernameField = new ModernTextField("Masukkan nama pemain Anda...");
    }

    /**
     * Inisialisasi tombol ultra-modern
     */
    private void initializeButtons() {
        playButton = new UltraModernButton("MULAI GAME", ColorConstants.ACCENT_GREEN, ButtonStyle.PRIMARY);
        quitButton = new UltraModernButton("KELUAR", ColorConstants.ACCENT_RED, ButtonStyle.SECONDARY);
        refreshButton = new UltraModernButton("🔄", ColorConstants.ACCENT_BLUE, ButtonStyle.ICON);

        playButton.setPreferredSize(new Dimension(200, 60));
        quitButton.setPreferredSize(new Dimension(150, 60));
        refreshButton.setPreferredSize(new Dimension(50, 50));
    }

    /**
     * Atur sistem tata letak ultra-modern
     */
    private void setupLayout() {
        setLayout(new BorderLayout());

        // Kontainer utama dengan latar belakang partikel
        JPanel mainContainer = new ParticleBackgroundPanel(backgroundImage, particles, () -> isInitialized, () -> globalOpacity);
        mainContainer.setLayout(new BorderLayout(0, 30));
        mainContainer.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));

        // Bagian hero
        JPanel heroSection = createHeroSection();

        // Bagian statistik (tabel)
        JPanel statsSection = createStatsSection();

        // Bagian kontrol (input & tombol)
        JPanel controlSection = createControlSection();

        mainContainer.add(heroSection, BorderLayout.NORTH);
        mainContainer.add(statsSection, BorderLayout.CENTER);
        mainContainer.add(controlSection, BorderLayout.SOUTH);

        add(mainContainer, BorderLayout.CENTER);
    }

    /**
     * Buat bagian hero dengan judul animasi
     */
    private JPanel createHeroSection() {
        JPanel hero = new TransparentPanel();
        hero.setLayout(new BorderLayout());
        hero.setPreferredSize(new Dimension(0, 120));

        // Judul animasi dengan efek glow
        JLabel titleLabel = new JLabel("⚡ COLLECT THE SKILL BALLS", SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                // Efek glow
                g2d.setColor(new Color(ColorConstants.ACCENT_BLUE.getRed(), ColorConstants.ACCENT_BLUE.getGreen(), ColorConstants.ACCENT_BLUE.getBlue(), 50));
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - 2;

                for (int i = 0; i < 5; i++) {
                    g2d.drawString(getText(), x + i, y + i);
                    g2d.drawString(getText(), x - i, y - i);
                }

                // Teks utama
                g2d.setColor(ColorConstants.TEXT_PRIMARY);
                g2d.drawString(getText(), x, y);

                g2d.dispose();
            }
        };
        titleLabel.setFont(FontConstants.FONT_TITLE);
        titleLabel.setForeground(ColorConstants.TEXT_PRIMARY);

        hero.add(titleLabel, BorderLayout.CENTER);
        return hero;
    }

    /**
     * Buat bagian statistik dengan kartu glassmorphism
     */
    private JPanel createStatsSection() {
        JPanel statsContainer = new TransparentPanel();
        statsContainer.setLayout(new BorderLayout());

        // Kontainer kartu kaca
        JPanel glassCard = new GlassmorphismCard();
        glassCard.setLayout(new BorderLayout(20, 20));
        glassCard.setBorder(BorderFactory.createEmptyBorder(30, 35, 30, 35));

        // Header dengan tombol refresh
        JPanel headerPanel = new TransparentPanel();
        headerPanel.setLayout(new BorderLayout());

        JLabel statsTitle = new JLabel("🏆 HALL OF FAME");
        statsTitle.setFont(FontConstants.FONT_HEADING);
        statsTitle.setForeground(ColorConstants.TEXT_PRIMARY);

        JPanel refreshPanel = new TransparentPanel();
        refreshPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        refreshPanel.add(refreshButton);

        headerPanel.add(statsTitle, BorderLayout.WEST);
        headerPanel.add(refreshPanel, BorderLayout.EAST);

        // Kontainer tabel dengan scroll kustom
        JScrollPane scrollPane = new JScrollPane(scoreTable);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setPreferredSize(new Dimension(0, 300));

        // Scrollbar ultra-modern
        scrollPane.getVerticalScrollBar().setUI(new UltraModernScrollBarUI());
        scrollPane.getHorizontalScrollBar().setUI(new UltraModernScrollBarUI());

        glassCard.add(headerPanel, BorderLayout.NORTH);
        glassCard.add(scrollPane, BorderLayout.CENTER);

        statsContainer.add(glassCard, BorderLayout.CENTER);
        return statsContainer;
    }

    /**
     * Buat bagian kontrol dengan input modern dan tombol
     */
    private JPanel createControlSection() {
        JPanel controlContainer = new TransparentPanel();
        controlContainer.setLayout(new BorderLayout());
        controlContainer.setPreferredSize(new Dimension(0, 180));

        // Kartu input
        JPanel inputCard = new GlassmorphismCard();
        inputCard.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 25));

        // Bagian nama pemain
        JPanel nameSection = new TransparentPanel();
        nameSection.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 0));

        JLabel nameLabel = new JLabel("👤 Player Name:");
        nameLabel.setFont(FontConstants.FONT_SUBHEADING);
        nameLabel.setForeground(ColorConstants.TEXT_PRIMARY);

        nameSection.add(nameLabel);
        nameSection.add(usernameField);

        // Bagian tombol
        JPanel buttonSection = new TransparentPanel();
        buttonSection.setLayout(new FlowLayout(FlowLayout.CENTER, 25, 0));
        buttonSection.add(playButton);
        buttonSection.add(quitButton);

        inputCard.add(nameSection);
        inputCard.add(buttonSection);

        controlContainer.add(inputCard, BorderLayout.CENTER);
        return controlContainer;
    }

    /**
     * Atur penanganan event yang ditingkatkan
     */
    private void setupEventHandlers() {
        playButton.addActionListener(e -> handlePlayButtonWithAnimation());
        quitButton.addActionListener(e -> handleQuitButtonWithAnimation());
        refreshButton.addActionListener(e -> handleRefreshButtonWithAnimation());

        usernameField.addActionListener(e -> handlePlayButtonWithAnimation());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleQuitButtonWithAnimation();
            }
        });

        // Tetap pertahankan listener ini. Ini akan dipicu ketika seleksi diatur oleh klik satu kali.
        scoreTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = scoreTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String username = (String) tableModel.getValueAt(selectedRow, 0);
                    // Hapus karakter medal emoji sebelum mengatur teks
                    username = username.replaceAll("[^\\p{L}\\p{N}\\s]", "").trim();
                    usernameField.setText(username);
                }
            }
        });

        // Tambahkan blok kode ini untuk mendukung klik satu kali
        scoreTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Pastikan itu adalah klik kiri dan klik satu kali
                if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 1) {
                    int row = scoreTable.rowAtPoint(e.getPoint());
                    if (row >= 0) {
                        scoreTable.setRowSelectionInterval(row, row); 
                    }
                }
            }
        });
    }

    /**
     * Penanganan tombol play yang ditingkatkan dengan animasi pegas
     */
    private void handlePlayButtonWithAnimation() {
        String username = usernameField.getCleanText();

        if (username.isEmpty()) {
            NotificationDialog.showModernNotification(this, "Please enter your player name!", NotificationDialog.NotificationType.WARNING);
            usernameField.requestFocus();
            return;
        }

        if (username.length() < 2) {
            NotificationDialog.showModernNotification(this, "Player name must be at least 2 characters!", NotificationDialog.NotificationType.WARNING);
            usernameField.requestFocus();
            return;
        }

        // Status loading animasi
        playButton.setLoadingState(true);

        Timer loadingTimer = new Timer(1200, e -> {
            if (viewModel.startGame(username)) {
                stopBackgroundMusic();
                startExitAnimation(() -> {
                    setVisible(false);
                    SwingUtilities.invokeLater(() -> {
                        GameView gameView = new GameView(username, this);
                        gameView.setVisible(true);
                    });
                });
            }
            playButton.setLoadingState(false);
        });
        loadingTimer.setRepeats(false);
        loadingTimer.start();
    }

    /**
     * Penanganan tombol keluar yang ditingkatkan dengan konfirmasi
     */
    private void handleQuitButtonWithAnimation() {
        int choice = ConfirmationDialog.showModernConfirmDialog(
            this, "Ready to leave the game?", "See you next time! 👋", "Stay & Play", "Exit Game"
        );
        
        // Perhatikan ini:
        if (choice == JOptionPane.NO_OPTION) { 
            stopBackgroundMusic();
            startExitAnimation(() -> System.exit(0));
        }
    }

    /**
     * Penanganan tombol refresh yang ditingkatkan
     */
    private void handleRefreshButtonWithAnimation() {
        refreshButton.startSpinAnimation();

        Timer refreshTimer = new Timer(800, e -> {
            loadData();
            NotificationDialog.showModernNotification(this, "Hall of Fame updated! ✨", NotificationDialog.NotificationType.SUCCESS);
            refreshButton.stopSpinAnimation();
        });
        refreshTimer.setRepeats(false);
        refreshTimer.start();
    }

    /**
     * Memuat dan menyegarkan data
     */
    private void loadData() {
        viewModel.refreshData();
        updateTableWithAnimation();
    }

    /**
     * Perbarui tabel dengan animasi bertahap
     */
    private void updateTableWithAnimation() {
        tableModel.setRowCount(0);

        List<Player> players = viewModel.getPlayers();
        Timer staggerTimer = new Timer(UIConstants.STAGGER_ANIMATION_DELAY, null);

        staggerTimer.addActionListener(new ActionListener() {
            private int index = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (index < players.size()) {
                    Player player = players.get(index);
                    String medal = getMedalForRank(index);

                    Object[] rowData = {
                        medal + " " + player.getUsername(),
                        player.getScore(),
                        player.getCount()
                    };
                    tableModel.addRow(rowData);
                    index++;
                } else {
                    staggerTimer.stop();
                }

                scoreTable.revalidate();
                scoreTable.repaint();
            }
        });

        staggerTimer.start();
    }

    /**
     * Dapatkan emoji medali untuk peringkat pemain
     */
    private String getMedalForRank(int rank) {
        switch (rank) {
            case 0: return "🥇";
            case 1: return "🥈";
            case 2: return "🥉";
            case 3: return "🏆";
            case 4: return "⭐";
            default: return "🎯";
        }
    }

    /**
     * Musik latar belakang yang ditingkatkan
     */
    private void playBackgroundMusic() {
        try {
            AudioInputStream audioStream = AssetLoader.loadAudio(AssetLoader.AUDIO_MENU_MUSIC);
            
            if (audioStream != null) {
                backgroundMusic = AudioSystem.getClip();
                backgroundMusic.open(audioStream);
                backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
                System.out.println("🎵 Musik latar belakang dimulai");
            } else {
                System.err.println("🔇 Musik latar belakang tidak tersedia: Audio stream is null.");
            }
        } catch (Exception e) {
            System.err.println("🔇 Musik latar belakang tidak tersedia: " + e.getMessage());
        }
    }

    /**
     * Hentikan musik latar belakang
     */
    private void stopBackgroundMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
            backgroundMusic.close();
            backgroundMusic = null;
        }
    }

    /**
     * Mulai animasi masuk dengan fisika pegas
     */
    private void startEntranceAnimation() {
        globalOpacity = 0.0f;
        fadeTimer = new Timer(UIConstants.FADE_ANIMATION_DELAY_MS, e -> {
            globalOpacity += UIConstants.FADE_ANIMATION_STEP;
            if (globalOpacity >= 1.0f) {
                globalOpacity = 1.0f;
                fadeTimer.stop();
                isInitialized = true;
            }
            repaint();
        });
        fadeTimer.start();
    }

    /**
     * Mulai animasi keluar
     */
    private void startExitAnimation(Runnable callback) {
        globalOpacity = 1.0f;
        fadeTimer = new Timer(UIConstants.FADE_ANIMATION_DELAY_MS, e -> {
            globalOpacity -= UIConstants.FADE_ANIMATION_STEP;
            if (globalOpacity <= 0.0f) {
                globalOpacity = 0.0f;
                fadeTimer.stop();
                if (callback != null) {
                    callback.run();
                }
            }
            repaint();
        });
        fadeTimer.start();
    }

    /**
     * Tampilkan menu utama dengan animasi masuk
     */
    public void showMainMenu() {
        playBackgroundMusic();
        loadData();
        setVisible(true);
        startEntranceAnimation();
        toFront();
        requestFocus();
    }

    /**
     * Dapatkan nama pengguna saat ini
     */
    public String getCurrentUsername() {
        return usernameField.getCleanText();
    }

    /**
     * Bersihkan sumber daya
     */
    @Override
    public void dispose() {
        stopBackgroundMusic();
        if (particleTimer != null) particleTimer.stop();
        if (fadeTimer != null) fadeTimer.stop();
        super.dispose();
    }
}