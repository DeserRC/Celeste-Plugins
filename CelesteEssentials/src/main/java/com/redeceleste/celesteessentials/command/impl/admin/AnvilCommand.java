package com.redeceleste.celesteessentials.command.impl.admin;

import com.redeceleste.celesteessentials.CelesteEssentials;
import com.redeceleste.celesteessentials.command.Command;
import com.redeceleste.celesteessentials.manager.ConfigManager;
import com.redeceleste.celesteessentials.util.impl.BarUtil;
import com.redeceleste.celesteessentials.util.impl.ChatUtil;
import com.redeceleste.celesteessentials.util.impl.TitleUtil;
import lombok.SneakyThrows;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static com.redeceleste.celesteessentials.util.ReflectionUtil.getNMS;
import static com.redeceleste.celesteessentials.util.ReflectionUtil.sendPacket;

public class AnvilCommand extends Command {
    private final CelesteEssentials main;
    private final ConfigManager config;
    private final ChatUtil chat;
    private final BarUtil bar;
    private final TitleUtil title;

    private final Constructor<?> cmCon;
    private final Constructor<?> bpCon;
    private final Constructor<?> ppoowCon;
    private final Constructor<?> caCon;

    private final Method nextContainerCounter;
    private final Method addSlotListener;

    private final Field inventoryF;
    private final Field worldF;
    private final Field windowIdF;
    private final Field checkReachableF;
    private final Field activeContainerF;

    @SneakyThrows
    public AnvilCommand(CelesteEssentials main) {
        super("anvil", "bigorna");
        this.main = main;
        this.config = main.getConfigManager();
        this.chat = main.getMessageFactory().getChat();
        this.bar = main.getMessageFactory().getBar();
        this.title = main.getMessageFactory().getTitle();

        Class<?> cmClass = getNMS("ChatMessage");
        Class<?> bpClass = getNMS("BlockPosition");
        Class<?> ppoowClass = getNMS("PacketPlayOutOpenWindow");
        Class<?> icbcClass = getNMS("IChatBaseComponent");
        Class<?> caClass = getNMS("ContainerAnvil");
        Class<?> piClass = getNMS("PlayerInventory");
        Class<?> wClass = getNMS("World");
        Class<?> ehClass = getNMS("EntityHuman");
        Class<?> epClass = getNMS("EntityPlayer");
        Class<?> cClass = getNMS("Container");
        Class<?> icClass = getNMS("ICrafting");

        this.cmCon    = cmClass.getConstructor(String.class, Object[].class);
        this.bpCon    = bpClass.getConstructor(int.class, int.class, int.class);
        this.ppoowCon = ppoowClass.getConstructor(int.class, String.class, icbcClass);
        this.caCon    = caClass.getConstructor(piClass, wClass, bpClass, ehClass);

        this.nextContainerCounter = epClass.getMethod("nextContainerCounter");
        this.addSlotListener      = cClass.getMethod("addSlotListener", icClass);

        this.inventoryF       = epClass.getField("inventory");
        this.worldF           = epClass.getField("world");
        this.windowIdF        = caClass.getField("windowId");
        this.checkReachableF  = caClass.getField("checkReachable");
        this.activeContainerF = epClass.getField("activeContainer");
    }

    @SneakyThrows
    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            chat.send(sender, "This command cannot be executed via the console");
            return false;
        }

        Player p = (Player) sender;
        String perm = config.getConfig("Permission.Admin");
        String anvilPerm = config.getConfig("Permission.Anvil");
        if (!p.hasPermission(perm) && !p.hasPermission(anvilPerm)) {
            chat.send(p, "No-Permission.Admin");
            return false;
        }

        if (args.length != 0) {
            chat.send(p, "Anvil.Invalid-Argument");
            bar.send(p, "Anvil.Invalid-Argument-Bar");
            title.send(p, "Anvil.Invalid-Argument-Title");
            return false;
        }

        Location loc = p.getLocation();
        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();

        Object player = p.getClass().getMethod("getHandle").invoke(p);
        Object counter = nextContainerCounter.invoke(player);
        Object message = cmCon.newInstance("Repairing", new Object[] {});
        Object inventory = inventoryF.get(player);
        Object world = worldF.get(player);
        Object block = bpCon.newInstance(x, y, z);
        Object packet = ppoowCon.newInstance(counter, "minecraft:anvil", message);
        Object container = caCon.newInstance(inventory, world, block, player);

        sendPacket(p, packet);
        windowIdF.set(container, counter);
        checkReachableF.set(container, false);
        activeContainerF.set(player, container);
        addSlotListener.invoke(container, player);
        return false;
    }
}
