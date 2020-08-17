package com.redeceleste.celestehomes.events;

import com.redeceleste.celestehomes.managers.ConfigManager;
import com.redeceleste.celestehomes.managers.HomeManager;
import com.redeceleste.celestehomes.managers.TeleportManager;
import com.redeceleste.celestehomes.utils.LocationSerialize;
import com.redeceleste.celestehomes.utils.SendTitle;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public class TeleportEvent {
    public static void teleport(Player p, String name, String loc) {
        TeleportManager.cd.put(p, System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(10));
        p.teleport(LocationSerialize.deserialize(loc));
        p.playSound(p.getLocation(), ConfigManager.SoundSucessTeleport, 1, 1);
        SendTitle.sendFullTitle(p, 2,2,2, ConfigManager.MessageSucessTeleportTitle.replace("%home%", name).replace("%number%", HomeManager.numberHome(p, name)), ConfigManager.MessageSucessTeleportSubTitle.replace("%home%", name).replace("%number%", HomeManager.numberHome(p, name)));
    }
}
