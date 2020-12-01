package com.redeceleste.celestespawners.event;

import lombok.Getter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

@Getter
public abstract class Event extends org.bukkit.event.Event implements Cancellable {
    @Getter
    private static final HandlerList handler = new HandlerList();
    private boolean cancel = false;

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
