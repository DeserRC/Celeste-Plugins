package com.redeceleste.celesteessentials.listener;

import com.redeceleste.celesteessentials.CelesteEssentials;
import com.redeceleste.celesteessentials.manager.ConfigManager;
import com.redeceleste.celesteessentials.manager.PowerToolManager;
import com.redeceleste.celesteessentials.manager.WarpManager;
import lombok.SneakyThrows;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static com.redeceleste.celesteessentials.util.ReflectionUtil.*;
import static org.bukkit.GameMode.SURVIVAL;
import static org.bukkit.Material.AIR;
import static org.bukkit.Material.EXP_BOTTLE;
import static org.bukkit.Sound.ORB_PICKUP;
import static org.bukkit.event.block.Action.*;
import static org.bukkit.event.entity.EntityDamageEvent.DamageCause.VOID;

public class UserListener implements Listener {
    private final CelesteEssentials main;
    private final ConfigManager config;
    private final WarpManager warp;
    private final PowerToolManager pt;

    private final Object packet;

    @SneakyThrows
    public UserListener(CelesteEssentials main) {
        this.main = main;
        this.config = main.getConfigManager();
        this.warp = main.getWarpManager();
        this.pt = main.getPowerToolManager();
        main.getServer().getPluginManager().registerEvents(this, main);

        Class<?> ppiccClass = getNMS("PacketPlayInClientCommand");
        Class eccClass = getNMS("PacketPlayInClientCommand$EnumClientCommand");

        Constructor<?> ppiccCon = getCon(ppiccClass, eccClass);

        Enum<?> respawn = Enum.valueOf(eccClass, "PERFORM_RESPAWN");

        packet = ppiccCon.newInstance(respawn);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        e.setJoinMessage(null);
        p.setGameMode(SURVIVAL);

        Location loc = warp.getWarp("spawn");
        if (loc != null) p.teleport(loc);

        boolean useMotd = config.getMessage("Motd.Success.Use");
        if (!useMotd) return;

        List<String> message = config.getListMessage("Motd.Success.Message");
        message = message.stream().map(line -> line
                .replace("{player}", p.getName()))
                .collect(Collectors.toList());

        message.forEach(p::sendMessage);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        e.setQuitMessage(null);
        pt.clearPlayer(p);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onKick(PlayerKickEvent e) {
        if (e.getReason().equals("disconnect.spam")) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onExpInteract(PlayerInteractEvent e) {
        if (e.getAction().equals(LEFT_CLICK_AIR) || e.getAction().equals(LEFT_CLICK_BLOCK)) return;

        Player p = e.getPlayer();
        ItemStack item = e.getItem();
        if (item == null || !item.getType().equals(EXP_BOTTLE)) return;
        e.setCancelled(true);

        if (!p.isSneaking()) {
            if (item.getAmount() == 1) {
                p.setItemInHand(new ItemStack(AIR));
            } else {
                item.setAmount(item.getAmount() - 1);
                p.setItemInHand(new ItemStack(item));
            }

            p.giveExp(new Random().nextInt(5) + 1);
            p.playSound(p.getLocation(), ORB_PICKUP, 1, 1);
            return;
        }

        while (item.getAmount() > 0) {
            item.setAmount(item.getAmount() - 1);
            p.giveExp(new Random().nextInt(5) + 1);
        }

        p.playSound(p.getLocation(), ORB_PICKUP, 1, 1);
        p.setItemInHand(new ItemStack(AIR));
    }

    @EventHandler
    public void onPowerToolInteract(PlayerInteractEvent e) {
        if (e.getAction().equals(LEFT_CLICK_BLOCK) || e.getAction().equals(RIGHT_CLICK_BLOCK)) return;
        Player p = e.getPlayer();
        ItemStack item = e.getItem();
        if (item == null || item.getType().equals(AIR)) return;
        if (!pt.contains(p, item.getType())) return;
        pt.sendCommand(p, item.getType());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        if (!e.getCause().equals(VOID)) return;
        e.setCancelled(true);
        Player p = (Player) e.getEntity();
        Location loc = warp.getWarp("spawn");
        p.teleport(loc);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        e.setDeathMessage(null);
        main.getServer().getScheduler().scheduleSyncDelayedTask(main, () -> {
            try {
                Player p = e.getEntity();
                Object handle = p.getClass().getMethod("getHandle").invoke(p);
                Object connection = handle.getClass().getField("playerConnection").get(handle);
                connection.getClass().getMethod("a", packet.getClass()).invoke(connection, packet);
            } catch (ReflectiveOperationException | RuntimeException ex) {
                ex.printStackTrace();
            }
        });
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Location loc = warp.getWarp("spawn");
        if (loc == null) return;
        e.setRespawnLocation(loc);
    }
}
