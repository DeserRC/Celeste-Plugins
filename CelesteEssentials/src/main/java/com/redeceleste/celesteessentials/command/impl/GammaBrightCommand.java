package com.redeceleste.celesteessentials.command.impl;

import com.redeceleste.celesteessentials.CelesteEssentials;
import com.redeceleste.celesteessentials.command.Command;
import com.redeceleste.celesteessentials.manager.ConfigManager;
import com.redeceleste.celesteessentials.util.impl.BarUtil;
import com.redeceleste.celesteessentials.util.impl.ChatUtil;
import com.redeceleste.celesteessentials.util.impl.TitleUtil;
import lombok.SneakyThrows;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import static org.bukkit.potion.PotionEffectType.NIGHT_VISION;

public class GammaBrightCommand extends Command {
    private final CelesteEssentials main;
    private final ConfigManager config;
    private final ChatUtil chat;
    private final BarUtil bar;
    private final TitleUtil title;

    @SneakyThrows
    public GammaBrightCommand(CelesteEssentials main) {
        super("gammabright", "nightvision", "vision", "lanterna", "luz");
        this.main = main;
        this.config = main.getConfigManager();
        this.chat = main.getMessageFactory().getChat();
        this.bar = main.getMessageFactory().getBar();
        this.title = main.getMessageFactory().getTitle();
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            chat.send(sender, "This command cannot be executed via the console");
            return false;
        }

        Player p = (Player) sender;
        if (args.length != 0) {
            chat.send(p, "GammaBrightCommand.Invalid-Argument");
            bar.send(p, "GammaBrightCommand.Invalid-Argument-Bar");
            title.send(p, "GammaBrightCommand.Invalid-Argument-Title");
            return false;
        }

        String mode;
        if (p.hasPotionEffect(NIGHT_VISION)) {
            mode = config.getMessage("Disable");
            p.removePotionEffect(NIGHT_VISION);
        } else {
            mode = config.getMessage("Enable");
            p.addPotionEffect(new PotionEffect(NIGHT_VISION, 99999999, 1, false));
        }

        chat.send(p, "GammaBrightCommand.Success",
                chat.build("{mode}", mode));
        bar.send(p, "GammaBrightCommand.Success-Bar",
                chat.build("{mode}", mode));
        title.send(p, "GammaBrightCommand.Success-Title",
                chat.build("{mode}", mode));
        return false;
    }
}
