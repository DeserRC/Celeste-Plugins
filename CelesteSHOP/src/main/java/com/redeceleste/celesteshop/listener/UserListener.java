package com.redeceleste.celesteshop.listener;

import com.redeceleste.celesteshop.Main;
import com.redeceleste.celesteshop.dao.PointsDAO;
import com.redeceleste.celesteshop.factory.PointsFactory;
import com.redeceleste.celesteshop.model.PointsArgument;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.concurrent.CompletableFuture;

public class UserListener implements Listener {
    private final Main main;
    private final PointsFactory factory;
    private final PointsDAO dao;

    public UserListener(Main main) {
        this.main = main;
        this.factory = main.getPointsFactory();
        this.dao = main.getConnectionFactory().getDao();
        main.getServer().getPluginManager().registerEvents(this, main);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        CompletableFuture<PointsArgument> result = dao.getArgument(p.getName());
        result.thenAccept(r -> { if (r != null) factory.getPoints().put(r.getPlayer().toLowerCase(), r); });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (factory.getUpdate().contains(p.getName().toLowerCase())) {
            dao.replace(factory.getPoints().get(p.getName().toLowerCase()), true);
            factory.getUpdate().remove(p.getName().toLowerCase());
        }
        factory.getPoints().remove(p.getName().toLowerCase());
    }
}