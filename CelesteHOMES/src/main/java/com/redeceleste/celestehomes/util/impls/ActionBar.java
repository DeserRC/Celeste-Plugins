package com.redeceleste.celestehomes.util.impls;

import com.redeceleste.celestehomes.util.Packet;
import org.bukkit.entity.Player;

public class ActionBar extends Packet {
    public static void sendMessage(Player p, String message) {
        try {
            Object icb = getClassName("ChatMessage").getConstructor(String.class, Object[].class).newInstance(message, new Object[0]);
            Object packet = getClassName("PacketPlayOutChat").getConstructor(getClassName("IChatBaseComponent"), Byte.TYPE).newInstance(icb, (byte) 2);
            sendPacket(p, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}