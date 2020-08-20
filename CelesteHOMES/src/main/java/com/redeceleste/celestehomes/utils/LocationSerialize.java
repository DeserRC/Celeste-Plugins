package com.redeceleste.celestehomes.utils;

import java.util.StringJoiner;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationSerialize {

    public static String serialize(Location location) {
        StringJoiner sj = new StringJoiner(";");
        sj.add(location.getWorld().getName());
        sj.add(location.getX() + "");
        sj.add(location.getY() + "");
        sj.add(location.getZ() + "");
        sj.add(location.getYaw() + "");
        sj.add(location.getPitch() + "");
        return sj.toString();
    }

    public static Location deserialize(String serialized) {
        String[] s = serialized.split(";");
        return new Location(Bukkit.getWorld(s[0]),
                Double.parseDouble(s[1]),
                Double.parseDouble(s[2]),
                Double.parseDouble(s[3]),
                Float.parseFloat(s[4]),
                Float.parseFloat(s[5]));
    }
}