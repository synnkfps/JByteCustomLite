package me.synnk.Interface;

import me.synnk.Main;
import me.synnk.Managers.SettingsManager;
import me.synnk.Managers.SwitchManager;
import me.synnk.Utils.LogType;
import me.synnk.Utils.Logger;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Frame extends JFrame {
    public Integer width = 1400;
    public Integer height = 750;
    public static JTextPane decompiled = new JTextPane();
    public static JLabel className = new JLabel("Current Class: ");
    public static ArrayList<String> files = new ArrayList<>();
    public static DefaultMutableTreeNode root = new DefaultMutableTreeNode("Testing");
    public static JTree dir = new JTree(root);

    public static JRadioButtonMenuItem lightTheme = new JRadioButtonMenuItem("Light Theme");
    public static JRadioButtonMenuItem darkTheme = new JRadioButtonMenuItem("Dark Theme");
    public static JRadioButtonMenuItem bareTheme = new JRadioButtonMenuItem("Bare Bones Theme");

    public void addMainComponents() {
        // Jar Class Directory
        // Testing

        // JTree
        dir.setBounds(10, 5, 270, height-90);
        JScrollPane qPane = new JScrollPane(dir, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        add(qPane);
        // File Name
        className.setBounds(260+5+20, 15, 400, 20);

        // Decompiled class / method content
        decompiled.setAutoscrolls(true);
        decompiled.setText("decompiled class stuff will appear here.");
        decompiled.setBounds(260+5+20, 5+30, width-315, (height-90)-30);

        // Decompilers
        String[] dec = {"CFR v0.152", "Procyon 1.0.0", "QuiltFlower", "FernFlower", "Jadx"};
        JComboBox<String> decompilers = new JComboBox<>(dec);
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
        JMenu misc = new JMenu("Misc");

        ArrayList<JRadioButtonMenuItem> themesConstructor = new ArrayList<>();
        themesConstructor.add(lightTheme);
        themesConstructor.add(darkTheme);
        themesConstructor.add(bareTheme);

        JMenuItem systemInfo = new JMenuItem("System Info");
        JMenuItem about = new JMenuItem("About");

        SwitchManager.setItems(themesConstructor);
        // replacement to the switch case stuff
        fetchNewTheme();

        JMenuItem open = new JMenuItem("Open Jar/Class");
        JMenuItem close = new JMenuItem("Close Jar");
        JMenuItem exit = new JMenuItem("Exit");

        // adding
        stuff.add(file);
        stuff.add(settings);
        stuff.add(misc);

        // File tab
        file.add(open);
        file.add(close);
        file.add(exit);

        misc.add(systemInfo);
        misc.add(about);

        // Settings tab
        themesConstructor.forEach(settings::add);

        // Actions
        open.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int action = chooser.showOpenDialog(null);
            chooser.setDialogType(JFileChooser.OPEN_DIALOG);

            if (action == JFileChooser.APPROVE_OPTION) {
                fileLoaded(chooser);
            }

        });
        exit.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Thank you for using JByteCustom Lite!");
            dispose();
        });

        systemInfo.addActionListener(e -> {
            String[] infos = {"OS Name: "+System.getProperty("os.name"), "OS Architeture: " + System.getProperty("os.arch"), "Java Version: " + System.getProperty("java.version"), "VM Name: " + System.getProperty("java.vm.name"), "VM Vendor: "+System.getProperty("java.vm.vendor"), "Java Home: "+System.getProperty("java.home")};
            StringBuilder in = new StringBuilder();
            for (String i: infos) {
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

        setJMenuBar(stuff);
    }

    public Frame() {
        addMenuBar();
        addMainComponents();

        setDefaultLookAndFeelDecorated(true);
        setLayout(null); // ill change to gridbaglayout in future
        setTitle(Main.NAME + " " + Main.VERSION); // to do: show opened jar file
        setLocation(200, 200);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(width, height);
        setVisible(true);
    }

    public void fileLoaded(JFileChooser input) {
        File selectedFile = input.getSelectedFile();

        decompiled.setText(selectedFile.getName());
        className.setText("Current Class: " + selectedFile.getName());

        try (JarFile jar = new JarFile(new File(selectedFile.getAbsolutePath()))){
            root.setUserObject(jar.getName());
            Enumeration<JarEntry> entries = jar.entries();

            // Jar entries reading (useless rn)
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.isDirectory() || entry.getName().equals(JarFile.MANIFEST_NAME)) {
                    continue;
                }
            }

            // ok ngl I like it, keeping...
            System.out.println(jar.getComment()!=null?jar.getComment():"No Comments");
            System.out.println("Manifest Attributes: "+jar.getManifest().getMainAttributes().values());
        } catch (IOException w) {
            w.printStackTrace();
        }
    }
    public static void main(String[] args) {
        new Frame();
    }
    public static void showSettingChange(){JOptionPane.showMessageDialog(null, "The settings will take effect on the next restart!");}
    public static void fetchNewTheme() {
        switch (SettingsManager.getSetting("defaultTheme")) {
            case "0": lightTheme.setSelected(true);
                break;
            case "1": darkTheme.setSelected(true);
                break;
            case "2": bareTheme.setSelected(true);
                break;
            default: Logger.Log(LogType.ERROR, "defaultTheme option seems invalid.");
        }
    }
}
