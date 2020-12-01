package com.redeceleste.celestespawners.task;

import com.redeceleste.celestespawners.CelesteSpawners;
import com.redeceleste.celestespawners.dao.SpawnerDAO;
import com.redeceleste.celestespawners.factory.SpawnerFactory;
import com.redeceleste.celestespawners.model.SpawnerArgument;
import com.redeceleste.celestespawners.model.impl.SpawnerCustom;
import com.redeceleste.celestespawners.type.MobType;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;

import static com.redeceleste.celestespawners.util.LocationUtil.deserialize;
import static org.bukkit.Material.MOB_SPAWNER;

public class SpawnerGetTask implements Runnable {
    private final CelesteSpawners main;
    private final SpawnerFactory spawn;
    private final SpawnerDAO dao;

    public SpawnerGetTask(CelesteSpawners main) {
        this.main = main;
        this.spawn = main.getSpawnerFactory();
        this.dao = main.getConnectionFactory().getSpawner();
    }

    @Override
    public void run() {
        for (SpawnerArgument spawnerArg : dao.getAll().join()) {
            String name = ((SpawnerCustom) spawnerArg).getName();
            String location = spawnerArg.getLocation();

            long amount = spawnerArg.getAmount();
            
            MobType type = spawnerArg.getType();
            Block block = deserialize(location).getBlock();

            if (name == null) {
                spawnerArg = spawn.getSpawner().getArgument(location, amount, type);
            } else {
                spawnerArg = spawn.getSpawnerCustom().getArgument(location, name, amount);
                if (spawnerArg == null) continue;
            }

            spawn.getSpawners().put(location, spawnerArg);
            if (block.getType().equals(MOB_SPAWNER)) continue;

            block.setType(MOB_SPAWNER);
            ((CreatureSpawner) block.getState()).setSpawnedType(type.getEntity());
        }
    }
}
