package com.redeceleste.celesteessentials.util.impl;

import com.redeceleste.celesteessentials.CelesteEssentials;
import com.redeceleste.celesteessentials.manager.ConfigManager;
import com.redeceleste.celesteessentials.util.MessagesUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Map;

public class ChatUtil extends MessagesUtil {
    private final CelesteEssentials main;
    private final ConfigManager config;

    public ChatUtil(CelesteEssentials main) {
        this.main = main;
        this.config = main.getConfigManager();
    }

    @Override @SafeVarargs
    public final <T, U> void send(CommandSender sender, String path, Map.Entry<T, U>... map) {
        send(sender, path, config.getMessage(), map);
    }

    @Override @SafeVarargs
    public final <T, U> void send(CommandSender sender, String path, FileConfiguration file, Map.Entry<T, U>... map) {
        String message = path;

        boolean containsInConfig = config.contains(path, file);
        if (containsInConfig) {
            boolean containsUse = config.contains(path + ".Use", file);
            boolean useChat = containsUse ? config.get(path + ".Use", file) : false;
            if (!containsUse) message = config.get(path, file);
            else if (useChat) message = config.get(path + ".Message", file);
            else return;
        }

        for (Map.Entry<T, U> entry : map) {
            message = message.replace(entry.getKey().toString(), entry.getValue().toString());
        }
        sender.sendMessage(message);
    }
}
