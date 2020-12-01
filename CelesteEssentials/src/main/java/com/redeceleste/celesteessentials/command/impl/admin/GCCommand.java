package com.redeceleste.celesteessentials.command.impl.admin;

import com.redeceleste.celesteessentials.CelesteEssentials;
import com.redeceleste.celesteessentials.command.Command;
import com.redeceleste.celesteessentials.manager.ConfigManager;
import com.redeceleste.celesteessentials.util.impl.BarUtil;
import com.redeceleste.celesteessentials.util.impl.ChatUtil;
import com.redeceleste.celesteessentials.util.impl.TitleUtil;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.stream.Collectors;

import static com.redeceleste.celesteessentials.util.DateUtil.formatDate;
import static com.redeceleste.celesteessentials.util.ReflectionUtil.getNMS;
import static java.lang.Runtime.getRuntime;
import static java.lang.management.ManagementFactory.getRuntimeMXBean;

public class GCCommand extends Command {
    private final CelesteEssentials main;
    private final ConfigManager config;
    private final ChatUtil chat;
    private final BarUtil bar;
    private final TitleUtil title;

    private final Class<?> msClass;

    private final Object server;

    @SneakyThrows
    public GCCommand(CelesteEssentials main) {
        super("gc", "lag", "serverinfo");
        this.main = main;
        this.config = main.getConfigManager();
        this.chat = main.getMessageFactory().getChat();
        this.bar = main.getMessageFactory().getBar();
        this.title = main.getMessageFactory().getTitle();

        msClass = getNMS("MinecraftServer");
        server = msClass.getMethod("getServer").invoke(null);
    }

    @Override @SneakyThrows
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        String perm = config.getConfig("Permission.Admin");
        String gcPerm = config.getConfig("Permission.GC");
        if (!sender.hasPermission(perm) && !sender.hasPermission(gcPerm)) {
            chat.send(sender, "No-Permission.Admin");
            return false;
        }

        if (args.length != 0) {
            chat.send(sender, "GC.Invalid-Argument");
            bar.send(sender, "GC.Invalid-Argument-Bar");
            title.send(sender, "GC.Invalid-Argument-Title");
            return false;
        }

        int onlinePlayers = Bukkit.getOnlinePlayers().size();
        long startTime = getRuntimeMXBean().getStartTime();
        double[] recentTps = (double[]) msClass.getField("recentTps").get(server);
        long byteMaxMemory = getRuntime().maxMemory() / 1024 / 1024;
        long byteTotalMemory = getRuntime().totalMemory() / 1024 / 1024;
        long byteFreeMemory = getRuntime().freeMemory() / 1024 / 1024;
        int amountWorlds = 0;
        int amountChunks = 0;
        int amountEntities = 0;

        for (World world : Bukkit.getWorlds()) {
            amountWorlds++;
            amountChunks += world.getLoadedChunks().length;
            amountEntities += world.getEntities().size();
        }

        StringBuilder sb = new StringBuilder();
        for (double tps : recentTps) {
            String tpsFormat = config.formatTps(tps);
            sb.append(tpsFormat);
            sb.append(", ");
        }

        String players = String.valueOf(onlinePlayers);
        String upTime = formatDate(startTime);
        String tps = sb.toString();
        String maxMemory = String.valueOf(byteMaxMemory);
        String totalMemory = String.valueOf(byteTotalMemory);
        String freeMemory = String.valueOf(byteFreeMemory);
        String worlds = String.valueOf(amountWorlds);
        String chunks = String.valueOf(amountChunks);
        String entities = String.valueOf(amountEntities);

        List<String> message = config.getListMessage("GC.Success");
        message = message.stream().map(line -> line
                .replace("{players}", players)
                .replace("{time}", upTime)
                .replace("{tps}", tps)
                .replace("{maxmemory}", maxMemory)
                .replace("{totalmemory}", totalMemory)
                .replace("{freememory}", freeMemory)
                .replace("{worlds}", worlds)
                .replace("{chunks}", chunks)
                .replace("{entities}", entities)).collect(Collectors.toList());

        message.forEach(sender::sendMessage);
        return false;
    }
}
