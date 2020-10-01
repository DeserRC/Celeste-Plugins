package com.redeceleste.celesteshop.event.impl;

import com.redeceleste.celesteshop.event.Event;
import lombok.Getter;
import org.bukkit.command.CommandSender;

@Getter
public class PointsRemoveEvent extends Event {
    private final Integer targetValue;
    private final Integer value;

    public PointsRemoveEvent(CommandSender player, String target, Boolean online, Integer targetValue, Integer value) {
        super(player, target, online);
        this.targetValue = targetValue;
        this.value = value;
    }
}
