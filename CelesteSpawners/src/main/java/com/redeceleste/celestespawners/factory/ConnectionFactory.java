package com.redeceleste.celestespawners.factory;

import com.redeceleste.celestespawners.CelesteSpawners;
import com.redeceleste.celestespawners.dao.SpawnerDAO;
import com.redeceleste.celestespawners.database.DataBase;
import com.redeceleste.celestespawners.database.impl.MySQL;
import com.redeceleste.celestespawners.database.impl.SQLite;
import com.redeceleste.celestespawners.manager.ConfigManager;
import lombok.Getter;

@Getter
public class ConnectionFactory {
    private final CelesteSpawners main;
    private final ConfigManager config;
    private final DataBase dataBase;
    private final SpawnerDAO spawner;

    public ConnectionFactory(CelesteSpawners main) {
        this.main = main;
        this.config = main.getConfigManager();

        boolean useMySQL = config.getConfig("MySQL.Use");
        if (useMySQL) {
            String host = config.getConfig("MySQL.Host");
            String user = config.getConfig("MySQL.User");
            String database = config.getConfig("MySQL.DataBase");
            String password = config.getConfig("MySQL.Password");
            this.dataBase = new MySQL(main, host, user, database, password);
        } else this.dataBase = new SQLite(main);

        this.spawner = new SpawnerDAO(main, dataBase);
    }
}