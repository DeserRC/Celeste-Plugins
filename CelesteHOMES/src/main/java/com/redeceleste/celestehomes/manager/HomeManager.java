package com.redeceleste.celestehomes.manager;

import com.redeceleste.celestehomes.Main;
import com.redeceleste.celestehomes.model.UserArgument;
import com.redeceleste.celestehomes.model.impls.User;
import com.redeceleste.celestehomes.builder.UserBuilder;
import com.redeceleste.celestehomes.builder.LocationBuilder;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeManager {
    public static Boolean isHome(Player p, String name) {
        try {
            UserArgument user = Main.getInstance().getUserDAO().cache.get(p.getName());
            return user.getHomes().containsKey(name.toLowerCase());
        } catch (Exception e) {
            return false;
        }
    }

    public static Integer isNumber(Player p) {
        List<Integer> list = new ArrayList<>();
        try {
            UserArgument user = Main.getInstance().getUserDAO().cache.get(p.getName());
            for (UserBuilder userBuilder : user.getHomes().values()) {
                list.add(userBuilder.getNumber());
            }

            for (int i=1;i<54;i++) {
                if (!list.contains(i)) {
                    return i;
                }
            }

        } catch (Exception ignored) { }
        return 1;
    }

    public static String numberHome(Player p, String name) {
        UserArgument user = Main.getInstance().getUserDAO().cache.get(p.getName());
        return user.getHomes().get(name.toLowerCase()).getNumber().toString();
    }

    public static void homeTeleport(Player p, String name) {
        UserArgument user = Main.getInstance().getUserDAO().cache.get(p.getName());
        TeleportManager.teleportPlayer(p, user.getHomes().get(name.toLowerCase()).getName(), user.getHomes().get(name.toLowerCase()).getLocation());
    }

    public static void setHome(Player p, String name) {
        if (!Main.getInstance().update.contains(p.getName())) {
            Main.getInstance().update.add(p.getName());
        }

        if (!Main.getInstance().getUserDAO().cache.containsKey(p.getName())) {
            Main.getInstance().getUserDAO().cache.put(p.getName(), new User(p.getName(), new HashMap<>()));
        }

        UserArgument user = Main.getInstance().getUserDAO().cache.get(p.getName());
        UserBuilder userBuilder = new UserBuilder(isNumber(p), LocationBuilder.serialize(p.getLocation()), name);
        user.getHomes().put(name.toLowerCase(), userBuilder);
    }

    public static void delHome(Player p, String name) {
        if (!Main.getInstance().update.contains(p.getName())) {
            Main.getInstance().update.add(p.getName());
        }
        
        UserArgument user = Main.getInstance().getUserDAO().cache.get(p.getName());
        user.getHomes().remove(name.toLowerCase());
    }
}
