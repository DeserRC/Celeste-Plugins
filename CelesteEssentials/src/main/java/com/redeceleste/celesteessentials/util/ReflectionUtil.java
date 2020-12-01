package com.redeceleste.celesteessentials.util;

import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionUtil {
    private static final String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.") [3];
    private static final String pathNMS = "net.minecraft.server." + version + ".";
    private static final String pathOBC = "org.bukkit.craftbukkit." + version + ".";

    @SneakyThrows
    public static void sendPacket(CommandSender sender, Object packet) {
        Object handle = sender.getClass().getMethod("getHandle").invoke(sender);
        Object connection = handle.getClass().getField("playerConnection").get(handle);
        connection.getClass().getMethod("sendPacket", getNMS("Packet")).invoke(connection, packet);
    }

    public static Class<?> getNMS(String nms) throws ClassNotFoundException {
        return Class.forName(pathNMS + nms);
    }

    public static Class<?> getOBC(String nms) throws ClassNotFoundException {
        return Class.forName(pathOBC + nms);
    }

    public static Class<?> getClazz(String path, String clazzName) throws ClassNotFoundException {
        return Class.forName(path + clazzName);
    }

    public static Constructor<?> getCon(String nms, Class<?>... parameterClass) throws ClassNotFoundException, NoSuchMethodException {
        return getNMS(nms).getConstructor(parameterClass);
    }

    public static Constructor<?> getCon(Class<?> clazz, Class<?>... parameterClass) throws NoSuchMethodException {
        return clazz.getConstructor(parameterClass);
    }

    public static Constructor<?> getDcCon(String nms, Class<?>... parameterClass) throws ClassNotFoundException, NoSuchMethodException {
        Constructor<?> constructor = getNMS(nms).getDeclaredConstructor(parameterClass);
        constructor.setAccessible(true);
        return constructor;
    }

    public static Constructor<?> getDcCon(Class<?> clazz, Class<?>... parameterClass) throws NoSuchMethodException {
        Constructor<?> constructor = clazz.getDeclaredConstructor(parameterClass);
        constructor.setAccessible(true);
        return constructor;
    }

    public static Method getMethod(String nms, String methodName, Class<?>... parameterClass) throws ClassNotFoundException, NoSuchMethodException {
        return getNMS(nms).getMethod(methodName, parameterClass);
    }

    public static Method getMethod(Class<?> clazz, String methodName, Class<?>... parameterClass) throws NoSuchMethodException {
        return clazz.getMethod(methodName, parameterClass);
    }

    public static Method getDcMethod(String nms, String methodName, Class<?>... parameterClass) throws ClassNotFoundException, NoSuchMethodException {
        Method method = getNMS(nms).getDeclaredMethod(methodName, parameterClass);
        method.setAccessible(true);
        return method;
    }

    public static Method getDcMethod(Class<?> clazz, String methodName, Class<?>... parameterClass) throws NoSuchMethodException {
        Method method = clazz.getDeclaredMethod(methodName, parameterClass);
        method.setAccessible(true);
        return method;
    }

    public static Field getField(String nms, String variableName) throws ClassNotFoundException, NoSuchFieldException {
        return getNMS(nms).getField(variableName);
    }

    public static Field getField(Class<?> clazz, String variableName) throws NoSuchFieldException {
        return clazz.getField(variableName);
    }

    public static Field getDcField(String nms, String variableName) throws ClassNotFoundException, NoSuchFieldException {
        Field field = getNMS(nms).getDeclaredField(variableName);
        field.setAccessible(true);
        return field;
    }

    public static Field getDcField(Class<?> clazz, String variableName) throws NoSuchFieldException {
        Field field = clazz.getDeclaredField(variableName);
        field.setAccessible(true);
        return field;
    }

    public static Object instance(Constructor<?> constructor, Object... args) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        return constructor.newInstance(args);
    }

    public static Object invoke(Method method, Object instance, Object... args) throws InvocationTargetException, IllegalAccessException {
        return method.invoke(instance, args);
    }

    public static Object invokeStatic(Method method, Object... args) throws InvocationTargetException, IllegalAccessException {
        return method.invoke(null, args);
    }

    public static Object get(Field field, Object instance) throws IllegalAccessException {
        return field.get(instance);
    }

    public static Class<?> getType(Field field) {
        return field.getType();
    }

    public static boolean isEqualsOrMoreRecent(int checkVersion) {
        return Integer.parseInt(version.split("_") [1]) >= checkVersion;
    }
}
