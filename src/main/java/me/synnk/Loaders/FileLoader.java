package me.synnk.Loaders;

import me.synnk.Interface.Frame;
import me.synnk.Interface.FrameRegisters;
import me.synnk.Main;
import me.synnk.Utils.LogType;
import me.synnk.Utils.Logger;
import org.benf.cfr.reader.api.CfrDriver;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Objects;

public class FileLoader {

    /*
    @TODO: Add something like an array or something like that so each opened file will have their own content on the decompiled text panel
     */
    public static StringBuilder loadFile(File file) {
        StringBuilder decompiledCodeBuilder = new StringBuilder();

        if (file.isFile()) {
                try (InputStream inputStream = Files.newInputStream(file.toPath())) {
                    ClassReader classReader = new ClassReader(inputStream);
                    ClassNode classNode = new ClassNode(Opcodes.ASM9);
                    classReader.accept(classNode, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);

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
                } catch (IOException e) {
                    e.printStackTrace();
                }
        } else {
            Logger.Log(LogType.INFO, "Skipping folder " + file.getName());
        }

        return new StringBuilder(decompiledCodeBuilder.toString());
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
