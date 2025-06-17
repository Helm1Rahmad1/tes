package utils;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 * AssetLoader - Kelas utilitas untuk memuat sumber daya game (gambar, suara, dll.)
 * Memusatkan logika pemuatan aset dan penanganan kesalahan, serta jalur aset.
 */
public class AssetLoader {

    // --- Image Asset Paths ---
    public static final String BG_MENU_IMAGE = "/assets/images/Background/BackgroundMenu.png";

    // Character Sprites
    public static final String CHAR_DOWN_F1 = "/assets/images/Character/MainCharacter_LW_F1.png";
    public static final String CHAR_DOWN_F2 = "/assets/images/Character/MainCharacter_LW_F2.png";
    public static final String CHAR_DOWN_F3 = "/assets/images/Character/MainCharacter_LW_F3.png";
    public static final String CHAR_DOWN_F4 = "/assets/images/Character/MainCharacter_LW_F4.png";

    public static final String CHAR_UP_F1 = "/assets/images/Character/MainCharacter_LW_F5.png";
    public static final String CHAR_UP_F2 = "/assets/images/Character/MainCharacter_LW_F6.png";
    public static final String CHAR_UP_F3 = "/assets/images/Character/MainCharacter_LW_F7.png";
    public static final String CHAR_UP_F4 = "/assets/images/Character/MainCharacter_LW_F8.png";

    public static final String CHAR_LEFT_F1 = "/assets/images/Character/MainCharacter_LW_F9.png";
    public static final String CHAR_LEFT_F2 = "/assets/images/Character/MainCharacter_LW_F10.png";
    public static final String CHAR_LEFT_F3 = "/assets/images/Character/MainCharacter_LW_F11.png";
    public static final String CHAR_LEFT_F4 = "/assets/images/Character/MainCharacter_LW_F12.png";

    public static final String CHAR_RIGHT_F1 = "/assets/images/Character/MainCharacter_LW_F13.png";
    public static final String CHAR_RIGHT_F2 = "/assets/images/Character/MainCharacter_LW_F14.png";
    public static final String CHAR_RIGHT_F3 = "/assets/images/Character/MainCharacter_LW_F15.png";
    public static final String CHAR_RIGHT_F4 = "/assets/images/Character/MainCharacter_LW_F16.png";

    // Gem Images
    public static final String GOLDEN_GEM = "/assets/images/Gems/GoldenGem.png";
    public static final String FROST_GEM = "/assets/images/Gems/FrostGem.png";
    public static final String PURPLE_GEM = "/assets/images/Gems/PurpleGem.png";
    public static final String RUBY_GEM = "/assets/images/Gems/RubbyGem.png";
    public static final String CYAN_GEM = "/assets/images/Gems/CyanGem.png";
    public static final String SAPPHIRE_GEM = "/assets/images/Gems/SapphireGem.png";
    public static final String EMERALD_GEM = "/assets/images/Gems/EmeraldGem.png";
    public static final String ORANGE_GEM = "/assets/images/Gems/OrangeGem.png";
    public static final String BLACK_GEM = "/assets/images/Gems/Bomb.png";

    // --- Audio Asset Paths ---
    public static final String AUDIO_MENU_MUSIC = "/assets/sounds/menu_music.wav";
    public static final String AUDIO_GAME_MUSIC = "/assets/sounds/game_music.wav";
    public static final String AUDIO_GAME_OVER = "/assets/sounds/game_over.wav";


    /**
     * Memuat BufferedImage dari jalur yang diberikan.
     * @param path Jalur ke sumber daya gambar (misalnya "/assets/images/Background/BackgroundMenu.png").
     * @return BufferedImage yang dimuat, atau null jika gagal.
     */

    public static BufferedImage loadImage(String path) {
        try {
            URL imageUrl = AssetLoader.class.getResource(path);
            if (imageUrl == null) {
                System.err.println("Error: Image resource not found at " + path);
                return null;
            }
            return ImageIO.read(imageUrl);
        } catch (IOException e) {
            System.err.println("Error loading image " + path + ": " + e.getMessage());
            return null;
        }
    }

    /**
     * Memuat AudioInputStream dari jalur yang diberikan.
     * @param path Jalur ke sumber daya audio (misalnya "/assets/sounds/menu_music.wav").
     * @return AudioInputStream yang dimuat, atau null jika gagal.
     */

    public static AudioInputStream loadAudio(String path) {
        try {
            URL audioUrl = AssetLoader.class.getResource(path);
            if (audioUrl == null) {
                System.err.println("Error: Audio resource not found at " + path);
                return null;
            }
            return AudioSystem.getAudioInputStream(audioUrl);
        } catch (UnsupportedAudioFileException e) {
            System.err.println("Error loading audio " + path + ": Unsupported audio file type - " + e.getMessage());
            return null;
        } catch (IOException e) {
            System.err.println("Error loading audio " + path + ": " + e.getMessage());
            return null;
        }
    }

}