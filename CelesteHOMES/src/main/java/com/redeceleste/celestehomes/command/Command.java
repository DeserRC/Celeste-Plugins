package com.redeceleste.celestehomes.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.defaults.BukkitCommand;

import java.lang.reflect.Field;
import java.util.Arrays;

public abstract class Command extends BukkitCommand {
    public Command(String cmd, String... alias) {
        super(cmd);
        setAliases(Arrays.asList(alias));

        try {
            Field commandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMap.setAccessible(true);
            CommandMap cm = (CommandMap) commandMap.get(Bukkit.getServer());
            cm.register(cmd, this);
        } catch (Exception ignored) {
        }
    }
}