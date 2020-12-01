package com.redeceleste.celestespawners.listener;

import com.redeceleste.celestespawners.CelesteSpawners;
import com.redeceleste.celestespawners.builder.SpawnerEventBuilder;
import com.redeceleste.celestespawners.event.impl.BreakSpawnerEvent;
import com.redeceleste.celestespawners.factory.SpawnerFactory;
import com.redeceleste.celestespawners.manager.ConfigManager;
import com.redeceleste.celestespawners.manager.impl.SpawnerCustomManager;
import com.redeceleste.celestespawners.manager.impl.SpawnerManager;
import com.redeceleste.celestespawners.model.SpawnerArgument;
import com.redeceleste.celestespawners.model.impl.Spawner;
import com.redeceleste.celestespawners.model.impl.SpawnerCustom;
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
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import static com.redeceleste.celestespawners.util.LocationUtil.serialize;
import static org.bukkit.Material.AIR;
import static org.bukkit.Material.MOB_SPAWNER;
import static org.bukkit.Sound.valueOf;
import static org.bukkit.enchantments.Enchantment.SILK_TOUCH;

public class BlockBreakListener implements Listener {
    private final CelesteSpawners main;
    private final SpawnerFactory spawn;
    private final ConfigManager config;
    private final SpawnerManager spawner;
    private final SpawnerCustomManager spawnerCustom;
    private final ChatUtil chat;
    private final BarUtil bar;
    private final TitleUtil title;

