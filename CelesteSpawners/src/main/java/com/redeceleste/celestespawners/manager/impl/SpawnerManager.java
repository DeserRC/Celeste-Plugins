package com.redeceleste.celestespawners.manager.impl;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.redeceleste.celestespawners.CelesteSpawners;
import com.redeceleste.celestespawners.builder.SpawnerBuilder;
import com.redeceleste.celestespawners.factory.SpawnerFactory;
import com.redeceleste.celestespawners.manager.ConfigManager;
import com.redeceleste.celestespawners.manager.HologramManager;
import com.redeceleste.celestespawners.manager.Manager;
import com.redeceleste.celestespawners.model.SpawnerArgument;
import com.redeceleste.celestespawners.type.MobType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class SpawnerManager extends Manager {
    private final CelesteSpawners main;
    private final SpawnerFactory spawn;
    private final ConfigManager config;
    private final HologramManager hologram;

    public SpawnerManager(CelesteSpawners main, SpawnerFactory spawn) {
        super(main, spawn);
        this.main = main;
        this.spawn = spawn;
        this.config = main.getConfigManager();
        this.hologram = main.getHologramManager();
    }

    public void createSpawner(String location, MobType type) {
        createSpawner(location, 1L, type);
    }

    public void createSpawner(String location, long amount, MobType type) {
        Hologram hologramSpawner = hologram.createHologram(location, type.getName(), amount, type.getHead());
        spawn.getSpawners().put(location, new SpawnerBuilder()
                .location(location)
                .hologram(hologramSpawner)
                .type(type)
                .amount(amount).buildSpawner());
    }

    public SpawnerArgument getArgument(String location, long amount, MobType type) {
        Hologram hologramSpawner = hologram.createHologram(location, type.getName(), amount, type.getHead());
        return new SpawnerBuilder()
                .location(location)
                .hologram(hologramSpawner)
                .type(type)
                .amount(amount).buildSpawner();
    }

    public ItemStack getSpawner(long amount, MobType type) {
        if (!isExist(type.getName())) return null;
        String name = config.getConfig("Spawner.Spawner.Name");
        List<String> lore = config.getListConfig("Spawner.Spawner.Lore");
        return getSpawner(type.getName(), String.valueOf(amount), name, lore, type, false);
    }

    public boolean isExist(String mobName) {
        MobType type = MobType.getMob(mobName);
        return type != null;
    }
}
