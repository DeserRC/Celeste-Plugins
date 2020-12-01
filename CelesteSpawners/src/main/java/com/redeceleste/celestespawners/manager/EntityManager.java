package com.redeceleste.celestespawners.manager;

import com.redeceleste.celestespawners.CelesteSpawners;
import lombok.SneakyThrows;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.HashSet;
import java.util.Set;

import static com.redeceleste.celestespawners.util.ReflectionUtil.getNMS;
import static org.bukkit.DyeColor.WHITE;

public class EntityManager {
    private final CelesteSpawners main;
    private final ConfigManager config;

    public EntityManager(CelesteSpawners main) {
        this.main = main;
        this.config = main.getConfigManager();
    }

    public void setName(LivingEntity entity, String fileName, long amount, boolean isCustom) {
        String nameFormat = config.getConfig("Stack-Mobs.Name");
        String amountFormatted = config.formatDecimal(amount);

        nameFormat = nameFormat
                .replace("{amount}", amountFormatted)
                .replace("{type}", fileName);

        entity.setCustomName(nameFormat);
        entity.setCustomNameVisible(true);
        entity.setCanPickupItems(false);

        entity.setMetadata("amount", new FixedMetadataValue(main, amount));
        entity.setMetadata("name", new FixedMetadataValue(main, fileName));
        entity.setMetadata("iscustom", new FixedMetadataValue(main, isCustom));
        setAI(entity, false);

        switch (entity.getType()) {
            case SLIME:
                ((Slime) entity).setSize(1);
                break;
            case MAGMA_CUBE:
                ((MagmaCube) entity).setSize(1);
                break;
            case ZOMBIE:
                ((Zombie) entity).setBaby(false);
                ((Zombie) entity).setVillager(false);
                break;
            case SHEEP:
                ((Sheep) entity).setColor(WHITE);
                break;
        }
    }

    public void editName(LivingEntity entity, String fileName, long amount) {
        String nameFormat = config.getConfig("Stack-Mobs.Name");
        long newAmount = amount + getAmount(entity);
        String formatted = config.formatDecimal(newAmount);

        nameFormat = nameFormat
                .replace("{amount}", formatted)
                .replace("{type}", fileName);

        entity.setCustomName(nameFormat);
        entity.removeMetadata("amount", main);
        entity.setMetadata("amount", new FixedMetadataValue(main, newAmount));
    }

    public String getName(LivingEntity entity) {
        if (!entity.hasMetadata("name")) return null;
        return entity.getMetadata("name").get(0).asString();
    }

    public long getAmount(LivingEntity entity) {
        if (!entity.hasMetadata("amount")) return 0L;
        return entity.getMetadata("amount").get(0).asLong();
    }

    public boolean isCustom(LivingEntity entity) {
        if (!entity.hasMetadata("iscustom")) return false;
        return entity.getMetadata("iscustom").get(0).asBoolean();
    }

    public Set<LivingEntity> getNearestEntity(Location loc, EntityType entity) {
        Set<LivingEntity> entities = new HashSet<>();
        int radius = config.getConfig("Stack-Mobs.Radius");

        for (Entity nearby : loc.getWorld().getNearbyEntities(loc, radius, 100, radius)) {
            if (nearby.getType() == entity) entities.add((LivingEntity) nearby);
        }
        return entities;
    }

    @SneakyThrows
    private void setAI(LivingEntity entity, boolean AI) {
        Class<?> tagClass = getNMS("NBTTagCompound");

        Object handle = entity.getClass().getMethod("getHandle").invoke(entity);
        Object tag = handle.getClass().getMethod("getNBTTag").invoke(handle);

        if (tag == null) {
            tag = tagClass.newInstance();
        }

        handle.getClass().getMethod("c", tagClass).invoke(handle, tag);
        tagClass.getMethod("setInt", String.class, int.class).invoke(tag, "NoAI", AI ? 0 : 1);
        handle.getClass().getMethod("f", tagClass).invoke(handle, tag);
    }
}
