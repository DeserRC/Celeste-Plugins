package com.redeceleste.celestehomes.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class ActionBar {
    private static Class<?> CRAFTPLAYERCLASS;
    private static Class<?> PACKET_PLAYER_CHAT_CLASS;
    private static Class<?> ICHATCOMP;
    private static Class<?> CHATMESSAGE;
    private static Class<?> PACKET_CLASS;
    private static Constructor<?> PACKET_PLAYER_CHAT_CONSTRUCTOR;
    private static Constructor<?> CHATMESSAGE_CONSTRUCTOR;
    private static final String SERVER_VERSION;

    static {
        String name = Bukkit.getServer().getClass().getName();
        name = name.substring(name.indexOf("craftbukkit.") + "craftbukkit.".length());
        name = (SERVER_VERSION = name.substring(0, name.indexOf(".")));
        try {
            ActionBar.CRAFTPLAYERCLASS = Class.forName("org.bukkit.craftbukkit." + ActionBar.SERVER_VERSION + ".entity.CraftPlayer");
            ActionBar.PACKET_PLAYER_CHAT_CLASS = Class.forName("net.minecraft.server." + ActionBar.SERVER_VERSION + ".PacketPlayOutChat");
            ActionBar.PACKET_CLASS = Class.forName("net.minecraft.server." + ActionBar.SERVER_VERSION + ".Packet");
            ActionBar.ICHATCOMP = Class.forName("net.minecraft.server." + ActionBar.SERVER_VERSION + ".IChatBaseComponent");
            ActionBar.PACKET_PLAYER_CHAT_CONSTRUCTOR = ActionBar.PACKET_PLAYER_CHAT_CLASS.getConstructor(ActionBar.ICHATCOMP, Byte.TYPE);
            ActionBar.CHATMESSAGE = Class.forName("net.minecraft.server." + ActionBar.SERVER_VERSION + ".ChatMessage");
            ActionBar.CHATMESSAGE_CONSTRUCTOR = ActionBar.CHATMESSAGE.getDeclaredConstructor(String.class, Object[].class);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendActionBarMessage(final Player player, final String message) {
        try {
            final Object icb = ActionBar.CHATMESSAGE_CONSTRUCTOR.newInstance(message, new Object[0]);
            final Object packet = ActionBar.PACKET_PLAYER_CHAT_CONSTRUCTOR.newInstance(icb, (byte)2);
            final Object craftplayerInst = ActionBar.CRAFTPLAYERCLASS.cast(player);
            final Method methodHandle = ActionBar.CRAFTPLAYERCLASS.getMethod("getHandle", (Class<?>[])new Class[0]);
            final Object methodhHandle = methodHandle.invoke(craftplayerInst, new Object[0]);
            final Object playerConnection = methodhHandle.getClass().getField("playerConnection").get(methodhHandle);
            playerConnection.getClass().getMethod("sendPacket", ActionBar.PACKET_CLASS).invoke(playerConnection, packet);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}