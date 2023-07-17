package me.synnk.Loaders;

import org.benf.cfr.reader.api.CfrDriver;
import org.benf.cfr.reader.api.OutputSinkFactory;
import org.benf.cfr.reader.api.SinkReturns;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.io.*;
import java.util.*;

public class FileLoader {
    private static String decompiled;

    /**
     * @TODO: if the user wants bytecode analysed (not bytecode disassembled) code, just send em the ASM method, if the user wants the CFR output, send them the CFR output
     * @TODO: these selections is under the [Settings -> Decompiler] menu
     */
    public static StringBuilder loadClass(File file) {

        try {
            decompiled = null;
            OutputSinkFactory mySink = new OutputSinkFactory() {
                @Override
                public List<SinkClass> getSupportedSinks(SinkType sinkType, Collection<SinkClass> collection) {
                    if (sinkType == SinkType.JAVA && collection.contains(SinkClass.DECOMPILED)) {
                        return Arrays.asList(SinkClass.DECOMPILED, SinkClass.STRING);
                    } else {
                        return Collections.singletonList(SinkClass.STRING);
                    }
                }
                @Override
                public <T> Sink<T> getSink(SinkType sinkType, SinkClass sinkClass) {
                    if (sinkType == SinkType.JAVA && sinkClass == SinkClass.DECOMPILED) {
                        return x -> { decompiled = ((SinkReturns.Decompiled)x).getJava(); };
                    }
                    return ignore -> {};
                }
            };
            CfrDriver cfrDriver = new CfrDriver.Builder().withOutputSink(mySink).build();
            cfrDriver.analyse(Collections.singletonList(file.getPath()));
        } catch (Exception e) {
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            return new StringBuilder(sw.toString());
        }
        return new StringBuilder(decompiled);
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
