package com.unpri.charactermanager.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.ParseException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;

import com.unpri.charactermanager.model.Character;
import com.unpri.charactermanager.model.CharacterManager;
import com.unpri.charactermanager.model.CharacterType;

/**
 * Form tambah karakter. Ruang nilai dibatasi lewat pilihan komponen:
 * teks bebas hanya untuk nama, role lewat JComboBox, level lewat JSpinner.
 */
public class CharacterFormPanel extends JPanel {

    private static final int NAME_MIN = 3;
    private static final int NAME_MAX = 20;
    private static final int PANEL_WIDTH = 240;

    private final CharacterManager manager;
    private final Runnable onAdded;                   // dipanggil setelah karakter berhasil ditambah

    private final JTextField nameField = new JTextField();
    private final JComboBox<CharacterType> roleBox = new JComboBox<>(CharacterType.values());
    private final JSpinner levelSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 99, 1));
    private final JLabel messageLabel = new JLabel(" ");
    private final Border originalFieldBorder;         // border asli, disimpan sebelum pernah diganti

    public CharacterFormPanel(CharacterManager manager, Runnable onAdded) {
        super(new BorderLayout());
        this.manager = manager;
        this.onAdded = onAdded;
        this.originalFieldBorder = nameField.getBorder();

        setBackground(AppTheme.BG_MAIN);
        setBorder(AppTheme.FORM_BORDER);

        JPanel grid = new JPanel(new GridLayout(0, 1, 0, 6));
        grid.setBackground(AppTheme.BG_MAIN);
        grid.add(createCaption("Tambah Karakter", AppTheme.FONT_BODY_BOLD));
        grid.add(createCaption("Nama (3-20 huruf)", AppTheme.FONT_BODY));
        grid.add(nameField);
        grid.add(createCaption("Role", AppTheme.FONT_BODY));
        grid.add(roleBox);
        grid.add(createCaption("Level (1-99)", AppTheme.FONT_BODY));
        grid.add(levelSpinner);

        JButton saveButton = new JButton("Simpan");
        saveButton.addActionListener(e -> onSave());   // ActionListener minimal (dibahas penuh di Pertemuan 9)
        grid.add(saveButton);

        messageLabel.setFont(AppTheme.FONT_BODY);
        add(grid, BorderLayout.NORTH);
        add(messageLabel, BorderLayout.CENTER);

        setPreferredSize(new Dimension(PANEL_WIDTH, getPreferredSize().height));
    }

    private JLabel createCaption(String text, Font font) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(font);
        lbl.setForeground(AppTheme.TEXT_MAIN);
        return lbl;
    }

    /** Alur simpan: bersihkan, validasi nama (View), baca spinner, buat Character (Model). */
    private void onSave() {
        clearMessage();

        String name = nameField.getText().trim();          // bersihkan spasi di tepi
        String problem = validateName(name);
        if (problem != null) {
            showError(problem);
            nameField.setBorder(AppTheme.ERROR_BORDER);     // sinyal tambahan selain warna teks
            nameField.requestFocusInWindow();               // arahkan pengguna kembali ke field
            return;
        }

        try {
            levelSpinner.commitEdit();                      // paksa teks yang sedang diketik menjadi nilai
        } catch (ParseException ex) {
            showError("Level harus angka bulat 1-99");
            levelSpinner.requestFocusInWindow();
            return;
        }

        CharacterType role = (CharacterType) roleBox.getSelectedItem();   // cast: item bertipe Object
        int level = (Integer) levelSpinner.getValue();                    // cast: nilai bertipe Object

        try {
            manager.addCharacter(new Character(name, role, level));
        } catch (IllegalArgumentException | IllegalStateException ex) {
            showError(ex.getMessage());                     // aturan Model ditampilkan apa adanya
            return;
        }

        nameField.setText("");
        levelSpinner.setValue(1);
        showSuccess("Karakter " + name + " berhasil ditambahkan");
        onAdded.run();                                      // minta jendela memperbarui daftar
    }

    /** Hanya MENGEMBALIKAN pesan (atau null). Tidak menyentuh komponen apa pun. */
       private String validateName(String name) {
        if (name.isEmpty()) {
            return "Nama wajib diisi";
        }
        if (name.length() < NAME_MIN || name.length() > NAME_MAX) {
            return "Nama harus " + NAME_MIN + "-" + NAME_MAX + " karakter";
        }
        if (!name.matches("[\\p{L} ]+")) {
            return "Nama hanya boleh huruf dan spasi";
        }
        if (name.contains("  ")) {
            return "Nama tidak boleh memuat spasi berurutan";
        }
        if (manager.isNameTaken(name)) {
            return "Nama sudah dipakai";
        }
        return null;
    }

    private void clearMessage() {
        messageLabel.setText(" ");
        nameField.setBorder(originalFieldBorder);           // pulihkan border asli
    }

    private void showError(String text) {
        setMessage(text, AppTheme.ERROR);
    }

    private void showSuccess(String text) {
        setMessage(text, AppTheme.SUCCESS);
    }

    private void setMessage(String text, Color color) {
        messageLabel.setForeground(color);
        messageLabel.setText("<html><body style='width:200px'>" + text + "</body></html>");
    }
}