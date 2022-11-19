package me.synnk;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import me.synnk.Interface.Frame;
import me.synnk.Managers.SettingsManager;

public class Main {
    public static String NAME = "JByteCustom Lite";
    public static String VERSION = "0.8.2";

    public static Integer LIGHT = 0;
    public static Integer DARK = 1;
    public static Integer PreferredTheme = LIGHT;

    public static void main(String[] args) {
        System.out.println("Hello world!");
        // Initialize Settings
        SettingsManager.initSettings();
        SettingsManager.load();

        if (PreferredTheme==LIGHT) {
            FlatLightLaf.setup();
        } else if (PreferredTheme==DARK) {
            FlatDarkLaf.setup();
        }

        // Frame
        new Frame();
    }

}