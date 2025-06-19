package view.components.panels;

import javax.swing.*;

/**
 * Turunan dari JPanel yang secara default
 * diatur untuk menjadi transparan, berguna sebagai wadah yang tidak terlihat
 * untuk elemen UI lain atau sebagai dasar untuk rendering 
 */
public class TransparentPanel extends JPanel {
    public TransparentPanel() {
        setOpaque(false); 
    }
}