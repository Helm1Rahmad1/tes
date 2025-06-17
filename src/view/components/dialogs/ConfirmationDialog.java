package view.components.dialogs;

import javax.swing.*;

public class ConfirmationDialog {

    /**
     * Menampilkan dialog konfirmasi modern dengan dua opsi kustom.
     * 
     * @param parent JFrame induk yang akan menjadi parent dari dialog ini.
     * @param title Judul dari dialog.
     * @param message Pesan yang akan ditampilkan di dialog.
     * @param option1 Label untuk opsi pertama.
     * @param option2 Label untuk opsi kedua.
     * @return Integer yang menunjukkan opsi yang dipilih pengguna (0 untuk opsi pertama, 1 untuk opsi kedua).
     */

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
