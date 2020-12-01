package com.redeceleste.celesteessentials.manager;

import com.redeceleste.celesteessentials.CelesteEssentials;
import lombok.SneakyThrows;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static com.redeceleste.celesteessentials.util.ReflectionUtil.getNMS;
import static com.redeceleste.celesteessentials.util.ReflectionUtil.getOBC;

public class GodManager {
    private final CelesteEssentials main;

    private final Method getHandle;

    private final Field abilitiesF;
    private final Field isInvulnerableF;

    @SneakyThrows
    public GodManager(CelesteEssentials main) {
        this.main = main;

        Class<?> cpClass = getOBC("entity.CraftPlayer");
        Class<?> epClass = getNMS("EntityPlayer");
        Class<?> paClass = getNMS("PlayerAbilities");

        this.getHandle = cpClass.getMethod("getHandle");

        this.abilitiesF = epClass.getField("abilities");
        this.isInvulnerableF = paClass.getField("isInvulnerable");
    }

    @SneakyThrows
    public void setGodMode(Player p, boolean status) {
        Object player = getHandle.invoke(p);
        Object abilities = abilitiesF.get(player);
        isInvulnerableF.set(abilities, status);
    }

    @SneakyThrows
    public boolean isGodMode(Player p) {
        Object player = getHandle.invoke(p);
        Object abilities = abilitiesF.get(player);
        return isInvulnerableF.getBoolean(abilities);
    }
}
