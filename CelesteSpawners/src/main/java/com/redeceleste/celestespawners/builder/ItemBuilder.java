package com.redeceleste.celestespawners.builder;

import lombok.SneakyThrows;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Pattern;

import static com.redeceleste.celestespawners.type.EnchantType.getEnchant;
import static com.redeceleste.celestespawners.util.ReflectionUtil.*;
import static java.util.Arrays.asList;
import static org.bukkit.Material.*;
import static org.bukkit.enchantments.Enchantment.DURABILITY;
import static org.bukkit.inventory.ItemFlag.HIDE_DESTROYS;
import static org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS;
import static org.bukkit.potion.PotionEffectType.getById;
import static org.bukkit.potion.PotionEffectType.getByName;

public class ItemBuilder {
    private final ItemStack itemStack;

    public ItemBuilder(Material material, int amount) {
        this.itemStack = new ItemStack(material, amount);
    }

    public ItemBuilder(Material material, int amount, int data) {
        this.itemStack = new ItemStack(material, amount, (short) data);
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

    public ItemBuilder setAmount(int amount) {
        itemStack.setAmount(amount);
        return this;
    }

    public ItemBuilder setName(String name) {
        if (name == null || name.equals("")) return this;
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(name);
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setLore(String... lore) {
        if (lore == null || lore.length == 0) return this;
        ItemMeta meta = itemStack.getItemMeta();
        meta.setLore(asList(lore));
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        if (lore == null || lore.size() == 0) return this;
        ItemMeta meta = itemStack.getItemMeta();
        meta.setLore(lore);
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder addLore(String... lore) {
        if (lore == null || lore.length == 0) return this;
        ItemMeta meta = itemStack.getItemMeta();
        List<String> newLore = meta.getLore();

        if (newLore == null) newLore = new ArrayList<>();
        newLore.addAll(asList(lore));
        meta.setLore(newLore);
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder addLore(List<String> lore) {
        if (lore == null || lore.size() == 0) return this;
        ItemMeta meta = itemStack.getItemMeta();
        List<String> newLore = meta.getLore();

        if (newLore == null) newLore = new ArrayList<>();
        newLore.addAll(lore);
        meta.setLore(newLore);
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder removeLore(String... lore) {
        if (lore == null || lore.length == 0) return this;
        ItemMeta meta = itemStack.getItemMeta();
        List<String> newLore = meta.getLore();

        if (newLore == null) newLore = new ArrayList<>();
        newLore.removeAll(asList(lore));
        meta.setLore(newLore);
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder removeLore(List<String> lore) {
        if (lore == null || lore.size() == 0) return this;
        ItemMeta meta = itemStack.getItemMeta();
        List<String> newLore = meta.getLore();

        if (newLore == null) newLore = new ArrayList<>();
        newLore.removeAll(lore);
        meta.setLore(newLore);
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder removeLoreLine(int index) {
        if (index < 0) return this;
        ItemMeta meta = itemStack.getItemMeta();
        List<String> newLore = meta.getLore();

        if (newLore == null) newLore = new ArrayList<>();
        newLore.remove(index);
        meta.setLore(newLore);
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder replaceLore(String lore, int index) {
        if (lore == null || lore.length() == 0 || index < 0) return this;
        ItemMeta meta = itemStack.getItemMeta();
        List<String> newLore = meta.getLore();

        if (newLore == null) newLore = new ArrayList<>();
        if (index >= newLore.size()) {
            for (int i=newLore.size(); i<=index; i++) {
                newLore.add("§c");
            }
        }

        newLore.set(index, lore);
        meta.setLore(newLore);
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder addEnchant(String... enchantment) {
        if (enchantment == null) return this;
        for (String en : enchantment) {
            String[] splitEnchant = en.split(":");
            Enchantment enchant = getEnchant(splitEnchant[0]);
            if (enchant == null) continue;
            int level = Integer.parseInt(splitEnchant[1]);
            itemStack.addUnsafeEnchantment(enchant, level);
        }
        return this;
    }

    public ItemBuilder addEnchant(List<String> enchantment) {
        if (enchantment == null) return this;
        for (String en : enchantment) {
            String[] splitEnchant = en.split(":");
            Enchantment enchant = getEnchant(splitEnchant[0]);
            if (enchant == null) continue;
            int level = Integer.parseInt(splitEnchant[1]);
            itemStack.addUnsafeEnchantment(enchant, level);
        }
        return this;
    }

    public ItemBuilder addEnchant(String enchantment, int level) {
        if (enchantment == null) return this;
        Enchantment enchant = getEnchant(enchantment);

        if (enchant == null) return this;
        itemStack.addUnsafeEnchantment(enchant, level);
        return this;
    }

    @SafeVarargs
    public final ItemBuilder addEnchant(Map.Entry<String, Integer>... enchantments) {
        if (enchantments == null) return this;
        for (Map.Entry<String, Integer> en : enchantments) {
            Enchantment enchant = getEnchant(en.getKey());
            if (enchant == null) continue;
            int level = en.getValue();
            itemStack.addUnsafeEnchantment(enchant, level);
        }
        return this;
    }

    public ItemBuilder addEnchant(HashMap<String, Integer> enchantments) {
        if (enchantments == null) return this;
        for (String en : enchantments.keySet()) {
            Enchantment enchant = getEnchant(en);
            if (enchant == null) continue;
            int level = enchantments.get(en);
            itemStack.addUnsafeEnchantment(enchant, level);
        }
        return this;
    }

    public ItemBuilder removeEnchantment(String... enchantments) {
        for (String en : enchantments) {
            Enchantment enchant = getEnchant(en);
            if (enchant == null) continue;
            itemStack.removeEnchantment(enchant);
        }
        return this;
    }

    public ItemBuilder setDurability(short durability) {
        itemStack.setDurability(durability);
        return this;
    }

    public ItemBuilder addDurability(short durability) {
        itemStack.setDurability((short) (itemStack.getDurability() + durability));
        return this;
    }

    public ItemBuilder setInfinityDurability() {
        itemStack.setDurability(Short.MAX_VALUE);
        ItemMeta meta = itemStack.getItemMeta();
        meta.addItemFlags(HIDE_DESTROYS);
        return this;
    }

    public ItemBuilder setGlow(boolean glow) {
        if (!glow) return this;
        itemStack.addUnsafeEnchantment(DURABILITY, 1);
        ItemMeta meta = itemStack.getItemMeta();
        meta.addItemFlags(HIDE_ENCHANTS);
        itemStack.setItemMeta(meta);
        return this;
    }

    @SneakyThrows
    public ItemBuilder setSkull(String texture, UUID uuid) {
        if (itemStack.getType() != SKULL_ITEM) return this;

        texture = "http://textures.minecraft.net/texture/" + texture;
        SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
        Class<?> profileClass = getClazz("com.mojang.authlib.", "GameProfile");
        Class<?> propertyClass = getClazz("com.mojang.authlib.properties.", "Property");

        Constructor<?> profileCon = getCon(profileClass, UUID.class, String.class);
        Constructor<?> propertyCon = getCon(propertyClass, String.class, String.class);
        Field propertiesField = getDcField(profileClass, "properties");

        String encoded = Base64.getEncoder().encodeToString(String.format("{textures:{SKIN:{url:\"%s\"}}}", new Object[] { texture }).getBytes());
        Object profile = instance(profileCon, uuid, null);
        Object property = instance(propertyCon, "textures", encoded);

        Class<?> propertiesClass = getType(propertiesField);

        Method put = getMethod(propertiesClass,"put", Object.class, Object.class);
        invoke(put, propertiesField.get(profile),"textures", property);

        Field profileField = getDcField(meta.getClass(), "profile");
        profileField.set(meta, profile);
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setSkullOwner(String owner) {
        SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
        meta.setOwner(owner);
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setMob(EntityType type) {
        if (itemStack.getType() != MOB_SPAWNER) return this;
        ItemMeta meta = itemStack.getItemMeta();
        BlockState state = ((BlockStateMeta) meta).getBlockState();
        ((CreatureSpawner) state).setSpawnedType(type);
        ((BlockStateMeta) meta).setBlockState(state);
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
        if (!itemStack.getType().equals(WOOL)) return this;
        itemStack.setDurability(color.getData());
        return this;
    }

    public ItemBuilder setArmorColor(Color color) {
        LeatherArmorMeta meta = (LeatherArmorMeta) itemStack.getItemMeta();
        meta.setColor(color);
        return this;
    }

    public ItemBuilder addPotion(List<String> potions) {
        if (itemStack.getType() != POTION) return this;
        for (String pt : potions) {
            String[] splitPotion = pt.split(":");
            if (splitPotion.length < 2) continue;
            String potionName = splitPotion[0];
            int duration = Integer.parseInt(splitPotion[1]);
            int amplifier = Integer.parseInt(splitPotion[2]);

            PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
            PotionEffectType type = getByName(potionName);
            if (type == null) {
                if (!Pattern.matches("[0-9]+", potionName)) return this;
                int id = Integer.parseInt(potionName);
                type = getById(id);
                if (type == null) return this;
            }

            PotionEffect effect = type.createEffect(duration * 20, amplifier);
            meta.addCustomEffect(effect, true);
            itemStack.setItemMeta(meta);

            Potion potion = Potion.fromItemStack(itemStack);
            potion.setSplash(potion.isSplash());
            potion.apply(itemStack);
        }
        return this;
    }

    public ItemBuilder addPotion(String potionName, Integer duration, Integer amplifier) {
        if (itemStack.getType() != POTION) return this;
        PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
        PotionEffectType type = getByName(potionName);
        if (type == null) {
            if (!Pattern.matches("[0-9]+", potionName)) return this;
            int id = Integer.parseInt(potionName);
            type = getById(id);
            if (type == null) return this;
        }

        PotionEffect effect = type.createEffect(duration * 20, amplifier);
        meta.addCustomEffect(effect, true);
        itemStack.setItemMeta(meta);

        Potion potion = Potion.fromItemStack(itemStack);
        potion.setSplash(potion.isSplash());
        potion.apply(itemStack);
        return this;
    }

    public ItemBuilder removePotion(String potionName) {
        if (itemStack.getType() != POTION) return this;
        PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
        PotionEffectType type = getByName(potionName);
        if (type == null) {
            if (!Pattern.matches("[0-9]+", potionName)) return this;
            int id = Integer.parseInt(potionName);
            type = getById(id);
            if (type == null) return this;
        }

        meta.removeCustomEffect(type);
        itemStack.setItemMeta(meta);

        Potion potion = Potion.fromItemStack(itemStack);
        potion.setSplash(potion.isSplash());
        potion.apply(itemStack);
        return this;
    }

    public ItemBuilder clearPotion() {
        if (itemStack.getType() != POTION) return this;
        PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
        meta.clearCustomEffects();
        itemStack.setItemMeta(meta);

        Potion potion = Potion.fromItemStack(itemStack);
        potion.setSplash(potion.isSplash());
        potion.apply(itemStack);
        return this;
    }

    @SneakyThrows
    public <T> ItemBuilder addNBTTag(T key, T value) {
        Class<?> craftItemStackClazz = getOBC("inventory.CraftItemStack");
        Class<?> itemStackClazz = getNMS("ItemStack");
        Class<?> compoundClazz = getNMS("NBTTagCompound");

        Method asNMSCopy = getMethod(craftItemStackClazz, "asNMSCopy", ItemStack.class);
        Method hasTag = getMethod(itemStackClazz, "hasTag");
        Method getTag = getMethod(itemStackClazz, "getTag");

        Object nmsItem = invokeStatic(asNMSCopy, itemStack);
        Boolean isExist = (Boolean) invoke(hasTag, nmsItem);
        Object compound;

        if (isExist) compound = invoke(getTag, nmsItem);
        else compound = compoundClazz.newInstance();

        Class<?> tagClazz = getNMS("NBTTagString");
        Class<?> baseClazz = getNMS("NBTBase");

        Constructor<?> tagCon = getCon(tagClazz, String.class);

        Method set = getMethod(compoundClazz, "set", String.class, baseClazz);
        Method setTag = getMethod(itemStackClazz, "setTag", compoundClazz);
        Method getItemMeta = getMethod(craftItemStackClazz, "getItemMeta", itemStackClazz);

        Object tag = instance(tagCon, value.toString());
        invoke(set, compound, key.toString(), tag);
        invoke(setTag, nmsItem, compound);

        ItemMeta meta = (ItemMeta) invokeStatic(getItemMeta, nmsItem);
        itemStack.setItemMeta(meta);
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

    public ItemStack toItemStack() {
        return itemStack;
    }
}