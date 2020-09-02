package com.redeceleste.celestehomes.builder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class UserBuilder {
    private Integer number;
    private String location, home;
}