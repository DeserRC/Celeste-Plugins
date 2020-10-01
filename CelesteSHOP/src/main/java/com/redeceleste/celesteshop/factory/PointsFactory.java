package com.redeceleste.celesteshop.factory;

import com.redeceleste.celesteshop.Main;
import com.redeceleste.celesteshop.manager.MapManager;
import com.redeceleste.celesteshop.manager.PointsManager;
import com.redeceleste.celesteshop.manager.SetManager;
import com.redeceleste.celesteshop.model.PointsArgument;
import lombok.Getter;

@Getter
public class PointsFactory {
    private final Main main;
    private final PointsManager manager;
    private final MapManager<String, PointsArgument> points;
    private final SetManager<String> update;

    public PointsFactory(Main main) {
        this.main = main;
        this.manager = new PointsManager(main, this);
        this.points = new MapManager<>();
        this.update = new SetManager<>();
    }
}
