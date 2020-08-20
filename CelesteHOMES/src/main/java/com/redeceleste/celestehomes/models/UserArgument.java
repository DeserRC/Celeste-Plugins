package com.redeceleste.celestehomes.models;

import com.redeceleste.celestehomes.models.impls.UserBuilder;

import java.util.HashMap;

public abstract class UserArgument {
    public abstract String getName();
    public abstract HashMap<String, UserBuilder> getHomes();
}