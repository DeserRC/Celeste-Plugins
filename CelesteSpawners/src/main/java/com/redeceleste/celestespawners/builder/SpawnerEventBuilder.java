package com.redeceleste.celestespawners.builder;

import com.redeceleste.celestespawners.event.impl.BreakSpawnerEvent;
import com.redeceleste.celestespawners.event.impl.PlaceSpawnerEvent;
import com.redeceleste.celestespawners.type.MobType;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class SpawnerEventBuilder {
    private Player player;
    private Block block;
    private MobType type;
    private boolean isCustom;
    private boolean useSilkTouch;

    public SpawnerEventBuilder player(Player player) {
        this.player = player;
        return this;
    }

    public SpawnerEventBuilder block(Block block) {
        this.block = block;
        return this;
    }

    public SpawnerEventBuilder type(MobType type) {
        this.type = type;
        return this;
    }

    public SpawnerEventBuilder isCustom(boolean isCustom) {
        this.isCustom = isCustom;
        return this;
    }

    public SpawnerEventBuilder useSilkTouch(boolean useSilkTouch) {
        this.useSilkTouch = useSilkTouch;
        return this;
    }

    public void buildPlace() {
        PlaceSpawnerEvent event = new PlaceSpawnerEvent(player, block, type, isCustom);
        Bukkit.getPluginManager().callEvent(event);
    }

    public void buildBreak() {
        BreakSpawnerEvent event = new BreakSpawnerEvent(player, block, type, isCustom, useSilkTouch);
        Bukkit.getPluginManager().callEvent(event);
    }
}
