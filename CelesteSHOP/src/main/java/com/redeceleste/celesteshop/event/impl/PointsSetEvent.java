package com.redeceleste.celesteshop.event.impl;

import com.redeceleste.celesteshop.event.Event;
import lombok.Getter;
import org.bukkit.command.CommandSender;

@Getter
public class PointsSetEvent extends Event {
    private final Integer value;

    public PointsSetEvent(CommandSender player, String target, Boolean online, Integer value) {
        super(player, target, online);
        this.value = value;
    }
}
