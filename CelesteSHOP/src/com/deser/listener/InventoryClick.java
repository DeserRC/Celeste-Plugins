package com.deser.listener;

import com.deser.commands.Shop;
import com.deser.database.query.Query;
import com.deser.utils.ActionBar;
import com.deser.utils.ItemBuilder;
import com.deser.utils.SendTitle;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import static com.deser.Main.shopItens;

public class InventoryClick implements Listener {

    public void addList(InventoryArguments ia) {
        shopItens.add(ia);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        Query query = new Query();
        Shop shop = new Shop();

        for (InventoryArguments si : shopItens) {
            if (e.getInventory().getTitle().equals(shop.getTitle())) {
                if (si.getSlot() == e.getSlot()) {
                    menuConfirm(p);
                }
            }
            if (e.getInventory().getTitle().equals(ChatColor.DARK_GRAY + "CONFIRMAÇÃO")) {
                if (e.getSlot() == 15) {
                    if (query.getCashs(p) >= (si.getPrice())) {
                        query.updateCashs(p, query.getCashs(p) - si.getPrice());
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), si.getCMD().replace("%player%", p.getName()));
                        ActionBar.sendActionBarMessage(p, si.getActionbarmessage());
                        SendTitle.sendTitle(p, 1, 1, 1, si.getTitlebuy(), si.getSubtitlebuy());
                        p.closeInventory();
                        return;
                    } else {
                        ActionBar.sendActionBarMessage(p, shop.getInsufficientCash());
                        p.closeInventory();
                        return;
                    }
                }
                if (e.getSlot() == 11) {
                    p.closeInventory();
                    shop.openInventory(p);
                }
            }
            e.setCancelled(true);
        }
    }

    public void menuConfirm (Player p) {
        Inventory inventory = Bukkit.createInventory(null, 27, ChatColor.DARK_GRAY + "Confirmação");
        inventory.setItem(15, new ItemBuilder(Material.WOOL).setDyeColor(DyeColor.GREEN).setName(ChatColor.GREEN + "Sim, desejo comprar.").toItemStack());
        inventory.setItem(11, new ItemBuilder(Material.WOOL).setDyeColor(DyeColor.RED).setName(ChatColor.DARK_RED + "Não desejo comprar.").toItemStack());
        p.closeInventory();
        p.openInventory(inventory);
    }
}