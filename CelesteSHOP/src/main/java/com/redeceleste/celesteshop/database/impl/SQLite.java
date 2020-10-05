package com.redeceleste.celesteshop.database.impl;

import com.redeceleste.celesteshop.Main;
import com.redeceleste.celesteshop.database.DataBase;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Getter
public class SQLite implements DataBase {
    private final Main main;
    private final File file;
    private Connection connection;

    public SQLite(Main main) {
        this.main = main;
        this.file = new File(main.getDataFolder(), "/shop.db");
        openConnection();
    }

    @Override
    public synchronized void openConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + file);
            createTables();
        } catch (SQLException | ClassNotFoundException e) {
            System.err.printf("Can't connect to SQLite: %s", e.getMessage());
            Bukkit.getPluginManager().disablePlugin(main);
        }
    }

    @Override
    public synchronized void closeConnection() {
        if (!isConnect()) return;

        try {
            connection.close();
        } catch (SQLException e) {
            System.err.printf("An error occurred while closing a connection to SQLite: %s", e.getMessage());
        }
    }

    @Override
    public synchronized void createTables() {
        try (PreparedStatement stm = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `shop` (`key` VARCHAR(16) PRIMARY KEY, `points` INT(8) NOT NULL)")) {
            stm.executeUpdate();
        } catch(SQLException e) {
            System.err.printf("Can't create table in SQLite: %s", e.getMessage());
            Bukkit.getPluginManager().disablePlugin(main);
        }
    }

    @Override
    public Boolean isConnect() {
        try {
            return !connection.isClosed();
        } catch (SQLException e) {
            System.err.printf("An error occurred while verifying that the SQLite is connected: %s", e.getMessage());
            return false;
        }
    }
}
