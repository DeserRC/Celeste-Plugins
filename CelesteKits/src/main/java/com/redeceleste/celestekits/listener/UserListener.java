package com.redeceleste.celestekits.listener;

import com.redeceleste.celestekits.CelesteKit;
import com.redeceleste.celestekits.dao.UserDAO;
import com.redeceleste.celestekits.factory.UserFactory;
import com.redeceleste.celestekits.model.UserArgument;
import lombok.SneakyThrows;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.concurrent.CompletableFuture;

public class UserListener implements Listener {
    private final CelesteKit main;
    private final UserFactory user;
    private final UserDAO dao;

    @SneakyThrows
    public UserListener(CelesteKit main) {
        this.main = main;
        this.user = main.getUserFactory();
        this.dao = main.getConnectionFactory().getDao();
        main.getServer().getPluginManager().registerEvents(this, main);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        CompletableFuture<UserArgument> kitArg = dao.getArgument(p.getName());
        kitArg.thenAccept(arg -> {
            if (arg != null) {
                user.getCooldown().put(arg.getPlayer().toLowerCase(), arg);
            }
        });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (user.getUpdate().contains(p.getName().toLowerCase())) {
            UserArgument argument = user.getCooldown().get(p.getName().toLowerCase());
            dao.replace(argument, true);
        }
        user.getCooldown().remove(p.getName().toLowerCase());
        user.getUpdate().remove(p.getName().toLowerCase());
    }
}
