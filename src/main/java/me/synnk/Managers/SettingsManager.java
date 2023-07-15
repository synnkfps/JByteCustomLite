package me.synnk.Managers;

import me.synnk.Main;
import me.synnk.Utils.LogType;
import me.synnk.Utils.Logger;

import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Stream;

public class SettingsManager {

    // @TODO: dynamic path
    public static String path = System.getProperty("user.home") + "\\Documents\\jbclite.jbc";
    //public static String path = "C:\\Users\\SynnK\\IdeaProjects\\JByteCustomLite\\src\\main\\resources\\jbclite.jbc";

    public static void clearCache() {
        File file = new File("cache_folder" + File.separator);
        try (Stream<Path> pathStream = Files.walk(file.toPath())) {
            pathStream.sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException e) {
            System.out.println("Cache Folder doesnt exists, passing");
        }
    }

    public static ArrayList<String> readSettings() {
        ArrayList<String> result = new ArrayList<>();
        try {
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileReader f = new FileReader(file);
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
    public static String getSetting(String toSearch)  {
        String tmp = null;
        for (String setting_name: readSettings()) {
            if (setting_name.split(":")[0].equalsIgnoreCase(toSearch)) {
                tmp = setting_name.split(":")[1];
                break;
            }
        }
        return tmp;
    }

    // useless function
    public static void verifySettings() {
        boolean stillValid = false;
        boolean hasExtras = false;
        for (String i: readSettings()) {
            String splitted = i.split(":")[1].toLowerCase(); // cuz :[0] will be the setting name

            if (splitted.equals("true") || splitted.equals("false")) {
                System.out.println("Value " + splitted + " is a boolean.");
                stillValid = true;
                hasExtras = true;
            } else if (splitted.charAt(0)=='\'' && splitted.charAt(splitted.length()-1)=='\'') {
                System.out.println("Value " + splitted + " is a string");
                stillValid = true;
                hasExtras = true;
            } else {
                if (splitted.contains(".")) {
                    try {
                        Float.parseFloat(splitted);
                        System.out.println("Value " + splitted + " is a float/double.");
                        stillValid = true;
                        hasExtras = true;
                    } catch (Exception e) {
                        stillValid = false;
                    }
                // bruh handling
                } else {
                    try {
                        Integer.parseInt(splitted);
                        System.out.println("Value " + splitted + " is a integer.");
                        stillValid = true;
                        hasExtras = true;
                    } catch (Exception e) {
                        stillValid = false;
                    }
                }
                if (!stillValid) {
                    System.out.println("Value " + splitted + " is unknown.");
                }
            }
        }
    }

    public static void append(String setting_name, String value) {
        boolean alreadyExists = false;
        File file = new File(path);
        FileWriter fr = null;

        for (String i: readSettings()) {
            if (i.contains(setting_name)) {
                System.out.println("Setting name already exists.");
                alreadyExists = true;
                break;
            }
        }
        try {
            if (!alreadyExists) {
                fr = new FileWriter(file, true);
                fr.write("\n" + setting_name + ":" + value);
            }
            assert fr != null;
            fr.close();
        } catch (Exception ignored) {

        }
    }

    public static void changeSetting(String setting_name, String new_value) {
        ArrayList<String> tempList = new ArrayList<>();
        for (String setting: readSettings()) {
            String name = setting.split(":")[0];
            String value = setting.split(":")[1];

            tempList.add(name+":"+(name.equalsIgnoreCase(setting_name)?new_value:value));
        }

        try {
            PrintWriter writer = new PrintWriter(path);
            writer.print("");
            writer.close();

            FileWriter f = new FileWriter(path, true);
            for (String setting: tempList) {
                f.write(setting+"\n");
            }
            f.close();
        } catch (IOException e) {
            System.out.println("Error occurred while changing the setting. Stacktrace: " + Arrays.toString(e.getStackTrace()));
        }
    }

    public static void load() {
        for (String setting: readSettings()) {
            String name = setting.split(":")[0];
            String value = setting.split(":")[1];

            if (name.equals("showWelcome") && value.equals("true")) {
                JOptionPane.showMessageDialog(null, "Welcome to JByteCustom Lite!");
            }

            if (name.equals("defaultTheme")) {
                switch (value) {
                    case "0":
                        Main.PreferredTheme = Main.LIGHT;
                        break;
                    case "1":
                        Main.PreferredTheme = Main.DARK;
                        break;
                    case "2":
                        Main.PreferredTheme = Main.BARE_BONES;
                        break;
                }
            }
        }
    }

    public static void initSettings() {
        Logger.Log(LogType.INFO, "Settings Manager Initializing...");
        // changeSetting("showWelcome", "false");
        // changeSetting("defaultTheme", "0");
        for (String setting: readSettings()) {
            Logger.Log(LogType.INFO, "Setting initialized > " + setting);
        }
        verifySettings();
    }
}
