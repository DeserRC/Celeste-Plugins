package com.redeceleste.celestehomes.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;

public abstract class ReflectionUtil {
    protected static void sendPacket(Player p, Object packet) {
        try {
            Object handle = p.getClass().getMethod("getHandle").invoke(p);
            Object connection = handle.getClass().getField("playerConnection").get(handle);
            connection.getClass().getMethod("sendPacket", getNMS("Packet")).invoke(connection, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected static Class<?> getNMS(String nms) {
        try {
            return Class.forName("net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + "." + nms);
        } catch (Exception ignored) {
            return null;
        }
    }

    protected static Class<?> getDC(String nms) {
        try {
            return getNMS(nms).getDeclaredClasses()[0];
        } catch (Exception ignored) {
            return null;
        }
    }

    protected static Constructor<?> getCon(String nms, Class<?>... clas) {
        try {
            return getNMS(nms).getConstructor(clas);
        } catch (Exception ignored) {
            return null;
        }
    }
}
