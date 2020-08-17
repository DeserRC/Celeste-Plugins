package com.redeceleste.celestehomes.models.impls;

import com.redeceleste.celestehomes.models.UserArgument;

import java.util.HashMap;

public class User extends UserArgument {

    private String name;
    private HashMap<String, UserBuilder> homes;

    public User(String name, HashMap<String, UserBuilder> homes) {
        this.name = name;
        this.homes = homes;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public HashMap<String, UserBuilder> getHomes() {
        return homes;
    }
}