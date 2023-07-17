package me.synnk.Discord;

import club.minnced.discord.rpc.*;
import me.synnk.Main;
import me.synnk.Utils.LogType;
import me.synnk.Utils.Logger;

import java.util.ArrayList;

public class Discord {
    public static DiscordRPC discordRPC;
    public static long startTimestamp;
    public static DiscordRichPresence presence = new DiscordRichPresence();

    public static String currentDecompiler = "Not Decompiling";

    public static void init() {
        discordRPC = DiscordRPC.INSTANCE;
        String applicationId = "1130599936359157810";
        DiscordEventHandlers handlers = new DiscordEventHandlers();
        handlers.ready = (user) -> Logger.Log(LogType.INFO, "Discord RPC has started!");
        discordRPC.Discord_Initialize(applicationId, handlers, true, "");
        startTimestamp = System.currentTimeMillis();

        updatePresence("AFK'ing", "Just chilling");
    }

    public static void updatePresence(String details, String state) {
        presence.details = details;
        presence.state = state;

        presence.largeImageKey = "java";
        presence.largeImageText = "JByteCustomLite v"+ Main.VERSION;
        presence.smallImageKey = "small";
        presence.smallImageText = "Running on Java " +  System.getProperty("java.version");

        presence.joinSecret = "java";

        presence.startTimestamp = startTimestamp;

        refresh();
    }

    public static void changeSmallImageText(String newText) {
        Discord.presence.smallImageText = newText;
        refresh(); // we have to refresh tho
    }


    public static void refresh(){
        discordRPC.Discord_UpdatePresence(presence);
    }
}