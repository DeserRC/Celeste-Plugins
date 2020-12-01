package com.redeceleste.celestespawners.factory;

import com.redeceleste.celestespawners.CelesteSpawners;
import com.redeceleste.celestespawners.manager.EntityManager;
import com.redeceleste.celestespawners.manager.ItemManager;
import com.redeceleste.celestespawners.manager.impl.SpawnerCustomManager;
import com.redeceleste.celestespawners.manager.impl.SpawnerManager;
import com.redeceleste.celestespawners.model.SpawnerArgument;
import lombok.Getter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter
public class SpawnerFactory {
    private final CelesteSpawners main;
    private final SpawnerManager spawner;
    private final SpawnerCustomManager spawnerCustom;
    private final EntityManager entity;
    private final ItemManager item;
    private final Map<String, SpawnerArgument> spawners;
    private final Map<String, Long> cooldown;
    private final Set<String> deleteSpawners;

    public SpawnerFactory(CelesteSpawners main) {
        this.main = main;
        this.spawner = new SpawnerManager(main, this);
        this.spawnerCustom = new SpawnerCustomManager(main, this);
        this.entity = new EntityManager(main);
        this.item = new ItemManager(main);
        this.spawners = new HashMap<>();
        this.cooldown = new HashMap<>();
        this.deleteSpawners = new HashSet<>();
    }
}
