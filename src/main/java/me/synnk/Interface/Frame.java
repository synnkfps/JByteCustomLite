package me.synnk.Interface;

import me.synnk.Decompiler.Decompile;
import me.synnk.Discord.Discord;
import me.synnk.Loaders.TransferHandle;
import me.synnk.Main;
import me.synnk.Managers.SettingsManager;
import me.synnk.Managers.SwitchManager;
import me.synnk.Renders.FileTreeRenderer;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

public class Frame extends JFrame {
    public Integer width = 1400;
    public Integer height = 750;
    public static RSyntaxTextArea decompiled = new RSyntaxTextArea();
    public static JScrollPane scrollPane = new JScrollPane(decompiled);
    public static JLabel className = new JLabel("Current Class: ");
    public static ArrayList<String> files = new ArrayList<>();
    public static DefaultMutableTreeNode root = new DefaultMutableTreeNode("Drag and drop stuff here!");
    public static JTree dir = new JTree(root);

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

        RTextScrollPane scrollPane = new RTextScrollPane(decompiled);
        add(scrollPane);

        // Decompiled class / method content
        decompiled.setText("decompiled class stuff will appear here.");
        decompiled.setFont(new Font("consolas", Font.PLAIN, 12));
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

        // new Theme system
        JMenu theme = new JMenu("Theme");
        ArrayList<JMenuItem> themes_names = new ArrayList<>();
        JMenuItem light = new JMenuItem("Light Theme");
        JMenuItem dark = new JMenuItem("Dark Theme");
        JMenuItem bares = new JMenuItem("Bare Bones Theme");
        themes_names.add(light);
        themes_names.add(dark);
        themes_names.add(bares);

        // so we don't have to add each one of them individually
        SwitchManager.setItems(themes_names);
        themes_names.forEach(SwitchManager::addSwitchHook); // easier hook

        theme.add(light);
        theme.add(dark);
        theme.add(bares);
        settingsMenu.add(theme);
        // troll is done

        JMenu decompiler = new JMenu("Decompiler");
        JMenuItem methods_and_fields = new JMenuItem("Methods n' Fields");
        JMenuItem CFR_DECOMPILER = new JMenuItem("CFR Decompiler");

        CFR_DECOMPILER.addActionListener(e -> {
            try {
                Decompile.decompileClass(Main.actualFile.getPath());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        decompiler.add(methods_and_fields);
        decompiler.add(CFR_DECOMPILER);
        settingsMenu.add(decompiler);

        JMenu miscMenu = new JMenu("Misc");

        JMenuItem systemInfoItem = new JMenuItem("System Info");
        JMenu helpMenu = new JMenu("Help");

        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(Frame.this,
                "JByteCustom Lite\nVersion 1.0\n\nA lightweight Java bytecode viewer and decompiler.",
                "About", JOptionPane.INFORMATION_MESSAGE));
        helpMenu.add(aboutItem);
        //JMenuItem aboutItem = new JMenuItem("About");

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



        // Actions
        openItem.addActionListener(e -> {
            Discord.updatePresence("Choosing a file...", Discord.presence.state);
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogType(JFileChooser.OPEN_DIALOG);
            chooser.setCurrentDirectory(new File(System.getProperty("user.home") + File.separator + "Desktop"));

            // Create a file filter for .jar and .class files
            FileNameExtensionFilter filter = new FileNameExtensionFilter("JAR and CLASS Files", "jar", "class");
            chooser.setFileFilter(filter);

            int action = chooser.showOpenDialog(null);
            if (action == JFileChooser.APPROVE_OPTION) {
                FrameRegisters.loadJar(new File(chooser.getSelectedFile().getPath()));
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

        setJMenuBar(menuBar);
    }

    public Frame() {
        decompiled.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);

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
