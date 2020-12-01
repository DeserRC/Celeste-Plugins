package com.redeceleste.celestespawners.model;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.redeceleste.celestespawners.type.MobType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public abstract class SpawnerArgument {
    private final String location;
    private transient final Hologram hologram;
    private final MobType type;
    private long amount;

    public void addAmount(long amount) {
        this.amount += amount;
    }

    public void removeAmount(long amount) {
        this.amount -= amount;
    }
}
