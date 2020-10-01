package com.redeceleste.celesteshop.util;

import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Constructor;

public abstract class ReflectionUtil extends MessagesUtil {
    @SneakyThrows
    protected void sendPacket(CommandSender sender, Object packet) {
        Object handle = sender.getClass().getMethod("getHandle").invoke(sender);
        Object connection = handle.getClass().getField("playerConnection").get(handle);
        connection.getClass().getMethod("sendPacket", getNMS("Packet")).invoke(connection, packet);
    }

    protected Class<?> getNMS(String nms) throws ClassNotFoundException {
        return Class.forName("net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + "." + nms);
    }

    protected Class<?> getDC(String nms) throws ClassNotFoundException {
        return getNMS(nms).getDeclaredClasses()[0];
    }

    protected Constructor<?> getCon(String nms, Class<?>... clazz) throws NoSuchMethodException, ClassNotFoundException {
        return getNMS(nms).getConstructor(clazz);
    }
}
