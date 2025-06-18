package view.components.panels;

import javax.swing.*;

// Kelas TransparentPanel adalah turunan dari JPanel yang memiliki properti transparan
public class TransparentPanel extends JPanel {
    public TransparentPanel() {
        setOpaque(false); 
    }
}