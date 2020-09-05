package com.redeceleste.celestehomes.util.impls;

import com.redeceleste.celestehomes.util.ReflectionUtil;
import org.bukkit.entity.Player;

public class TitleUtil extends ReflectionUtil {
    public static void sendTitle(Player p, String title, String subtitle, Integer fadeIn, Integer show, Integer fadeOut) {
        try {
            if (title != null) {
                title.replace('&', '§').replace("%player%", p.getName());
                Object icb = getCon("ChatMessage", String.class, Object[].class).newInstance(title, new Object[0]);
                Object packetPlay = getDC("PacketPlayOutTitle").getField("TITLE").get(null);
                Object packet = getCon("PacketPlayOutTitle", getDC("PacketPlayOutTitle"), getNMS("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE).newInstance(packetPlay, icb, fadeIn, show, fadeOut);
                sendPacket(p, packet);
            }
            if (subtitle != null) {
                subtitle.replace('&', '§').replace("%player%", p.getName());
                Object icb = getCon("ChatMessage", String.class, Object[].class).newInstance(subtitle, new Object[0]);
                Object packetPlay = getDC("PacketPlayOutTitle").getField("SUBTITLE").get(null);
                Object packet = getCon("PacketPlayOutTitle", getDC("PacketPlayOutTitle"), getNMS("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE).newInstance(packetPlay, icb, fadeIn, show, fadeOut);
                sendPacket(p, packet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}