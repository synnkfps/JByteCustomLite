package me.synnk.Utils;

import java.lang.*;
import java.util.Date;


public class Logger {
    public static void Log(LogType level, String message) {
        Date d = new Date();
        String levelOut = level==LogType.COMMON ? " " : " [" + level + "] ";
        String s = "[" + d.toInstant().toString().split("T")[1].split("\\.")[0] + "]" + levelOut + message;

        System.out.println(s);
    }
}

