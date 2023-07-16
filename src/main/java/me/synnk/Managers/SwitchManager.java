package me.synnk.Managers;

import me.synnk.Interface.Frame;

import javax.swing.*;
import java.util.ArrayList;

public class SwitchManager {
    private static ArrayList<JMenuItem> items = new ArrayList<>();

    public static void setItems(ArrayList<JMenuItem> newItems) {
        items = newItems;
    }

    public static void addSwitchHook(JMenuItem item) {
        item.addActionListener(e -> {
            for (JMenuItem i: items) {
                if (i.getText().equalsIgnoreCase(item.getText())) {
                    i.setSelected(true);

                    SettingsManager.changeSetting("defaultTheme", String.valueOf(items.indexOf(i)));
                    Frame.showSettingChange();
                }
            }
        });
    }
}
