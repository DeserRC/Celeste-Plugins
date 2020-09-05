package com.redeceleste.celestehomes.util.impls;

import com.redeceleste.celestehomes.util.ReflectionUtil;
import org.bukkit.entity.Player;

public class ActionBarUtil extends ReflectionUtil {
    public static void sendMessage(Player p, String message) {
        try {
            Object icb = getCon("ChatMessage", String.class, Object[].class).newInstance(message, new Object[0]);
            Object packet = getCon("PacketPlayOutChat", getNMS("IChatBaseComponent"), Byte.TYPE).newInstance(icb, (byte) 2);
            sendPacket(p, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}