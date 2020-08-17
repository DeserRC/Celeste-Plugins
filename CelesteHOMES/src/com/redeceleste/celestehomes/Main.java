package com.redeceleste.celestehomes;

import com.redeceleste.celestehomes.commands.*;
import com.redeceleste.celestehomes.database.AutoSave;
import com.redeceleste.celestehomes.database.MySQL;
import com.redeceleste.celestehomes.database.dao.DAO;
import com.redeceleste.celestehomes.events.InventoryEvent;
import com.redeceleste.celestehomes.managers.ConfigManager;
import com.redeceleste.celestehomes.models.UserArgument;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class Main extends JavaPlugin{

    private static Main instance;
    private MySQL mySQL;
    private DAO DAO = new DAO();
    public ArrayList<String> update = new ArrayList<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        instance = this;
        getServer().getPluginManager().registerEvents(new InventoryEvent(), this);
        getCommand("sethome").setExecutor(new SetHome());
        getCommand("delhome").setExecutor(new DelHome());
        getCommand("home").setExecutor(new Home());
        getCommand("homes").setExecutor(new Homes());
        getCommand("ahome").setExecutor(new AHome());
        openSQL();
        loadAll();
        ConfigManager.loadMessage();
        AutoSave.saveLooping();
    }

    @Override
    public void onDisable() {
        AutoSave.save();
        HandlerList.unregisterAll(this);
    }

    //Get Main Methods
    public static Main getInstance() {
        return instance;
    }

    public void openSQL() {
        mySQL = new MySQL(getConfig().getString("MySQL.Host"), getConfig().getString("MySQL.User"),getConfig().getString("MySQL.DataBase"), getConfig().getString("MySQL.Password"));
    }

    public MySQL getMySql() {
        return mySQL;
    }

    public DAO getDAO() {
        return DAO;
    }

    //Load all Database in HashMAP
    public void loadAll(){
        for (UserArgument userArgument : DAO.getAll()){
            DAO.cache.put(userArgument.getName(), userArgument);
        }
    }
}

