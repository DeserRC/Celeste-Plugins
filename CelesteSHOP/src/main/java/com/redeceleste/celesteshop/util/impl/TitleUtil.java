package com.redeceleste.celesteshop.util.impl;

import com.redeceleste.celesteshop.Main;
import com.redeceleste.celesteshop.manager.ConfigManager;
import com.redeceleste.celesteshop.model.ConfigType;
import com.redeceleste.celesteshop.util.ReflectionUtil;
import lombok.SneakyThrows;
import org.bukkit.command.CommandSender;

import java.util.Map;

public class TitleUtil extends ReflectionUtil {
    private final Main main;
    private final ConfigManager manager;

    public TitleUtil(Main main) {
        this.main = main;
        this.manager = main.getConfigManager();
    }

    @Override @SafeVarargs @SneakyThrows
    public final <T, U> void send(CommandSender sender, String path, ConfigType type, Map.Entry<T, U>... map) {
        String title = path;
        String subtitle = path;

        if (manager.contains(path, type)) {
            if (manager.get(path + ".Use", type)) {
                title = manager.get(path + ".Title", type);
                subtitle = manager.get(path + ".SubTitle", type);
            } else return;
        }

        for (Map.Entry<T, U> entry : map) {
            title = title.replace(entry.getKey().toString(), entry.getValue().toString());
            subtitle = subtitle.replace(entry.getKey().toString(), entry.getValue().toString());
        }

        if (title != null) {
            Object icb = getCon("ChatMessage", String.class, Object[].class).newInstance(title, new Object[0]);
            Object packetPlay = getDC("PacketPlayOutTitle").getField("TITLE").get(null);
            Object packet = getCon("PacketPlayOutTitle", getDC("PacketPlayOutTitle"), getNMS("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE).newInstance(packetPlay, icb, 3, 3, 3);
            sendPacket(sender, packet);
        }

        if (subtitle != null) {
            Object icb = getCon("ChatMessage", String.class, Object[].class).newInstance(subtitle, new Object[0]);
            Object packetPlay = getDC("PacketPlayOutTitle").getField("SUBTITLE").get(null);
            Object packet = getCon("PacketPlayOutTitle", getDC("PacketPlayOutTitle"), getNMS("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE).newInstance(packetPlay, icb, 3, 3, 3);
            sendPacket(sender, packet);
        }
    }
}