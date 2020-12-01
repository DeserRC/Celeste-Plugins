package com.redeceleste.celestekits.event.impl;

import com.redeceleste.celestekits.event.Event;
import com.redeceleste.celestekits.model.KitArgument;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;

@AllArgsConstructor
@Getter
public class UserGetKitEvent extends Event {
    private final Player player;
    private final KitArgument kit;
}
