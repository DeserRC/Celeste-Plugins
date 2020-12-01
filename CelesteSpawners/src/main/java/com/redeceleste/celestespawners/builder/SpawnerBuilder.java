package com.redeceleste.celestespawners.builder;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.redeceleste.celestespawners.model.SpawnerArgument;
import com.redeceleste.celestespawners.model.impl.Spawner;
import com.redeceleste.celestespawners.model.impl.SpawnerCustom;
import com.redeceleste.celestespawners.type.ArmorType;
import com.redeceleste.celestespawners.type.MobType;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class SpawnerBuilder {
    private String location;
    private Hologram hologram;
    private MobType type;
    private long amount;
    private String name;
    private ItemStack head;
    private Map<ArmorType, ItemStack> armors;

    public SpawnerBuilder location(String location) {
        this.location = location;
        return this;
    }

    public SpawnerBuilder hologram(Hologram hologram) {
        this.hologram = hologram;
        return this;
    }

    public SpawnerBuilder type(MobType type) {
        this.type = type;
        return this;
    }

    public SpawnerBuilder amount(long amount) {
        this.amount = amount;
        return this;
    }

    public SpawnerBuilder name(String name) {
        this.name = name;
        return this;
    }

    public SpawnerBuilder head(ItemStack head) {
        this.head = head;
        return this;
    }

    public SpawnerBuilder armors(Map<ArmorType, ItemStack> armors) {
        this.armors = armors;
        return this;
    }

    public SpawnerArgument buildSpawner() {
        return new Spawner(location, hologram, type, amount);
    }

    public SpawnerArgument buildCustom() {
        return new SpawnerCustom(location, hologram, type, amount, name, head, armors);
    }
}
