package com.redeceleste.celestespawners.listener;

import com.redeceleste.celestespawners.CelesteSpawners;
import com.redeceleste.celestespawners.manager.impl.SpawnerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class UserListener implements Listener {
    private final CelesteSpawners main;
    private final SpawnerManager spawner;

    public UserListener(CelesteSpawners main) {
        this.main = main;
        this.spawner = main.getSpawnerFactory().getSpawner();
        main.getServer().getPluginManager().registerEvents(this, main);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        spawner.removeCooldown(p.getName());
    }
}
