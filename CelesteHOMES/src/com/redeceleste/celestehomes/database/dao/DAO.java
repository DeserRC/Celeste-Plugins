package com.redeceleste.celestehomes.database.dao;

import com.google.gson.Gson;
import com.redeceleste.celestehomes.Main;
import com.redeceleste.celestehomes.models.UserArgument;
import com.redeceleste.celestehomes.models.impls.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;

public class DAO {

    private final Gson gson = new Gson();
    public final HashMap<String, UserArgument> cache = new HashMap<>();

    public boolean isExists(String key) {
        try {
            if (Main.getInstance().getMySql().isConnect()) {
                Main.getInstance().openSQL();
            }
            PreparedStatement stm = Main.getInstance().getMySql().getConnection().prepareStatement("SELECT * FROM `homes` WHERE `key` = ?");
            stm.setString(1, key);
            return stm.executeQuery().next();
        } catch (Exception ignored) {
            return false;
        }
    }

    public HashSet<UserArgument> getAll() {
        HashSet<UserArgument> userArguments = new HashSet<>();
        try {
            if (Main.getInstance().getMySql().isConnect()) {
                Main.getInstance().openSQL();
            }
            PreparedStatement stm = Main.getInstance().getMySql().getConnection().prepareStatement("SELECT * FROM `homes`");
            ResultSet resultSet = stm.executeQuery();
            while (resultSet.next()) {
                String json = resultSet.getString("json");
                userArguments.add(gson.fromJson(json, User.class));
            }
        } catch (SQLException ignored) {
            return null;
        }
        return userArguments;
    }

    public UserArgument getArgument(String key, Class<? extends UserArgument> clazz) {
        try (Connection connection = Main.getInstance().getMySql().getConnection()){
            if (Main.getInstance().getMySql().isConnect()) {
                Main.getInstance().openSQL();
            }
            PreparedStatement stm = connection.prepareStatement("SELECT * FROM `homes` WHERE `key` = ?");
            stm.setString(1, key);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                String json = rs.getString("json");
                return gson.fromJson(json, clazz);
            }
            return null;
        } catch (Exception ignored) {
            return null;
        }
    }

    public void insert(final UserArgument player) {
        try {
            if (Main.getInstance().getMySql().isConnect()) {
                Main.getInstance().openSQL();
            }
            String json = gson.toJson(player);
            PreparedStatement stm = Main.getInstance().getMySql().getConnection().prepareStatement("INSERT INTO `homes`(`key`, `json`) VALUES (?,?) ON DUPLICATE KEY UPDATE `json` = '<json>'".replace("<json>", json));
            stm.setString(1, player.getName());
            stm.setString(2, json);
            stm.executeUpdate();
        } catch (Exception ignored) {
            System.out.print("An error occurred while saving");
        }
    }

    public void delete(String key) {
        try {
            if (Main.getInstance().getMySql().isConnect()) {
                Main.getInstance().openSQL();
            }

            PreparedStatement stm = Main.getInstance().getMySql().getConnection().prepareStatement("DELETE FROM `homes` WHERE `key` = ?");
            stm.setString(1, key);
            stm.executeUpdate();

        } catch (Exception ignored) {
            System.out.print("An error ocurred while purge");
        }
    }
}

