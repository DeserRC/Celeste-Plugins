package com.redeceleste.celesteshop.manager;

import com.redeceleste.celesteshop.Main;
import com.redeceleste.celesteshop.builder.PointsBuilder;
import com.redeceleste.celesteshop.dao.PointsDAO;
import com.redeceleste.celesteshop.factory.PointsFactory;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

public class PointsManager {
    private final Main main;
    private final PointsDAO dao;
    private final PointsFactory factory;

    public PointsManager(Main main, PointsFactory factory) {
        this.main = main;
        this.dao = main.getConnectionFactory().getDao();
        this.factory = factory;
    }

    public Integer getPoints(String p) {
        return getPoints(p, true);
    }

    public Integer getPoints(String p, Boolean online) {
        if (!online) {
            CompletableFuture<Boolean> result = dao.isExists(p);
            AtomicInteger points = new AtomicInteger();

            if (!result.join()) {
                 return null;
            }

            dao.getArgument(p).thenAccept(r -> points.set(r.getPoints())).join();
            return points.intValue();
        }

        if (factory.getPoints().contains(p.toLowerCase())) {
            return factory.getPoints().get(p.toLowerCase()).getPoints();
        }

        return 0;
    }

    public void payPoints(CommandSender p, String t, Integer value, Boolean online) {
        Integer pValue = getPoints(p.getName(), true);
        Integer tValue = getPoints(t, online);

        Bukkit.getPluginManager().callEvent(new PointsBuilder()
                .setPlayer(p)
                .setTarget(t)
                .setPlayerValue(pValue)
                .setTargetValue(tValue)
                .setValue(value)
                .setOnline(online).buildPay());
    }

    public void addPoints(CommandSender p, String t, Integer value, Boolean online) {
        Integer tValue = getPoints(t, online);

        Bukkit.getPluginManager().callEvent(new PointsBuilder()
                .setPlayer(p)
                .setTarget(t)
                .setTargetValue(tValue)
                .setValue(value)
                .setOnline(online).buildAdd());
    }

    public void removePoints(CommandSender p, String t, Integer value, Boolean online) {
        Integer tValue = getPoints(t, online);

        Bukkit.getPluginManager().callEvent(new PointsBuilder()
                .setPlayer(p)
                .setTarget(t)
                .setTargetValue(tValue)
                .setValue(value)
                .setOnline(online).buildRemove());
    }

    public void setPoints(CommandSender p, String t, Integer value, Boolean online) {
        Bukkit.getPluginManager().callEvent(new PointsBuilder()
                .setPlayer(p)
                .setTarget(t)
                .setValue(value)
                .setOnline(online).buildSet());
    }

    public void resetPoints(CommandSender p, String t, Boolean online) {
        Bukkit.getPluginManager().callEvent(new PointsBuilder()
                .setPlayer(p)
                .setTarget(t)
                .setOnline(online).buildReset());
    }
}
