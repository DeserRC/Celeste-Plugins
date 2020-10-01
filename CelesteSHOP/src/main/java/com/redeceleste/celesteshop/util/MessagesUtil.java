package com.redeceleste.celesteshop.util;

import com.redeceleste.celesteshop.model.ConfigType;
import org.bukkit.command.CommandSender;

import java.util.AbstractMap;
import java.util.Map;

public abstract class MessagesUtil {
    public <T, U> Map.Entry<T, U> build(T key, U value) {
        return new AbstractMap.SimpleEntry<>(key, value);
    }

    @SafeVarargs
    public final <T, U> void send(CommandSender sender, String path, Map.Entry<T, U>... map) {
        send(sender, path, ConfigType.message, true, map);
    }

    @SafeVarargs
    public final <T, U> void send(CommandSender sender, String path, Boolean isConfig, Map.Entry<T, U>... map) {
        send(sender, path, ConfigType.message, isConfig, map);
    }

    @SafeVarargs
    public final <T, U> void send(CommandSender sender, String path, ConfigType type, Map.Entry<T, U>... map) {
        send(sender, path, type,true, map);
    }

    public abstract <T, U> void send(CommandSender sender, String path, ConfigType type, Boolean isConfig, Map.Entry<T, U>... map);
}
