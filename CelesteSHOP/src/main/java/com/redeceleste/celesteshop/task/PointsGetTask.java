package com.redeceleste.celesteshop.task;

import com.redeceleste.celesteshop.Main;
import com.redeceleste.celesteshop.dao.PointsDAO;
import com.redeceleste.celesteshop.factory.PointsFactory;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PointsGetTask implements Runnable {
    private final Main main;
    private final PointsFactory factory;
    private final PointsDAO dao;

    public PointsGetTask(Main main) {
        this.main = main;
        this.factory = main.getPointsFactory();
        this.dao = main.getConnectionFactory().getDao();
    }

    @Override
    public void run() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            dao.getArgument(p.getName()).thenAccept(r -> { if (r != null) factory.getPoints().put(p.getName().toLowerCase(), r);});
        }
    }
}
