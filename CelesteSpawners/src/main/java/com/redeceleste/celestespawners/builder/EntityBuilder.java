package com.redeceleste.celestespawners.builder;

import com.redeceleste.celestespawners.event.impl.PlayerKilledEntityEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;

public class EntityBuilder {
    private LivingEntity entity;
    private String name;
    private long amount;
    private boolean isCustom;

    public EntityBuilder entity(LivingEntity entity) {
        this.entity = entity;
        return this;
    }

    public EntityBuilder name(String name) {
        this.name = name;
        return this;
    }

    public EntityBuilder amount(long amount) {
        this.amount = amount;
        return this;
    }

    public EntityBuilder isCustom(boolean isCustom) {
        this.isCustom = isCustom;
        return this;
    }

    public void build() {
        PlayerKilledEntityEvent event = new PlayerKilledEntityEvent(entity, name, amount, isCustom);
        Bukkit.getPluginManager().callEvent(event);
    }
}
