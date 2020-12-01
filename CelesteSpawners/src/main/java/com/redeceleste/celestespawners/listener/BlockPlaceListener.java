package com.redeceleste.celestespawners.listener;

import com.redeceleste.celestespawners.CelesteSpawners;
import com.redeceleste.celestespawners.builder.SpawnerEventBuilder;
import com.redeceleste.celestespawners.event.impl.PlaceSpawnerEvent;
import com.redeceleste.celestespawners.manager.ConfigManager;
import com.redeceleste.celestespawners.manager.impl.SpawnerCustomManager;
import com.redeceleste.celestespawners.manager.impl.SpawnerManager;
import com.redeceleste.celestespawners.model.SpawnerArgument;
import com.redeceleste.celestespawners.type.MobType;
import com.redeceleste.celestespawners.util.impl.BarUtil;
import com.redeceleste.celestespawners.util.impl.ChatUtil;
import com.redeceleste.celestespawners.util.impl.TitleUtil;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static com.redeceleste.celestespawners.util.LocationUtil.serialize;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.bukkit.Material.MOB_SPAWNER;
import static org.bukkit.Sound.valueOf;

public class BlockPlaceListener implements Listener {
    private final CelesteSpawners main;
    private final ConfigManager config;
    private final SpawnerManager spawner;
    private final SpawnerCustomManager spawnerCustom;
    private final ChatUtil chat;
    private final BarUtil bar;
    private final TitleUtil title;

