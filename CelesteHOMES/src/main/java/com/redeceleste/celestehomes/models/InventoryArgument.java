package com.redeceleste.celestehomes.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

import java.util.List;

@AllArgsConstructor
@Getter
public abstract class InventoryArgument {
    private String name;
    private Material material;
    private Integer data, slot, amount;
    private Boolean glow;
    private List<String> lore;
    private List<String> enchantament;
}
