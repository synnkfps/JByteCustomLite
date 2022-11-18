package me.synnk;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import me.synnk.Interface.Frame;
import me.synnk.Managers.SettingsManager;

public class Main {
    public static String NAME = "JByteCustom Lite";
    public static String VERSION = "0.7";

    public static void main(String[] args) {
        System.out.println("Hello world!");
        // Initialize Settings
        SettingsManager.initSettings();

        // Frame
        new Frame();
    }

}