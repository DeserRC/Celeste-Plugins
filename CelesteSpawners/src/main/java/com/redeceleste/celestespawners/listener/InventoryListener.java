package com.redeceleste.celestespawners.listener;

import com.redeceleste.celestespawners.CelesteSpawners;
import com.redeceleste.celestespawners.builder.PurchaseBuilder;
import com.redeceleste.celestespawners.holder.InventoryConfirmHolder;
import com.redeceleste.celestespawners.holder.InventoryShopHolder;
import com.redeceleste.celestespawners.manager.ConfigManager;
import com.redeceleste.celestespawners.manager.InventoryManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryListener implements Listener {
    private final CelesteSpawners main;
    private final ConfigManager config;
    private final InventoryManager inventory;

    public InventoryListener(CelesteSpawners main) {
        this.main = main;
        this.config = main.getConfigManager();
        this.inventory = main.getInventoryManager();
        main.getServer().getPluginManager().registerEvents(this, main);
    }

    @EventHandler
    public void onShopClickEvent(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        Inventory inv = e.getClickedInventory();

        if (inv == null) return;
        if (!(inv.getHolder() instanceof InventoryShopHolder)) return;

        e.setCancelled(true);
        int page = ((InventoryShopHolder) inv.getHolder()).getPage();

        int slot = config.getInventory("Inventory.Custom-Items.Next-Page.Slot");
        if (slot == e.getSlot()) {
            inventory.nextPage(p, inv, page);
            return;
        }

        slot = config.getInventory("Inventory.Custom-Items.Previous-Page.Slot");
        if (slot == e.getSlot()) {
            inventory.previousPage(p, inv, page);
            return;
        }

        for (FileConfiguration itemFile : config.getItems().values()) {
            slot = config.get("Slot", itemFile);
            if (slot != e.getSlot()) continue;
            inventory.confirm(p, itemFile, page);
            return;
        }
    }
    @EventHandler
    public void onConfirmClickEvent(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        Inventory inv = e.getClickedInventory();

        if (inv == null) return;
        if (!(inv.getHolder() instanceof InventoryConfirmHolder)) return;

        e.setCancelled(true);
        InventoryConfirmHolder holder = (InventoryConfirmHolder) inv.getHolder();
        FileConfiguration file = holder.getItemFile();
        int amount = holder.getAmount();
        int page = holder.getPage();

        int slot = config.getInventory("Inventory-Confirm.Custom-Items.Add.Slot");
        if (slot == e.getSlot()) {
            holder.addAmount();
            inventory.changeAmount(p, file, inv, amount + 1, page);
            return;
        }

        slot = config.getInventory("Inventory-Confirm.Custom-Items.Remove.Slot");
        if (slot == e.getSlot()) {
            if (amount <= 1) return;
            holder.removeAmount();
            inventory.changeAmount(p, file, inv, amount - 1, page);
            return;
        }

        slot = config.getInventory("Inventory-Confirm.Custom-Items.Deny.Slot");
        if (slot == e.getSlot()) {
            p.closeInventory();
            inventory.open(p, page);
            return;
        }

        slot = config.getInventory("Inventory-Confirm.Custom-Items.Confirm.Slot");
        if (slot == e.getSlot()) {
            String name = config.get("Name", file);

            int initialAmount = config.get("Amount", file);
            int finalAmount = initialAmount * amount;

            double price = config.get("Price.Money", file);
            price = price * finalAmount;

            ItemStack itemStack = holder.getItem();
            new PurchaseBuilder()
                    .player(p)
                    .name(name)
                    .amount(finalAmount)
                    .page(page)
                    .price(price)
                    .item(itemStack)
                    .file(file).build();
        }
    }
}