package com.redeceleste.celesteshop.event;

import lombok.Getter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/*
Run events via PointsManager
Code: CelesteSHOP.getInstance().getPointsFactory().getManager()
 */

@Getter
public abstract class Event extends org.bukkit.event.Event implements Cancellable {
    private static final HandlerList handler = new HandlerList();
    private Boolean cancel = false;

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
