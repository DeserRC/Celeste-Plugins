package com.redeceleste.celestekits.database.impl;

import com.redeceleste.celestekits.CelesteKit;
import com.redeceleste.celestekits.database.DataBase;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Getter
public class SQLite implements DataBase {
    private final CelesteKit main;
    private final File file;
    private Connection connection;

    public SQLite(CelesteKit main) {
        this.main = main;
        this.file = new File(main.getDataFolder(), "/kits.db");
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
        try (PreparedStatement stm = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `kits` (`player` VARCHAR(16) PRIMARY KEY, `kit` TEXT NOT NULL)")) {
            stm.executeUpdate();
        } catch (SQLException e) {
            System.err.printf("It was not possible create the table in SQLite: %s", e.getMessage());
            Bukkit.getPluginManager().disablePlugin(main);
        }
    }

    @Override
    public boolean isConnect() {
        try {
            return !connection.isClosed();
        } catch (SQLException e) {
            System.err.printf("Not possible check if the connection with SQLite is open: %s", e.getMessage());
            return false;
        }
    }
}
