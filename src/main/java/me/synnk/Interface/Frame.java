package me.synnk.Interface;

import me.synnk.Decompiler.Decompile;
import me.synnk.Loaders.FileLoader;
import me.synnk.Loaders.TransferHandle;
import me.synnk.Main;
import me.synnk.Managers.SettingsManager;
import me.synnk.Managers.SwitchManager;
import me.synnk.Renders.FileTreeRenderer;
import me.synnk.Utils.LogType;
import me.synnk.Utils.Logger;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Frame extends JFrame {
    public Integer width = 1400;
    public Integer height = 750;
    public static JTextArea decompiled = new JTextArea();
    public static JScrollPane scrollPane = new JScrollPane(decompiled);
    public static JLabel className = new JLabel("Current Class: ");
    public static ArrayList<String> files = new ArrayList<>();
    public static DefaultMutableTreeNode root = new DefaultMutableTreeNode("Drag and drop stuff here!");
    public static JTree dir = new JTree(root);

    public static JRadioButtonMenuItem lightTheme = new JRadioButtonMenuItem("Light Theme");
    public static JRadioButtonMenuItem darkTheme = new JRadioButtonMenuItem("Dark Theme");
    public static JRadioButtonMenuItem bareTheme = new JRadioButtonMenuItem("Bare Bones Theme");

    public static HashMap<String, String> content = new HashMap<>();

    public void addMainComponents() {
        // Jar Class Directory
        // Testing

        // JTree
        JScrollPane qPane = new JScrollPane(dir, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        qPane.setBounds(10, 5, 270, height - 90);

        add(qPane);
        // File Name
        className.setBounds(300, 15, 400, 20);

        // Decompiled class / method content
        decompiled.setText("decompiled class stuff will appear here.");
        scrollPane.setBounds(300, 35, width - 320, height - 125);

        // Decompilers
        String[] dec = {"CFR v0.152", "Procyon 1.0.0", "QuiltFlower", "FernFlower", "Jadx"};
        JComboBox<String> decompilers = new JComboBox<>(dec);
        decompilers.setBounds(width - 130, 10, 100, 20);

        // add stuff
        TreeCellRenderer customTreeCellRenderer = new FileTreeRenderer.CustomTreeCellRenderer(dir);
        dir.setCellRenderer(customTreeCellRenderer);
        dir.setTransferHandler(new TransferHandle());
        FrameRegisters.registerFileClicking(); // for dir
        add(scrollPane);
        add(className);
        add(decompilers);
    }

    public void addMenuBar() {
        // Menu Bar
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenu settingsMenu = new JMenu("Settings");
        JMenu miscMenu = new JMenu("Misc");

        ArrayList<JRadioButtonMenuItem> themeItems = new ArrayList<>();
        themeItems.add(lightTheme);
        themeItems.add(darkTheme);
        themeItems.add(bareTheme);

        JMenuItem systemInfoItem = new JMenuItem("System Info");
        JMenu helpMenu = new JMenu("Help");

        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(Frame.this,
                "JByteCustom Lite\nVersion 1.0\n\nA lightweight Java bytecode viewer and decompiler.",
                "About", JOptionPane.INFORMATION_MESSAGE));
        helpMenu.add(aboutItem);
        //JMenuItem aboutItem = new JMenuItem("About");

        SwitchManager.setItems(themeItems);
        // replacement to the switch case stuff


        JMenuItem openItem = new JMenuItem("Open Jar/Class");
        JMenuItem closeJar = new JMenuItem("Close JAR");
        closeJar.addActionListener(e -> {
            SettingsManager.clearCache();
            Frame.content.clear();
            ((DefaultTreeModel) dir.getModel()).setRoot(new DefaultMutableTreeNode());
            decompiled.setText("");
            className.setText("");
        });
        JMenuItem exitItem = new JMenuItem("Exit");

        // adding
        menuBar.add(fileMenu);
        menuBar.add(settingsMenu);
        menuBar.add(miscMenu);

        // File menu
        fileMenu.add(openItem);
        fileMenu.add(closeJar);
        fileMenu.add(exitItem);

        miscMenu.add(systemInfoItem);
        miscMenu.add(aboutItem);

        // Settings menu
        themeItems.forEach(settingsMenu::add);

        // Actions
        openItem.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogType(JFileChooser.OPEN_DIALOG);
            chooser.setCurrentDirectory(new File(System.getProperty("user.home") + File.separator + "Desktop"));

            // Create a file filter for .jar and .class files
            FileNameExtensionFilter filter = new FileNameExtensionFilter("JAR and CLASS Files", "jar", "class");
            chooser.setFileFilter(filter);

            int action = chooser.showOpenDialog(null);
            if (action == JFileChooser.APPROVE_OPTION) {
                FrameRegisters.fileLoaded(new File(chooser.getSelectedFile().getPath()));
            }
        });

        exitItem.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Thank you for using JByteCustom Lite!");
            dispose();
        });

        systemInfoItem.addActionListener(e -> {
            String[] infos = {"OS Name: " + System.getProperty("os.name"), "OS Architecture: " + System.getProperty("os.arch"), "Java Version: " + System.getProperty("java.version"), "VM Name: " + System.getProperty("java.vm.name"), "VM Vendor: " + System.getProperty("java.vm.vendor"), "Java Home: " + System.getProperty("java.home")};
            StringBuilder in = new StringBuilder();
            for (String i : infos) {
                in.append(i).append("\n");
            }
            JOptionPane.showMessageDialog(null, in);
        });

        // Dumb theme switcher
        // WIP
        lightTheme.addActionListener(e -> {
            SwitchManager.switchTo(lightTheme, "0");
        });
        darkTheme.addActionListener(e -> {
            SwitchManager.switchTo(darkTheme, "1");
        });
        bareTheme.addActionListener(e -> {
            SwitchManager.switchTo(bareTheme, "2");
        });

        setJMenuBar(menuBar);
    }

    public Frame() {
        addMenuBar();
        addMainComponents();

        setDefaultLookAndFeelDecorated(true);
        setLayout(null); // We are going to use bounds

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(width, height);
        setTitle(Main.NAME + " " + Main.VERSION);
        setVisible(true);
    }

    public static void showSettingChange() {
        JOptionPane.showMessageDialog(null, "Please restart the application for the changes to take effect.");
    }
}
