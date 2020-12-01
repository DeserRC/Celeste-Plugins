package com.redeceleste.celestespawners.manager;

import com.redeceleste.celestespawners.CelesteSpawners;
import com.redeceleste.celestespawners.builder.ItemBuilder;
import com.redeceleste.celestespawners.factory.SpawnerFactory;
import com.redeceleste.celestespawners.model.SpawnerArgument;
import com.redeceleste.celestespawners.model.impl.Spawner;
import com.redeceleste.celestespawners.model.impl.SpawnerCustom;
import com.redeceleste.celestespawners.type.MobType;
import lombok.SneakyThrows;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

import static com.redeceleste.celestespawners.util.LocationUtil.serialize;
import static com.redeceleste.celestespawners.util.ReflectionUtil.*;
import static java.util.stream.Collectors.toList;
import static org.bukkit.Material.MOB_SPAWNER;

public abstract class Manager {
    private final CelesteSpawners main;
    private final SpawnerFactory spawn;
    private final ConfigManager config;
    private final HologramManager hologram;

    private final Method asNMSCopy;
    private final Method hasTag;
    private final Method getTag;
    private final Method getString;

    @SneakyThrows
    public Manager(CelesteSpawners main, SpawnerFactory spawner) {
        this.main = main;
        this.spawn = spawner;
        this.config = main.getConfigManager();
        this.hologram = main.getHologramManager();

        Class<?> ctsClass = getOBC("inventory.CraftItemStack");
        Class<?> isClass = getNMS("ItemStack");
        Class<?> NTCClass = getNMS("NBTTagCompound");

        asNMSCopy = getMethod(ctsClass, "asNMSCopy", ItemStack.class);
        hasTag = getMethod(isClass, "hasTag");
        getTag = getMethod(isClass, "getTag");
        getString = getMethod(NTCClass, "getString", String.class);
    }

    public void addCooldown(String player) {
        spawn.getCooldown().put(player.toLowerCase(), System.currentTimeMillis() + 2000L);
    }

    public void removeCooldown(String player) {
        spawn.getCooldown().remove(player.toLowerCase());
    }

    public boolean containsCooldown(String player) {
        return spawn.getCooldown().containsKey(player.toLowerCase());
    }

    public long getCooldown(String player) {
        return spawn.getCooldown().get(player.toLowerCase());
    }

    public void deleteSpawner(SpawnerArgument spawnerArg) {
        spawnerArg.removeAmount(1L);
        hologram.deleteHologram(spawnerArg);
        spawn.getSpawners().remove(spawnerArg.getLocation());
        spawn.getDeleteSpawners().add(spawnerArg.getLocation());
    }

    public void addSpawner(SpawnerArgument spawnerArg) {
        addSpawner(spawnerArg, 1L);
    }
    
    public void addSpawner(SpawnerArgument spawnerArg, long amount) {
        spawnerArg.addAmount(amount);
        hologram.updateHologram(spawnerArg);
    }
    
    public void removeSpawner(SpawnerArgument spawnerArg) {
        spawnerArg.removeAmount(1L);
        hologram.updateHologram(spawnerArg);
    }
    
    @SneakyThrows
    protected ItemStack getSpawner(String name, String amount, String nameFormat, List<String> loreFormat, MobType type, boolean isCustom) {
        nameFormat = nameFormat
                .replace("{amount}", amount)
                .replace("{type}", name);

        loreFormat = loreFormat.stream().map(r -> r
                .replace("{amount}", amount)
                .replace("{type}", name))
                .collect(toList());

        return new ItemBuilder(MOB_SPAWNER, 1)
                .setName(nameFormat)
                .setLore(loreFormat)
                .setMob(type.getEntity())
                .addNBTTag("amount", amount)
                .addNBTTag("name", name)
                .addNBTTag("iscustom", isCustom)
                .toItemStack();
    }

    @SneakyThrows
    public String getName(ItemStack item) {
        Object nmsItem = invokeStatic(asNMSCopy, item);
        Object compound = invoke(getTag, nmsItem);
        Object name = getString.invoke(compound,"name");
        return name.toString();
    }

    @SneakyThrows
    public long getAmount(ItemStack item) {
        Object nmsItem = invokeStatic(asNMSCopy, item);
        Object compound = invoke(getTag, nmsItem);
        Object amount = getString.invoke(compound,"amount");
        return Long.parseLong(amount.toString());
    }

    @SneakyThrows
    public boolean isCustom(ItemStack item) {
        Object nmsItem = invokeStatic(asNMSCopy, item);
        Object compound = invoke(getTag, nmsItem);
        Object isCustom = getString.invoke(compound,"iscustom");
        return Boolean.parseBoolean(isCustom.toString());
    }

    public MobType getMob(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        BlockState state = ((BlockStateMeta) meta).getBlockState();
        EntityType entity = ((CreatureSpawner) state).getSpawnedType();
        return MobType.getMob(entity);
    }

    public SpawnerArgument getNearestSpawner(Location loc, String customName) {
        String initialLocation = serialize(loc);

        boolean containsSpawner = spawn.getSpawners().containsKey(initialLocation);
        if (containsSpawner) return spawn.getSpawners().get(initialLocation);

        int radius = config.getConfig("Stack-Spawner.Radius");
        Location locMin = loc.clone().subtract(radius, radius, radius);
        Location locMax = loc.clone().add(radius, radius, radius);

        for (double x = locMin.getX(); x <= locMax.getX(); x++) { for (double y = locMin.getY(); y <= locMax.getY(); y++) { for (double z = locMin.getZ(); z <= locMax.getZ(); z++) {
            Location radiusLocation = new Location(loc.getWorld(), x, y, z);
            String location = serialize(radiusLocation);

            containsSpawner = spawn.getSpawners().containsKey(location);
            if (!containsSpawner) continue;

            SpawnerArgument spawnerArg = spawn.getSpawners().get(location);
            String mobName;

            if (spawnerArg instanceof Spawner) mobName = spawnerArg.getType().getName();
            else mobName = ((SpawnerCustom) spawnerArg).getName();

            if (mobName.equalsIgnoreCase(customName)) return spawnerArg;
        } } }
        return null;
    }

    public void logPlace(Player p, String location, String mobName, long amount) {
        try {
            String formatted = config.formatDecimal(amount);
            String message = "[PLACE] " + p.getName() + " placed " + formatted + " spawners " + mobName + " on location " + location;
            config.putLogSpawner(message);
        } catch (IOException e) {
            System.err.printf("An error occurred while saving the player log %s of type PLACE: %s", p.getName(), e.getMessage());
        }
    }

    public void logBreak(Player p, String location, String mobName, long amount) {
        try {
            String formatted = config.formatDecimal(amount);
            String message = "[BREAK] " + p.getName() + " broken " + formatted + " spawners " + mobName + " on location " + location;
            config.putLogSpawner(message);
        } catch (IOException e) {
            System.err.printf("An error occurred while saving the player log %s of type BREAK: %s", p.getName(), e.getMessage());
        }
    }

    public void logSilk(Player p, String location, String mobName, long amount) {
        try {
            String formatted = config.formatDecimal(amount);
            String message = "[SILKTOUCH] " + p.getName() + " broken " + formatted + " spawners " + mobName + " without SilkTouch on location " + location;
            config.putLogSpawner(message);
        } catch (IOException e) {
            System.err.printf("An error occurred while saving the player log %s of type SILKTOUCH: %s", p.getName(), e.getMessage());
        }
    }
}
