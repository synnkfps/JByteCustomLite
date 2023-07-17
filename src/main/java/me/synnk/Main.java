package me.synnk;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import me.synnk.Interface.Frame;
import me.synnk.Managers.SettingsManager;

import java.io.File;
import java.util.Objects;

public class Main {
    public static String NAME = "JByteCustom Lite";
    public static String VERSION = "0.9.3";

    public static Integer LIGHT = 0;
    public static Integer DARK = 1;
    public static Integer BARE_BONES = 2;
    public static Integer PreferredTheme = LIGHT;
    public static File actualFile;

    public static void main(String[] args) {
        System.out.println("Hello world!");

        // Initialize Settings
        SettingsManager.initSettings();
        SettingsManager.load();
        SettingsManager.clearCache();

        if (Objects.equals(PreferredTheme, LIGHT)) {
            FlatLightLaf.setup();
        } else if (Objects.equals(PreferredTheme, DARK)) {
            FlatDarkLaf.setup();
        }
        // Frame
        new Frame();
    }

}