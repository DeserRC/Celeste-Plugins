package com.redeceleste.celestekits.factory;

import com.redeceleste.celestekits.CelesteKit;
import com.redeceleste.celestekits.dao.UserDAO;
import com.redeceleste.celestekits.database.DataBase;
import com.redeceleste.celestekits.database.impl.MySQL;
import com.redeceleste.celestekits.database.impl.SQLite;
import com.redeceleste.celestekits.manager.ConfigManager;
import lombok.Getter;

@Getter
public class ConnectionFactory {
    private final CelesteKit main;
    private final ConfigManager config;
    private final DataBase dataBase;
    private final UserDAO dao;

    public ConnectionFactory(CelesteKit main) {
        this.main = main;
        this.config = main.getConfigManager();

        if (config.getConfig("MySQL.Use")) {
            this.dataBase = new MySQL(main, config.getConfig("MySQL.Host"), config.getConfig("MySQL.User"), config.getConfig("MySQL.DataBase"), config.getConfig("MySQL.Password"));
        } else {
            this.dataBase = new SQLite(main);
        }

        this.dao = new UserDAO(main, dataBase);
    }
}