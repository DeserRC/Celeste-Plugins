package com.redeceleste.celesteessentials.command.impl.admin;

import com.redeceleste.celesteessentials.CelesteEssentials;
import com.redeceleste.celesteessentials.command.Command;
import com.redeceleste.celesteessentials.manager.ConfigManager;
import com.redeceleste.celesteessentials.type.GamemodeType;
import com.redeceleste.celesteessentials.util.impl.BarUtil;
import com.redeceleste.celesteessentials.util.impl.ChatUtil;
import com.redeceleste.celesteessentials.util.impl.TitleUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.redeceleste.celesteessentials.type.GamemodeType.*;
import static java.util.Arrays.copyOfRange;

public class GamemodeCommand extends Command {
    private final CelesteEssentials main;
    private final ConfigManager config;
    private final ChatUtil chat;
    private final BarUtil bar;
    private final TitleUtil title;

    public GamemodeCommand(CelesteEssentials main) {
        super("gamemode", "gm", "gmc", "gms", "gmsp", "gma");
        this.main = main;
        this.config = main.getConfigManager();
        this.chat = main.getMessageFactory().getChat();
        this.bar = main.getMessageFactory().getBar();
        this.title = main.getMessageFactory().getTitle();
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        String perm = config.getConfig("Permission.Admin");
        String gmPerm = config.getConfig("Permission.Gamemode");
        String gmOtherPerm = config.getConfig("Permission.Gamemode-Other-Players");
        if (!sender.hasPermission(perm) && !sender.hasPermission(gmPerm) && !sender.hasPermission(gmOtherPerm)) {
            chat.send(sender, "No-Permission.Admin");
            return false;
        }

        GamemodeType mode;
        if (commandLabel.equalsIgnoreCase("gms"))       mode = SURVIVAL;
        else if (commandLabel.equalsIgnoreCase("gmc"))  mode = CREATIVE;
        else if (commandLabel.equalsIgnoreCase("gmsp")) mode = SPECTATOR;
        else if (commandLabel.equalsIgnoreCase("gma"))  mode = ADVENTURE;
        else {
            if (args.length == 0) {
                chat.send(sender, "Gamemode.Invalid-Argument");
                bar.send(sender, "Gamemode.Invalid-Argument-Bar");
                title.send(sender, "Gamemode.Invalid-Argument-Title");
                return false;
            }

            mode = get(args[0]);

            if (mode == null) {
                chat.send(sender, "Gamemode.Gamemode-Not-Found",
                        chat.build("{mode}", args[0]));
                bar.send(sender, "Gamemode.Gamemode-Not-Found-Bar",
                        chat.build("{mode}", args[0]));
                title.send(sender, "Gamemode.Gamemode-Not-Found-Title",
                        chat.build("{mode}", args[0]));
                return false;
            }
            args = copyOfRange(args, 1, args.length);
        }

        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                chat.send(sender, "This command cannot be executed via the console");
                return false;
            }

            Player p = (Player) sender;
            if (!p.hasPermission(perm) && !p.hasPermission(gmPerm)) {
                chat.send(p, "No-Permission.Admin");
                return false;
            }

            p.setGameMode(mode.getGamemode());

            chat.send(p, "Gamemode.Success",
                    chat.build("{mode}", mode.getName()));
            bar.send(p, "Gamemode.Success-Bar",
                    chat.build("{mode}", mode.getName()));
            title.send(p, "Gamemode.Success-Title",
                    chat.build("{mode}", mode.getName()));
            return false;
        }

        if (!sender.hasPermission(perm) && !sender.hasPermission(gmOtherPerm)) {
            chat.send(sender, "No-Permission.Admin");
            return false;
        }

        if (args.length != 1) {
            chat.send(sender, "Gamemode.Invalid-Argument");
            bar.send(sender, "Gamemode.Invalid-Argument-Bar");
            title.send(sender, "Gamemode.Invalid-Argument-Title");
            return false;
        }

        Player t = Bukkit.getPlayer(args[0]);
        if (t == null) {
            chat.send(sender, "Gamemode.Player-Not-Found",
                    chat.build("{player}", args[0]));
            bar.send(sender, "Gamemode.Player-Not-Found-Bar",
                    chat.build("{player}", args[0]));
            title.send(sender, "Gamemode.Player-Not-Found-Title",
                    chat.build("{player}", args[0]));
            return false;
        }

        t.setGameMode(mode.getGamemode());
        chat.send(sender, "Gamemode.Success-Other-Player",
                chat.build("{player}", t.getName()),
                chat.build("{mode}", mode.getName()));
        bar.send(sender, "Gamemode.Success-Other-Player-Bar",
                chat.build("{player}", t.getName()),
                chat.build("{mode}", mode.getName()));
        title.send(sender, "Gamemode.Success-Other-Player-Title",
                chat.build("{player}", t.getName()),
                chat.build("{mode}", mode.getName()));

        chat.send(t, "Gamemode.Receive",
                chat.build("{executor}", sender.getName()),
                chat.build("{mode}", mode.getName()));
        bar.send(t, "Gamemode.Receive-Bar",
                chat.build("{executor}", sender.getName()),
                chat.build("{mode}", mode.getName()));
        title.send(t, "Gamemode.Receive-Title",
                chat.build("{executor}", sender.getName()),
                chat.build("{mode}", mode.getName()));
        return false;
    }
}
