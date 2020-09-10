package com.redeceleste.celestehomes.dao;

import com.google.gson.Gson;
import com.redeceleste.celestehomes.Main;
import com.redeceleste.celestehomes.model.UserArgument;
import com.redeceleste.celestehomes.model.impls.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.HashSet;

public class UserDAO {
    private final Gson gson = new Gson();
    public final HashMap<String, UserArgument> cache = new HashMap<>();

    public Boolean isExists(String key) {
        try (PreparedStatement stm = Main.getInstance().getMySQL().getConnection().prepareStatement("SELECT 1 FROM `homes` WHERE `key` = ?")) {
            stm.setString(1, key);
            ResultSet rs = stm.executeQuery();
            return rs.next();
        } catch (Exception ignored) { }
        return false;
    }

    public HashSet<UserArgument> getAll() {
        HashSet<UserArgument> userArguments = new HashSet<>();
        try (PreparedStatement stm = Main.getInstance().getMySQL().getConnection().prepareStatement("SELECT `json` FROM `homes`")) {
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                String json = rs.getString("json");
                userArguments.add(gson.fromJson(json, User.class));
            }
        } catch (Exception ignored) { }
        return userArguments;
    }

    public UserArgument getArgument(String key) {
        try (PreparedStatement stm = Main.getInstance().getMySQL().getConnection().prepareStatement("SELECT `json` FROM `homes` WHERE `key` = ?")) {
            stm.setString(1, key);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                String json = rs.getString("json");
                return gson.fromJson(json, User.class);
            }
        } catch (Exception ignored) { }
        return null;
    }

    public void insert(UserArgument userArgument) {
        try (PreparedStatement stm = Main.getInstance().getMySQL().getConnection().prepareStatement("INSERT INTO `homes`(`key`, `json`) VALUES (?,?) ON DUPLICATE KEY UPDATE `json` = ?")) {
            String json = gson.toJson(userArgument);
            stm.setString(1, userArgument.getPlayer());
            stm.setString(2, json);
            stm.setString(3, json);
            stm.executeUpdate();
        } catch (Exception ignored) {
            System.out.print("An error occurred while saving");
        }
    }

    public void delete(String key) {
        try (PreparedStatement stm = Main.getInstance().getMySQL().getConnection().prepareStatement("DELETE FROM `homes` WHERE `key` = ?")) {
            stm.setString(1, key);
            stm.executeUpdate();
        } catch (Exception ignored) {
            System.out.print("An error occurred while purge");
        }
    }
}

