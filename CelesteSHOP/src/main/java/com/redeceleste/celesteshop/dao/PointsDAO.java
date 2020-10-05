package com.redeceleste.celesteshop.dao;

import com.redeceleste.celesteshop.CelesteSHOP;
import com.redeceleste.celesteshop.database.DataBase;
import com.redeceleste.celesteshop.model.PointsArgument;
import com.redeceleste.celesteshop.model.impl.Points;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.concurrent.CompletableFuture;

public class PointsDAO {
    private final CelesteSHOP main;
    private final DataBase dataBase;

    public PointsDAO(CelesteSHOP main, DataBase dataBase) {
        this.main = main;
        this.dataBase = dataBase;
    }

    public CompletableFuture<Boolean> isExists(String key) {
        return CompletableFuture.supplyAsync(() -> {
            try (PreparedStatement stm = dataBase.getConnection().prepareStatement("SELECT 1 FROM `shop` WHERE `key` = ?")) {
                stm.setString(1, key);
                ResultSet rs = stm.executeQuery();
                return rs.next();
            } catch (SQLException e) {
                System.err.printf("An error occurred while checking if the player \"%s\" exists: %s", key, e.getMessage());
            }
            return false;
        }, main.getScheduled());
    }

    public CompletableFuture<HashSet<PointsArgument>> getAll() {
        return CompletableFuture.supplyAsync(() -> {
            HashSet<PointsArgument> set = new HashSet<>();
            try (PreparedStatement stm = dataBase.getConnection().prepareStatement("SELECT `key`, `points` FROM `shop`")) {
                ResultSet rs = stm.executeQuery();
                while (rs.next()) {
                    String key = rs.getString("key");
                    Integer points = rs.getInt("points");
                    set.add(new Points(key, points));
                }
            } catch (SQLException e) {
                System.err.printf("An error occurred while getting all data: %s", e.getMessage());
            }
            return set;
        }, main.getScheduled());
    }

    public CompletableFuture<PointsArgument> getArgument(String key) {
        return CompletableFuture.supplyAsync(() -> {
            try (PreparedStatement stm = dataBase.getConnection().prepareStatement("SELECT `points` FROM `shop` WHERE `key` = ?")) {
                stm.setString(1, key);
                ResultSet rs = stm.executeQuery();
                if (rs.next()) {
                    Integer points = rs.getInt("points");
                    return new Points(key, points);
                }
            } catch (SQLException e) {
                System.err.printf("An error occurred while getting data of \"%s\": %s", key, e.getMessage());
            }
            return null;
        }, main.getScheduled());
    }

    public <T extends PointsArgument> void replace(T argument, Boolean async) {
        if (!async) {
            try (PreparedStatement stm = dataBase.getConnection().prepareStatement("REPLACE INTO `shop` (`key`, `points`) VALUES (?,?)")) {
                stm.setString(1, argument.getPlayer());
                stm.setInt(2, argument.getPoints());
                stm.executeUpdate();
            } catch (SQLException e) {
                System.err.printf("An error occurred while saving data of \"%s\": %s", argument.getPlayer(), e.getMessage());
            }
            return;
        }

        CompletableFuture.runAsync(() -> {
            try (PreparedStatement stm = dataBase.getConnection().prepareStatement("REPLACE INTO `shop` (`key`, `points`) VALUES (?,?)")) {
                stm.setString(1, argument.getPlayer());
                stm.setInt(2, argument.getPoints());
                stm.executeUpdate();
            } catch (SQLException e) {
                System.err.printf("An error occurred while saving data of \"%s\": %s", argument.getPlayer(), e.getMessage());
            }
        }, main.getScheduled());
    }

    public void delete(String key, Boolean async) {
        if (!async) {
            try (PreparedStatement stm = dataBase.getConnection().prepareStatement("DELETE FROM `shop` WHERE `key` = ?")) {
                stm.setString(1, key);
                stm.executeUpdate();
            } catch (SQLException e) {
                System.err.printf("An error occurred while deleting data of \"%s\": %s", key, e.getMessage());
            }
            return;
        }

        CompletableFuture.runAsync(() -> {
            try (PreparedStatement stm = dataBase.getConnection().prepareStatement("DELETE FROM `shop` WHERE `key` = ?")) {
                stm.setString(1, key);
                stm.executeUpdate();
            } catch (SQLException e) {
                System.err.printf("An error occurred while deleting data of \"%s\": %s", key, e.getMessage());
            }
        }, main.getScheduled());
    }
}
