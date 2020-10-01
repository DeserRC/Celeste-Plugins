package com.redeceleste.celesteshop.command;

import lombok.Getter;
import org.bukkit.command.CommandSender;

@Getter
public abstract class CommandArgument {
    private final Boolean isPlayerExclusive;
    private final String argumentName;
    private final String[] argumentAliases;

    public CommandArgument(Boolean isPlayerExclusive, String argumentName, String... argumentAliases) {
        this.isPlayerExclusive = isPlayerExclusive;
        this.argumentName = argumentName;
        this.argumentAliases = argumentAliases;
    }

    public abstract void execute(CommandSender sender, String[] args);
}
