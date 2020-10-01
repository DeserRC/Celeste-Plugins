package com.redeceleste.celesteshop.manager;

import java.util.HashSet;

public class SetManager<T> {
    private final HashSet<T> set = new HashSet<>();

    public void put(T key) {
        set.add(key);
    }

    public void remove(T key) {
        set.remove(key);
    }

    public Boolean contains(T key) {
        return set.contains(key);
    }

    public void clear() {
        set.clear();
    }
}
