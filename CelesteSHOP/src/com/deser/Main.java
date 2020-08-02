package com.deser;

import com.deser.commands.Cashs;
import com.deser.commands.Shop;
import com.deser.database.MySQL;
import com.deser.database.query.Query;
import com.deser.listener.InventoryArguments;
import com.deser.listener.InventoryClick;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class Main extends JavaPlugin {
    public static ArrayList<InventoryArguments> shopItens = new ArrayList<>();

    @Override
    public void onEnable() {
        MySQL sql = new MySQL();
        getServer().getPluginManager().registerEvents(new Query(), this);
        getServer().getPluginManager().registerEvents(new InventoryClick(), this);
        getCommand("shop").setExecutor(new Shop());
        getCommand("cashs").setExecutor(new Cashs());

        loadConfig();
        sql.openConnection();
        sql.createTable();
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
    }

    public void loadConfig() {
        getConfig().options().copyDefaults(true);
        saveConfig();
    }
}