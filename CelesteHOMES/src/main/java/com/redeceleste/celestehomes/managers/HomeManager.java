package com.redeceleste.celestehomes.managers;

import com.redeceleste.celestehomes.Main;
import com.redeceleste.celestehomes.models.impls.User;
import com.redeceleste.celestehomes.models.impls.UserBuilder;
import com.redeceleste.celestehomes.utils.LocationSerialize;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeManager {
    public static Boolean isHome(Player p, String name) {
        try {
            User user = (User) Main.getInstance().getDAO().cache.get(p.getName());
            return user.getHomes().containsKey(name);
        } catch (Exception e) {
            return false;
        }
    }

    public static Integer isNumber(Player p) {
        List<Integer> list = new ArrayList<>();
        try {
            User user = (User) Main.getInstance().getDAO().cache.get(p.getName());
            for (UserBuilder userBuilder : user.getHomes().values()) {
                list.add(userBuilder.getNumber());
            }

            for (int i=1;i<54;i++) {
                if (!list.contains(i)) {
                    return i;
                }
            }

        } catch (Exception ignored) {
        }
        return 1;
    }

    public static String numberHome(Player p, String name) {
        try {
            User user = (User) Main.getInstance().getDAO().cache.get(p.getName());
            return user.getHomes().get(name).getNumber().toString();
        } catch (Exception e) {
            return null;
        }
    }

    public static void homeTeleport(Player p, String name) {
        User user = (User) Main.getInstance().getDAO().cache.get(p.getName());
        TeleportManager.teleportPlayer(p, user.getHomes().get(name).getLocation(), name);
    }

    public static void setHome(Player p, String name) {
        if (!Main.getInstance().update.contains(p.getName())) {
            Main.getInstance().update.add(p.getName());
        }

        if (!Main.getInstance().getDAO().cache.containsKey(p.getName())) {
            Main.getInstance().getDAO().cache.put(p.getName(), new User(p.getName(), new HashMap<>()));
        }

        User user = (User) Main.getInstance().getDAO().cache.get(p.getName());
        UserBuilder userBuilder = new UserBuilder(isNumber(p), LocationSerialize.serialize(p.getLocation()), name);
        user.getHomes().put(name, userBuilder);
    }

    public static void delHome(Player p, String name) {
        User user = (User) Main.getInstance().getDAO().cache.get(p.getName());
        user.getHomes().remove(name);
    }
}
