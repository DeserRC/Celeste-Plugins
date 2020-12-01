package com.redeceleste.celestespawners.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.StringJoiner;

public class LocationUtil {
    public static String serialize(Location location) {
        StringJoiner sj = new StringJoiner(";");
        sj.add(location.getWorld().getName());
        sj.add(location.getX() + "");
        sj.add(location.getY() + "");
        sj.add(location.getZ() + "");
        return sj.toString();
    }

    public static Location deserialize(String serializedLocation) {
        String[] split = serializedLocation.split(";");
        return new Location(Bukkit.getWorld(split[0]),
                Double.parseDouble(split[1]),
                Double.parseDouble(split[2]),
                Double.parseDouble(split[3]));
    }
}