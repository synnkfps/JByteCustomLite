package me.synnk.Interface;

import me.synnk.Main;

import javax.swing.*;

public class Frame extends JFrame {
    public Integer width = 1400;
    public Integer height = 750;

    public void addMainComponents() {
        // Jar Class Directory
        JTree dir = new JTree();
        dir.setBounds(15, 5, 260, height-90);

        // File Name
        JLabel className = new JLabel("Current Class: ");
        className.setBounds(260+5+20, 15, 400, 10);

        // Decompiled class / method content
        JTextPane decompiled = new JTextPane();
        decompiled.setText("decompiled class stuff will appear here.");
        decompiled.setBounds(260+5+20, 5+30, width-315, (height-90)-30);

        // Decompilers
        String[] dec = {"CFR v0.152", "Procyon 1.0.0", "QuiltFlower", "FernFlower", "Jadx"};
        JComboBox<String> decompilers = new JComboBox<String>(dec);
        decompilers.setBounds(width-130, 10, 100, 20);

        // add stuff
        add(dir);
        add(decompiled);
        add(className);
        add(decompilers);
    }
    public void addMenuBar() {
        // Menu Bar
        JMenuBar stuff = new JMenuBar();

        JMenu file = new JMenu("File");
        JMenu about = new JMenu("About");

        JMenuItem open = new JMenuItem("Open Jar");
        JMenuItem close = new JMenuItem("Close Jar");

        JMenuItem exit = new JMenuItem("Exit");

        // adding
        stuff.add(file);
        stuff.add(about);

        file.add(open);
        file.add(close);
        file.add(exit);

        // Actions
        exit.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Thank you for using JByteCustom Lite!");
            dispose();
        });

        setJMenuBar(stuff);
    }

    public Frame() {
        addMenuBar();
        addMainComponents();

        setLayout(null); // ill change to gridbaglayout in future
        setTitle(Main.NAME + " " + Main.VERSION); // to do: show opened jar file
        setLocation(200, 200);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(width, height);
        setVisible(true);
    }

    public static void main(String[] args) {
        new Frame();
    }
}
