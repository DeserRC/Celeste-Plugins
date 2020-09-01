package com.redeceleste.celestehomes.models.impls;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserBuilder {
    private Integer number;
    private String location, home;

    @Override
    public String toString() {
        return "{" + "number=" + number + ",homes{" + ",Home=" + home + ",Location=" + location + "}}";
    }
}