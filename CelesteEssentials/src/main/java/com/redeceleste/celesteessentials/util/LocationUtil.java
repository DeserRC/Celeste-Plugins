package com.redeceleste.celesteessentials.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.StringJoiner;

public class LocationUtil {
    public static String serialize(Location location, boolean yawAndPitch) {
        StringJoiner sj = new StringJoiner(":");
        sj.add(location.getWorld().getName());
        sj.add(location.getX() + "");
        sj.add(location.getY() + "");
        sj.add(location.getZ() + "");
        if (yawAndPitch) {
            sj.add(location.getYaw() + "");
            sj.add(location.getPitch() + "");
        }
        return sj.toString();
    }

    public static Location deserialize(String serializedLocation) {
        String[] split = serializedLocation.split(":");
        if (split.length == 3) {
            return new Location(Bukkit.getWorld(split[0]),
                    Double.parseDouble(split[1]),
                    Double.parseDouble(split[2]),
                    Double.parseDouble(split[3]));
        }
        return new Location(Bukkit.getWorld(split[0]),
                Double.parseDouble(split[1]),
                Double.parseDouble(split[2]),
                Double.parseDouble(split[3]),
                Float.parseFloat(split[4]),
                Float.parseFloat(split[5]));
    }

    public static Location getLocation(Location loc, String[] args) {
        Double x = loc.getX();
        Double y = loc.getY();
        Double z = loc.getZ();

        if (args[0].contains("~")) {
            args[0] = args[0].replace("~", "");
            if (!args[0].equals("")) x += Double.parseDouble(args[0]);
        } else x = Double.parseDouble(args[0]);

        if (args[1].contains("~")) {
            args[1] = args[1].replace("~", "");
            if (!args[1].equals("")) y += Double.parseDouble(args[1]);
        } else y = Double.parseDouble(args[1]);

        if (args[2].contains("~")) {
            args[2] = args[2].replace("~", "");
            if (!args[2].equals("")) z += Double.parseDouble(args[2]);
        } else z = Double.parseDouble(args[2]);
        return new Location(loc.getWorld(), x, y, z, loc.getYaw(), loc.getPitch());
    }
}
