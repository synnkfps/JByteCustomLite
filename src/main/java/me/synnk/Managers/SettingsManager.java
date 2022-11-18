package me.synnk.Managers;

import java.io.*;
import java.util.ArrayList;

public class SettingsManager {

    // @TODO: dynamic path
    public static String path = "C:\\Users\\SynnK\\IdeaProjects\\JByteCustomLite\\src\\main\\resources\\jbclite.jbc";

    public static ArrayList<String> readSettings() {
        ArrayList<String> result = new ArrayList<>();
        try {
            FileReader f = new FileReader(path);
            StringBuilder sb = new StringBuilder();
            while (f.ready()) {
                char c = (char) f.read();
                if (c == '\n') {
                    result.add(sb.toString());
                    sb = new StringBuilder();
                } else {
                    sb.append(c);
                }
            }
            if (sb.length() > 0) {
                result.add(sb.toString());
            }
        } catch(IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    // WIP
    public static void getSetting(String setting_name)  {
        ArrayList<String> result = readSettings();
        for (String i: result) {
            System.out.println(i);
        }
    }

    public static void append(String setting_name, String value) {
        try {
            FileWriter f = new FileWriter(path, true);
            BufferedWriter b = new BufferedWriter(f);
            PrintWriter p = new PrintWriter(b);

            p.println(setting_name+":"+value);
            f.close(); b.close(); p.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        getSetting("dieFile");
    }
}
