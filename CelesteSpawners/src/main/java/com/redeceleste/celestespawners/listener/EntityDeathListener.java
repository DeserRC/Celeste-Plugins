package com.redeceleste.celestespawners.listener;

import com.redeceleste.celestespawners.CelesteSpawners;
import com.redeceleste.celestespawners.builder.EntityBuilder;
import com.redeceleste.celestespawners.event.impl.PlayerKilledEntityEvent;
import com.redeceleste.celestespawners.manager.ConfigManager;
import com.redeceleste.celestespawners.manager.EntityManager;
import com.redeceleste.celestespawners.manager.impl.SpawnerCustomManager;
import com.redeceleste.celestespawners.type.MobType;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.redeceleste.celestespawners.type.MobType.getDrop;
import static com.redeceleste.celestespawners.type.MobType.getMob;
import static org.bukkit.enchantments.Enchantment.LOOT_BONUS_MOBS;

public class EntityDeathListener implements Listener {
    private final CelesteSpawners main;
    private final ConfigManager config;
    private final SpawnerCustomManager spawnerCustom;
    private final EntityManager entity;

    public EntityDeathListener(CelesteSpawners main) {
        this.main = main;
        this.config = main.getConfigManager();
        this.spawnerCustom = main.getSpawnerFactory().getSpawnerCustom();
        this.entity = main.getSpawnerFactory().getEntity();
        main.getServer().getPluginManager().registerEvents(this, main);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        LivingEntity livingEntity = e.getEntity();
        if (livingEntity.getCustomName() == null) return;
        if (livingEntity.getCustomName().isEmpty()) return;
        if (!livingEntity.isCustomNameVisible()) return;
        e.setDroppedExp(0);
        e.getDrops().clear();
        if (!(livingEntity.getKiller() instanceof Player)) return;

        String fileName = entity.getName(livingEntity);
        long amount = entity.getAmount(livingEntity);
        boolean isCustom = entity.isCustom(livingEntity);

        if (fileName == null || amount == 0L) return;
        new EntityBuilder()
                .entity(livingEntity)
                .name(fileName)
                .amount(amount)
                .isCustom(isCustom).build();
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerKilledEntityEvent(PlayerKilledEntityEvent e) {
        LivingEntity livingEntity = e.getEntity();
        String fileName = e.getName();
        long amount = e.getAmount();
        boolean isCustom = e.getIsCustom();

        Player p = livingEntity.getKiller();
        int looting = 1;
        Location loc = livingEntity.getLocation();

        if (p.getItemInHand().containsEnchantment(LOOT_BONUS_MOBS)) {
            looting = p.getItemInHand().getEnchantmentLevel(LOOT_BONUS_MOBS) + 1;
        }

        Random random = new Random();
        double multiply = config.getConfig("Spawner.Looting-Multiplier");
        int finalAmount = (int) (looting * multiply * amount);
        List<ItemStack> drops = new ArrayList<>();

        if (isCustom) {
            Map<ItemStack, Integer> drop = spawnerCustom.getDrops(fileName);
            for (ItemStack item : drop.keySet()) {
                int chance = drop.get(item);
                int number = random.nextInt(100) + 1;

                if (chance < number) continue;
                ItemStack finalItem = item.clone();
                finalItem.setAmount(finalAmount);
                drops.add(finalItem);
            }
        } else {
            MobType type = getMob(fileName);
            drops = getDrop(type, finalAmount);
        }

        for (ItemStack item : drops) {
            loc.getBlock().getWorld().dropItemNaturally(loc, item);
        }
    }
}
