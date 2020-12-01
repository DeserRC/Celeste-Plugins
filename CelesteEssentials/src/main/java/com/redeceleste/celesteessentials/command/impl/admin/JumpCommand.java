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

import static org.bukkit.Material.*;

public class JumpCommand extends Command {
    private final CelesteEssentials main;
    private final ConfigManager config;
    private final ChatUtil chat;
    private final BarUtil bar;
    private final TitleUtil title;

    public JumpCommand(CelesteEssentials main) {
        super("jump", "pular");
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
        String jumpPerm = config.getConfig("Permission.Jump");
        if (!p.hasPermission(perm) && !p.hasPermission(jumpPerm)) {
            chat.send(p, "No-Permission.Admin");
            return false;
        }

        if (args.length != 0) {
            chat.send(p, "Jump.Invalid-Argument");
            bar.send(p, "Jump.Invalid-Argument-Bar");
            title.send(p, "Jump.Invalid-Argument-Title");
            return false;
        }

        float yaw = p.getLocation().getYaw();
        float pitch = p.getLocation().getPitch();

        Set<Material> blocks = new HashSet<>();
        blocks.add(AIR);
        blocks.add(BARRIER);

        Block block = p.getTargetBlock(blocks, 100);
        Location loc = block.getLocation();

        loc.setYaw(yaw);
        loc.setPitch(pitch);
        loc.add(0,1,0);
        p.teleport(loc);

        chat.send(sender, "Jump.Success");
        bar.send(sender, "Jump.Success-Bar");
        title.send(sender, "Jump.Success-Title");
        return false;
    }
}

