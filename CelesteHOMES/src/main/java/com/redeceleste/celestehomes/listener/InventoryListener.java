package com.redeceleste.celestehomes.listener;

import com.redeceleste.celestehomes.Main;
import com.redeceleste.celestehomes.builder.InventoryBuilder;
import com.redeceleste.celestehomes.manager.ConfigManager;
import com.redeceleste.celestehomes.manager.HomeManager;
import com.redeceleste.celestehomes.model.InventoryArgument;
import com.redeceleste.celestehomes.model.UserArgument;
import com.redeceleste.celestehomes.builder.UserBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class InventoryListener implements Listener {
    public InventoryListener() {
        Main.getInstance().getServer().getPluginManager().registerEvents(this, Main.getInstance());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Inventory inventory = e.getClickedInventory();

        if (inventory == null) return;
        if (!(e.getInventory().getHolder() instanceof InventoryBuilder)) return;

        Player p = ((InventoryBuilder) e.getInventory().getHolder()).getPlayer();

        if (inventory.getTitle().contains(ConfigManager.TitleGUI.replace("%player%", p.getName()))) {
            try {
                UserArgument user = Main.getInstance().getUserDAO().cache.get(p.getName());
                for (InventoryArgument ai : ConfigManager.Template) {
                    for (UserBuilder userBuilder : user.getHomes().values()) {
                        if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ai.getName()
                                .replace("%number%", userBuilder.getNumber().toString())
                                .replace("%home%", userBuilder.getName()))) {
                            e.setCancelled(true);
                            p.closeInventory();
                            HomeManager.homeTeleport(p, userBuilder.getName());
                            return;
                        }
                    }
                }
            } catch (Exception ignored) { }
            e.setCancelled(true);
        }
    }
}
