package com.redeceleste.celestespawners.task;

import com.redeceleste.celestespawners.CelesteSpawners;
import com.redeceleste.celestespawners.manager.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.*;

import static java.util.concurrent.TimeUnit.SECONDS;

public class KillAllTask implements Runnable {
    private final CelesteSpawners main;
    private final ConfigManager config;

    public KillAllTask(CelesteSpawners main) {
        this.main = main;
        this.config = main.getConfigManager();
    }

    @Override
    public void run() {
        int time = config.getConfig("Times.Remove-Entities-Start");

        String message = config.getMessage("Remove-Entities.Start");
        message = message.replace("{time}", String.valueOf(time));

        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(message);
        Bukkit.broadcastMessage("");

        main.getScheduled().schedule(this::clear, time, SECONDS);
    }

    public void clear() {
        for (World world : Bukkit.getWorlds()) { for (Entity entity : world.getEntities()) {
            if (entity instanceof LivingEntity || entity instanceof Item) {
                if (!(entity instanceof ArmorStand) && !(entity instanceof Player) && !(entity instanceof Villager)) {
                    entity.remove();
                }
            }
        } }

        String message = config.getMessage("Remove-Entities.Finish");

        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(message);
        Bukkit.broadcastMessage("");
    }
}
