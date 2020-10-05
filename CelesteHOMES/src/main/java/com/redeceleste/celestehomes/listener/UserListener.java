package com.redeceleste.celestehomes.listener;

import com.redeceleste.celestehomes.CelesteHomes;
import com.redeceleste.celestehomes.model.UserArgument;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class UserListener implements Listener {
    public UserListener() {
        CelesteHomes.getInstance().getServer().getPluginManager().registerEvents(this, CelesteHomes.getInstance());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        CelesteHomes.getInstance().getExecutorService().execute(() -> {
            if (CelesteHomes.getInstance().getUserDAO().isExists(p.getName())) {
                UserArgument userArgument = CelesteHomes.getInstance().getUserDAO().getArgument(p.getName());
                CelesteHomes.getInstance().getUserDAO().cache.put(userArgument.getPlayer(), userArgument);
            }
        });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        CelesteHomes.getInstance().getExecutorService().execute(() -> {
            if (CelesteHomes.getInstance().update.contains(p.getName())) {
                UserArgument userArgument = CelesteHomes.getInstance().getUserDAO().cache.get(p.getName());
                CelesteHomes.getInstance().getUserDAO().insert(userArgument);
                CelesteHomes.getInstance().update.remove(p.getName());
            }
            CelesteHomes.getInstance().getUserDAO().cache.remove(p.getName());
        });
    }
}
