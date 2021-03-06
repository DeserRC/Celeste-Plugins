package com.redeceleste.celesteshop.util.impl;

import com.redeceleste.celesteshop.CelesteSHOP;
import com.redeceleste.celesteshop.manager.ConfigManager;
import com.redeceleste.celesteshop.model.ConfigType;
import com.redeceleste.celesteshop.util.MessagesUtil;
import org.bukkit.command.CommandSender;

import java.util.Map;

public class ChatUtil extends MessagesUtil {
    private final CelesteSHOP main;
    private final ConfigManager config;

    public ChatUtil(CelesteSHOP main) {
        this.main = main;
        this.config = main.getConfigManager();
    }

    @Override @SafeVarargs
    public final <T, U> void send(CommandSender sender, String path, ConfigType type, Map.Entry<T, U>... map) {
        String message = path;

        if (config.contains(path, type)) {
            if (!config.contains(path + ".Use", type)) {
                message = config.get(path, type);
            } else if (config.get(path + ".Use", type)) {
                message = config.get(path + ".Message", type);
            } else return;
        }

        for (Map.Entry<T, U> entry : map) {
            message = message.replace(entry.getKey().toString(), entry.getValue().toString());
        }

        sender.sendMessage(message);
    }
}
