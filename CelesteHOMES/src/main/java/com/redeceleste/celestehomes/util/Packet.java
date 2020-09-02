package com.redeceleste.celestehomes.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public abstract class Packet {
    protected static void sendPacket(Player p, Object packet) {
        try {
            Object handle = p.getClass().getMethod("getHandle").invoke(p);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", getClassName("Packet")).invoke(playerConnection, packet);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected static Class<?> getClassName(String name) {
        try {
            return Class.forName("net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + "." + name);
        }
        catch (Exception ignored) {
            return null;
        }
    }
}
