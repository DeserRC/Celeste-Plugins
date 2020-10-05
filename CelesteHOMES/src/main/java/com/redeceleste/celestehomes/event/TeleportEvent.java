package com.redeceleste.celestehomes.event;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class TeleportEvent extends Event implements Cancellable {
    private static HandlerList handlerList = new HandlerList();
    private Player player;
    private String name;
    private String loc;
    private Boolean cancelled;

    public TeleportEvent(Player p, String name, String loc) {
        this.player = p;
        this.name = name;
        this.loc = loc;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }
}
