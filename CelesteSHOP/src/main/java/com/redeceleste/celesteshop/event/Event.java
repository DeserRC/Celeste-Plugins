package com.redeceleste.celesteshop.event;

import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

@Getter
public abstract class Event extends org.bukkit.event.Event implements Cancellable {
    private final CommandSender player;
    private final String target;
    private final Boolean online;
    private static final HandlerList handler = new HandlerList();
    private Boolean cancel;

    public Event(CommandSender player, String target, Boolean online) {
        this.player = player;
        this.target = target;
        this.online = online;
        this.cancel = false;
    }

    public HandlerList getHandlers() {
        return handler;
    }

    public static HandlerList getHandlerList() {
        return handler;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public boolean isCancelled() {
        return cancel;
    }
}
