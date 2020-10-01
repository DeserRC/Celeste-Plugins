package com.redeceleste.celesteshop.database.impl;

import com.redeceleste.celesteshop.Main;
import com.redeceleste.celesteshop.database.DataBase;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Getter
public class MySQL implements DataBase {
    private final Main main;
    private final String host, database, user, password;
    private Connection connection;

    public MySQL(Main main, String host, String user, String database, String password) {
        this.main = main;
        this.host = host;
        this.database = database;
        this.user = user;
        this.password = password;
        openConnection();
    }

    @Override
    public synchronized void openConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + host + "/" + database + "?autoReconnect=true", user, password);
            createTables();
        } catch (SQLException | ClassNotFoundException e) {
            System.err.printf("Can't connect to MySQL %s", e.getMessage());
            Bukkit.getPluginManager().disablePlugin(main);
        }
    }

    @Override
    public synchronized void closeConnection() {
        if (!isConnect()) return;

        try {
            connection.close();
        } catch (SQLException e) {
            System.err.printf("An error occurred while closing a connection to MySQL: %s", e.getMessage());
        }
    }

    @Override
    public synchronized void createTables() {
        try (PreparedStatement stm = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `shop` (`key` VARCHAR(16) PRIMARY KEY, `points` INT(8) NOT NULL)")) {
            stm.executeUpdate();
        } catch (SQLException e) {
            System.err.printf("Can't create table in MySQL: %s", e.getMessage());
            Bukkit.getPluginManager().disablePlugin(main);
        }
    }

    @Override
    public Boolean isConnect() {
        try {
            return !connection.isClosed();
        } catch (SQLException e) {
            System.err.printf("An error occurred while verifying that the MySQL is connected: %s", e.getMessage());
            return false;
        }
    }
}
