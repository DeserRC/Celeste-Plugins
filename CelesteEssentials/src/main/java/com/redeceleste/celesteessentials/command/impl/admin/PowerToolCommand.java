package com.redeceleste.celesteessentials.command.impl.admin;

import com.redeceleste.celesteessentials.CelesteEssentials;
import com.redeceleste.celesteessentials.command.Command;
import com.redeceleste.celesteessentials.manager.ConfigManager;
import com.redeceleste.celesteessentials.manager.PowerToolManager;
import com.redeceleste.celesteessentials.util.impl.BarUtil;
import com.redeceleste.celesteessentials.util.impl.ChatUtil;
import com.redeceleste.celesteessentials.util.impl.TitleUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.StringJoiner;

import static org.bukkit.Material.AIR;

public class PowerToolCommand extends Command {
    private final CelesteEssentials main;
    private final PowerToolManager pt;
    private final ConfigManager config;
    private final ChatUtil chat;
    private final BarUtil bar;
    private final TitleUtil title;

    public PowerToolCommand(CelesteEssentials main) {
        super("powertool", "pt");
        this.main = main;
        this.pt = main.getPowerToolManager();
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
        String ptPerm = config.getConfig("Permission.PowerTool");
        if (!p.hasPermission(perm) && !p.hasPermission(ptPerm)) {
            chat.send(p, "No-Permission.Admin");
            return false;
        }

        ItemStack item = p.getItemInHand();
        if (item == null || item.getType().equals(AIR)) {
            chat.send(p, "PowerTool.Item-Not-Found");
            bar.send(p, "PowerTool.Item-Not-Found-Bar");
            title.send(p, "PowerTool.Item-Not-Found-Title");
            return false;
        }

        String name = item.getType().name();
        if (args.length == 0) {
            pt.removePT(p, item.getType());
            chat.send(sender, "PowerTool.Success-Remove",
                    chat.build("{material}", name));
            bar.send(sender, "PowerTool.Success-Remove-Bar",
                    chat.build("{material}", name));
            title.send(sender, "PowerTool.Success-Remove-Title",
                    chat.build("{material}", name));
            return false;
        }

        StringJoiner sj = new StringJoiner(" ");
        for (int i=0; i<args.length; i++) {
            sj.add(args[i]);
        }

        pt.addPT(p, item.getType(), sj.toString());
        chat.send(sender, "PowerTool.Success",
                chat.build("{material}", name),
                chat.build("{command}", sj));
        bar.send(sender, "PowerTool.Success-Bar",
                chat.build("{material}", name),
                chat.build("{command}", sj));
        title.send(sender, "PowerTool.Success-Title",
                chat.build("{material}", name),
                chat.build("{command}", sj));
        return false;
    }
}
