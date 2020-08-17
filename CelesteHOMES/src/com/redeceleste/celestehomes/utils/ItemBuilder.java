package com.redeceleste.celestehomes.utils;

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
import java.util.List;
import java.util.Map;


public class ItemBuilder {
    private ItemStack is;

    public ItemBuilder(Material m){
        this(m, 1);
    }

    public ItemBuilder(ItemStack is){
        this.is=is;
    }

    public ItemBuilder(Material m, int quantia){
        is= new ItemStack(m, quantia);
    }

    public ItemBuilder(Material m, int quantia, short durabilidade){
        is = new ItemStack(m, quantia, durabilidade);
    }

    public ItemBuilder clone(){
        return new ItemBuilder(is);
    }

    public ItemBuilder setData(int data){
        is.setDurability((byte) data);
        return this;
    }

    public ItemBuilder setDurability(short durabilidade){
        is.setDurability(durabilidade);
        return this;
    }

    public ItemBuilder addLores(List<String> lore) {
        ItemMeta im = is.getItemMeta();
        List<String> newLore = im.getLore();
        newLore.addAll(lore);
        im.setLore(newLore);
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        is.setAmount(amount);
        return this;
    }

    public ItemBuilder setDurability(int durabilidade){
        is.setDurability(Short.valueOf(""+durabilidade));
        return this;
    }

    public ItemBuilder setName(String nome){
        if (nome.equalsIgnoreCase("nulo")) return this;
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(nome);
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder addUnsafeEnchantment(Enchantment ench, int level){
        is.addUnsafeEnchantment(ench, level);
        return this;
    }

    public ItemBuilder addEnchants(List<String> enchants){
        if (enchants.get(0).equalsIgnoreCase("nulo")) return this;
        for (String s : enchants) {
            Enchantment ench = Enchantment.getByName(s.split(":")[0]);
            int level = Integer.valueOf(s.split(":")[1]);
            is.addUnsafeEnchantment(ench, level);
        }
        return this;
    }

    public ItemBuilder setGlow(boolean b) {
        if (!b) return this;
        is.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        ItemMeta im = is.getItemMeta();
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder setType(Material m) {
        is.setType(m);
        return this;
    }

    public ItemBuilder removeEnchantment(Enchantment ench){
        is.removeEnchantment(ench);
        return this;
    }

    public ItemBuilder setSkullOwner(String dono){
        try{
            SkullMeta im = (SkullMeta)is.getItemMeta();
            im.setOwner(dono);
            is.setItemMeta(im);
        } catch(ClassCastException expected){}
        return this;
    }

    public ItemBuilder addEnchant(Enchantment ench, int level){
        ItemMeta im = is.getItemMeta();
        im.addEnchant(ench, level, true);
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder addEnchantments(Map<Enchantment, Integer> enchantments){
        is.addEnchantments(enchantments);
        return this;
    }

    public ItemBuilder setInfinityDurability(){
        is.setDurability(Short.MAX_VALUE);
        return this;
    }

    public ItemBuilder addItemFlag(ItemFlag flag){
        ItemMeta im = is.getItemMeta();
        im.addItemFlags(flag);
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder setLore(String... lore){
        ItemMeta im = is.getItemMeta();
        im.setLore(Arrays.asList(lore));
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        if (lore.get(0).equalsIgnoreCase("nulo")) return this;
        ItemMeta im = is.getItemMeta();
        im.setLore(lore);
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder removeLoreLine(String linha){
        ItemMeta im = is.getItemMeta();
        List<String> lore = new ArrayList<>(im.getLore());
        if(!lore.contains(linha))return this;
        lore.remove(linha);
        im.setLore(lore);
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder removeLoreLine(int index){
        ItemMeta im = is.getItemMeta();
        List<String> lore = new ArrayList<>(im.getLore());
        if(index<0||index>lore.size())return this;
        lore.remove(index);
        im.setLore(lore);
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder addLoreLine(String linha){
        ItemMeta im = is.getItemMeta();
        List<String> lore = new ArrayList<>();
        if(im.hasLore())lore = new ArrayList<>(im.getLore());
        lore.add(linha);
        im.setLore(lore);
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder addLoreLine(String linha, int pos){
        ItemMeta im = is.getItemMeta();
        List<String> lore = new ArrayList<>(im.getLore());
        lore.set(pos, linha);
        im.setLore(lore);
        is.setItemMeta(im);
        return this;
    }

    @SuppressWarnings("deprecation")
    public ItemBuilder setDyeColor(DyeColor cor){
        this.is.setDurability(cor.getData());
        return this;
    }

    @Deprecated
    public ItemBuilder setWoolColor(DyeColor cor){
        if(!is.getType().equals(Material.WOOL))return this;
        this.is.setDurability(cor.getData());
        return this;
    }

    public ItemBuilder setLeatherArmorColor(Color cor){
        try{
            LeatherArmorMeta im = (LeatherArmorMeta)is.getItemMeta();
            im.setColor(cor);
        } catch(ClassCastException expected){}
        return this;
    }

    public static String serializeEnchant(String enchant) {
        String en;
        switch (enchant.toLowerCase()) {
            case "fireprotection": {
                en = "PROTECTION_FIRE";
                break;
            }
            case "featherfalling": {
                en = "PROTECTION_FALL";
                break;
            }
            case "unbreaking": {
                en = "DURABILITY";
                break;
            }
            case "thorns": {
                en = "THORNS";
                break;
            }
            case "aquaaffinity": {
                en = "WATER_WORKER";
                break;
            }
            case "projectileprotection": {
                en = "PROTECTION_PROJECTILE";
                break;
            }
            case "sharpness": {
                en = "DAMAGE_ALL";
                break;
            }
            case "smite": {
                en = "DAMAGE_UNDEAD";
                break;
            }
            case "infinity": {
                en = "ARROW_INFINITE";
                break;
            }
            case "protection": {
                en = "PROTECTION_ENVIRONMENTAL";
                break;
            }
            case "baneofarthropods": {
                en = "DAMAGE_ARTHROPODS";
                break;
            }
            case "respiration": {
                en = "OXYGEN";
                break;
            }
            case "silktouch": {
                en = "SILK_TOUCH";
                break;
            }
            case "fortune": {
                en = "LOOT_BONUS_BLOCKS";
                break;
            }
            case "looting": {
                en = "LOOT_BONUS_MOBS";
                break;
            }
            case "knockback": {
                en = "KNOCKBACK";
                break;
            }
            case "flame": {
                en = "ARROW_FIRE";
                break;
            }
            case "power": {
                en = "ARROW_DAMAGE";
                break;
            }
            case "blastprotection": {
                en = "PROTECTION_EXPLOSIONS";
                break;
            }
            case "efficiency": {
                en = "DIG_SPEED";
                break;
            }
            case "fireaspect": {
                en = "FIRE_ASPECT";
                break;
            }
            case "punch": {
                en = "ARROW_KNOCKBACK";
                break;
            }
            default:
                en = null;
        }
        return en;
    }

    public ItemStack toItemStack(){
        return is;
    }
}