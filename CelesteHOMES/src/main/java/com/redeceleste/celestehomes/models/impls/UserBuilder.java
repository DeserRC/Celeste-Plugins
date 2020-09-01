package com.redeceleste.celestehomes.models.impls;

public class UserBuilder {
    private Integer number;
    private String location, home;

    public UserBuilder(Integer number, String loc, String home) {
        this.number = number;
        this.location = loc;
        this.home = home;
    }

    public Integer getNumber() {
        return number;
    }

    public String getLocation() {
        return location;
    }

    public String getHome() {
        return home;
    }

    @Override
    public String toString() {
        return "{" + "number=" + number + ",homes{" + ",Home=" + home + ",Location=" + location + "}}";
    }
}