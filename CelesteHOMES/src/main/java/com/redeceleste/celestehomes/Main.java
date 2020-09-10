package com.redeceleste.celestehomes;

import com.redeceleste.celestehomes.command.impls.*;
import com.redeceleste.celestehomes.command.impls.admin.AHomeCommand;
import com.redeceleste.celestehomes.database.MySQL;
import com.redeceleste.celestehomes.dao.UserDAO;
import com.redeceleste.celestehomes.listener.InventoryListener;
import com.redeceleste.celestehomes.listener.UserListener;
import com.redeceleste.celestehomes.manager.ConfigManager;
import com.redeceleste.celestehomes.model.UserArgument;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Getter
public class Main extends JavaPlugin {

    @Getter
    private static Main instance;
    private MySQL mySQL;
    private UserDAO UserDAO = new UserDAO();
    private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
    public HashSet<String> update = new HashSet<>();

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        openSQL();
        load();
        purge();
        new UserListener();
        new InventoryListener();
        new SetHomeCommand();
        new DelHomeCommand();
        new HomeCommand();
        new HomesCommand();
        new AHomeCommand();
        ConfigManager.loadMessage();
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
        executorService.shutdown();
    }

    private void openSQL() {
        mySQL = new MySQL(getConfig().getString("MySQL.Host"), getConfig().getString("MySQL.User"), getConfig().getString("MySQL.DataBase"), getConfig().getString("MySQL.Password"));
    }

    private void load() {
        executorService.execute(() -> {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (UserDAO.isExists(p.getName())) {
                    UserArgument userArgument = UserDAO.getArgument(p.getName());
                    UserDAO.cache.put(userArgument.getPlayer(), userArgument);
                }
            }
        });
    }

    @Deprecated
    private void purge() {
        if (!Boolean.parseBoolean(getConfig().getString("Purge.Use"))) return;

        long time = Long.parseLong(getConfig().getString("Purge.Time"));

        executorService.execute(() -> {
            for (UserArgument userArgument : UserDAO.getAll()) {
                OfflinePlayer p = getServer().getOfflinePlayer(userArgument.getPlayer());
                if (p.getLastPlayed() >= System.currentTimeMillis() + TimeUnit.DAYS.toMillis(time)) {
                    UserDAO.delete(userArgument.getPlayer());
                }
            }
        });
    }
}

