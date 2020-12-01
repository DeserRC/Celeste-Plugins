package com.redeceleste.celestespawners.factory;

import com.redeceleste.celestespawners.CelesteSpawners;
import com.redeceleste.celestespawners.task.*;
import lombok.Getter;

@Getter
public class TaskFactory {
    private final CelesteSpawners main;
    private final SpawnerUpdateTask spawnerUpdateAsync;
    private final SpawnerUpdateTask spawnerUpdateSync;
    private final SpawnerGetTask spawnerGet;
    private final EntitySpawnTask entitySpawn;
    private final KillAllTask killAll;

    public TaskFactory(CelesteSpawners main) {
        this.main = main;
        this.spawnerUpdateAsync = new SpawnerUpdateTask(main, true);
        this.spawnerUpdateSync = new SpawnerUpdateTask(main, false);
        this.spawnerGet = new SpawnerGetTask(main);
        this.entitySpawn = new EntitySpawnTask(main);
        this.killAll = new KillAllTask(main);
    }
}
