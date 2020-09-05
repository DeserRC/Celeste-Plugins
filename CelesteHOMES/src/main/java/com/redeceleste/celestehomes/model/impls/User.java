package com.redeceleste.celestehomes.model.impls;

import com.redeceleste.celestehomes.builder.UserBuilder;
import com.redeceleste.celestehomes.model.UserArgument;

import java.util.HashMap;

public class User extends UserArgument {
    public User(String player, HashMap<String, UserBuilder> homes) {
        super(player, homes);
    }
}