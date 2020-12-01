package com.redeceleste.celestespawners.builder;

import com.redeceleste.celestespawners.event.impl.PurchaseEvent;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PurchaseBuilder {
    private Player player;
    private String name;
    private int amount;
    private int page;
    private double price;
    private ItemStack item;
    private FileConfiguration itemFile;

    public PurchaseBuilder player(Player player) {
        this.player = player;
        return this;
    }

    public PurchaseBuilder name(String name) {
        this.name = name;
        return this;
    }

    public PurchaseBuilder amount(int amount) {
        this.amount = amount;
        return this;
    }

    public PurchaseBuilder page(int page) {
        this.page = page;
        return this;
    }

    public PurchaseBuilder price(double price) {
        this.price = price;
        return this;
    }

    public PurchaseBuilder item(ItemStack item) {
        this.item = item;
        return this;
    }

    public PurchaseBuilder file(FileConfiguration itemFile) {
        this.itemFile = itemFile;
        return this;
    }

    public void build() {
        PurchaseEvent event = new PurchaseEvent(player, name, amount, page, price, item, itemFile);
        Bukkit.getPluginManager().callEvent(event);
    }
}
