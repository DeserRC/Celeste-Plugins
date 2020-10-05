package com.redeceleste.celestehomes.listener;

import com.redeceleste.celestehomes.CelesteHomes;
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
        CelesteHomes.getInstance().getServer().getPluginManager().registerEvents(this, CelesteHomes.getInstance());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Inventory inventory = e.getClickedInventory();

        if (inventory == null) return;
        if (!(e.getInventory().getHolder() instanceof InventoryBuilder)) return;

        Player t = (Player) e.getWhoClicked();
        Player p = ((InventoryBuilder) e.getInventory().getHolder()).getPlayer();

        if (inventory.getTitle().contains(ConfigManager.TitleGUI.replace("%player%", p.getName()))) {
            try {
                UserArgument user = CelesteHomes.getInstance().getUserDAO().cache.get(p);
                for (InventoryArgument ai : ConfigManager.Template) {
                    for (UserBuilder userBuilder : user.getHomes().values()) {
                        if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ai.getName()
                                .replace("%number%", userBuilder.getNumber().toString())
                                .replace("%home%", userBuilder.getName()))) {
                            e.setCancelled(true);
                            t.closeInventory();
                            HomeManager.homeTeleport(t, userBuilder.getName());
                            return;
                        }
                    }
                }
            } catch (Exception ignored) { }
            e.setCancelled(true);
        }
    }
}
