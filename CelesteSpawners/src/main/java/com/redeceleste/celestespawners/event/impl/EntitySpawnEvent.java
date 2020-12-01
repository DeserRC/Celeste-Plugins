package com.redeceleste.celestespawners.event.impl;

import com.redeceleste.celestespawners.event.Event;
import com.redeceleste.celestespawners.model.SpawnerArgument;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class EntitySpawnEvent extends Event {
    private final SpawnerArgument spawnerArg;
}
