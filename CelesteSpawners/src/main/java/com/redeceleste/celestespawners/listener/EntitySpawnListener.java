package com.redeceleste.celestespawners.listener;

import com.redeceleste.celestespawners.CelesteSpawners;
import com.redeceleste.celestespawners.event.impl.EntitySpawnEvent;
import com.redeceleste.celestespawners.manager.ConfigManager;
import com.redeceleste.celestespawners.manager.EntityManager;
import com.redeceleste.celestespawners.model.SpawnerArgument;
import com.redeceleste.celestespawners.model.impl.Spawner;
import com.redeceleste.celestespawners.model.impl.SpawnerCustom;
import com.redeceleste.celestespawners.type.ArmorType;
import com.redeceleste.celestespawners.type.MobType;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Random;
import java.util.Set;

import static com.redeceleste.celestespawners.util.LocationUtil.deserialize;
import static org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.SPAWNER;

public class EntitySpawnListener implements Listener {
    private final CelesteSpawners main;
    private final ConfigManager config;
    private final EntityManager entity;

    public EntitySpawnListener(CelesteSpawners main) {
        this.main = main;
        this.config = main.getConfigManager();
        this.entity = main.getSpawnerFactory().getEntity();
        main.getServer().getPluginManager().registerEvents(this, main);
    }

    @EventHandler
    public void onCreateSpawn(CreatureSpawnEvent e){
        if (e.getSpawnReason() == SPAWNER) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityTarget(EntityTargetEvent e) {
        if (e.getEntity().getCustomName() == null) return;
        if (e.getEntity().getCustomName().isEmpty()) return;
        if (!e.getEntity().isCustomNameVisible()) return;
        e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onEntitySpawn(EntitySpawnEvent e) {
        SpawnerArgument spawnerArg = e.getSpawnerArg();

        String location = spawnerArg.getLocation();
        long amount = spawnerArg.getAmount();

        Location loc = deserialize(location);
        MobType type = spawnerArg.getType();

        Set<LivingEntity> nearestEntities = entity.getNearestEntity(loc, type.getEntity());

        for (LivingEntity nearestEntity : nearestEntities) {
            String mobName;
            if (spawnerArg instanceof Spawner) {
                mobName = type.getName();
            } else mobName = ((SpawnerCustom) spawnerArg).getName();

            String nearestName = entity.getName(nearestEntity);
            if (nearestName == null) continue;
            if (nearestName.equalsIgnoreCase(mobName)) {
                entity.editName(nearestEntity, mobName, amount);
                return;
            }
        }

        boolean randomSpawn = config.getConfig("Spawner.Random-Spawn.Use");
        int x = 1, z = 0;
        if (randomSpawn) {
            int radius = config.getConfig("Spawner.Random-Spawn.Radius");
            Random random = new Random();
            do { x = (random.nextInt(radius * 2 + 1) - radius);
            } while (x == 0);

            do { z = (random.nextInt(radius * 2 + 1) - radius);
            } while (z == 0);
        }

        loc.add(x, 0, z);

        LivingEntity newEntity = (LivingEntity) loc.getWorld().spawnEntity(loc, type.getEntity());
        if (spawnerArg instanceof Spawner) {
            String mobName = type.getName();
            newEntity.getEquipment().clear();
            entity.setName(newEntity, mobName, amount, false);
            return;
        }

        Map<ArmorType, ItemStack> hash = ((SpawnerCustom) spawnerArg).getArmor();
        for (ArmorType armor : hash.keySet()) {
            ItemStack item = hash.get(armor);
            armor.apply(newEntity, item, armor);
        }

        String fileName = ((SpawnerCustom) spawnerArg).getName();
        entity.setName(newEntity, fileName, amount, true);
    }
}
