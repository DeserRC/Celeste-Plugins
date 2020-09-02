package com.redeceleste.celestehomes.manager;

import com.redeceleste.celestehomes.Main;
import com.redeceleste.celestehomes.util.impls.ActionBar;
import com.redeceleste.celestehomes.builder.LocationBuilder;
import com.redeceleste.celestehomes.util.impls.SendTitle;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class TeleportManager {
    public static HashMap<Player, Long> cd = new HashMap<>();
    private static Location pos1, pos2;
    private static Integer delay;

    public static void teleportPlayer(Player p, String loc, String name) {
        if (PermissionManager.hasDelayOtherTeleportBypass(p)) {
            teleport(p, name, loc);
            return;
        }

        if (cd.containsKey(p)) {
            if (cd.get(p) >= System.currentTimeMillis()) {
                ActionBar.sendMessage(p, ConfigManager.DelayFromOtherTeleportMessage.replace("%%delay%", String.valueOf(cd.get(p) - System.currentTimeMillis())));
                return;
            } else {
                cd.remove(p);
            }
        }

        if (PermissionManager.hasDelayBypass(p)) {
            teleport(p, name, loc);
            return;
        } else {
            delay = Integer.parseInt(ConfigManager.Delay);
            pos1 = p.getLocation();
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                if (delay == 0) {
                    teleport(p, name, loc);
                    this.cancel();
                } else {
                    SendTitle.sendTitle(p, ConfigManager.MessageWaitingTeleportTitle.replace("%delay%", String.valueOf(delay)), ConfigManager.MessageWaitingTeleportSubTitle.replace("%delay%", String.valueOf(delay)), 1,1,1);
                    p.playSound(p.getLocation(), ConfigManager.SoundWaitingTeleport, 1, 1);
                    delay--;
                }
                pos2 = p.getLocation();
                if (pos1.getX() != pos2.getX() || pos1.getY() != pos2.getY() || pos1.getZ() != pos2.getZ() || pos1.getWorld() != pos2.getWorld()) {
                    SendTitle.sendTitle(p, ConfigManager.MessageCancelTeleportTitle, ConfigManager.MessageCancelTeleportSubTitle,1,1,1);
                    p.playSound(p.getLocation(), ConfigManager.SoundCancelTeleport, 1, 1);
                    this.cancel();
                }
            }
        }.runTaskTimerAsynchronously(Main.getInstance(), 0L, 20L);
    }

    private static void teleport(Player p, String name, String loc) {
        TeleportManager.cd.put(p, System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(10));
        p.teleport(LocationBuilder.deserialize(loc));
        p.playSound(p.getLocation(), ConfigManager.SoundSucessTeleport, 1, 1);
        SendTitle.sendTitle(p, ConfigManager.MessageSucessTeleportTitle.replace("%home%", name).replace("%number%", HomeManager.numberHome(p, name)), ConfigManager.MessageSucessTeleportSubTitle.replace("%home%", name).replace("%number%", HomeManager.numberHome(p, name)),1,1,1);
    }
}