package com.redeceleste.celestespawners.type;

import com.redeceleste.celestespawners.builder.ItemBuilder;
import lombok.Getter;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.Material.*;

@Getter
public enum MobType {
    BAT("Morcego", "MHF_Bat", EntityType.BAT),
    BLAZE("Blaze", "MHF_Blaze", EntityType.BLAZE),
    CAVE_SPIDER("Aranha da Caverna", "MHF_CaveSpider", EntityType.CAVE_SPIDER),
    CHICKEN("Galinha", "MHF_Chicken", EntityType.CHICKEN),
    COW("Vaca", "MHF_Cow", EntityType.COW),
    CREEPER("Creeper", "MHF_Creeper", EntityType.CREEPER),
    ENDERMAN("Enderman", "MHF_Enderman", EntityType.ENDERMAN),
    GHAST("Ghast", "MHF_Ghast", EntityType.GHAST),
    GIANT("Zumbi Gigante", "MHF_Zombie", EntityType.GIANT),
    GUARDIAN("Guardião", "MHF_Guardian", EntityType.GUARDIAN),
    IRON_GOLEM("Iron Golem", "MHF_Golem", EntityType.IRON_GOLEM),
    MAGMA_CUBE("Lava Slime", "MHF_LavaSlime", EntityType.MAGMA_CUBE),
    MUSHROOM_COW("Vaca Cogumelo", "MHF_MushroomCow", EntityType.MUSHROOM_COW),
    OCELOT("Jaguatirica", "MHF_Ocelot", EntityType.OCELOT),
    PIG("Porco", "MHF_Pig", EntityType.PIG),
    PIG_ZOMBIE("Porco Zumbi", "MHF_PigZombie", EntityType.PIG_ZOMBIE),
    SHEEP("Ovelha", "MHF_Sheep", EntityType.SHEEP),
    SKELETON("Esqueleto", "MHF_Skeleton", EntityType.SKELETON),
    SLIME("Slime", "MHF_Slime", EntityType.SLIME),
    SPIDER("Aranha", "MHF_Spider", EntityType.SPIDER),
    SQUID("Lula", "MHF_Squid", EntityType.SQUID),
    VILLAGER("Vilager", "MHF_Villager", EntityType.VILLAGER),
    WITCH("Bruxa", "MHF_Witch", EntityType.WITCH),
    WITHER("Wither", "MHF_Wither", EntityType.WITHER),
    WOLF("Lobo", "MHF_WOLF", EntityType.WOLF),
    ZOMBIE("Zumbi", "MHF_Zombie", EntityType.ZOMBIE);

    private final String name;
    private final ItemStack head;
    private final EntityType entity;

    MobType(String name, String skull, EntityType entity) {
        this.name = name;
        this.entity = entity;
        this.head = new ItemBuilder(SKULL_ITEM, 1)
                .setData(3)
                .setSkullOwner(skull)
                .toItemStack();
    }

    public static MobType getMob(EntityType entity) {
        String name = entity.name().toUpperCase();
        return valueOf(name);
    }

    public static MobType getMob(String mobName) {
        for (MobType type : values()) {
            String entityName = type.entity.name();
            String typeName = type.getName();
            if (entityName.equalsIgnoreCase(mobName)) return type;
            if (typeName.equalsIgnoreCase(mobName)) return type;
        }
        return null;
    }

    public static List<ItemStack> getDrop(MobType type, int amount) {
        List<ItemStack> drops = new ArrayList<>();
        switch (type) {
            case BAT:
                break;
            case BLAZE:
                drops.add(new ItemStack(BLAZE_ROD, amount));
                break;
            case CAVE_SPIDER:
                drops.add(new ItemStack(SPIDER_EYE, amount));
                break;
            case CHICKEN:
                drops.add(new ItemStack(RAW_CHICKEN, amount));
                break;
            case COW:
                drops.add(new ItemStack(RAW_BEEF, amount));
                break;
            case CREEPER:
                drops.add(new ItemStack(SULPHUR, amount));
                break;
            case ENDERMAN:
                drops.add(new ItemStack(ENDER_PEARL, amount));
                break;
            case GHAST:
                drops.add(new ItemStack(GHAST_TEAR, amount));
                break;
            case GIANT:
                break;
            case GUARDIAN:
                drops.add(new ItemStack(PRISMARINE_CRYSTALS, amount));
                break;
            case IRON_GOLEM:
                drops.add(new ItemStack(IRON_INGOT, amount));
                break;
            case MAGMA_CUBE:
                drops.add(new ItemStack(MAGMA_CREAM, amount));
                break;
            case MUSHROOM_COW:
                drops.add(new ItemStack(RED_MUSHROOM, amount));
                break;
            case OCELOT:
                break;
            case PIG:
                drops.add(new ItemStack(PORK, amount));
                break;
            case PIG_ZOMBIE:
                drops.add(new ItemStack(GOLD_NUGGET, amount));
                break;
            case SHEEP:
                drops.add(new ItemStack(WOOL, amount));
                break;
            case SKELETON:
                drops.add(new ItemStack(BONE, amount));
                break;
            case SLIME:
                drops.add(new ItemStack(SLIME_BALL, amount));
                break;
            case SPIDER:
                drops.add(new ItemStack(STRING, amount));
                break;
            case SQUID:
                drops.add(new ItemStack(INK_SACK, amount));
                break;
            case VILLAGER:
                break;
            case WITCH:
                drops.add(new ItemStack(SUGAR, amount));
                break;
            case WITHER:
                drops.add(new ItemStack(NETHER_STAR, amount));
                break;
            case WOLF:
                break;
            case ZOMBIE:
                drops.add(new ItemStack(ROTTEN_FLESH, amount));
                break;
        }
        return drops;
    }
}
