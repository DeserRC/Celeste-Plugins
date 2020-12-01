package com.redeceleste.celestespawners.task;

import com.redeceleste.celestespawners.CelesteSpawners;
import com.redeceleste.celestespawners.event.impl.EntitySpawnEvent;
import com.redeceleste.celestespawners.factory.SpawnerFactory;
import com.redeceleste.celestespawners.manager.ConfigManager;
import com.redeceleste.celestespawners.model.SpawnerArgument;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import static com.redeceleste.celestespawners.util.LocationUtil.deserialize;

public class EntitySpawnTask implements Runnable {
    private final CelesteSpawners main;
    private final SpawnerFactory spawn;
    private final ConfigManager config;

    public EntitySpawnTask(CelesteSpawners main) {
        this.main = main;
        this.spawn = main.getSpawnerFactory();
        this.config = main.getConfigManager();
    }

    @Override
    public void run() {
        int radius = config.getConfig("Spawner.Range-Limit");
        for (SpawnerArgument spawnerArg : spawn.getSpawners().values()) {
            String location = spawnerArg.getLocation();
            Location loc = deserialize(location);

            for (Entity entity : loc.getWorld().getNearbyEntities(loc, radius, 100, radius)) {
                if (!(entity instanceof Player)) continue;
                Bukkit.getPluginManager().callEvent(new EntitySpawnEvent(spawnerArg));
            }
        }
    }
}
