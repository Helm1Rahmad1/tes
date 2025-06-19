package view.components.dialogs;

import javax.swing.*;

/**
 * Menyediakan fungsionalitas untuk menampilkan
 * dialog konfirmasi modern kepada pengguna. Dialog ini digunakan untuk meminta
 * pengguna mengonfirmasi suatu tindakan dengan memberikan dua opsi pilihan
 */
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
