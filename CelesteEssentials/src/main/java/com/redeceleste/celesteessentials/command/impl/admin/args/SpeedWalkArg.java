package com.redeceleste.celesteessentials.command.impl.admin.args;

import com.redeceleste.celesteessentials.CelesteEssentials;
import com.redeceleste.celesteessentials.command.CommandArgument;
import com.redeceleste.celesteessentials.manager.ConfigManager;
import com.redeceleste.celesteessentials.util.impl.BarUtil;
import com.redeceleste.celesteessentials.util.impl.ChatUtil;
import com.redeceleste.celesteessentials.util.impl.TitleUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpeedWalkArg extends CommandArgument {
    private final CelesteEssentials main;
    private final ConfigManager config;
    private final ChatUtil chat;
    private final BarUtil bar;
    private final TitleUtil title;

    public SpeedWalkArg(CelesteEssentials main) {
        super(true,"walk", "caminhar");
        this.main = main;
        this.config = main.getConfigManager();
        this.chat = main.getMessageFactory().getChat();
        this.bar = main.getMessageFactory().getBar();
        this.title = main.getMessageFactory().getTitle();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player p = (Player) sender;
        String mode = config.getMessage("Speed.Walk");
        float speed = Integer.parseInt(args[0]);
        p.setWalkSpeed(speed / 10);

        chat.send(p, "Speed.Success",
                chat.build("{mode}", mode),
                chat.build("{speed}", speed));
        bar.send(p, "Speed.Success-Bar",
                chat.build("{mode}", mode),
                chat.build("{speed}", speed));
        title.send(p, "Speed.Success-Title",
                chat.build("{mode}", mode),
                chat.build("{speed}", speed));
    }
}
