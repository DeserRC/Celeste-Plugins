package com.redeceleste.celesteshop.builder;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ItemBuilder {
    private final ItemStack itemStack;

    public ItemBuilder(Material material) {
        this (material,1);
    }

    public ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ItemBuilder(Material material, Integer amount) {
        itemStack = new ItemStack(material, amount);
    }

    public ItemBuilder(Material material, Integer amount, Short durability) {
        itemStack = new ItemStack(material, amount, durability);
    }

    public ItemBuilder clone() {
        return new ItemBuilder(itemStack);
    }

    public ItemBuilder setData(int data) {
        itemStack.setDurability((byte) data);
        return this;
    }

    public ItemBuilder setDurability(Short durability) {
        itemStack.setDurability(durability);
        return this;
    }

    public ItemBuilder addLores(List<String> lore) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> newLore = itemMeta.getLore();
        newLore.addAll(lore);
        itemMeta.setLore(newLore);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setAmount(Integer amount) {
        itemStack.setAmount(amount);
        return this;
    }

    public ItemBuilder setDurability(Integer durability){
        itemStack.setDurability(Short.parseShort("" + durability));
        return this;
    }

    public ItemBuilder setName(String name) {
        if (name.equalsIgnoreCase(null)) return this;
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder addUnsafeEnchantment(String enchant, Integer level) {
        itemStack.addUnsafeEnchantment(serializeEnchant(enchant), level);
        return this;
    }

    public ItemBuilder addEnchants(List<String> enchants) {
        if (enchants == null) return this;
        for (String s : enchants) {
            serializeEnchant(s.split(":")[0]);
            Enchantment enchant = serializeEnchant(s.split(":")[0]);
            Integer level = Integer.valueOf(s.split(":")[1]);
            itemStack.addUnsafeEnchantment(enchant, level);
        }
        return this;
    }

    public ItemBuilder setGlow(Boolean glow) {
        if (!glow) return this;
        itemStack.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setType(Material material) {
        itemStack.setType(material);
        return this;
    }

    public ItemBuilder removeEnchantment(String enchant) {
        itemStack.removeEnchantment(serializeEnchant(enchant));
        return this;
    }

    public ItemBuilder setSkullOwner(String owner) {
        try{
            SkullMeta itemMeta = (SkullMeta)itemStack.getItemMeta();
            itemMeta.setOwner(owner);
            itemStack.setItemMeta(itemMeta);
        } catch(Exception expected) { }
        return this;
    }

    public ItemBuilder addEnchant(String enchant, Integer level) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.addEnchant(serializeEnchant(enchant), level, true);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder addEnchantments(HashMap<Enchantment, Integer> enchantments) {
        itemStack.addEnchantments(enchantments);
        return this;
    }

    public ItemBuilder setInfinityDurability() {
        itemStack.setDurability(Short.MAX_VALUE);
        return this;
    }

    public ItemBuilder addItemFlag(ItemFlag flag) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.addItemFlags(flag);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setLore(String... lore) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setLore(Arrays.asList(lore));
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        if (lore == null) return this;
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder removeLoreLine(String line) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> lore = new ArrayList<>(itemMeta.getLore());
        if (!lore.contains(line)) return this;
        lore.remove(line);
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder removeLoreLine(Integer index) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> lore = new ArrayList<>(itemMeta.getLore());
        if(index < 0 || index> lore.size()) return this;
        lore.remove(index);
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder addLoreLine(String line) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> lore = new ArrayList<>();
        if (itemMeta.hasLore()) {
            lore = new ArrayList<>(itemMeta.getLore());
        }
        lore.add(line);
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder addLoreLine(String line, Integer pos) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> lore = new ArrayList<>(itemMeta.getLore());
        lore.set(pos, line);
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    @SuppressWarnings("deprecation")
    public ItemBuilder setDyeColor(DyeColor color) {
        itemStack.setDurability(color.getData());
        return this;
    }

    @Deprecated
    public ItemBuilder setWoolColor(DyeColor color) {
        if (!itemStack.getType().equals(Material.WOOL)) return this;
        itemStack.setDurability(color.getData());
        return this;
    }

    public ItemBuilder setLeatherArmorColor(Color color) {
        try {
            LeatherArmorMeta itemMeta = (LeatherArmorMeta) itemStack.getItemMeta();
            itemMeta.setColor(color);
        } catch(Exception ignored) { }
        return this;
    }

    public static Enchantment serializeEnchant(String enchant) {
        String en;
        switch (enchant.toLowerCase()) {
            case "fireprotection":
                en = "PROTECTION_FIRE";
                break;
            case "featherfalling":
                en = "PROTECTION_FALL";
                break;
            case "unbreaking":
                en = "DURABILITY";
                break;
            case "thorns":
                en = "THORNS";
                break;
            case "aquaaffinity":
                en = "WATER_WORKER";
                break;
            case "projectileprotection":
                en = "PROTECTION_PROJECTILE";
                break;
            case "sharpness":
                en = "DAMAGE_ALL";
                break;
            case "smite":
                en = "DAMAGE_UNDEAD";
                break;
            case "infinity":
                en = "ARROW_INFINITE";
                break;
            case "protection":
                en = "PROTECTION_ENVIRONMENTAL";
                break;
            case "baneofarthropods":
                en = "DAMAGE_ARTHROPODS";
                break;
            case "respiration":
                en = "OXYGEN";
                break;
            case "silktouch":
                en = "SILK_TOUCH";
                break;
            case "fortune":
                en = "LOOT_BONUS_BLOCKS";
                break;
            case "looting":
                en = "LOOT_BONUS_MOBS";
                break;
            case "knockback":
                en = "KNOCKBACK";
                break;
            case "flame":
                en = "ARROW_FIRE";
                break;
            case "power":
                en = "ARROW_DAMAGE";
                break;
            case "blastprotection":
                en = "PROTECTION_EXPLOSIONS";
                break;
            case "efficiency":
                en = "DIG_SPEED";
                break;
            case "fireaspect":
                en = "FIRE_ASPECT";
                break;
            case "punch":
                en = "ARROW_KNOCKBACK";
                break;
            default:
                en = enchant;
        }
        return Enchantment.getByName(en);
    }

    public ItemStack toItemStack() {
        return itemStack;
    }
}
