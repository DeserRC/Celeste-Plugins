package com.redeceleste.celesteshop.util.impl;

import com.redeceleste.celesteshop.Main;
import com.redeceleste.celesteshop.manager.ConfigManager;
import com.redeceleste.celesteshop.model.ConfigType;
import com.redeceleste.celesteshop.util.MessagesUtil;
import org.bukkit.command.CommandSender;

import java.util.Map;

public class ChatUtil extends MessagesUtil {
    private final Main main;
    private final ConfigManager manager;

    public ChatUtil(Main main) {
        this.main = main;
        this.manager = main.getConfigManager();
    }

    @Override @SafeVarargs
    public final <T, U> void send(CommandSender sender, String path, ConfigType type, Boolean isConfig, Map.Entry<T, U>... map) {
        String message = path;

        if (isConfig) {
            message = manager.get(path, type);
        }

        for (Map.Entry<T, U> entry : map) {
            message = message.replace(entry.getKey().toString(), entry.getValue().toString());
        }

        sender.sendMessage(message);
    }
}
