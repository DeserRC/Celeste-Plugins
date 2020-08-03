package com.deser.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.deser.Main;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class MySQL implements Listener {
    private Plugin pl = Main.getPlugin(Main.class);

    private Connection connection;

    private String host, database, user, password;


    public void openConnection() {
        host = pl.getConfig().getString("MySQL.Host");
        database = pl.getConfig().getString("MySQL.DataBase");
        user = pl.getConfig().getString("MySQL.User");
        password = pl.getConfig().getString("MySQL.Password");

        try {
            synchronized (this) {
                if (isConnect()) {
                    return;
                }

                Class.forName("com.mysql.jdbc.Driver");
                setConnection(DriverManager.getConnection("jdbc:mysql://" + this.host + ":3306/" + this.database, this.user, this.password));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        if (isConnect()) {
            try {
                getConnection().close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void createTable() {
        try {
            PreparedStatement statement = getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS cashs (player VARCHAR(16) PRIMARY KEY, cash INT(8) NOT NULL)");
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Boolean isConnect() {
        return connection != null;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}

