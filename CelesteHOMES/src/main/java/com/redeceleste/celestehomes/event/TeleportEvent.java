package com.redeceleste.celestehomes.event;

import com.redeceleste.celestehomes.builder.LocationBuilder;
import com.redeceleste.celestehomes.manager.ConfigManager;
import com.redeceleste.celestehomes.manager.HomeManager;
import com.redeceleste.celestehomes.manager.TeleportManager;
import com.redeceleste.celestehomes.util.impls.TitleUtil;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public class TeleportEvent {
    public static void teleport(Player p, String name, String loc) {
        TeleportManager.cd.put(p.getName(), System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(Long.parseLong(ConfigManager.DelayFromOtherTeleport)));
        TitleUtil.sendTitle(p, ConfigManager.MessageSucessTeleportTitle
                .replace("%home%", name)
                .replace("%number%", HomeManager.numberHome(p, name)), ConfigManager.MessageSucessTeleportSubTitle
                .replace("%home%", name)
                .replace("%number%", HomeManager.numberHome(p, name)),1,1,1);
        p.teleport(LocationBuilder.deserialize(loc));
        p.playSound(p.getLocation(), ConfigManager.SoundSucessTeleport, 1, 1);
    }
}
