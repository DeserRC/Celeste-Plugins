package com.redeceleste.celestehomes.database;

import com.redeceleste.celestehomes.Main;
import com.redeceleste.celestehomes.model.UserArgument;
import org.bukkit.scheduler.BukkitRunnable;

public class AutoSave {
    public static void save() {
        for (UserArgument userArgument : Main.getInstance().getUserDAO().cache.values()) {
            if (Main.getInstance().update.contains(userArgument.getName())) {
                Main.getInstance().getUserDAO().insert(userArgument);
            }
        }
    }

    public static void saveLooping() {
        new BukkitRunnable() {
            public void run() {
                save();
            }
        }.runTaskTimerAsynchronously(Main.getInstance(), 72000L, 72000L);
    }
}
