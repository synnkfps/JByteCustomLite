package me.synnk.Interface;

import me.synnk.Main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Frame extends JFrame {

    public void addMenuBar() {
        // Menu Bar
        JMenuBar stuff = new JMenuBar();

        JMenu file = new JMenu("File");
        JMenu about = new JMenu("About");

        JMenuItem open = new JMenuItem("Open Jar");
        JMenuItem exit = new JMenuItem("Exit");

        // adding
        stuff.add(file);
        stuff.add(about);
        file.add(open);
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

        setLayout(null); // ill change to gridbaglayout in future
        setTitle(Main.NAME + " " + Main.VERSION); // to do: show opened jar file
        setLocation(200, 200);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1400, 750);
        setVisible(true);
    }

    public static void main(String[] args) {
        new Frame();
    }
}
