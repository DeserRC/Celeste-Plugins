package com.redeceleste.celestehomes.runnable;

import com.redeceleste.celestehomes.Main;
import com.redeceleste.celestehomes.model.UserArgument;
import org.bukkit.scheduler.BukkitRunnable;

public class SaveTask extends BukkitRunnable {
    public SaveTask() {
        runTaskTimerAsynchronously(Main.getInstance(), 72000L, 72000L);
    }

    @Override
    public void run() {
        save();
    }

    public static void save() {
        for (UserArgument userArgument : Main.getInstance().getUserDAO().cache.values()) {
            if (Main.getInstance().update.contains(userArgument.getPlayer())) {
                Main.getInstance().getUserDAO().insert(userArgument);
            }
        }
    }
}
