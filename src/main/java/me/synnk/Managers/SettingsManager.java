package me.synnk.Managers;

import me.synnk.Utils.LogType;
import me.synnk.Utils.Logger;

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
            if (i.toLowerCase().contains(setting_name.toLowerCase())) {
                System.out.println("Setting Found: name=" + i.split(":")[0] + " value=" + i.split(":")[1]);
                break;
            }
        }
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
                } else if (!splitted.contains(".")) {
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

    public static void initSettings() {
        Logger.Log(LogType.INFO, "Settings Manager Initializing...");
        for (String setting: readSettings()) {
            Logger.Log(LogType.INFO, "Setting initialized > " + setting);
        }
        verifySettings();
    }
}
