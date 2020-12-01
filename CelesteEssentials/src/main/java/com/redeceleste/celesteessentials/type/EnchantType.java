package com.redeceleste.celesteessentials.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.enchantments.Enchantment;

import static org.bukkit.enchantments.Enchantment.getByName;

@AllArgsConstructor
@Getter
public enum EnchantType {
    AQUAAFFINITY("WATER_WORKER"),
    BANEOFARTHROPODS("DAMAGE_ARTHROPODS"),
    BLASTPROTECTION("PROTECTION_EXPLOSIONS"),
    EFFICIENCY("DIG_SPEED"),
    FEATHERFALLING("PROTECTION_FALL"),
    FIREASPECT("FIRE_ASPECT"),
    FIREPROTECTION("PROTECTION_FIRE"),
    FLAME("ARROW_FIRE"),
    FORTUNE("LOOT_BONUS_BLOCKS"),
    INFINITY("ARROW_INFINITE"),
    KNOCKBACK("KNOCKBACK"),
    LOOTING("LOOT_BONUS_MOBS"),
    POWER("ARROW_DAMAGE"),
    PROJECTILEPROTECTION("PROTECTION_PROJECTILE"),
    PROTECTION("PROTECTION_ENVIRONMENTAL"),
    PUNCH("ARROW_KNOCKBACK"),
    RESPIRATION("OXYGEN"),
    SHARPNESS("DAMAGE_ALL"),
    SILKTOUCH("SILK_TOUCH"),
    SMITE("DAMAGE_UNDEAD"),
    THORNS("THORNS"),
    UNBREAKING("DURABILITY");

    private final String name;

    public static EnchantType getType(String enchant) {
        for (EnchantType type : values()) {
           if (type.name().equalsIgnoreCase(enchant)) return type;
           if (type.getName().equalsIgnoreCase(enchant)) return type;
        }
        return null;
    }

    public static Enchantment getEnchant(String enchant) {
        EnchantType type = getType(enchant);
        if (type == null) return null;
        return getByName(type.getName());
    }
}
