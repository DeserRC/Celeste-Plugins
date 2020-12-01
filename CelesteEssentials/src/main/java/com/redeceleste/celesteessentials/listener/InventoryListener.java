package com.redeceleste.celesteessentials.listener;

import com.redeceleste.celesteessentials.CelesteEssentials;
import com.redeceleste.celesteessentials.holder.InventoryWarpHolder;
import com.redeceleste.celesteessentials.manager.ConfigManager;
import com.redeceleste.celesteessentials.manager.WarpManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class InventoryListener implements Listener {
    private final CelesteEssentials main;
    private final ConfigManager config;
    private final WarpManager warp;

    public InventoryListener(CelesteEssentials main) {
        this.main = main;
        this.config = main.getConfigManager();
        this.warp = main.getWarpManager();
        main.getServer().getPluginManager().registerEvents(this, main);
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        Inventory inv = e.getClickedInventory();

        if (inv == null) return;
        if (!(inv.getHolder() instanceof InventoryWarpHolder)) return;

        e.setCancelled(true);
        FileConfiguration warpFile = config.getWarp();
        for (String name : config.getKeys("", config.getWarp())) {
            String path = name + ".";
            int slot = config.get(path + ".Inventory.Slot", warpFile);
            if (slot != e.getSlot()) continue;
            p.closeInventory();
            warp.teleportPlayer(p, name);
        }
    }
}