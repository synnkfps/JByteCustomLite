package me.synnk.Interface;

import me.synnk.Decompiler.Decompile;
import me.synnk.Managers.SettingsManager;
import me.synnk.Utils.LogType;
import me.synnk.Utils.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
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
                        handleFileDoubleClick(selectedFile);
                    }
                }
            }
        });
    }


    private static void handleFileDoubleClick(File file) {
        // Handle the double-clicked file here

        if (!Frame.content.containsKey(file.getPath())) {
            if ("class".equals(getFileExtension(file))) {
                try {
                    String decompiledCode = readBytecode(file);
                    decompiled.setText(decompiledCode);
                    Frame.content.put(file.getPath(), decompiledCode);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                loadTextFile(file);
            }
        } else {
            decompiled.setText(Frame.content.get(file.getPath()));
        }
    }

    private static String getFileExtension(File file) {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1).toLowerCase();
        }
        return "";
    }

    public static String readBytecode(File file) throws IOException {
        try (InputStream inputStream = new FileInputStream(file)) {
            ClassReader classReader = new ClassReader(inputStream);
            ClassNode classNode = new ClassNode(Opcodes.ASM9);
            classReader.accept(classNode, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);

            StringBuilder bytecodeBuilder = new StringBuilder();

            // Visit class information
            bytecodeBuilder.append("Class: ").append(classNode.name).append("\n");
            bytecodeBuilder.append("Superclass: ").append(classNode.superName).append("\n");
            bytecodeBuilder.append("Access: ").append(classNode.access).append("\n\n");

            // Visit fields
            bytecodeBuilder.append("Fields:").append("\n");
            for (FieldNode field : classNode.fields) {
                bytecodeBuilder.append(fieldToString(field)).append("\n");
            }
            bytecodeBuilder.append("\n");

            // Visit methods
            bytecodeBuilder.append("Methods:").append("\n");
            for (MethodNode method : classNode.methods) {
                bytecodeBuilder.append(methodToString(method)).append("\n");
            }

            return bytecodeBuilder.toString();
        }
    }

    private static String fieldToString(FieldNode fieldNode) {
        String access = getAccessModifier(fieldNode.access);
        return access + " " + fieldNode.name + ": " + fieldNode.desc;
    }

    private static String methodToString(MethodNode methodNode) {
        String access = getAccessModifier(methodNode.access);
        return access + " " + methodNode.name + methodNode.desc;
    }

    private static String getAccessModifier(int access) {
        StringBuilder modifier = new StringBuilder();

        if ((access & Opcodes.ACC_PUBLIC) != 0) {
            modifier.append("public ");
        } else if ((access & Opcodes.ACC_PROTECTED) != 0) {
            modifier.append("protected ");
        } else if ((access & Opcodes.ACC_PRIVATE) != 0) {
            modifier.append("private ");
        } else {
            modifier.append("package-private ");
        }

        if ((access & Opcodes.ACC_STATIC) != 0) {
            modifier.append("static ");
        }

        if ((access & Opcodes.ACC_FINAL) != 0) {
            modifier.append("final ");
        }

        if ((access & Opcodes.ACC_ABSTRACT) != 0) {
            modifier.append("abstract ");
        }

        if ((access & Opcodes.ACC_SYNCHRONIZED) != 0) {
            modifier.append("synchronized ");
        }

        if ((access & Opcodes.ACC_NATIVE) != 0) {
            modifier.append("native ");
        }

        if ((access & Opcodes.ACC_STRICT) != 0) {
            modifier.append("strictfp ");
        }

        return modifier.toString().trim();
    }

    private static void loadTextFile(File file) {
        if (file.isFile()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                StringBuilder fileContentBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    fileContentBuilder.append(line).append("\n");
                }
                String fileContent = fileContentBuilder.toString();
                Frame.content.put(file.getPath(), fileContent);
                decompiled.setText(fileContent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Skipping folder: " + file.getPath());
        }
    }

    public static void fetchNewTheme() {
        switch (SettingsManager.getSetting("defaultTheme")) {
            case "0":
                Frame.lightTheme.setSelected(true);
                break;
            case "1":
                Frame.darkTheme.setSelected(true);
                break;
            case "2":
                Frame.bareTheme.setSelected(true);
                break;
            default:
                Logger.Log(LogType.ERROR, "defaultTheme option seems invalid.");
        }
    }
}
