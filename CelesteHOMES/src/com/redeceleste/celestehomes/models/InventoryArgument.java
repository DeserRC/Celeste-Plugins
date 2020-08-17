package com.redeceleste.celestehomes.models;

import org.bukkit.Material;

import java.util.List;

public abstract class InventoryArgument {
    public abstract String getName();
    public abstract Material getMaterial();
    public abstract Integer getData();
    public abstract Integer getSlot();
    public abstract Integer getAmount();
    public abstract Boolean getGlow();
    public abstract List<String> getLore();
    public abstract List<String> getEnchantament();
}
