package com.redeceleste.celestehomes.models;

import org.bukkit.Material;

import java.util.List;

public abstract class InventoryArgument {
    private String name;
    private Material material;
    private Integer data, slot, amount;
    private Boolean glow;
    private List<String> lore;
    private List<String> en;

    public InventoryArgument(String name, Material material, Integer data, Integer slot, Integer amount, Boolean glow, List<String> lore, List<String> en) {
        this.name = name;
        this.material = material;
        this.data = data;
        this.slot = slot;
        this.amount = amount;
        this.glow = glow;
        this.lore = lore;
        this.en = en;
    }

    public String getName() {
        return name;
    }

    public Material getMaterial() {
        return material;
    }

    public Integer getData() {
        return data;
    }

    public Integer getSlot() {
        return slot;
    }

    public Integer getAmount() {
        return amount;
    }

    public Boolean getGlow() {
        return glow;
    }

    public List<String> getLore() {
        return lore;
    }

    public List<String> getEnchantament() {
        return en;
    }
}
