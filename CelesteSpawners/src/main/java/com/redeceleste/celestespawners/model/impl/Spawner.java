package com.redeceleste.celestespawners.model.impl;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.redeceleste.celestespawners.model.SpawnerArgument;
import com.redeceleste.celestespawners.type.MobType;

public class Spawner extends SpawnerArgument {
    public Spawner(String location, Hologram hologram, MobType type, long amount) {
        super(location, hologram, type, amount);
    }
}
