package com.redeceleste.celestekits.dao;

import com.google.gson.Gson;
import com.redeceleste.celestekits.CelesteKit;
import com.redeceleste.celestekits.database.DataBase;
import com.redeceleste.celestekits.model.UserArgument;
import com.redeceleste.celestekits.model.impl.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.concurrent.CompletableFuture;

public class UserDAO {
    private final CelesteKit main;
    private final DataBase dataBase;
    private final Gson gson;

    public UserDAO(CelesteKit main, DataBase dataBase) {
        this.main = main;
        this.dataBase = dataBase;
        this.gson = new Gson();
    }

    public CompletableFuture<Boolean> isExists(String player) {
        return CompletableFuture.supplyAsync(() -> {
            try (PreparedStatement stm = dataBase.getConnection().prepareStatement("SELECT 1 FROM `kits` WHERE `player` = ?")) {
                stm.setString(1, player);
                ResultSet rs = stm.executeQuery();
                return rs.next();
            } catch (SQLException e) {
                System.err.printf("An Errorr occurred while checking if the player \"%s\" exists in the database: %s", player, e.getMessage());
            }
            return false;
        }, main.getScheduled());
    }

    public CompletableFuture<HashSet<UserArgument>> getAll() {
        return CompletableFuture.supplyAsync(() -> {
            HashSet<UserArgument> kit = new HashSet<>();
            try (PreparedStatement stm = dataBase.getConnection().prepareStatement("SELECT `kit` FROM `kits`")) {
                ResultSet rs = stm.executeQuery();
                while (rs.next()) {
                    String json = rs.getString("kit");
                    kit.add(gson.fromJson(json, User.class));
                }
            } catch (SQLException e) {
                System.err.printf("An Errorr get all players data: %s", e.getMessage());
            }
            return kit;
        }, main.getScheduled());
    }

    public CompletableFuture<UserArgument> getArgument(String player) {
        return CompletableFuture.supplyAsync(() -> {
            try (PreparedStatement stm = dataBase.getConnection().prepareStatement("SELECT `kit` FROM `kits` WHERE `player` = ?")) {
                stm.setString(1, player);
                ResultSet rs = stm.executeQuery();
                if (rs.next()) {
                    String json = rs.getString("kit");
                    return gson.fromJson(json, User.class);
                }
            } catch (SQLException e) {
                System.err.printf("An Errorr occurred while get data of player \"%s\": %s", player, e.getMessage());
            }
            return null;
        }, main.getScheduled());
    }

    public <T extends UserArgument> void replace(T argument, Boolean async) {
        if (!async) {
            try (PreparedStatement stm = dataBase.getConnection().prepareStatement("REPLACE INTO `kits` (`player`, `kit`) VALUES (?,?)")) {
                stm.setString(1, argument.getPlayer());
                stm.setString(2, gson.toJson(argument));
                stm.executeUpdate();
            } catch (SQLException e) {
                System.err.printf("An Errorr saving da of player \"%s\": %s", argument.getPlayer(), e.getMessage());
            }
            return;
        }

        CompletableFuture.runAsync(() -> {
            try (PreparedStatement stm = dataBase.getConnection().prepareStatement("REPLACE INTO `kits` (`player`, `kit`) VALUES (?,?)")) {
                stm.setString(1, argument.getPlayer());
                stm.setString(2, gson.toJson(argument));
                stm.executeUpdate();
            } catch (SQLException e) {
                System.err.printf("An Errorr saving da of player \"%s\": %s", argument.getPlayer(), e.getMessage());
            }
        }, main.getScheduled());
    }

    public void delete(String player, Boolean async) {
        if (!async) {
            try (PreparedStatement stm = dataBase.getConnection().prepareStatement("DELETE FROM `kits` WHERE `player` = ?")) {
                stm.setString(1, player);
                stm.executeUpdate();
            } catch (SQLException e) {
                System.err.printf("An Errorr deleting data of player \"%s\": %s", player, e.getMessage());
            }
            return;
        }

        CompletableFuture.runAsync(() -> {
            try (PreparedStatement stm = dataBase.getConnection().prepareStatement("DELETE FROM `kits` WHERE `player` = ?")) {
                stm.setString(1, player);
                stm.executeUpdate();
            } catch (SQLException e) {
                System.err.printf("An Errorr deleting data of player \"%s\": %s", player, e.getMessage());
            }
        }, main.getScheduled());
    }
}
