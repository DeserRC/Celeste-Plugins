package com.redeceleste.celestespawners.event.impl;

import com.redeceleste.celestespawners.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.LivingEntity;

@AllArgsConstructor
@Getter
public class PlayerKilledEntityEvent extends Event {
    private final LivingEntity entity;
    private final String name;
    private final Long amount;
    private final Boolean isCustom;
}
