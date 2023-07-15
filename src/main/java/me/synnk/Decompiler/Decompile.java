package me.synnk.Decompiler;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Decompile {
    public void decompileClass(String classFilePath) throws Exception {
        byte[] bytecode = readClassFile(classFilePath); // Implement the logic to read the class file

        ClassReader classReader = new ClassReader(bytecode);
        ClassVisitor classVisitor = new CustomClassVisitor();
        classReader.accept(classVisitor, 0);
    }

    static class CustomClassVisitor extends ClassVisitor {

        public CustomClassVisitor() {
            super(Opcodes.ASM7);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            // Process the method during decompilation
            System.out.println("Decompiling method: " + name);
            return super.visitMethod(access, name, descriptor, signature, exceptions);
        }
    }

    public static byte[] readClassFile(String classFilePath) throws IOException {
        try (InputStream inputStream = Files.newInputStream(Paths.get(classFilePath))) {
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            return buffer;
        }
    }
}
