package com.unpri.charactermanager.view;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.unpri.charactermanager.model.Character;
import com.unpri.charactermanager.model.CharacterType;

public class MainWindow extends JFrame {

    private static final String VERSION = "v0.5";   // satu sumber untuk judul dan label versi

    private ArrayList<Character> characterList;     // data dari Main.java (Model)

    public MainWindow(ArrayList<Character> characterList) {
        super("Character Manager " + VERSION);
        this.characterList = characterList;

        initComponents();                           // 1. susun semua area
        pack();                                     // 2. ukuran awal = preferred size isi
        setMinimumSize(new Dimension(560, 380));    // 3. batas terkecil yang rapi
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);                // 4. pusatkan SETELAH pack()
    }

    private void initComponents() {
        getContentPane().setBackground(AppTheme.BG_MAIN);   // cegah celah abu-abu
        setLayout(new BorderLayout());

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createListArea(),    BorderLayout.CENTER);      // satu-satunya yang tumbuh
    }

    /** Header (NORTH): judul di kiri, jumlah karakter di kanan. */
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(AppTheme.BG_MAIN);
        header.setBorder(AppTheme.HEADER_BORDER);

        JLabel title = new JLabel("Character Manager");
        title.setFont(AppTheme.FONT_TITLE);
        title.setForeground(AppTheme.TEXT_MAIN);

        JLabel count = new JLabel(characterList.size() + " karakter");
        count.setFont(AppTheme.FONT_BODY);
        count.setForeground(AppTheme.GOLD);

        header.add(title, BorderLayout.WEST);
        header.add(count, BorderLayout.EAST);
        return header;
    }

    /** Daftar berscroll (CENTER): kartu -> panel cards -> holder (NORTH) -> JScrollPane. */
    private JScrollPane createListArea() {
        JPanel cards = new JPanel(new GridLayout(0, 1, 0, 8));
        cards.setBackground(AppTheme.BG_MAIN);
        for (Character c : characterList) {
            cards.add(createCharacterLabel(c));      // ke PANEL, bukan ke scroll pane
        }

        JPanel holder = new JPanel(new BorderLayout());
        holder.setBackground(AppTheme.BG_MAIN);
        holder.setBorder(AppTheme.LIST_BORDER);
        holder.add(cards, BorderLayout.NORTH);       // kartu mempertahankan tinggi alami

        JScrollPane scroll = new JScrollPane(holder);   // bungkus HOLDER
        scroll.setBorder(null);
        scroll.getViewport().setBackground(AppTheme.BG_MAIN);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setPreferredSize(new Dimension(460, 280)); // cegah jendela raksasa
        return scroll;
    }

    /** Satu kartu karakter (sama seperti v0.4). */
    private JLabel createCharacterLabel(Character c) {
        String info = c.getName() + " | " + c.getType()
                    + " | Lv." + c.getLevel()
                    + " | HP:" + c.getHitPoints()
                    + " | ATK:" + c.getAttackPower();

        JLabel lbl = new JLabel(info);
        lbl.setBackground(AppTheme.colorFor(c.getType()));
        lbl.setOpaque(true);
        lbl.setBorder(AppTheme.CARD_BORDER);

        if (c.getLevel() >= AppTheme.HIGH_LEVEL_THRESHOLD) {
            lbl.setForeground(AppTheme.GOLD);
            lbl.setFont(AppTheme.FONT_BODY_BOLD);
        } else {
            lbl.setForeground(AppTheme.TEXT_LIGHT);
            lbl.setFont(AppTheme.FONT_BODY);
        }

        lbl.setToolTipText(AppTheme.describe(c.getType())
                + " | HP " + c.getHitPoints() + ", ATK " + c.getAttackPower());

        if (c.getLevel() <= AppTheme.DISABLED_LEVEL) {
            lbl.setEnabled(false);
        } else {
            lbl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
        return lbl;
    }
}