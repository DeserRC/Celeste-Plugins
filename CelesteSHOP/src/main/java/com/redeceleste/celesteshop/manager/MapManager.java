package com.redeceleste.celesteshop.manager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public class MapManager<T, U> {
    private final HashMap<T, U> map = new HashMap<>();

    public void put(T key, U argument) {
        map.put(key, argument);
    }

    public void remove(T key) {
        map.remove(key);
    }

    public U get(T key) {
        return map.get(key);
    }

    public Boolean contains(T key) {
        return map.containsKey(key);
    }

    public Collection<U> getAll() {
        return map.values();
    }

    public Set<T> getKeys() {
        return map.keySet();
    }

    public void clear() {
        map.clear();
    }
}
