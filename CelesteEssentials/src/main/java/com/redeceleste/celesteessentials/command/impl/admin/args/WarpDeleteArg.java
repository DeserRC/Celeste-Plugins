package com.redeceleste.celesteessentials.command.impl.admin.args;

import com.redeceleste.celesteessentials.CelesteEssentials;
import com.redeceleste.celesteessentials.command.CommandArgument;
import com.redeceleste.celesteessentials.manager.WarpManager;
import com.redeceleste.celesteessentials.util.impl.BarUtil;
import com.redeceleste.celesteessentials.util.impl.ChatUtil;
import com.redeceleste.celesteessentials.util.impl.TitleUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WarpDeleteArg extends CommandArgument {
    private final CelesteEssentials main;
    private final WarpManager warp;
    private final ChatUtil chat;
    private final BarUtil bar;
    private final TitleUtil title;

    public WarpDeleteArg(CelesteEssentials main) {
        super(true,"delete", "deletar");
        this.main = main;
        this.warp = main.getWarpManager();
        this.chat = main.getMessageFactory().getChat();
        this.bar = main.getMessageFactory().getBar();
        this.title = main.getMessageFactory().getTitle();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player p = (Player) sender;
        if (args.length != 1) {
            chat.send(p, "Warp.Invalid-Argument");
            bar.send(p, "Warp.Invalid-Argument-Bar");
            title.send(p, "Warp.Invalid-Argument-Title");
            return;
        }

        if (!warp.warpExist(args[0])) {
            chat.send(sender, "Warp.Warp-Not-Found",
                    chat.build("{warp}", args[0]));
            bar.send(sender, "Warp.Warp-Not-Found-Bar",
                    chat.build("{warp}", args[0]));
            title.send(sender, "Warp.Warp-Not-Found-Title",
                    chat.build("{warp}", args[0]));
            return;
        }

        warp.deleteWarp(args[0]);
        chat.send(sender, "Warp.Success-Delete",
                chat.build("{warp}", args[0]));
        bar.send(sender, "Warp.Success-Delete-Bar",
                chat.build("{warp}", args[0]));
        title.send(sender, "Warp.Success-Delete-Title",
                chat.build("{warp}", args[0]));
    }
}
