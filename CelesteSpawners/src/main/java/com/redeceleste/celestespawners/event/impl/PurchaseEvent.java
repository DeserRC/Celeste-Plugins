package com.redeceleste.celestespawners.event.impl;

import com.redeceleste.celestespawners.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
@Getter
public class PurchaseEvent extends Event {
    private final Player player;
    private final String name;
    private final Integer amount;
    private final Integer page;
    private final Double price;
    private final ItemStack item;
    private final FileConfiguration itemFile;
}
