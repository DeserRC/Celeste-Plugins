package com.redeceleste.celestehomes.models;

import com.redeceleste.celestehomes.models.impls.UserBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;

@AllArgsConstructor
@Getter
public abstract class UserArgument {
    private String name;
    private HashMap<String, UserBuilder> homes;
}