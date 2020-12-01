package com.redeceleste.celesteessentials.command.impl.admin;

import com.redeceleste.celesteessentials.CelesteEssentials;
import com.redeceleste.celesteessentials.command.Command;
import com.redeceleste.celesteessentials.manager.ConfigManager;
import com.redeceleste.celesteessentials.util.impl.BarUtil;
import com.redeceleste.celesteessentials.util.impl.ChatUtil;
import com.redeceleste.celesteessentials.util.impl.TitleUtil;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.regex.Pattern;

public class TimeCommand extends Command {
    private final CelesteEssentials main;
    private final ConfigManager config;
    private final ChatUtil chat;
    private final BarUtil bar;
    private final TitleUtil title;

    public TimeCommand(CelesteEssentials main) {
        super("time", "tempo");
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
        String timePerm = config.getConfig("Permission.Time");
        if (!p.hasPermission(perm) && !p.hasPermission(timePerm)) {
            chat.send(p, "No-Permission.Admin");
            return false;
        }

        if (args.length != 1 || Pattern.compile("[^0-9:]").matcher(args[0]).find()) {
            chat.send(p, "Time.Invalid-Argument");
            bar.send(p, "Time.Invalid-Argument-Bar");
            title.send(p, "Time.Invalid-Argument-Title");
            return false;
        }

        String[] split = args[0].split(":");
        int hours = Integer.parseInt(split[0]);
        int minutes = 0;

        if (split.length != 1) minutes = Integer.parseInt(split[1]);

        World world = p.getWorld();
        long tick = timeToTick(hours, minutes);
        world.setTime(tick);

        String time = hours + ":" + minutes;
        if (String.valueOf(minutes).length() == 1) time = time + "0";

        chat.send(p, "Time.Success",
                chat.build("{world}", world),
                chat.build("{time}", time));
        bar.send(p, "Time.Success-Bar",
                chat.build("{world}", world),
                chat.build("{time}", time));
        title.send(p, "Time.Success-Title",
                chat.build("{world}", world),
                chat.build("{time}", time));
        return false;
    }

    private long timeToTick(Integer hours, int minutes) {
        long tick = 18000L;
        tick += hours * 1000;
        tick += (minutes / 60) * 1000;
        tick %= 24000;
        return tick;
    }
}
