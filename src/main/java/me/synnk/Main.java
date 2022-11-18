package me.synnk;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import me.synnk.Interface.Frame;

public class Main {
    public static String NAME = "JByteCustom Lite";
    public static String VERSION = "0.5";

    public static void main(String[] args) {
        System.out.println("Hello world!");
        //FlatDarkLaf.setup();
        FlatLightLaf.setup();
        new Frame();
    }
}