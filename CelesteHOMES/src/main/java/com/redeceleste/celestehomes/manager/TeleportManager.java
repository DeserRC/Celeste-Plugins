package com.redeceleste.celestehomes.manager;

import com.redeceleste.celestehomes.Main;
import com.redeceleste.celestehomes.event.TeleportEvent;
import com.redeceleste.celestehomes.util.impls.ActionBarUtil;
import com.redeceleste.celestehomes.util.impls.TitleUtil;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class TeleportManager {
    public static HashMap<String, Long> cd = new HashMap<>();
    private static Location pos1, pos2;
    private static Integer delay;

    public static void teleportPlayer(Player p, String loc, String name) {
        if (PermissionManager.hasDelayOtherTeleportBypass(p)) {
            TeleportEvent.teleport(p, name, loc);
            return;
        }

        if (cd.containsKey(p.getName())) {
            if (cd.get(p.getName()) >= System.currentTimeMillis()) {
                System.out.print("A" + cd.get(p.getName()));
                ActionBarUtil.sendMessage(p, ConfigManager.DelayFromOtherTeleportMessage
                        .replace("%%delay%", String.valueOf(cd.get(p.getName()) - System.currentTimeMillis())));
                return;
            } else {
                System.out.print("B");
                cd.remove(p.getName());
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
                System.out.print(delay);
                if (delay == 0) {
                    TeleportEvent.teleport(p, name, loc);
                    cancel();
                    return;
                } else {
                    TitleUtil.sendTitle(p, ConfigManager.MessageWaitingTeleportTitle
                            .replace("%delay%", String.valueOf(delay)), ConfigManager.MessageWaitingTeleportSubTitle
                            .replace("%delay%", String.valueOf(delay)), 1,1,1);
                    p.playSound(p.getLocation(), Sound.valueOf(ConfigManager.SoundWaitingTeleport), 1, 1);
                    delay--;
                }
                pos2 = p.getLocation();
                if (pos1.getX() != pos2.getX() || pos1.getY() != pos2.getY() || pos1.getZ() != pos2.getZ() || pos1.getWorld() != pos2.getWorld()) {
                    TitleUtil.sendTitle(p, ConfigManager.MessageCancelTeleportTitle, ConfigManager.MessageCancelTeleportSubTitle,1,1,1);
                    p.playSound(p.getLocation(), Sound.valueOf(ConfigManager.SoundCancelTeleport), 1, 1);
                    cancel();
                }
            }
        }.runTaskTimerAsynchronously(Main.getInstance(), 0L, 20L);
    }
}