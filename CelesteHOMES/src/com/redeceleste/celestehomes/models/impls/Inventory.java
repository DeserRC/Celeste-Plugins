package com.redeceleste.celestehomes.models.impls;

import com.redeceleste.celestehomes.models.InventoryArgument;
import org.bukkit.Material;

import java.util.List;

public class Inventory extends InventoryArgument {
    private String name;
    private Material material;
    private Integer data, slot, amount;
    private Boolean glow;
    private List<String> lore;
    private List<String> en;

    public Inventory(String name, Material material, Integer data, Integer slot, Integer amount, Boolean glow, List<String> lore, List<String> en) {
        this.name = name;
        this.material = material;
        this.data = data;
        this.slot = slot;
        this.amount = amount;
        this.glow = glow;
        this.lore = lore;
        this.en = en;
    }

    public Inventory(String name, Material material, Integer data, Boolean glow, List<String> lore, List<String> en) {
        this.name = name;
        this.material = material;
        this.data = data;
        this.glow = glow;
        this.lore = lore;
        this.en = en;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Material getMaterial() {
        return material;
    }

    @Override
    public Integer getData() {
        return data;
    }

    @Override
    public Integer getSlot() {
        return slot;
    }

    @Override
    public Integer getAmount() {
        return amount;
    }

    @Override
    public Boolean getGlow() {
        return glow;
    }

    @Override
    public List<String> getLore() {
        return lore;
    }

    @Override
    public List<String> getEnchantament() {
        return en;
    }
}
