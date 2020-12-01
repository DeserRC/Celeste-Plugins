package com.redeceleste.celestespawners.model.impl;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.redeceleste.celestespawners.model.SpawnerArgument;
import com.redeceleste.celestespawners.type.ArmorType;
import com.redeceleste.celestespawners.type.MobType;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

@Getter
public class SpawnerCustom extends SpawnerArgument {
    private final String name;
    private transient final ItemStack head;
    private transient final Map<ArmorType, ItemStack> armor;

    public SpawnerCustom(String location, Hologram hologram, MobType type, long amount, String name, ItemStack head, Map<ArmorType, ItemStack> armor) {
        super(location, hologram, type, amount);
        this.name = name;
        this.head = head;
        this.armor = armor;
    }
}
