package com.unpri.charactermanager.view;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.unpri.charactermanager.model.Character;
import com.unpri.charactermanager.model.CharacterManager;
import com.unpri.charactermanager.model.CharacterType;

public class MainWindow extends JFrame {

    private static final String VERSION = "v0.6";   // satu sumber untuk judul dan label versi

    private final CharacterManager manager;         // sekarang menerima manager, bukan list salinan

    private JPanel cards;                           // panel kartu, diisi ulang oleh refreshList()
    private JLabel countLabel;
    private JLabel statusInfo;
    private JLabel totalValue;
    private JLabel averageValue;
    private JLabel highestValue;
    private JLabel highCountValue;

    public MainWindow(CharacterManager manager) {
        super("Character Manager " + VERSION);
        this.manager = manager;

        initComponents();                           // 1. susun semua area
        refreshList();                              // 2. isi data awal
        pack();                                     // 3. ukuran awal = preferred size isi
        setMinimumSize(new Dimension(820, 460));    // 4. batas terkecil yang rapi
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);                // 5. pusatkan SETELAH pack()
    }

    private void initComponents() {
        getContentPane().setBackground(AppTheme.BG_MAIN);   // cegah celah abu-abu
        setLayout(new BorderLayout());

        add(createTopPanel(),    BorderLayout.NORTH);       // header + statistik
        add(new CharacterFormPanel(manager, this::refreshList), BorderLayout.WEST);   // form
        add(createListArea(),    BorderLayout.CENTER);      // satu-satunya yang tumbuh
        add(createLegendPanel(), BorderLayout.EAST);
        add(createStatusPanel(), BorderLayout.SOUTH);
    }

    /** Mengisi ulang daftar dan semua angka dari Model, lalu menata ulang tampilan. */
    private void refreshList() {
        ArrayList<Character> list = manager.getAllCharacters();

        cards.removeAll();
        for (Character c : list) {
            cards.add(createCharacterLabel(c));
        }

        countLabel.setText(list.size() + " karakter");
        totalValue.setText(String.valueOf(list.size()));
        averageValue.setText(String.format("%.2f", calculateAverageLevel(list)));
        highestValue.setText(String.valueOf(findHighestLevel(list)));
        highCountValue.setText(String.valueOf(countHighLevel(list)));
        statusInfo.setText("Total: " + list.size() + " karakter | Siap");

        cards.revalidate();   // minta Layout Manager menghitung ulang
        cards.repaint();      // gambar ulang hasilnya
    }

    private JPanel createTopPanel() {
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(AppTheme.BG_MAIN);
        top.add(createHeaderPanel(), BorderLayout.NORTH);
        top.add(createStatsPanel(),  BorderLayout.CENTER);
        return top;
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(AppTheme.BG_MAIN);
        header.setBorder(AppTheme.HEADER_BORDER);

        JLabel title = new JLabel("Character Manager");
        title.setFont(AppTheme.FONT_TITLE);
        title.setForeground(AppTheme.TEXT_MAIN);

        countLabel = new JLabel();
        countLabel.setFont(AppTheme.FONT_BODY);
        countLabel.setForeground(AppTheme.GOLD);

        header.add(title, BorderLayout.WEST);
        header.add(countLabel, BorderLayout.EAST);
        return header;
    }

    private JPanel createStatsPanel() {
        JPanel stats = new JPanel(new GridLayout(2, 2, 8, 8));
        stats.setBackground(AppTheme.BG_MAIN);
        stats.setBorder(AppTheme.STATS_BORDER);

        totalValue = new JLabel();
        averageValue = new JLabel();
        highestValue = new JLabel();
        highCountValue = new JLabel();

        stats.add(createStatCell("Total karakter", totalValue));
        stats.add(createStatCell("Rata-rata level", averageValue));
        stats.add(createStatCell("Level tertinggi", highestValue));
        stats.add(createStatCell("Level tinggi (>= " + AppTheme.HIGH_LEVEL_THRESHOLD + ")", highCountValue));
        return stats;
    }

    private JPanel createStatCell(String caption, JLabel valueLabel) {
        JPanel cell = new JPanel(new BorderLayout());
        cell.setBackground(AppTheme.BG_PANEL);
        cell.setBorder(AppTheme.CARD_BORDER);

        JLabel cap = new JLabel(caption);
        cap.setFont(AppTheme.FONT_BODY);
        cap.setForeground(AppTheme.TEXT_MUTED);

        valueLabel.setFont(AppTheme.FONT_TITLE);
        valueLabel.setForeground(AppTheme.GOLD);

        cell.add(cap, BorderLayout.NORTH);
        cell.add(valueLabel, BorderLayout.CENTER);
        return cell;
    }

    private JScrollPane createListArea() {
        cards = new JPanel(new GridLayout(0, 1, 0, 8));
        cards.setBackground(AppTheme.BG_MAIN);

        JPanel holder = new JPanel(new BorderLayout());
        holder.setBackground(AppTheme.BG_MAIN);
        holder.setBorder(AppTheme.LIST_BORDER);
        holder.add(cards, BorderLayout.NORTH);       // kartu mempertahankan tinggi alami

        JScrollPane scroll = new JScrollPane(holder);   // bungkus HOLDER
        scroll.setBorder(null);
        scroll.getViewport().setBackground(AppTheme.BG_MAIN);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setPreferredSize(new Dimension(460, 280));
        return scroll;
    }

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

    private JPanel createLegendPanel() {
        JPanel legend = new JPanel();
        legend.setLayout(new BoxLayout(legend, BoxLayout.Y_AXIS));   // target = panel ini sendiri
        legend.setBackground(AppTheme.BG_MAIN);
        legend.setBorder(AppTheme.LEGEND_BORDER);

        JLabel head = new JLabel("Legenda Role");
        head.setFont(AppTheme.FONT_BODY_BOLD);
        head.setForeground(AppTheme.TEXT_MAIN);
        legend.add(head);
        legend.add(Box.createVerticalStrut(8));

        for (CharacterType type : CharacterType.values()) {
            JLabel chip = new JLabel(type.name());
            chip.setOpaque(true);
            chip.setBackground(AppTheme.colorFor(type));
            chip.setForeground(AppTheme.TEXT_LIGHT);
            chip.setFont(AppTheme.FONT_MONO);
            chip.setBorder(AppTheme.CARD_BORDER);
            chip.setToolTipText(AppTheme.describe(type));
            legend.add(chip);
            legend.add(Box.createVerticalStrut(6));
        }
        legend.add(Box.createVerticalGlue());
        return legend;
    }

    private JPanel createStatusPanel() {
        JPanel status = new JPanel(new BorderLayout());
        status.setBackground(AppTheme.BG_STATUS);
        status.setBorder(AppTheme.STATUS_BORDER);

        statusInfo = new JLabel();
        statusInfo.setFont(AppTheme.FONT_MONO);
        statusInfo.setForeground(AppTheme.TEXT_MUTED);

        JLabel version = new JLabel(VERSION);
        version.setFont(AppTheme.FONT_MONO);
        version.setForeground(AppTheme.TEXT_MUTED);

        status.add(statusInfo, BorderLayout.WEST);
        status.add(version, BorderLayout.EAST);
        return status;
    }

    private double calculateAverageLevel(ArrayList<Character> list) {
        if (list.isEmpty()) {
            return 0;   // hindari pembagian dengan nol
        }
        int sum = 0;
        for (Character c : list) {
            sum += c.getLevel();
        }
        return (double) sum / list.size();
    }

    private int findHighestLevel(ArrayList<Character> list) {
        int max = 0;
        for (Character c : list) {
            if (c.getLevel() > max) {
                max = c.getLevel();
            }
        }
        return max;
    }

    private int countHighLevel(ArrayList<Character> list) {
        int count = 0;
        for (Character c : list) {
            if (c.getLevel() >= AppTheme.HIGH_LEVEL_THRESHOLD) {
                count++;
            }
        }
        return count;
    }
}