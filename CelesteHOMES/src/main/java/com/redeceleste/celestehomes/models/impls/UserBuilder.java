package com.redeceleste.celestehomes.models.impls;

public class UserBuilder {

    private Integer number;
    private String loc, name;

    public UserBuilder(Integer number, String loc, String home) {
        this.number = number;
        this.loc = loc;
        this.name = home;
    }

    public Integer getNumber() {
        return number;
    }

    public String getLocation() {
        return loc;
    }

    public String getHome() {
        return name;
    }

    @Override
    public String toString() {
        return "{" + "number=" + number + ",homes{" + ",Home=" + name + ",Location=" + loc + "}}";
    }
}