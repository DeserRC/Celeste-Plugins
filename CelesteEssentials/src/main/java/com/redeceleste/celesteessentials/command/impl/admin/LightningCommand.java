package com.redeceleste.celesteessentials.command.impl.admin;

import com.redeceleste.celesteessentials.CelesteEssentials;
import com.redeceleste.celesteessentials.command.Command;
import com.redeceleste.celesteessentials.manager.ConfigManager;
import com.redeceleste.celesteessentials.util.impl.BarUtil;
import com.redeceleste.celesteessentials.util.impl.ChatUtil;
import com.redeceleste.celesteessentials.util.impl.TitleUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

import static org.bukkit.Material.AIR;
import static org.bukkit.Material.BARRIER;

public class LightningCommand extends Command {
    private final CelesteEssentials main;
    private final ConfigManager config;
    private final ChatUtil chat;
    private final BarUtil bar;
    private final TitleUtil title;

    public LightningCommand(CelesteEssentials main) {
        super("lightning", "thunder", "thor", "relampago", "raio");
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
        String perm = config.getConfig("Permission.Admin");
        String lightningPerm = config.getConfig("Permission.Lightning");
        if (!p.hasPermission(perm) && !p.hasPermission(lightningPerm)) {
            chat.send(p, "No-Permission.Admin");
            return false;
        }

        if (args.length != 0) {
            chat.send(p, "Lightning.Invalid-Argument");
            bar.send(p, "Lightning.Invalid-Argument-Bar");
            title.send(p, "Lightning.Invalid-Argument-Title");
            return false;
        }

        Set<Material> blocks = new HashSet<>();
        blocks.add(AIR);
        blocks.add(BARRIER);

        Block block = p.getTargetBlock(blocks, 1000);
        Location loc = block.getLocation();
        p.getWorld().strikeLightning(loc);

        chat.send(p, "Lightning.Success");
        bar.send(p, "Lightning.Success-Bar");
        title.send(p, "Lightning.Success-Title");
        return false;
    }
}
