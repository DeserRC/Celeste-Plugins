package com.redeceleste.celesteshop.builder;

import com.redeceleste.celesteshop.event.impl.*;
import org.bukkit.command.CommandSender;

public class PointsBuilder {
    private CommandSender player;
    private String target;
    private Boolean online;
    private Integer playerValue;
    private Integer targetValue;
    private Integer value;

    public PointsBuilder setPlayer(CommandSender player) {
        this.player = player;
        return this;
    }

    public PointsBuilder setTarget(String target) {
        this.target = target;
        return this;
    }

    public PointsBuilder setOnline(Boolean online) {
        this.online = online;
        return this;
    }

    public PointsBuilder setPlayerValue(Integer playerValue) {
        this.playerValue = playerValue;
        return this;
    }

    public PointsBuilder setTargetValue(Integer targetValue) {
        this.targetValue = targetValue;
        return this;
    }

    public PointsBuilder setValue(Integer value) {
        this.value = value;
        return this;
    }

    public PointsPayEvent buildPay() {
        return new PointsPayEvent(player, target, online, playerValue, targetValue, value);
    }

    public PointsAddEvent buildAdd() {
        return new PointsAddEvent(player, target, online, targetValue, value);
    }

    public PointsRemoveEvent buildRemove() {
        return new PointsRemoveEvent(player, target, online, targetValue, value);
    }

    public PointsSetEvent buildSet() {
        return new PointsSetEvent(player, target, online, value);
    }

    public PointsResetEvent buildReset() {
        return new PointsResetEvent(player, target, online);
    }
}
