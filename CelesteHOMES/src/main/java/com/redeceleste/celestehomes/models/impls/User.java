package com.redeceleste.celestehomes.models.impls;

import com.redeceleste.celestehomes.models.UserArgument;

import java.util.HashMap;

public class User extends UserArgument {
    public User(String name, HashMap<String, UserBuilder> homes) {
        super(name, homes);
    }
}