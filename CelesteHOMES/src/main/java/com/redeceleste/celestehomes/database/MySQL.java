package com.redeceleste.celestehomes.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.redeceleste.celestehomes.Main;
import lombok.Getter;
import org.bukkit.Bukkit;

public class MySQL {

    @Getter
    private Connection connection;
    private final String host, database, user, password;

    public MySQL(String host, String user, String database, String password) {
        this.host = host;
        this.database = database;
        this.user = user;
        this.password = password;
        openConnection();
    }

    public void openConnection() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + host + "/" + database + "?autoReconnect=true", user, password);
            createTables();
        } catch (SQLException ignored) {
            System.out.print("Can't connect to MySQL");
            Bukkit.getPluginManager().disablePlugin(Main.getInstance());
        }
    }

    public void closeConnection() {
        if (connection == null)
            return;
        try {
            connection.close();
        } catch (SQLException ignored) {
            System.out.print("There was an error when closing a connection to MySQL");
        }
    }

    private void createTables() {
        try {
            PreparedStatement stm = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `homes`(`key` VARCHAR(16) NOT NULL, `json` TEXT NOT NULL, PRIMARY KEY (`key`))");
            stm.executeUpdate();
            stm.close();
        } catch (Exception ignored) {
            System.out.print("Can't create table in MySQL.");
            Bukkit.getPluginManager().disablePlugin(Main.getInstance());
        }
    }

    public Boolean isConnect() {
        try {
            return !connection.isClosed();
        } catch (Exception ignored) {
            return false;
        }
    }
}