package com.redeceleste.celesteshop.factory;

import com.redeceleste.celesteshop.Main;
import com.redeceleste.celesteshop.dao.PointsDAO;
import com.redeceleste.celesteshop.database.DataBase;
import com.redeceleste.celesteshop.database.impl.MySQL;
import com.redeceleste.celesteshop.database.impl.SQLite;
import com.redeceleste.celesteshop.manager.ConfigManager;
import lombok.Getter;

@Getter
public class ConnectionFactory {
    private final Main main;
    private final ConfigManager config;
    private final DataBase dataBase;
    private final PointsDAO dao;

    public ConnectionFactory(Main main) {
        this.main = main;
        this.config = main.getConfigManager();

        if (config.getConfig("MySQL.Use")) {
            dataBase = new MySQL(main, config.getConfig("MySQL.Host"), config.getConfig("MySQL.User"), config.getConfig("MySQL.DataBase"), config.getConfig("MySQL.Password"));
        } else {
            dataBase = new SQLite(main);
        }

        this.dao = new PointsDAO(main, dataBase);
    }
}