package me.synnk.Loaders;

import me.synnk.Interface.Frame;
import me.synnk.Interface.FrameRegisters;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.io.*;
import java.util.Objects;

public class FileLoader {

    /*
    @TODO: Add something like an array or something like that so each opened file will have their own content on the decompiled text panel
     */
    public static void loadFile(File file) throws IOException {
        if (!Objects.equals(file.getName().split("\\.")[1], "class")) {
            Frame.decompiled.setText(FrameRegisters.readBytecode(new File(file.getPath())));
            return;
        }

        if (file.isFile()) { // Check if it is a file
            try (InputStream inputStream = new FileInputStream(file)) {
                ClassReader classReader = new ClassReader(inputStream);
                ClassNode classNode = new ClassNode(Opcodes.ASM9);
                classReader.accept(classNode, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);

                StringBuilder decompiledCodeBuilder = new StringBuilder();

                // Visit class information
                decompiledCodeBuilder.append("Class: ").append(classNode.name).append("\n");
                decompiledCodeBuilder.append("Superclass: ").append(classNode.superName).append("\n");
                decompiledCodeBuilder.append("Access: ").append(classNode.access).append("\n\n");

                // Visit fields
                decompiledCodeBuilder.append("Fields:").append("\n");
                for (FieldNode field : classNode.fields) {
                    decompiledCodeBuilder.append(fieldToString(field)).append("\n");
                }
                decompiledCodeBuilder.append("\n");

                // Visit methods
                decompiledCodeBuilder.append("Methods:").append("\n");
                for (MethodNode method : classNode.methods) {
                    decompiledCodeBuilder.append(methodToString(method)).append("\n");
                }

                String fileContent = decompiledCodeBuilder.toString();
                Frame.decompiled.setText(fileContent);
                Frame.content.put(file.getPath(), fileContent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Skipping folder: " + file.getPath());
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
}
