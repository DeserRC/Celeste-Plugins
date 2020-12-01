package com.redeceleste.celestespawners.database.impl;

import com.redeceleste.celestespawners.CelesteSpawners;
import com.redeceleste.celestespawners.database.DataBase;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Getter
public class SQLite implements DataBase {
    private final CelesteSpawners main;
    private final File file;
    private Connection connection;

    public SQLite(CelesteSpawners main) {
        this.main = main;
        this.file = new File(main.getDataFolder(), "/spawner.db");
        openConnection();
    }

    @Override
    public synchronized void openConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + file);
            createTables();
        } catch (SQLException | ClassNotFoundException e) {
            System.err.printf("Not possible connect to SQLite: %s", e.getMessage());
            Bukkit.getPluginManager().disablePlugin(main);
        }
    }

    @Override
    public synchronized void closeConnection() {
        if (!isConnect()) return;

        try {
            connection.close();
        } catch (SQLException e) {
            System.err.printf("An error occurred while close SQLite: %s", e.getMessage());
        }
    }

    @Override
    public synchronized void createTables() {
        try (PreparedStatement stm = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `spawner` (`location` VARCHAR(128) PRIMARY KEY, `spawners` TEXT NOT NULL)")) {
            stm.executeUpdate();
        } catch(SQLException e) {
            System.err.printf("It was not possible create the table in SQLite: %s", e.getMessage());
            Bukkit.getPluginManager().disablePlugin(main);
        }
    }

    @Override
    public Boolean isConnect() {
        try {
            return !connection.isClosed();
        } catch (SQLException e) {
            System.err.printf("Not possible check if the connection with SQLite is open: %s", e.getMessage());
            return false;
        }
    }
}
