package me.synnk.Loaders;

import me.synnk.Interface.Frame;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

public class FileLoader {

    public static void loadFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!Objects.equals(file.getName().split("\\.")[1], "class")) {
                    Frame.decompiled.setText(Frame.decompiled.getText() + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(file + "|" + file.getName() + "|" + file.getAbsolutePath());
    }
}
