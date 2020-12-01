package com.redeceleste.celestespawners.listener;

import com.redeceleste.celestespawners.CelesteSpawners;
import com.redeceleste.celestespawners.manager.ItemManager;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import static org.bukkit.Sound.ITEM_PICKUP;
import static org.bukkit.event.inventory.InventoryType.HOPPER;

public class ItemListener implements Listener {
    private final CelesteSpawners main;
    private final ItemManager item;

    public ItemListener(CelesteSpawners main) {
        this.main = main;
        this.item = main.getSpawnerFactory().getItem();
        main.getServer().getPluginManager().registerEvents(this, main);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onItemSpawn(ItemSpawnEvent e) {
        Item itemEntity = e.getEntity();
        Location loc = e.getLocation();
        ItemStack itemStack = itemEntity.getItemStack();
        Item nearestItem = item.getNearestItem(loc, itemStack);

        if (nearestItem != null) {
            item.addItem(nearestItem, itemStack.getAmount());
            itemEntity.remove();
            return;
        }
        item.addItem(itemEntity);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onItemMerge(ItemMergeEvent e) {
        Item itemEntity = e.getEntity();
        Item itemTarget = e.getTarget();
        if (!item.containsMetaData(itemEntity) && !item.containsMetaData(itemTarget)) return;

        e.setCancelled(true);
        ItemStack itemStack = itemEntity.getItemStack();
        int entityAmount;

        if (item.containsMetaData(itemEntity)) entityAmount = item.getAmount(itemEntity);
        else entityAmount = itemStack.getAmount();

        item.addItem(itemTarget, entityAmount);
        itemEntity.remove();
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerPickup(PlayerPickupItemEvent e) {
        Item itemEntity = e.getItem();
        if (!itemEntity.hasMetadata("item")) return;

        e.setCancelled(true);
        Player p = e.getPlayer();
        Inventory inv = p.getInventory();
        ItemStack itemStack = itemEntity.getItemStack();
        int spaces = item.getSlots(inv, itemStack.getMaxStackSize(), itemStack.getType());

        if (spaces <= 0) return;

        item.collectAnimation(p, itemEntity);
        p.playSound(p.getLocation(), ITEM_PICKUP, 0.5F, 10F);

        if (spaces >= item.getAmount(itemEntity)) {
            item.give(inv, itemStack, item.getAmount(itemEntity));
            itemEntity.remove();
        } else {
            item.give(inv, itemStack, item.getAmount(itemEntity));
            item.addItem(itemEntity, -spaces);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onHopperPickup(InventoryPickupItemEvent e) {
        Inventory inv = e.getInventory();
        Item itemEntity = e.getItem();
        if (!inv.getType().equals(HOPPER)) return;
        if (!item.containsMetaData(itemEntity)) return;

        e.setCancelled(true);
        ItemStack itemStack = itemEntity.getItemStack();
        int spaces = item.getSlots(inv, itemStack.getMaxStackSize(), itemStack.getType());

        if (spaces <= 0) return;

        if (spaces >= item.getAmount(itemEntity)) {
            item.give(inv, itemStack, item.getAmount(itemEntity));
            itemEntity.remove();
        } else {
            item.give(inv, itemStack, item.getAmount(itemEntity));
            item.addItem(itemEntity, -spaces);
        }
    }
}
