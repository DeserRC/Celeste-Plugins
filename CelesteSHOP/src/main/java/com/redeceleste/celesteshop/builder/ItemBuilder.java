package com.redeceleste.celesteshop.builder;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemBuilder {
    private final ItemStack itemStack;

    public ItemBuilder(Material material, Integer amount) {
        this.itemStack = new ItemStack(material, amount);
    }

    public ItemBuilder(Material material, Integer amount, int data) {
        this.itemStack = new ItemStack(material, amount, (byte) data);
    }

    public ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ItemBuilder setMaterial(Material material) {
        itemStack.setType(material);
        return this;
    }

    public ItemBuilder setData(int data) {
        itemStack.setDurability((short) data);
        return this;
    }

    public ItemBuilder setAmount(Integer amount) {
        itemStack.setAmount(amount);
        return this;
    }

    public ItemBuilder setName(String name) {
        ItemMeta meta = itemStack.getItemMeta();

        meta.setDisplayName(name);
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setLore(String... lore) {
        if (lore == null) return this;

        ItemMeta meta = itemStack.getItemMeta();

        meta.setLore(Arrays.asList(lore));
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        if (lore == null) return this;

        ItemMeta meta = itemStack.getItemMeta();

        meta.setLore(lore);
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder addLore(String... lore) {
        if (lore == null) return this;

        ItemMeta meta = itemStack.getItemMeta();
        List<String> newLore = meta.getLore();

        newLore.addAll(Arrays.asList(lore));
        meta.setLore(newLore);
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder addLore(List<String> lore) {
        if (lore == null) return this;

        ItemMeta meta = itemStack.getItemMeta();
        List<String> newLore = meta.getLore();

        newLore.addAll(lore);
        meta.setLore(newLore);
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder removeLore(String... lore) {
        if (lore == null) return this;

        ItemMeta meta = itemStack.getItemMeta();
        List<String> newLore = meta.getLore();

        newLore.removeAll(Arrays.asList(lore));
        meta.setLore(newLore);
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder removeLore(List<String> lore) {
        if (lore == null) return this;

        ItemMeta meta = itemStack.getItemMeta();
        List<String> newLore = meta.getLore();

        newLore.removeAll(lore);
        meta.setLore(newLore);
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder removeLoreLine(Integer index) {
        if (index < 0) return this;

        ItemMeta meta = itemStack.getItemMeta();
        List<String> newLore = meta.getLore();

        newLore.remove(index);
        meta.setLore(newLore);
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder replaceLore(String lore, Integer index) {
        if (lore == null || index < 0) return this;

        ItemMeta meta = itemStack.getItemMeta();
        List<String> newLore = meta.getLore();

        newLore.set(index, lore);
        meta.setLore(newLore);
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder addEnchant(String... enchantment) {
        if (enchantment == null) return this;

        Arrays.stream(enchantment).forEach(en -> {
            String[] splitEnchant = en.split(":");
            Enchantment enchant = serializeEnchant(splitEnchant[0]);
            Integer level = Integer.parseInt(splitEnchant[1]);

            itemStack.addUnsafeEnchantment(enchant, level);
        }); return this;
    }

    public ItemBuilder addEnchant(List<String> enchantment) {
        if (enchantment == null) return this;

        enchantment.forEach(en -> {
            String[] splitEnchant = en.split(":");
            Enchantment enchant = serializeEnchant(splitEnchant[0]);
            Integer level = Integer.parseInt(splitEnchant[1]);

            itemStack.addUnsafeEnchantment(enchant, level);
        }); return this;
    }

    public ItemBuilder addEnchant(String enchantment, Integer level) {
        if (enchantment == null) return this;

        Enchantment enchant = serializeEnchant(enchantment);
        itemStack.addUnsafeEnchantment(enchant, level);
        return this;
    }

    @SafeVarargs
    public final ItemBuilder addEnchant(Map.Entry<String, Integer>... enchantments) {
        if (enchantments == null) return this;

        for (Map.Entry<String, Integer> en : enchantments) {
            Enchantment enchant = serializeEnchant(en.getKey());
            Integer level = en.getValue();

            itemStack.addUnsafeEnchantment(enchant, level);
        } return this;
    }

    public ItemBuilder addEnchant(HashMap<String, Integer> enchantments) {
        if (enchantments == null) return this;

        enchantments.keySet().forEach(en -> {
            Enchantment enchant = serializeEnchant(en);
            Integer level = enchantments.get(en);

            itemStack.addUnsafeEnchantment(enchant, level);
        }); return this;
    }

    public ItemBuilder removeEnchantment(String enchantments) {
        Enchantment enchant = serializeEnchant(enchantments);

        itemStack.removeEnchantment(enchant);
        return this;
    }

    public ItemBuilder setDurability(Short durability) {
        itemStack.setDurability(durability);
        return this;
    }

    public ItemBuilder addDurability(Short durability) {
        itemStack.setDurability((short) (itemStack.getDurability() + durability));
        return this;
    }

    public ItemBuilder setInfinityDurability() {
        itemStack.setDurability(Short.MAX_VALUE);
        ItemMeta meta = itemStack.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
        return this;
    }

    public ItemBuilder setGlow(Boolean glow) {
        if (!glow) return this;

        itemStack.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        ItemMeta meta = itemStack.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemStack.setItemMeta(meta);
        return this;
    }

    @Deprecated
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

    public ItemBuilder setArmorColor(Color color) {
        LeatherArmorMeta meta = (LeatherArmorMeta) itemStack.getItemMeta();
        meta.setColor(color);
        return this;
    }

    public ItemBuilder addItemFlag(ItemFlag... flag) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.addItemFlags(flag);
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder removeItemFlag(ItemFlag... flag) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.removeItemFlags(flag);
        itemStack.setItemMeta(meta);
        return this;
    }

    private Enchantment serializeEnchant(String enchant) {
        String en = enchant;
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
        }
        return Enchantment.getByName(en);
    }

    public ItemStack toItemStack() {
        return itemStack;
    }
}