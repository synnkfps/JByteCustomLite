package me.synnk.Loaders;

import me.synnk.Interface.Frame;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

public class FileLoader {

    /*
    @TODO: Add something like an array or something like that so each opened file will have their own content on the decompiled text panel
     */
    public static void loadFile(File file) {
        if (file.isFile()) { // Check if it is a file
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                StringBuilder fileContentBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    fileContentBuilder.append(line).append("\n");
                }
                String fileContent = fileContentBuilder.toString();

                Frame.content.put(file.getPath(), fileContent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Skipping folder: " + file.getPath());
        }
    }


}
