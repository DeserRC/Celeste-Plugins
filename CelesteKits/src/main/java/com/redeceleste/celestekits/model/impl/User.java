package com.redeceleste.celestekits.model.impl;

import com.redeceleste.celestekits.model.UserArgument;

import java.util.Map;

public class User extends UserArgument {
    public User(String player, Map<String, Long> kit) {
        super(player, kit);
    }
}
