package com.redeceleste.celestehomes;

import com.redeceleste.celestehomes.command.impls.*;
import com.redeceleste.celestehomes.database.AutoSave;
import com.redeceleste.celestehomes.database.MySQL;
import com.redeceleste.celestehomes.dao.UserDAO;
import com.redeceleste.celestehomes.event.InventoryEvent;
import com.redeceleste.celestehomes.manager.ConfigManager;
import com.redeceleste.celestehomes.model.UserArgument;
import lombok.Getter;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@Getter
public class Main extends JavaPlugin{

    @Getter
    private static Main instance;
    private MySQL mySQL;
    private UserDAO UserDAO = new UserDAO();
    public ArrayList<String> update = new ArrayList<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        instance = this;
        new InventoryEvent();
        new SetHomeCommand();
        new DelHomeCommand();
        new HomeCommand();
        new HomesCommand();
        new AHomeCommand();
        openSQL();
        ConfigManager.loadMessage();
        AutoSave.saveLooping();
        loadAll();
    }

    @Override
    public void onDisable() {
        AutoSave.save();
        HandlerList.unregisterAll(this);
    }

    public void openSQL() {
        mySQL = new MySQL(getConfig().getString("MySQL.Host"), getConfig().getString("MySQL.User"),getConfig().getString("MySQL.DataBase"), getConfig().getString("MySQL.Password"));
    }

    //Load all Database in HashMAP
    public void loadAll() {
        for (UserArgument userArgument : UserDAO.getAll()){
            if (!Purge(getServer().getOfflinePlayer(userArgument.getName()))) {
                UserDAO.cache.put(userArgument.getName(), userArgument);
            } else {
                this.getUserDAO().delete(userArgument.getName());
            }
        }
    }

    private Boolean Purge(OfflinePlayer p) {
        return p.getLastPlayed() >= System.currentTimeMillis() + TimeUnit.DAYS.toMillis(Long.parseLong(ConfigManager.Purge));
    }
}

