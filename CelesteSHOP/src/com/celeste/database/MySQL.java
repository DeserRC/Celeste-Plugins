package com.celeste.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.celeste.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class MySQL implements Listener {
    private Connection connection;
    public String host, database, username, password, table;
    public int port;

    private Plugin pl = Main.getPlugin(Main.class);

    public void mysqlSetup() {
        host = pl.getConfig().getString("host");
        port = pl.getConfig().getInt("port");
        database = pl.getConfig().getString("database");
        username = pl.getConfig().getString("username");
        password = pl.getConfig().getString("password");
        table = pl.getConfig().getString("table");

        try {

            synchronized (this) {
                if (getConnection() != null && !getConnection().isClosed()) {
                    return;
                }

                Class.forName("com.mysql.jdbc.Driver");
                setConnection(
                        DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database,
                                this.username, this.password));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
