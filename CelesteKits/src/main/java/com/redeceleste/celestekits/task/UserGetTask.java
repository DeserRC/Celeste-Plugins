package com.redeceleste.celestekits.task;

import com.redeceleste.celestekits.MockKits;
import com.redeceleste.celestekits.dao.UserDAO;
import com.redeceleste.celestekits.factory.UserFactory;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class UserGetTask implements Runnable {
    private final MockKits main;
    private final UserFactory user;
    private final UserDAO dao;

    public UserGetTask(MockKits main) {
        this.main = main;
        this.user = main.getUserFactory();
        this.dao = main.getConnectionFactory().getDao();
    }

    @Override
    public void run() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            dao.getArgument(p.getName()).thenAccept(r -> {
                if (r != null) user.getCooldown().put(r.getPlayer().toLowerCase(), r);
            });
        }
    }
}
