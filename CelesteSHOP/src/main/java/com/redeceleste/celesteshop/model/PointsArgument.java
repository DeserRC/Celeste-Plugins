package com.redeceleste.celesteshop.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public abstract class PointsArgument {
    private final String player;
    private final Integer points;
}