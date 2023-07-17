package me.synnk.Interface;

import me.synnk.Loaders.FileLoader;
import me.synnk.Main;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static me.synnk.Interface.Frame.*;

public class FrameRegisters {
    private static final JTree dir = Frame.dir;

    public static void fileLoaded(File selectedFile) {

        decompiled.setText(selectedFile.getName());
        className.setText("Current Class: " + selectedFile.getName());

        try (JarFile jar = new JarFile(new File(selectedFile.getAbsolutePath()))) {
            root.setUserObject(jar.getName());
            Enumeration<JarEntry> entries = jar.entries();

            DefaultTreeModel model = (DefaultTreeModel) dir.getModel();
            DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) model.getRoot();
            rootNode.removeAllChildren(); // Clear existing tree nodes

            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String entryName = entry.getName();
                String[] pathElements = entryName.split("/"); // Split entry name into path elements

                DefaultMutableTreeNode currentNode = rootNode;
                for (String pathElement : pathElements) {
                    if (!pathElement.isEmpty()) {
                        DefaultMutableTreeNode newNode = getChildNode(currentNode, pathElement);
                        if (newNode == null) {
                            newNode = new DefaultMutableTreeNode(pathElement);
                            model.insertNodeInto(newNode, currentNode, currentNode.getChildCount());
                        }
                        currentNode = newNode;

                    }
                }

                // Create corresponding file in cache folder
                if (!entry.isDirectory()) {
                    String cacheFilePath = "cache_folder" + File.separator + entryName;
                    File cacheFile = new File(cacheFilePath);
                    cacheFile.getParentFile().mkdirs(); // Create parent directories if they don't exist
                    try (InputStream inputStream = jar.getInputStream(entry);
                         FileOutputStream outputStream = new FileOutputStream(cacheFile)) {
                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }
                    }
                }
            }

            model.reload(); // Refresh the JTree
        } catch (IOException w) {
            w.printStackTrace();
        }
    }

    private static DefaultMutableTreeNode getChildNode(DefaultMutableTreeNode parent, String childName) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) parent.getChildAt(i);
            if (childNode.getUserObject().equals(childName)) {
                return childNode;
            }
        }
        return null;
    }

    public static void registerFileClicking() {
        dir.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                TreePath selectedPath = dir.getPathForLocation(e.getX(), e.getY());
                if (selectedPath != null) {
                    Object[] w = selectedPath.getPath();

                    DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) selectedPath.getLastPathComponent();
                    Object userObject = selectedNode.getUserObject();
                    if (userObject instanceof String) {
                        StringBuilder filePathBuilder = new StringBuilder("cache_folder");
                        for (int i = 1; i < w.length; i++) {
                            filePathBuilder.append(File.separator).append(w[i].toString());
                        }
                        File selectedFile = new File(String.valueOf(filePathBuilder));
                        handleClassClick(selectedFile);
                    }
                }
            }
        });
    }

    // Handle the clicked file
    private static void handleClassClick(File file) {
        if (Frame.content.containsKey(file.getPath())) {
            decompiled.setText(Frame.content.get(file.getPath()));
        } else {
            if (getFileExtension(file).equals("class")) {
                String output = String.valueOf(FileLoader.loadClass(file));
                Frame.decompiled.setText(output);
                Frame.content.put(file.getPath(), output);

            } else {
                String output = loadTextFile(file);
                decompiled.setText(output);
                Frame.content.put(file.getPath(), output);
            }
        }

        Main.actualFile = file;
    }

    private static String getFileExtension(File file) {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1).toLowerCase();
        }
        return "";
    }

    public static String loadTextFile(File file) {
        StringBuilder output = new StringBuilder();
        if (file.isFile()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Skipping folder: " + file.getPath());
        }

        return String.valueOf(output);
    }
}
