package com.redeceleste.celesteshop.util.impl;

import com.redeceleste.celesteshop.Main;
import com.redeceleste.celesteshop.manager.ConfigManager;
import com.redeceleste.celesteshop.model.ConfigType;
import com.redeceleste.celesteshop.util.ReflectionUtil;
import lombok.SneakyThrows;
import org.bukkit.command.CommandSender;

import java.util.Map;

public class BarUtil extends ReflectionUtil {
    private final Main main;
    private final ConfigManager config;

    public BarUtil(Main main) {
        this.main = main;
        this.config = main.getConfigManager();
    }

    @Override @SafeVarargs @SneakyThrows
    public final <T, U> void send(CommandSender sender, String path, ConfigType type, Map.Entry<T, U>... map) {
        String message = path;

        if (config.contains(path, type)) {
            if (config.get(path + ".Use", type)) {
                message = config.get(path + ".Message", type);
            } else return;
        }

        for (Map.Entry<T, U> entry : map) {
            message = message.replace(entry.getKey().toString(), entry.getValue().toString());
        }

        Object icb = getCon("ChatMessage", String.class, Object[].class).newInstance(message, new Object[0]);
        Object packet = getCon("PacketPlayOutChat", getNMS("IChatBaseComponent"), Byte.TYPE).newInstance(icb, (byte) 2);
        sendPacket(sender, packet);
    }
}