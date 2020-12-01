package com.redeceleste.celestekits.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@AllArgsConstructor
@Getter
public abstract class UserArgument {
    private final String player;
    private final Map<String, Long> kit;
}
