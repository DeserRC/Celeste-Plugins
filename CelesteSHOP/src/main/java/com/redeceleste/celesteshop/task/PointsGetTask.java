package com.redeceleste.celesteshop.task;

import com.redeceleste.celesteshop.Main;
import com.redeceleste.celesteshop.dao.PointsDAO;
import com.redeceleste.celesteshop.factory.PointsFactory;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PointsGetTask implements Runnable {
    private final Main main;
    private final PointsDAO dao;
    private final PointsFactory factory;

    public PointsGetTask(Main main) {
        this.main = main;
        this.dao = main.getConnectionFactory().getDao();
        this.factory = main.getPointsFactory();
    }

    @Override
    public void run() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            dao.getArgument(p.getName()).thenAccept(r -> { if (r != null) factory.getPoints().put(p.getName().toLowerCase(), r);});
        }
    }
}
