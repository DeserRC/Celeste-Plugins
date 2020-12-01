package com.redeceleste.celestespawners.command;

import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.defaults.BukkitCommand;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public abstract class Command extends BukkitCommand {
    private Set<CommandArgument> arguments;

    @SneakyThrows
    public Command(String cmd, String... alias) {
        super(cmd);
        setAliases(Arrays.asList(alias));

        Field commandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
        commandMap.setAccessible(true);
        CommandMap cm = (CommandMap) commandMap.get(Bukkit.getServer());
        cm.register(cmd, this);
    }

    public void setArguments(CommandArgument... arguments) {
        this.arguments = Arrays.stream(arguments).collect(Collectors.toSet());
    }
}
