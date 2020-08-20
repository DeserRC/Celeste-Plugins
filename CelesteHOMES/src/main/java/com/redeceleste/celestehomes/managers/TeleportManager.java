package com.redeceleste.celestehomes.managers;

import com.redeceleste.celestehomes.Main;
import com.redeceleste.celestehomes.events.TeleportEvent;
import com.redeceleste.celestehomes.utils.ActionBar;
import com.redeceleste.celestehomes.utils.SendTitle;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class TeleportManager {
    public static HashMap<Player, Long> cd = new HashMap<>();
    private static Location pos1, pos2;
    private static Integer delay;

    public static void teleportPlayer(Player p, String loc, String name) {
        if (PermissionManager.hasDelayOtherTeleportBypass(p)) {
            TeleportEvent.teleport(p, name, loc);
            return;
        }

        if (cd.containsKey(p)) {
            if (cd.get(p) >= System.currentTimeMillis()) {
                ActionBar.sendActionBarMessage(p, ConfigManager.DelayFromOtherTeleportMessage.replace("%%delay%", String.valueOf(cd.get(p) - System.currentTimeMillis())));
                return;
            } else {
                cd.remove(p);
            }
        }

        if (PermissionManager.hasDelayBypass(p)) {
            TeleportEvent.teleport(p, name, loc);
            return;
        } else {
            delay = Integer.parseInt(ConfigManager.Delay);
            pos1 = p.getLocation();
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                if (delay == 0) {
                    TeleportEvent.teleport(p, name, loc);
                    this.cancel();
                } else {
                    SendTitle.sendFullTitle(p, 2,2,2, ConfigManager.MessageWaitingTeleportTitle.replace("%delay%", String.valueOf(delay)), ConfigManager.MessageWaitingTeleportSubTitle.replace("%delay%", String.valueOf(delay)));
                    p.playSound(p.getLocation(), ConfigManager.SoundWaitingTeleport, 1, 1);
                    delay--;
                }
                pos2 = p.getLocation();
                if (pos1.getX() != pos2.getX() || pos1.getY() != pos2.getY() || pos1.getZ() != pos2.getZ() || pos1.getWorld() != pos2.getWorld()) {
                    SendTitle.sendFullTitle(p, 2,2,2, ConfigManager.MessageCancelTeleportTitle, ConfigManager.MessageCancelTeleportSubTitle);
                    p.playSound(p.getLocation(), ConfigManager.SoundCancelTeleport, 1, 1);
                    this.cancel();
                }
            }
        }.runTaskTimerAsynchronously(Main.getInstance(), 0L, 20L);
    }
}