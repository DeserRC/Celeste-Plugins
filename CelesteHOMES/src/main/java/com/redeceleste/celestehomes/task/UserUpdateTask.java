package com.redeceleste.celestehomes.task;

import com.redeceleste.celestehomes.Main;
import com.redeceleste.celestehomes.model.UserArgument;
import org.bukkit.scheduler.BukkitRunnable;

public class UserUpdateTask extends BukkitRunnable {
    public UserUpdateTask() {
        runTaskTimerAsynchronously(Main.getInstance(), 72000L, 72000L);
    }

    @Override
    public void run() {
        update();
    }

    public static void update() {
        for (UserArgument userArgument : Main.getInstance().getUserDAO().cache.values()) {
            if (Main.getInstance().update.contains(userArgument.getPlayer())) {
                Main.getInstance().getUserDAO().insert(userArgument);
            }
        }
    }
}