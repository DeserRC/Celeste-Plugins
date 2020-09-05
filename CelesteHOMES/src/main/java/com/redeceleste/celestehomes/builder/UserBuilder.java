package com.redeceleste.celestehomes.builder;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserBuilder {
    private Integer number;
    private String location, name;
}