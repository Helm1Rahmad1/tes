package view.components.dialogs;

import javax.swing.*;

public class ConfirmationDialog {
    // Menampilkan dialog konfirmasi dengan dua opsi
    public static int showModernConfirmDialog(JFrame parent, String title, String message, String option1, String option2) {
        String[] options = {option1, option2};
        return JOptionPane.showOptionDialog( 
            parent, message, title,
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.QUESTION_MESSAGE,
            null, options, options[0]
        );
    }
}
