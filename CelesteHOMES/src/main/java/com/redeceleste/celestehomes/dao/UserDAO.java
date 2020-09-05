package com.redeceleste.celestehomes.dao;

import com.google.gson.Gson;
import com.redeceleste.celestehomes.Main;
import com.redeceleste.celestehomes.model.UserArgument;
import com.redeceleste.celestehomes.model.impls.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;

public class UserDAO {
    private final Gson gson = new Gson();
    public final HashMap<String, UserArgument> cache = new HashMap<>();

    public Boolean isExists(String key) {
        boolean result = false;
        try {
            PreparedStatement stm = Main.getInstance().getMySQL().getConnection().prepareStatement("SELECT * FROM `homes` WHERE `key` = ?");
            stm.setString(1, key);
            result = stm.executeQuery().next();
            stm.close();
        } catch (Exception ignored) {
        }
        return result;
    }

    public HashSet<UserArgument> getAll() {
        HashSet<UserArgument> userArguments = new HashSet<>();
        try {
            PreparedStatement stm = Main.getInstance().getMySQL().getConnection().prepareStatement("SELECT * FROM `homes`");
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                String json = rs.getString("json");
                userArguments.add(gson.fromJson(json, User.class));
            }
            rs.close();
            stm.close();
        } catch (SQLException ignored) {
        }
        return userArguments;
    }

    public UserArgument getArgument(String key) {
        UserArgument userArgument = null;
        try {
            PreparedStatement stm = Main.getInstance().getMySQL().getConnection().prepareStatement("SELECT * FROM `homes` WHERE `key` = ?");
            ResultSet rs = stm.executeQuery();
            stm.setString(1, key);
            if (rs.next()) {
                String json = rs.getString("json");
                userArgument = gson.fromJson(json, User.class);
            }
            rs.close();
            stm.close();
        } catch (Exception ignored) {
        }
        return userArgument;
    }

    public void insert(UserArgument userArgument) {
        if (!Main.getInstance().getMySQL().isConnect()) {
            Main.getInstance().openSQL();
        }

        try {
            String json = gson.toJson(userArgument);
            PreparedStatement stm = Main.getInstance().getMySQL().getConnection().prepareStatement("INSERT INTO `homes`(`key`, `json`) VALUES (?,?) ON DUPLICATE KEY UPDATE `json` = '<json>'".replace("<json>", json));
            stm.setString(1, userArgument.getPlayer());
            stm.setString(2, json);
            stm.executeUpdate();
            stm.close();
        } catch (Exception ignored) {
            System.out.print("An error occurred while saving");
        }
    }

    public void delete(String key) {
        try {
            PreparedStatement stm = Main.getInstance().getMySQL().getConnection().prepareStatement("DELETE FROM `homes` WHERE `key` = ?");
            stm.setString(1, key);
            stm.executeUpdate();
            stm.close();
        } catch (Exception ignored) {
            System.out.print("An error ocurred while purge");
        }
    }
}

