package com.redeceleste.celestehomes.listener;

import com.redeceleste.celestehomes.Main;
import com.redeceleste.celestehomes.model.UserArgument;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class UserListener implements Listener {
    public UserListener() {
        Main.getInstance().getServer().getPluginManager().registerEvents(this, Main.getInstance());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        if (Main.getInstance().getUserDAO().isExists(p.getName())) {
            UserArgument userArgument = Main.getInstance().getUserDAO().getArgument(p.getName());
            Main.getInstance().getUserDAO().cache.put(userArgument.getPlayer(), userArgument);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        try {
            UserArgument userArgument = Main.getInstance().getUserDAO().cache.get(p.getName());
            Main.getInstance().getUserDAO().insert(userArgument);
            Main.getInstance().getUserDAO().cache.remove(userArgument.getPlayer());
        } catch (Exception ignored) { }
    }
}
