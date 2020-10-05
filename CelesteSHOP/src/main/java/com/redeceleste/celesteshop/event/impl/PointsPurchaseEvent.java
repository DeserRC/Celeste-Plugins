package com.redeceleste.celesteshop.event.impl;

import com.redeceleste.celesteshop.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PointsPurchaseEvent extends Event {
    private final String target;
    private final Integer targetValue;
    private final Integer value;
    private final Boolean online;
}
