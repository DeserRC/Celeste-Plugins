package com.redeceleste.celestespawners.task;

import com.redeceleste.celestespawners.CelesteSpawners;
import com.redeceleste.celestespawners.dao.SpawnerDAO;
import com.redeceleste.celestespawners.factory.SpawnerFactory;
import com.redeceleste.celestespawners.model.SpawnerArgument;

public class SpawnerUpdateTask implements Runnable {
    private final CelesteSpawners main;
    private final SpawnerFactory spawn;
    private final SpawnerDAO dao;
    private final boolean async;

    public SpawnerUpdateTask(CelesteSpawners main, boolean async) {
        this.main = main;
        this.spawn = main.getSpawnerFactory();
        this.dao = main.getConnectionFactory().getSpawner();
        this.async = async;
    }

    @Override
    public void run() {
        for (String location : spawn.getDeleteSpawners()) {
            if (spawn.getSpawners().containsKey(location)) continue;
            dao.delete(location, async);
            spawn.getDeleteSpawners().remove(location);
        }

        for (SpawnerArgument spawnerArg : spawn.getSpawners().values()) {
            dao.replace(spawnerArg, async);
        }
    }
}
