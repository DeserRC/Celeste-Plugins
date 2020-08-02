package com.deser.database.query;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.deser.database.MySQL;
import org.bukkit.entity.Player;

public class Query extends MySQL {

    public boolean playerExists(Player p) {
        try {
            openConnection();
            PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM cashs WHERE player=?");
            statement.setString(1, p.getName().toLowerCase());

            ResultSet results = statement.executeQuery();
            return results.next();
        } catch (SQLException e) {
            return false;
        }
    }

    public void createPlayer(Player p, int cashs) {
        try {
            openConnection();
            PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM cashs WHERE player=?");
            statement.setString(1, p.getName().toLowerCase());
            ResultSet results = statement.executeQuery();
            results.next();
            if (!playerExists(p)) {
                PreparedStatement insert = getConnection().prepareStatement("INSERT INTO cashs (player,cash) VALUES (?,?)");
                insert.setString(1, p.getName().toLowerCase());
                insert.setInt(2, cashs);
                insert.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateCashs(Player p, int cashs) {
        try {
            PreparedStatement statement = getConnection().prepareStatement("UPDATE cashs SET cash=? WHERE player=?");
            statement.setInt(1, cashs);
            statement.setString(2, p.getName().toLowerCase());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public Integer getCashs(Player p) {
        try {
            openConnection();
            PreparedStatement statement = getConnection()
                    .prepareStatement("SELECT * FROM cashs WHERE player=?");
            statement.setString(1, p.getName().toLowerCase());
            ResultSet results = statement.executeQuery();
            results.next();

            return results.getInt("cash");
        } catch (SQLException e) {
            return null;
        }
    }
}