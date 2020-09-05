package com.redeceleste.celestehomes.manager;

import com.redeceleste.celestehomes.Main;
import org.bukkit.entity.Player;

public class PermissionManager {
    public static String getPermission(Player p) {
        if (p.isOp() || p.hasPermission(ConfigManager.Permission + "admin"))
            return "54";
        for (int n=54;n>0;n--) {
            if (p.hasPermission(ConfigManager.Permission + n)) {
                return String.valueOf(n);
            }
        }
        return "0";
    }

    public static Boolean hasAdmin(Player p) {
        return p.hasPermission(ConfigManager.Permission + "admin");
    }

    public static Boolean hasDelayBypass(Player p) {
        return p.hasPermission(ConfigManager.Permission + "bypass") || p.hasPermission(ConfigManager.Permission + "admin");
    }

    public static Boolean hasDelayOtherTeleportBypass(Player p) {
        return p.hasPermission(ConfigManager.Permission + "otherteleportbypass") || p.hasPermission(ConfigManager.Permission + "admin");
    }

    public static Integer getAmountHomes(Player p) {
        try {
            return Main.getInstance().getUserDAO().cache.get(p.getName()).getHomes().size();
        } catch (Exception e) {
            return 0;
        }
    }

    public static Integer remainingHomes(Player p, Integer i) {
        try {
            return i-Main.getInstance().getUserDAO().cache.get(p.getName()).getHomes().size();
        } catch (Exception e) {
            return i;
        }
    }
}