package com.redeceleste.celestehomes.database;

import com.redeceleste.celestehomes.Main;
import com.redeceleste.celestehomes.models.UserArgument;
import org.bukkit.scheduler.BukkitRunnable;

public class AutoSave {
    public static void save() {
        for (UserArgument userArgument : Main.getInstance().getDAO().cache.values()) {
            if (Main.getInstance().update.contains(userArgument.getName())) {
                Main.getInstance().getDAO().insert(userArgument);
            }
        }
    }

    public static void saveLooping() {
        new BukkitRunnable() {
            public void run() {
                for (UserArgument userArgument : Main.getInstance().getDAO().cache.values()) {
                    if (Main.getInstance().update.contains(userArgument.getName())) {
                        Main.getInstance().getDAO().insert(userArgument);
                    }
                }
            }
        }.runTaskTimerAsynchronously(Main.getInstance(), 72000L, 72000L);
    }
}
