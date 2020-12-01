package com.redeceleste.celestekits.builder;

import com.redeceleste.celestekits.event.impl.UserGetKitEvent;
import com.redeceleste.celestekits.model.KitArgument;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class UserEventBuilder {
    private Player player;
    private KitArgument kitArg;

    public UserEventBuilder player(Player player) {
        this.player = player;
        return this;
    }

    public UserEventBuilder kit(KitArgument kitArg) {
        this.kitArg = kitArg;
        return this;
    }

    public void build() {
        UserGetKitEvent event = new UserGetKitEvent(player, kitArg);
        Bukkit.getPluginManager().callEvent(event);
    }
}
