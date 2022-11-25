package me.synnk.Managers;

import me.synnk.Interface.Frame;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;

public class SwitchManager {
    private static ArrayList<JRadioButtonMenuItem> items = new ArrayList<>();

    public static void setItems(ArrayList<JRadioButtonMenuItem> newItems) {
        items = newItems;
    }

    public static void switchTo(JRadioButtonMenuItem item, String settingIndex) {
        System.out.println("Called switchTo function with parameters: " + item.getText() + " " + settingIndex);
        System.out.println(item.isSelected());
        if (item.isSelected()) {
            for (JRadioButtonMenuItem i : items) {
                if (i.getText().equals(item.getText())) {
                    System.out.println(i.getText());
                    i.setSelected(true);
                    SettingsManager.changeSetting("defaultTheme", settingIndex);
                    Frame.showSettingChange();
                } else {
                    i.setSelected(false);
                }
            }
        }
    }
}
