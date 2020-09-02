package com.redeceleste.celestehomes.util.impls;

import com.redeceleste.celestehomes.util.Packet;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;

public class SendTitle extends Packet {
    public static void sendTitle(Player p, String title, String subtitle, Integer fadeIn, Integer show, Integer fadeOut) {
        try {
            if (title != null) {
                title.replace('&', '§').replace("%player%", p.getName());
                Object packetPlay = getClassName("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null);
                Object chat = getClassName("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + title + "\"}");
                Constructor<?> constructor = getClassName("PacketPlayOutTitle").getConstructor(getClassName("PacketPlayOutTitle").getDeclaredClasses()[0], getClassName("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE);
                Object packet = constructor.newInstance(packetPlay, chat, fadeIn, show, fadeOut);
                sendPacket(p, packet);
            }
            if (subtitle != null) {
                subtitle.replace('&', '§').replace("%player%", p.getName());
                Object packetPlay = getClassName("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get(null);
                Object chat = getClassName("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + subtitle + "\"}");
                Constructor<?> constructor = getClassName("PacketPlayOutTitle").getConstructor(getClassName("PacketPlayOutTitle").getDeclaredClasses()[0], getClassName("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE);
                Object packet = constructor.newInstance(packetPlay, chat, fadeIn, show, fadeOut);
                sendPacket(p, packet);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}