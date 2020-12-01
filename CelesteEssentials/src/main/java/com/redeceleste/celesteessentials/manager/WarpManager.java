package com.redeceleste.celesteessentials.manager;

import com.redeceleste.celesteessentials.CelesteEssentials;
import com.redeceleste.celesteessentials.util.impl.BarUtil;
import com.redeceleste.celesteessentials.util.impl.ChatUtil;
import com.redeceleste.celesteessentials.util.impl.TitleUtil;
import lombok.SneakyThrows;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicInteger;

import static com.redeceleste.celesteessentials.util.LocationUtil.deserialize;
import static com.redeceleste.celesteessentials.util.LocationUtil.serialize;
import static java.util.Arrays.asList;
import static java.util.concurrent.TimeUnit.SECONDS;

public class WarpManager {
    private final CelesteEssentials main;
    private final ConfigManager config;
    private final ChatUtil chat;
    private final BarUtil bar;
    private final TitleUtil title;
    private ScheduledFuture<?> future;

    public WarpManager(CelesteEssentials main) {
        this.main = main;
        this.config = main.getConfigManager();
        this.chat = main.getMessageFactory().getChat();
        this.bar = main.getMessageFactory().getBar();
        this.title = main.getMessageFactory().getTitle();
    }

    @SneakyThrows
    public void createWarp(String name, Location loc, String permission) {
        List<String> lore = asList("&7Clicking will be teleported", "&7For the selected warp");
        List<String> en = asList("Sharpness:5", "Unbreaking:5");

        name = name.toLowerCase();
        config.getWarp().set(name + ".Location", serialize(loc, true));
        config.getWarp().set(name + ".Permission", permission);

        config.getWarp().set(name + ".Message.Use", false);
        config.getWarp().set(name + ".Message.Message", "&7You have been teleported to warp " + name.toUpperCase());

        config.getWarp().set(name + ".Message-Bar.Use", true);
        config.getWarp().set(name + ".Message-Bar.Message", "&7Teleported to warp " + name.toUpperCase());

        config.getWarp().set(name + ".Message-Title.Use", true);
        config.getWarp().set(name + ".Message-Title.Title", "&7Warp &6" + name.toUpperCase());
        config.getWarp().set(name + ".Message-Title.SubTitle", "&7Here you will find many things");

        config.getWarp().set(name + ".Inventory.Slot", 0);
        config.getWarp().set(name + ".Inventory.Amount", 1);
        config.getWarp().set(name + ".Inventory.Material", "STONE");
        config.getWarp().set(name + ".Inventory.Data", 0);
        config.getWarp().set(name + ".Inventory.Name", "&cChange Config");
        config.getWarp().set(name + ".Inventory.Glow", false);
        config.getWarp().set(name + ".Inventory.Lore", lore);
        config.getWarp().set(name + ".Inventory.Enchantment", en);
        config.getWarp().set(name + ".Inventory.Head.Use", false);
        config.getWarp().set(name + ".Inventory.Head.Texture", "");
        config.reloadWarp();
    }

    public void deleteWarp(String name) {
        if (!config.contains(name.toLowerCase(), config.getWarp())) return;
        config.getWarp().set(name.toLowerCase(), null);
        config.reloadWarp();
    }

    public Boolean teleportPlayer(Player p, String name) {
        if (!warpExist(name)) return null;
        if (!havePermission(p, name)) return false;

        String perm = config.getConfig("Permission.Admin");
        String bypassPerm = config.getConfig("Permission.Teleport-Bypass-Delay");
        if (!p.hasPermission(perm) && !p.hasPermission(bypassPerm)) {
            Sound soundDelay = Sound.valueOf(config.getConfig("Sounds.Delay"));
            Sound soundMove = Sound.valueOf(config.getConfig("Sounds.Move"));

            AtomicInteger delay = new AtomicInteger(config.getConfig("Teleport.Delay"));
            boolean cancelOnMove = config.getConfig("Teleport.Cancel-On-Move");
            Location loc = p.getLocation();

            main.getScheduled().scheduleAtFixedRate(() -> {
                Location newLoc = p.getLocation();
                if (cancelOnMove) if (loc.getX() != newLoc.getX() || loc.getY() != newLoc.getY() || loc.getZ() != newLoc.getZ() || loc.getWorld() != newLoc.getWorld()) {
                        chat.send(p, "Warp.Move");
                        bar.send(p, "Warp.Move-Bar");
                        title.send(p, "Warp.Move-Title");
                        p.playSound(p.getLocation(), soundMove, 1,1);
                        future.cancel(true);
                }

                chat.send(p, "Warp.Delay",
                        chat.build("{time}", delay));
                bar.send(p, "Warp.Delay-Bar",
                        chat.build("{time}", delay));
                title.send(p, "Warp.Delay-Title",
                        chat.build("{time}", delay));

                if (delay.getAndDecrement() == 0) {
                    Sound soundTeleport = Sound.valueOf(config.getConfig("Sounds.Teleport"));
                    p.teleport(getWarp(name));
                    p.playSound(p.getLocation(), soundTeleport, 1, 1);

                    chat.send(p, name.toLowerCase() + ".Message", config.getWarp());
                    bar.send(p, name.toLowerCase() + ".Message-Bar", config.getWarp());
                    title.send(p, name.toLowerCase() + ".Message-Title", config.getWarp());
                    future.cancel(true);
                }
                p.playSound(p.getLocation(), soundDelay, 1,1);
            }, 0,1, SECONDS);
        } else {
            Sound soundTeleport = Sound.valueOf(config.getConfig("Sounds.Teleport"));
            p.teleport(getWarp(name));
            p.playSound(p.getLocation(), soundTeleport, 1, 1);

            chat.send(p, name.toLowerCase() + ".Message", config.getWarp());
            bar.send(p, name.toLowerCase() + ".Message-Bar", config.getWarp());
            title.send(p, name.toLowerCase() + ".Message-Title", config.getWarp());
            return true;
        }
        return true;
    }

    public Location getWarp(String name) {
        if (!warpExist(name)) return null;
        String location = config.getWarp(name.toLowerCase() + ".Location");
        return deserialize(location);
    }

    public Boolean warpExist(String name) {
        return config.contains(name.toLowerCase(), config.getWarp());
    }

    private Boolean havePermission(Player p, String name) {
        if (!warpExist(name)) return null;
        String permission = config.getWarp(name.toLowerCase() + ".Permission");
        return permission.equals("") || p.hasPermission(permission);
    }
}