    public BlockBreakListener(CelesteSpawners main) {
        this.main = main;
        this.spawn = main.getSpawnerFactory();
        this.config = main.getConfigManager();
        this.spawner = main.getSpawnerFactory().getSpawner();
        this.spawnerCustom = main.getSpawnerFactory().getSpawnerCustom();
        this.chat = main.getMessageFactory().getChat();
        this.title = main.getMessageFactory().getTitle();
        this.bar = main.getMessageFactory().getBar();
        main.getServer().getPluginManager().registerEvents(this, main);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        Block block = e.getBlock();

        if (block.getType() != MOB_SPAWNER) return;

        Location loc = block.getLocation();
        String location = serialize(loc);

        if (!spawn.getSpawners().containsKey(location)) return;

        ItemStack item = p.getItemInHand();
        boolean onlySilkTouch = config.getConfig("Spawner.Silk-Touch.Use");
        boolean useSilkTouch = true;

        if (onlySilkTouch) {
            if (item.getType() == AIR) useSilkTouch = false;
            else if (!item.containsEnchantment(SILK_TOUCH)) useSilkTouch = false;
        }

        SpawnerArgument spawnerArg = spawn.getSpawners().get(location);
        MobType type = spawnerArg.getType();
        e.setCancelled(true);

        if (spawnerArg instanceof Spawner) {
            new SpawnerEventBuilder()
                    .player(p)
                    .block(block)
                    .type(type)
                    .isCustom(false)
                    .useSilkTouch(useSilkTouch)
                    .buildBreak();
        } else new SpawnerEventBuilder()
                .player(p)
                .block(block)
                .type(type)
                .isCustom(true)
                .useSilkTouch(useSilkTouch)
                .buildBreak();
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBreakSpawner(BreakSpawnerEvent e) {
        Player p = e.getPlayer();
        Block block = e.getBlock();

        Sound soundBreak = valueOf(config.getConfig("Sounds.Break-Spawner"));
        Sound soundSilk = valueOf(config.getConfig("Sounds.Without-Silk-Touch"));

        MobType type = e.getType();
        Location loc = block.getLocation();

        String location = serialize(loc);
        boolean isCustom = e.getIsCustom();
        boolean useSilkTouch = e.getUseSilkTouch();
        boolean isRemoveStacked = config.getConfig("Spawner.Silk-Touch.Remove-Stacked");

        SpawnerArgument spawnerArg = spawn.getSpawners().get(location);
        long amount = spawnerArg.getAmount();
        long totalAmount = 0L;

        if (!isCustom) {
            String name = type.getName();

            if (!p.isSneaking()) {
                if (!useSilkTouch) {
                    if (amount == 1) {
                        block.breakNaturally();
                        spawner.deleteSpawner(spawnerArg);
                    } else {
                        spawner.removeSpawner(spawnerArg);
                        totalAmount = spawnerArg.getAmount();
                    }

                    p.playSound(loc, soundSilk, 1, 1);
                    chat.send(p, "Break.Fail-Singular",
                            chat.build("{totalamount}", totalAmount),
                            chat.build("{type}", name));
                    bar.send(p, "Break.Fail-Singular-Bar",
                            chat.build("{totalamount}", totalAmount),
                            chat.build("{type}", name));
                    title.send(p, "Break.Fail-Singular-Title",
                            chat.build("{totalamount}", totalAmount),
                            chat.build("{type}", name));

                    spawner.logSilk(p, location, name, 1L);
                    return;
                }

                if (amount == 1) {
                    block.breakNaturally();
                    spawner.deleteSpawner(spawnerArg);
                } else {
                    spawner.removeSpawner(spawnerArg);
                    totalAmount = spawnerArg.getAmount();
                }

                ItemStack itemStack = spawner.getSpawner(1L, type);
                p.getInventory().addItem(itemStack);
                p.playSound(loc, soundBreak, 1, 1);
                chat.send(p, "Break.Success-Singular",
                        chat.build("{totalamount}", totalAmount),
                        chat.build("{type}", name));
                bar.send(p, "Break.Success-Singular-Bar",
                        chat.build("{totalamount}", totalAmount),
                        chat.build("{type}", name));
                title.send(p, "Break.Success-Singular-Title",
                        chat.build("{totalamount}", totalAmount),
                        chat.build("{type}", name));

                spawner.logBreak(p, location, name, 1L);
                return;
            }

            if (!useSilkTouch) {
                if (isRemoveStacked || amount == 1) {
                    block.breakNaturally();
                    spawner.deleteSpawner(spawnerArg);
                } else {
                    spawner.removeSpawner(spawnerArg);
                    totalAmount = spawnerArg.getAmount();
                    amount = 1L;
                }

                p.playSound(loc, soundSilk, 1, 1);
                chat.send(p, "Break.Fail",
                        chat.build("{amount}", amount),
                        chat.build("{totalamount}", totalAmount),
                        chat.build("{type}", name));
                bar.send(p, "Break.Fail-Bar",
                        chat.build("{amount}", amount),
                        chat.build("{totalamount}", totalAmount),
                        chat.build("{type}", name));
                title.send(p, "Break.Fail-Title",
                        chat.build("{amount}", amount),
                        chat.build("{totalamount}", totalAmount),
                        chat.build("{type}", name));

                spawner.logSilk(p, location, name, amount);
                return;
            }

            block.breakNaturally();
            spawner.deleteSpawner(spawnerArg);

            ItemStack itemStack = spawner.getSpawner(amount, type);
            p.getInventory().addItem(itemStack);
            p.playSound(loc, soundBreak, 1, 1);
            chat.send(p, "Break.Success",
                    chat.build("{amount}", amount),
                    chat.build("{totalamount}", totalAmount),
                    chat.build("{type}", name));
            bar.send(p, "Break.Success-Bar",
                    chat.build("{amount}", amount),
                    chat.build("{totalamount}", totalAmount),
                    chat.build("{type}", name));
            title.send(p, "Break.Success-Title",
                    chat.build("{amount}", amount),
                    chat.build("{totalamount}", totalAmount),
                    chat.build("{type}", name));

            spawner.logBreak(p, location, name, amount);
            return;
        }

        String name = ((SpawnerCustom) spawnerArg).getName();
        String fileName = spawnerCustom.getFileName(name);

        if (!p.isSneaking()) {
            if (!useSilkTouch) {
                if (amount == 1) {
                    block.breakNaturally();
                    spawnerCustom.deleteSpawner(spawnerArg);
                } else {
                    spawnerCustom.removeSpawner(spawnerArg);
                    totalAmount = spawnerArg.getAmount();
                }

                p.playSound(loc, soundSilk, 1, 1);

                chat.send(p, "Break.Fail-Singular",
                        chat.build("{totalamount}", totalAmount),
                        chat.build("{type}", name));
                bar.send(p, "Break.Fail-Singular-Bar",
                        chat.build("{totalamount}", totalAmount),
                        chat.build("{type}", name));
                title.send(p, "Break.Fail-Singular-Title",
                        chat.build("{totalamount}", totalAmount),
                        chat.build("{type}", name));

                spawner.logSilk(p, location, name, 1L);
                return;
            }

            if (amount == 1) {
                block.breakNaturally();
                spawnerCustom.deleteSpawner(spawnerArg);
            } else {
                spawnerCustom.removeSpawner(spawnerArg);
                totalAmount = spawnerArg.getAmount();
            }

            ItemStack itemStack = spawnerCustom.getSpawner(fileName, 1L, type);
            p.getInventory().addItem(itemStack);
            p.playSound(loc, soundBreak, 1, 1);
            chat.send(p, "Break.Success-Singular",
                    chat.build("{totalamount}", totalAmount),
                    chat.build("{type}", name));
            bar.send(p, "Break.Success-Singular-Bar",
                    chat.build("{totalamount}", totalAmount),
                    chat.build("{type}", name));
            title.send(p, "Break.Success-Singular-Title",
                    chat.build("{totalamount}", totalAmount),
                    chat.build("{type}", name));

            spawner.logBreak(p, location, name, 1L);
            return;
        }

        if (!useSilkTouch) {
            if (isRemoveStacked || amount == 1) {
                block.breakNaturally();
                spawnerCustom.deleteSpawner(spawnerArg);
            } else {
                spawnerCustom.removeSpawner(spawnerArg);
                totalAmount = spawnerArg.getAmount();
                amount = 1L;
            }

            p.playSound(loc, soundSilk, 1, 1);
            chat.send(p, "Break.Fail",
                    chat.build("{amount}", amount),
                    chat.build("{totalamount}", totalAmount),
                    chat.build("{type}", name));
            bar.send(p, "Break.Fail-Bar",
                    chat.build("{amount}", amount),
                    chat.build("{totalamount}", totalAmount),
                    chat.build("{type}", name));
            title.send(p, "Break.Fail-Title",
                    chat.build("{amount}", amount),
                    chat.build("{totalamount}", totalAmount),
                    chat.build("{type}", name));

            spawner.logSilk(p, location, name, amount);
            return;
        }

        block.breakNaturally();
        spawnerCustom.deleteSpawner(spawnerArg);

        ItemStack itemStack = spawnerCustom.getSpawner(fileName, amount, type);
        p.getInventory().addItem(itemStack);
        p.playSound(loc, soundBreak, 1, 1);
        chat.send(p, "Break.Success",
                chat.build("{amount}", amount),
                chat.build("{totalamount}", totalAmount),
                chat.build("{type}", name));
        bar.send(p, "Break.Success-Bar",
                chat.build("{amount}", amount),
                chat.build("{totalamount}", totalAmount),
                chat.build("{type}", name));
        title.send(p, "Break.Success-Title",
                chat.build("{amount}", amount),
                chat.build("{totalamount}", totalAmount),
                chat.build("{type}", name));

        spawner.logBreak(p, location, name, amount);
    }
}
