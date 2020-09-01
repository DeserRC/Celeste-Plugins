package com.redeceleste.celestehomes.models;

import com.redeceleste.celestehomes.models.impls.UserBuilder;
import lombok.AllArgsConstructor;

import java.util.HashMap;

public abstract class UserArgument {
    private String name;
    private HashMap<String, UserBuilder> homes;

    public UserArgument(String name, HashMap<String, UserBuilder> homes) {
        this.name = name;
        this.homes = homes;
    }

    public String getName() {
        return name;
    }

    public HashMap<String, UserBuilder> getHomes() {
        return homes;
    }
}