package com.redeceleste.celestehomes;

import com.redeceleste.celestehomes.command.impls.*;
import com.redeceleste.celestehomes.command.impls.admin.AHomeCommand;
import com.redeceleste.celestehomes.database.MySQL;
import com.redeceleste.celestehomes.dao.UserDAO;
import com.redeceleste.celestehomes.listener.InventoryListener;
import com.redeceleste.celestehomes.listener.UserListener;
import com.redeceleste.celestehomes.manager.ConfigManager;
import com.redeceleste.celestehomes.model.UserArgument;
import com.redeceleste.celestehomes.task.UserUpdateTask;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.concurrent.TimeUnit;

@Getter
public class Main extends JavaPlugin{

    @Getter
    private static Main instance;
    private MySQL mySQL;
    private UserDAO UserDAO = new UserDAO();
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
        new UserUpdateTask();
        ConfigManager.loadMessage();
    }

    @Override
    public void onDisable() {
        UserUpdateTask.update();
        HandlerList.unregisterAll(this);
    }

    private void openSQL() {
        mySQL = new MySQL(getConfig().getString("MySQL.Host"), getConfig().getString("MySQL.User"), getConfig().getString("MySQL.DataBase"), getConfig().getString("MySQL.Password"));
    }

    private void load() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (UserDAO.isExists(p.getName())) {
                UserDAO.cache.put(p.getName(), UserDAO.getArgument(p.getName()));
            }
        }
    }

    private void purge() {
        if (!Boolean.parseBoolean(getConfig().getString("Purge.Use"))) return;

        long time = Long.parseLong(getConfig().getString("Purge.Time"));

        for (UserArgument userArgument : UserDAO.getAll()) {
            OfflinePlayer p = getServer().getOfflinePlayer(userArgument.getPlayer());
            if (p.getLastPlayed() >= System.currentTimeMillis() + TimeUnit.DAYS.toMillis(time)) {
                UserDAO.delete(userArgument.getPlayer());
            }
        }
    }
}

