package com.redeceleste.celesteshop.event.impl;

import com.redeceleste.celesteshop.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.command.CommandSender;

@AllArgsConstructor
@Getter
public class PointsRemoveEvent extends Event {
    private final CommandSender player;
    private final String target;
    private final Integer targetValue;
    private final Integer value;
    private final Boolean online;
}
