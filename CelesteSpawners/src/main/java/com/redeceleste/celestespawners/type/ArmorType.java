package com.redeceleste.celestespawners.type;

import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

public enum ArmorType {
    HAND, HELMET, CHESTPLATE, LEGGINGS, BOOTS;

    public void apply(LivingEntity livingEntity, ItemStack item, ArmorType type) {
        switch (type) {
            case HAND:
                livingEntity.getEquipment().setItemInHand(item);
                livingEntity.getEquipment().setItemInHandDropChance(0);
                break;
            case HELMET:
                livingEntity.getEquipment().setHelmet(item);
                livingEntity.getEquipment().setHelmetDropChance(0);
                break;
            case CHESTPLATE:
                livingEntity.getEquipment().setChestplate(item);
                livingEntity.getEquipment().setChestplateDropChance(0);
                break;
            case LEGGINGS:
                livingEntity.getEquipment().setLeggings(item);
                livingEntity.getEquipment().setLeggingsDropChance(0);
                break;
            case BOOTS:
                livingEntity.getEquipment().setBoots(item);
                livingEntity.getEquipment().setBootsDropChance(0);
                break;
        }
    }

    public static ArmorType getArmor(String armorName) {
        return valueOf(armorName.toUpperCase());
    }
}
