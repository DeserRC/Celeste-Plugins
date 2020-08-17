package com.redeceleste.celestehomes.events;

import com.redeceleste.celestehomes.Main;
import com.redeceleste.celestehomes.managers.ConfigManager;
import com.redeceleste.celestehomes.managers.HomeManager;
import com.redeceleste.celestehomes.models.InventoryArgument;
import com.redeceleste.celestehomes.models.impls.User;
import com.redeceleste.celestehomes.models.impls.UserBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryEvent implements Listener {
    public static Player p;

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getInventory().getTitle().contains(ConfigManager.TitleGUI.replace("%player%", p.getName()))) {
            try {
                User user = (User) Main.getInstance().getDAO().cache.get(p.getName());
                for (InventoryArgument ai : ConfigManager.Template) {
                    for (UserBuilder userBuilder : user.getHomes().values()) {
                        if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ai.getName().replace("%number%", String.valueOf(userBuilder.getNumber())).replace("%home%", userBuilder.getHome()))) {
                            e.setCancelled(true);
                            p.closeInventory();
                            HomeManager.homeTeleport(p, userBuilder.getHome());
                            return;
                        }
                    }
                }
            }catch (Exception ignored) {
            }
            e.setCancelled(true);
        }
    }
}
