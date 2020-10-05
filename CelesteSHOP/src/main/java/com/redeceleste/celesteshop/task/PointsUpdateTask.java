package com.redeceleste.celesteshop.task;

import com.redeceleste.celesteshop.Main;
import com.redeceleste.celesteshop.dao.PointsDAO;
import com.redeceleste.celesteshop.factory.PointsFactory;
import com.redeceleste.celesteshop.model.PointsArgument;

public class PointsUpdateTask implements Runnable {
    private final Main main;
    private final PointsDAO dao;
    private final PointsFactory factory;
    private final Boolean async;

    public PointsUpdateTask(Main main, Boolean async) {
        this.main = main;
        this.dao = main.getConnectionFactory().getDao();
        this.factory = main.getPointsFactory();
        this.async = async;
    }

    @Override
    public void run() {
        for (PointsArgument points : factory.getPoints().getAll()) {
            if (factory.getUpdate().contains(points.getPlayer().toLowerCase())) {
                factory.getUpdate().remove(points.getPlayer().toLowerCase());
                dao.replace(points, async);
            }
        }
    }
}
