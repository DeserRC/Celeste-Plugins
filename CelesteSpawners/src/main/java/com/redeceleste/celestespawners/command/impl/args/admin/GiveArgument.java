package com.redeceleste.celestespawners.command.impl.args.admin;

import com.redeceleste.celestespawners.CelesteSpawners;
import com.redeceleste.celestespawners.command.CommandArgument;
import com.redeceleste.celestespawners.manager.ConfigManager;
import com.redeceleste.celestespawners.manager.impl.SpawnerManager;
import com.redeceleste.celestespawners.type.MobType;
import com.redeceleste.celestespawners.util.impl.BarUtil;
import com.redeceleste.celestespawners.util.impl.ChatUtil;
import com.redeceleste.celestespawners.util.impl.TitleUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.regex.Pattern;

public class GiveArgument extends CommandArgument {
    private final CelesteSpawners main;
    private final ConfigManager config;
    private final SpawnerManager spawner;
    private final ChatUtil chat;
    private final BarUtil bar;
    private final TitleUtil title;

    public GiveArgument(CelesteSpawners main) {
        super(false, "give", "dar", "enviar");
        this.main = main;
        this.config = main.getConfigManager();
        this.spawner = main.getSpawnerFactory().getSpawner();
        this.chat = main.getMessageFactory().getChat();
        this.bar = main.getMessageFactory().getBar();
        this.title = main.getMessageFactory().getTitle();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        String perm = config.getConfig("Permission.Admin");
        if (!sender.hasPermission(perm)) {
            chat.send(sender, "No-Permission.Admin");
            return;
        }

        if (!(args.length == 3) || Pattern.compile("[^0-9]").matcher(args[2]).find() || Integer.parseInt(args[2]) < 1) {
            chat.send(sender, "Give.Invalid-Argument");
            bar.send(sender, "Give.Invalid-Argument-Bar");
            title.send(sender, "Give.Invalid-Argument-Title");
            return;
        }

        Player t = Bukkit.getPlayer(args[0]);
        if (t == null) {
            chat.send(sender, "Give.Player-Not-Found",
                    chat.build("{player}", args[0]));
            bar.send(sender, "Give.Player-Not-Found-Bar",
                    chat.build("{player}", args[0]));
            title.send(sender, "Give.Player-Not-Found-Title",
                    chat.build("{player}", args[0]));
            return;
        }

        boolean spawnerExist = spawner.isExist(args[1]);
        if (!spawnerExist) {
            chat.send(sender, "Give.Spawner-Not-Found",
                    chat.build("{type}", args[1]));
            bar.send(sender, "Give.Spawner-Not-Found-Bar",
                    chat.build("{type}", args[1]));
            title.send(sender, "Give.Spawner-Not-Found-Title",
                    chat.build("{type}", args[1]));
            return;
        }

        MobType type = MobType.getMob(args[1]);
        long amount = Long.parseLong(args[2]);

        ItemStack item = spawner.getSpawner(amount, type);
        t.getInventory().addItem(item);

        chat.send(sender, "Give.Success",
                chat.build("{player}", t.getName()),
                chat.build("{amount}", amount),
                chat.build("{type}", type.getName()));
        bar.send(sender, "Give.Success-Bar",
                chat.build("{player}", t.getName()),
                chat.build("{amount}", amount),
                chat.build("{type}", type.getName()));
        title.send(sender, "Give.Success-Title",
                chat.build("{player}", t.getName()),
                chat.build("{amount}", amount),
                chat.build("{type}", type.getName()));

        chat.send(t, "Give.Receive",
                chat.build("{executor}", sender.getName()),
                chat.build("{amount}", amount),
                chat.build("{type}", type.getName()));
        bar.send(t, "Give.Receive-Bar",
                chat.build("{executor}", sender.getName()),
                chat.build("{amount}", amount),
                chat.build("{type}", type.getName()));
        title.send(t, "Give.Receive-Title",
                chat.build("{executor}", sender.getName()),
                chat.build("{amount}", amount),
                chat.build("{type}", type.getName()));
    }
}