    public BlockPlaceListener(CelesteSpawners main) {
        this.main = main;
        this.config = main.getConfigManager();
        this.spawner = main.getSpawnerFactory().getSpawner();
        this.spawnerCustom = main.getSpawnerFactory().getSpawnerCustom();
        this.chat = main.getMessageFactory().getChat();
        this.title = main.getMessageFactory().getTitle();
        this.bar = main.getMessageFactory().getBar();
        main.getServer().getPluginManager().registerEvents(this, main);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        Block block = e.getBlock();
        if (block.getType() != MOB_SPAWNER) return;

        String adminPerm = config.getConfig("Permission.Admin");
        String world = block.getWorld().getName();
        List<String> blackList = config.getListConfig("Black-List-World");
        if (blackList.contains(world) && !p.hasPermission(adminPerm)) {
            chat.send(p, "Place.Black-List",
                    chat.build("{world}", world));
            bar.send(p, "Place.Black-List-Bar",
                    chat.build("{world}", world));
            title.send(p, "Place.Black-List-Title",
                    chat.build("{world}", world));
            e.setCancelled(true);
            return;
        }

        if (spawner.containsCooldown(p.getName().toLowerCase())) {
            long time = spawner.getCooldown(p.getName()) - System.currentTimeMillis();
            if (time > 0) {
                String convert = String.valueOf(MILLISECONDS.toSeconds(time) + 1);
                chat.send(p, "Place.Cooldown",
                        chat.build("{time}", convert));
                bar.send(p, "Place.Cooldown-Bar",
                        chat.build("{time}", convert));
                title.send(p, "Place.Cooldown-Title",
                        chat.build("{time}", convert));
                e.setCancelled(true);
                return;
            } else spawner.removeCooldown(p.getName());
        }

        String fileName;
        String perm = config.getConfig("Permission.Place-Spawner");
        ItemStack item = e.getItemInHand();
        MobType mob = spawner.getMob(item);
        boolean isCustom = spawner.isCustom(item);

        if (!isCustom) {
            perm = perm.replace("{type}", mob.getName());
            fileName = mob.getName();
        } else {
            perm = perm.replace("{type}", spawnerCustom.getFileName(item));
            fileName = spawnerCustom.getName(item);
        }

        if (!p.hasPermission(perm) && !p.hasPermission(adminPerm)) {
            chat.send(p, "No-Permission.Place-Spawner",
                    chat.build("{type}", fileName));
            e.setCancelled(true);
            return;
        }

        new SpawnerEventBuilder()
                .player(p)
                .block(block)
                .type(mob)
                .isCustom(isCustom)
                .buildPlace();
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlaceSpawner(PlaceSpawnerEvent e) {
        Player p = e.getPlayer();
        Block block = e.getBlock();

        Sound soundPlace = valueOf(config.getConfig("Sounds.Place-Spawner"));
        Sound soundStack = valueOf(config.getConfig("Sounds.Stack-Spawner"));

        MobType type = e.getType();
        ItemStack item = p.getItemInHand();
        Location loc = block.getLocation();

        String location = serialize(loc);
        long totalAmount = 1L;
        boolean isCustom = e.getIsCustom();

        if (!isCustom) {
            String name = type.getName();
            long amount = item.getAmount() * spawner.getAmount(item);

            if (!p.isSneaking()) {
                if (amount == 1) {
                    p.setItemInHand(null);
                } else {
                    ItemStack remainder = spawner.getSpawner(amount - 1, type);
                    p.setItemInHand(remainder);
                }

                spawner.addCooldown(p.getName());
                SpawnerArgument nearestSpawner = spawner.getNearestSpawner(loc, name);

                if (nearestSpawner == null) {
                    spawner.createSpawner(location, type);
                    p.playSound(loc, soundPlace,1,1);
                } else {
                    block.breakNaturally();
                    spawner.addSpawner(nearestSpawner);
                    p.playSound(loc, soundStack,1,1);
                    totalAmount = nearestSpawner.getAmount();
                }

                chat.send(p, "Place.Success-Singular",
                        chat.build("{totalamount}", totalAmount),
                        chat.build("{type}", name));
                bar.send(p, "Place.Success-Singular-Bar",
                        chat.build("{totalamount}", totalAmount),
                        chat.build("{type}", name));
                title.send(p, "Place.Success-Singular-Title",
                        chat.build("{totalamount}", totalAmount),
                        chat.build("{type}", name));

                spawner.logPlace(p, location, name, 1L);
                return;
            }

            p.setItemInHand(null);
            spawner.addCooldown(p.getName());
            SpawnerArgument nearestSpawner = spawner.getNearestSpawner(loc, name);

            if (nearestSpawner == null) {
                spawner.createSpawner(location, amount, type);
                p.playSound(loc, soundPlace,1,1);
                totalAmount = amount;
            } else {
                block.breakNaturally();
                spawner.addSpawner(nearestSpawner, amount);
                p.playSound(loc, soundStack,1,1);
                totalAmount = nearestSpawner.getAmount();
            }

            chat.send(p, "Place.Success",
                    chat.build("{amount}", amount),
                    chat.build("{totalamount}", totalAmount),
                    chat.build("{type}", name));
            bar.send(p, "Place.Success-Bar",
                    chat.build("{amount}", amount),
                    chat.build("{totalamount}", totalAmount),
                    chat.build("{type}", name));
            title.send(p, "Place.Success-Title",
                    chat.build("{amount}", amount),
                    chat.build("{totalamount}", totalAmount),
                    chat.build("{type}", name));

            spawner.logPlace(p, location, name, amount);
            return;
        }

        String name = spawnerCustom.getName(item);
        String fileName = spawnerCustom.getFileName(item);
        long amount = item.getAmount() * spawnerCustom.getAmount(item);

        if (!p.isSneaking()) {
            if (amount == 1) {
                p.setItemInHand(null);
            } else {
                ItemStack remainder = spawnerCustom.getSpawner(fileName, amount - 1, type);
                p.getInventory().setItemInHand(remainder);
            }

            spawner.addCooldown(p.getName());
            SpawnerArgument nearestSpawner = spawnerCustom.getNearestSpawner(loc, name);

            if (nearestSpawner == null) {
                spawnerCustom.createSpawner(location, fileName);
                p.playSound(loc, soundPlace,1,1);
            } else {
                block.breakNaturally();
                spawnerCustom.addSpawner(nearestSpawner);
                p.playSound(loc, soundStack,1,1);
                totalAmount = nearestSpawner.getAmount();
            }

            chat.send(p, "Place.Success-Singular",
                    chat.build("{totalamount}", totalAmount),
                    chat.build("{type}", name));
            bar.send(p, "Place.Success-Singular-Bar",
                    chat.build("{totalamount}", totalAmount),
                    chat.build("{type}", name));
            title.send(p, "Place.Success-Singular-Title",
                    chat.build("{totalamount}", totalAmount),
                    chat.build("{type}", name));

            spawnerCustom.logPlace(p, location, name, 1L);
            return;
        }

        p.setItemInHand(null);
        spawner.addCooldown(p.getName());
        SpawnerArgument nearestSpawner = spawnerCustom.getNearestSpawner(loc, name);

        if (nearestSpawner == null) {
            spawnerCustom.createSpawner(location, fileName, amount);
            p.playSound(loc, soundPlace,1,1);
            totalAmount = amount;
        } else {
            block.breakNaturally();
            spawnerCustom.addSpawner(nearestSpawner, amount);
            p.playSound(loc, soundStack,1,1);
            totalAmount = nearestSpawner.getAmount();
        }

        chat.send(p, "Place.Success",
                chat.build("{amount}", amount),
                chat.build("{totalamount}", totalAmount),
                chat.build("{type}", name));
        bar.send(p, "Place.Success-Bar",
                chat.build("{amount}", amount),
                chat.build("{totalamount}", totalAmount),
                chat.build("{type}", name));
        title.send(p, "Place.Success-Title",
                chat.build("{amount}", amount),
                chat.build("{totalamount}", totalAmount),
                chat.build("{type}", name));

        spawnerCustom.logPlace(p, location, name, amount);
    }
}
