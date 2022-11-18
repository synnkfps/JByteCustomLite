package me.synnk.Interface;

import me.synnk.Main;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.net.URL;

public class Frame extends JFrame {
    public Integer width = 1400;
    public Integer height = 750;

    public void addMainComponents() {
        // Jar Class Directory
        // Testing
        DefaultMutableTreeNode style=new DefaultMutableTreeNode("testJar");
        DefaultMutableTreeNode color=new DefaultMutableTreeNode("me");
        DefaultMutableTreeNode font=new DefaultMutableTreeNode("synnk");
        DefaultMutableTreeNode e = new DefaultMutableTreeNode("JByteCustomLite");
        DefaultMutableTreeNode w = new DefaultMutableTreeNode("Main.class");
        e.add(w);
        font.add(e);
        color.add(font);
        style.add(color);

        // JTree
        JTree dir = new JTree(style);
        dir.setBounds(10, 5, 270, height-90);

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
        JMenu settings = new JMenu("Settings");

        JRadioButtonMenuItem lightTheme = new JRadioButtonMenuItem("Light Theme");
        JRadioButtonMenuItem darkTheme = new JRadioButtonMenuItem("Dark Theme");

        lightTheme.setSelected(true);

        JMenuItem open = new JMenuItem("Open Jar");
        JMenuItem close = new JMenuItem("Close Jar");
        JMenuItem exit = new JMenuItem("Exit");

        // adding
        stuff.add(file);
        stuff.add(settings);

        // File tab
        file.add(open);
        file.add(close);
        file.add(exit);

        // Settings tab
        settings.add(lightTheme);
        settings.add(darkTheme);

        // Actions
        exit.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Thank you for using JByteCustom Lite!");
            dispose();
        });

        lightTheme.addActionListener(e -> {

        });

        setJMenuBar(stuff);
    }

    public Frame() {
        addMenuBar();
        addMainComponents();

        URL iconURL = Main.class.getResource("/java.png");

        assert iconURL != null;
        ImageIcon icon = new ImageIcon(iconURL);
        setIconImage(icon.getImage());

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
