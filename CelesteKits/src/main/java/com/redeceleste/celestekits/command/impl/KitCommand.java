package com.redeceleste.celestekits.command.impl;

import com.redeceleste.celestekits.MockKits;
import com.redeceleste.celestekits.command.Command;
import com.redeceleste.celestekits.command.CommandArgument;
import com.redeceleste.celestekits.manager.ConfigManager;
import com.redeceleste.celestekits.manager.InventoryManager;
import com.redeceleste.celestekits.manager.KitManager;
import com.redeceleste.celestekits.manager.UserManager;
import com.redeceleste.celestekits.model.CategoryArgument;
import com.redeceleste.celestekits.model.KitArgument;
import com.redeceleste.celestekits.model.UserArgument;
import com.redeceleste.celestekits.util.impl.BarUtil;
import com.redeceleste.celestekits.util.impl.ChatUtil;
import com.redeceleste.celestekits.util.impl.TitleUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Arrays;
import java.util.Map;

import static com.redeceleste.celestekits.util.DateUtil.formatDate;

public class KitCommand extends Command {
    private final MockKits main;
    private final ConfigManager config;
    private final InventoryManager inventory;
    private final KitManager kit;
    private final UserManager user;
    private final ChatUtil chat;
    private final BarUtil bar;
    private final TitleUtil title;

    public KitCommand(MockKits main) {
        super("kit", "kits");
        this.main = main;
        this.config = main.getConfigManager();
        this.inventory = main.getInventoryManager();
        this.kit = main.getKitFactory().getKit();
        this.user = main.getUserFactory().getUser();
        this.chat = main.getMessageFactory().getChat();
        this.bar = main.getMessageFactory().getBar();
        this.title = main.getMessageFactory().getTitle();
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (args.length != 0) {
            setupArguments(sender, args);
            return false;
        }

        if (!(sender instanceof Player)) {
            chat.send(sender, "The console may not execute this command");
            return false;
        }

        Player p = (Player) sender;
        inventory.openMain(p);
        return false;
    }

    private void setupArguments(CommandSender sender, String[] args) {
        CommandArgument argument = null;
        for (CommandArgument commandArgument : getArguments()) {
            if (commandArgument.getArgumentName().equalsIgnoreCase(args[0]) || Arrays.stream(commandArgument.getArgumentAliases()).anyMatch(args[0]::equalsIgnoreCase)) {
                argument = commandArgument;
            }
        }

        if (argument != null) {
            if (argument.getIsPlayerExclusive() && !(sender instanceof Player)) {
                chat.send(sender, "The console may not execute this command");
                return;
            }

            String[] newArgs = Arrays.copyOfRange(args, 1, args.length);
            argument.execute(sender, newArgs);
            return;
        }

        if (!(sender instanceof Player)) {
            chat.send(sender, "The console may not execute this command");
            return;
        }

        Player p = (Player) sender;
        KitArgument kitArg = kit.getKit(args[0]);
        if (kitArg == null) {
            chat.send(p, "Kit.Invalid-Argument");
            bar.send(p, "Kit.Invalid-Argument-Bar");
            title.send(p, "Kit.Invalid-Argument-Title");
            return;
        }

        CategoryArgument categoryArg = null;
        for (File fileCategory : config.getCategories().keySet()) {
            FileConfiguration categoryFile = config.getCategories().get(fileCategory);
            Map<File, FileConfiguration> kits = config.getKits().get(categoryFile);

            for (File fileKit : kits.keySet()) {
                FileConfiguration kitFile = kits.get(fileKit);
                if (!kitArg.getFile().equals(kitFile)) continue;

                String categoryName = fileCategory.getName().replace(".yml", "");
                categoryArg = kit.getCategory(categoryName);
                break;
            }
        }

        FileConfiguration kitFile = kitArg.getFile();
        String name = config.get("Name", kitFile);

        String kitName = kitArg.getName();
        String kitPermission = kitArg.getPermission();

        String adminPermission = config.getConfig("Permission.Admin");
        if (!(kitPermission == null)) {
            if (!p.hasPermission(kitPermission) && !p.hasPermission(adminPermission)) {
                chat.send(p, "No-Permission.Kit",
                        chat.build("{name}", name));
                return;
            }
        }

        boolean containsCD = false;
        UserArgument userArg = user.getCooldown(p.getName());
        if (userArg != null) {
            containsCD = userArg.getKit().containsKey(kitName);
        }

        Long delay = containsCD ? userArg.getKit().get(kitName) : null;
        if (delay != null && delay - System.currentTimeMillis() <= 0) {
            containsCD = false;
            user.removeCooldown(p.getName(), kitName);
        }

        if (!containsCD || p.hasPermission(adminPermission)) {
            inventory.openConfirm(p, categoryArg, kitArg);
            return;
        }

        String delayFormatted = formatDate(delay);
        chat.send(p, "Kit.Delay",
                chat.build("{name}", name),
                chat.build("{delay}", delayFormatted));
        bar.send(p, "Kit.Delay-Bar",
                chat.build("{name}", name),
                chat.build("{delay}", delayFormatted));
        title.send(p, "Kit.Delay-Title",
                chat.build("{name}", name),
                chat.build("{delay}", delayFormatted));
    }
}
