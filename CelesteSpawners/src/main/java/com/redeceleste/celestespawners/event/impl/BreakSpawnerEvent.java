package com.redeceleste.celestespawners.event.impl;

import com.redeceleste.celestespawners.event.Event;
import com.redeceleste.celestespawners.type.MobType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

@AllArgsConstructor
@Getter
public class BreakSpawnerEvent extends Event {
    private final Player player;
    private final Block block;
    private final MobType type;
    private final Boolean isCustom;
    private final Boolean useSilkTouch;
}
