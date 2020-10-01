package com.redeceleste.celesteshop.event.impl;

import com.redeceleste.celesteshop.event.Event;
import lombok.Getter;
import org.bukkit.command.CommandSender;

@Getter
public class PointsPayEvent extends Event {
    private final Integer playerValue;
    private final Integer targetValue;
    private final Integer value;

    public PointsPayEvent(CommandSender player, String target, Boolean online, Integer playerValue, Integer targetValue, Integer value) {
        super(player, target, online);
        this.playerValue = playerValue;
        this.targetValue = targetValue;
        this.value = value;
    }
}
