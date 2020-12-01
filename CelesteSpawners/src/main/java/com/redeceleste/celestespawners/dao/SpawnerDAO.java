package com.redeceleste.celestespawners.dao;

import com.google.gson.Gson;
import com.redeceleste.celestespawners.CelesteSpawners;
import com.redeceleste.celestespawners.database.DataBase;
import com.redeceleste.celestespawners.model.SpawnerArgument;
import com.redeceleste.celestespawners.model.impl.SpawnerCustom;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class SpawnerDAO {
    private final CelesteSpawners main;
    private final DataBase dataBase;
    private final Gson gson;

    public SpawnerDAO(CelesteSpawners main, DataBase dataBase) {
        this.main = main;
        this.dataBase = dataBase;
        this.gson = new Gson();
    }

    public CompletableFuture<Boolean> isExists(String location) {
        return CompletableFuture.supplyAsync(() -> {
            try (PreparedStatement stm = dataBase.getConnection().prepareStatement("SELECT 1 FROM `spawner` WHERE `location` = ?")) {
                stm.setString(1, location);
                ResultSet rs = stm.executeQuery();
                return rs.next();
            } catch (SQLException e) {
                System.err.printf("An error occurred while check if spawner the location \"%s\" exist in database: %s", location, e.getMessage());
            }
            return false;
        }, main.getScheduled());
    }

    public CompletableFuture<Set<SpawnerArgument>> getAll() {
        return CompletableFuture.supplyAsync(() -> {
            Set<SpawnerArgument> spawnersArgs = new HashSet<>();
            try (PreparedStatement stm = dataBase.getConnection().prepareStatement("SELECT `spawners` FROM `spawner`")) {
                ResultSet rs = stm.executeQuery();
                while (rs.next()) {
                    String spawners = rs.getString("spawners");
                    SpawnerArgument spawnerArg = gson.fromJson(spawners, SpawnerCustom.class);
                    spawnersArgs.add(spawnerArg);
                }
            } catch (SQLException e) {
                System.err.printf("An error occurred while get data from all spawner: %s", e.getMessage());
            }
            return spawnersArgs;
        }, main.getScheduled());
    }

    public CompletableFuture<SpawnerArgument> getArgument(String location) {
        return CompletableFuture.supplyAsync(() -> {
            try (PreparedStatement stm = dataBase.getConnection().prepareStatement("SELECT `spawners` FROM `spawner` WHERE `location` = ?")) {
                stm.setString(1, location);
                ResultSet rs = stm.executeQuery();
                if (rs.next()) {
                    String spawners = rs.getString("spawners");
                    return gson.fromJson(spawners, SpawnerCustom.class);
                }
            } catch (SQLException e) {
                System.err.printf("An error occurred while get data from spawner the location \"%s\": %s", location, e.getMessage());
            }
            return null;
        }, main.getScheduled());
    }

    public <T extends SpawnerArgument> void replace(T spawnerArg, boolean async) {
        if (!async) {
            try (PreparedStatement stm = dataBase.getConnection().prepareStatement("REPLACE INTO `spawner` (`location`, `spawners`) VALUES (?,?)")) {
                stm.setString(1, spawnerArg.getLocation());
                stm.setString(2, gson.toJson(spawnerArg));
                stm.executeUpdate();
            } catch (SQLException e) {
                System.err.printf("An error occurred while save spawner data the location \"%s\": %s", spawnerArg.getLocation(), e.getMessage());
            }
            return;
        }

        CompletableFuture.runAsync(() -> {
            try (PreparedStatement stm = dataBase.getConnection().prepareStatement("REPLACE INTO `spawner` (`location`, `spawners`) VALUES (?,?)")) {
                stm.setString(1, spawnerArg.getLocation());
                stm.setString(2, gson.toJson(spawnerArg));
                stm.executeUpdate();
            } catch (SQLException e) {
                System.err.printf("An error occurred while save spawner data the location \"%s\": %s", spawnerArg.getLocation(), e.getMessage());
            }
        }, main.getScheduled());
    }

    public void delete(String location, boolean async) {
        if (!async) {
            try (PreparedStatement stm = dataBase.getConnection().prepareStatement("DELETE FROM `spawner` WHERE `location` = ?")) {
                stm.setString(1, location);
                stm.executeUpdate();
            } catch (SQLException e) {
                System.err.printf("An error occurred while delete spawner data the location \"%s\": %s", location, e.getMessage());
            }
            return;
        }

        CompletableFuture.runAsync(() -> {
            try (PreparedStatement stm = dataBase.getConnection().prepareStatement("DELETE FROM `spawner` WHERE `location` = ?")) {
                stm.setString(1, location);
                stm.executeUpdate();
            } catch (SQLException e) {
                System.err.printf("An error occurred while delete spawner data the location \"%s\": %s", location, e.getMessage());
            }
        }, main.getScheduled());
    }
}
