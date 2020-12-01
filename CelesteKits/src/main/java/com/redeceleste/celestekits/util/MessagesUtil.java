package com.redeceleste.celestekits.util;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.AbstractMap;
import java.util.Map;

public abstract class MessagesUtil {
    public <T, U> Map.Entry<T, U> build(T key, U value) {
        return new AbstractMap.SimpleEntry<>(key, value);
    }

    public abstract <T, U> void send(CommandSender sender, String path, Map.Entry<T, U>... map);

    public abstract <T, U> void send(CommandSender sender, String path, FileConfiguration file, Map.Entry<T, U>... map);
}