package com.redeceleste.celestehomes.model;

import com.redeceleste.celestehomes.builder.UserBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;

@AllArgsConstructor
@Getter
public abstract class UserArgument {
    private String player;
    private HashMap<String, UserBuilder> homes;
}