package com.redeceleste.celestespawners.api;

import com.redeceleste.celestespawners.CelesteSpawners;
import com.redeceleste.celestespawners.event.impl.EntitySpawnEvent;
import com.redeceleste.celestespawners.event.impl.PlayerKilledEntityEvent;
import com.redeceleste.celestespawners.exception.SpawnerNotFoundException;
import com.redeceleste.celestespawners.factory.SpawnerFactory;
import com.redeceleste.celestespawners.manager.impl.SpawnerCustomManager;
import com.redeceleste.celestespawners.manager.impl.SpawnerManager;
import com.redeceleste.celestespawners.model.SpawnerArgument;
import com.redeceleste.celestespawners.type.MobType;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import static com.redeceleste.celestespawners.type.MobType.*;
import static com.redeceleste.celestespawners.util.LocationUtil.serialize;

/**
 * The plugin also has some events
 * that can be heard with a listener
 *
 * Called when the player breaks a spawner
 * @see BlockBreakEvent
 * Called when the player place a spawner
 * @see BlockPlaceEvent
 * Called when an mobs is spawn
 * @see EntitySpawnEvent
 * Called when the player killed an mob
 * @see PlayerKilledEntityEvent
 *
 * Stack:
 * If you do not want an item to be stacked
 * use blacklist in metadata
 */
public class SpawnerAPI {
    private static final CelesteSpawners main = CelesteSpawners.getInstance();
    private static final SpawnerFactory spawn = main.getSpawnerFactory();
    private static final SpawnerManager spawner = main.getSpawnerFactory().getSpawner();
    private static final SpawnerCustomManager spawnerCustom = main.getSpawnerFactory().getSpawnerCustom();

    /**
     * Will create a spawner at the location
     *
     * @param location Location
     * @param amount Amount of spawners
     * @param entity The spawner entity
     */
    public void createSpawner(Location location, long amount, EntityType entity) {
        String loc = serialize(location);
        MobType type = getMob(entity);
        spawner.createSpawner(loc, amount, type);
    }

    /**
     * Will create a custom spawner at the location
     *
     * @param location Location
     * @param name Mob name
     * @param amount Amount of spawners
     * @throws SpawnerNotFoundException If the spawner does not exist
     */
    public void createCustomSpawner(Location location, String name, long amount) throws SpawnerNotFoundException {
        String loc = serialize(location);
        String spawnerFileName = spawnerCustom.getFileName(name);
        if (spawnerFileName == null) throw new SpawnerNotFoundException("This spawner not exist");
        spawnerCustom.createSpawner(loc, spawnerFileName, amount);
    }

    /**
     * Will delete the spawner
     *
     * @param spawnerArg an instance {@code SpawnerArgument}
     */
    public void deleteSpawner(SpawnerArgument spawnerArg) {
        spawner.deleteSpawner(spawnerArg);
    }

    /**
     * @param location Location
     * @return an instance {@code SpawnerArgument}
     * @throws SpawnerNotFoundException If the spawner does not exist
     */
    public SpawnerArgument getSpawnerArgument(Location location) throws SpawnerNotFoundException {
        String loc = serialize(location);
        boolean containsSpawner = spawn.getSpawners().containsKey(loc);
        if (!containsSpawner) throw new SpawnerNotFoundException("This spawner not exist");
        return spawn.getSpawners().get(loc);
    }

    /**
     * @param location Location
     * @param name The name of spawner or the name of custom spawner
     * @return an instance {@code SpawnerArgument}
     * @throws SpawnerNotFoundException If no spawner is found
     */
    public SpawnerArgument getNearestSpawner(Location location, String name) throws SpawnerNotFoundException {
        SpawnerArgument spawnerArg = spawner.getNearestSpawner(location, name);
        if (spawnerArg == null) throw new SpawnerNotFoundException("Not found spawners");
        return spawnerArg;
    }

    /**
     * @param amount Amount of spawners
     * @param entity The spawner entity
     * @return The Spawner in item
     * @throws SpawnerNotFoundException If the spawner does not exist
     */
    public ItemStack getSpawner(long amount, EntityType entity) throws SpawnerNotFoundException {
        MobType type = getMob(entity);
        ItemStack item = spawner.getSpawner(amount, type);
        if (item == null) throw new SpawnerNotFoundException("Not found spawners");
        return item;
    }

    /**
     * @param name Mob name
     * @param amount Amount of spawners
     * @param entity The spawner entity
     * @return The custom spawner in item
     * @throws SpawnerNotFoundException If the spawner does not exist
     */
    public ItemStack getCustomSpawner(String name, long amount, EntityType entity) throws SpawnerNotFoundException {
        String spawnerFileName = spawnerCustom.getFileName(name);
        MobType type = getMob(entity);
        ItemStack item = spawnerCustom.getSpawner(spawnerFileName, amount, type);
        if (item == null) throw new SpawnerNotFoundException("Not found spawners");
        return item;
    }
}