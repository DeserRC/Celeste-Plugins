package com.redeceleste.celesteshop.event.impl;

import com.redeceleste.celesteshop.event.Event;
import lombok.Getter;
import org.bukkit.command.CommandSender;

@Getter
public class PointsResetEvent extends Event {
    public PointsResetEvent(CommandSender player, String target, Boolean online) {
        super(player, target, online);
    }
}
