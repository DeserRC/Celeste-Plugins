package com.redeceleste.celesteessentials;

import com.redeceleste.celesteessentials.command.impl.*;
import com.redeceleste.celesteessentials.command.impl.admin.*;
import com.redeceleste.celesteessentials.command.impl.admin.args.*;
import com.redeceleste.celesteessentials.factory.MessageFactory;
import com.redeceleste.celesteessentials.listener.CommandListener;
import com.redeceleste.celesteessentials.listener.InventoryListener;
import com.redeceleste.celesteessentials.listener.UserListener;
import com.redeceleste.celesteessentials.listener.WorldListener;
import com.redeceleste.celesteessentials.manager.*;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.ScheduledExecutorService;

import static java.lang.Runtime.getRuntime;
import static java.util.concurrent.Executors.newScheduledThreadPool;
import static org.bukkit.event.HandlerList.unregisterAll;

@Getter
public class CelesteEssentials extends JavaPlugin {
    @Getter
    private static CelesteEssentials instance;

    private ScheduledExecutorService scheduled;
    private ConfigManager configManager;
    private InventoryManager inventoryManager;
    private CommandManager commandManager;
    private WarpManager warpManager;
    private GodManager godManager;
    private PowerToolManager powerToolManager;

    private MessageFactory messageFactory;

    @Override
    public void onEnable() {
        instance = this;

        scheduled = newScheduledThreadPool(getRuntime().availableProcessors());

        configManager = new ConfigManager(this);
        inventoryManager = new InventoryManager(this);

        messageFactory = new MessageFactory(this);

        commandManager = new CommandManager(this);
        warpManager = new WarpManager(this);
        godManager = new GodManager(this);
        powerToolManager = new PowerToolManager(this);

        loadCommands();
        loadListeners();
    }

    @Override
    public void onDisable() {
        scheduled.shutdown();
        unregisterAll();
    }

    private void loadCommands() {
        new AnvilCommand(this);
        new BroadCastCommand(this);
        new BroadCastWorldCommand(this);
        new BurnCommand(this);
        new ClearCommand(this);
        new CompactCommand(this);
        new CraftCommand(this);
        new CrashCommand(this);
        new EffectCommand(this);
        new EnchantCommand(this);
        new EssentialsCommand(this).setArguments(
                new EssentialsHelpArg(this),
                new EssentialsReloadArg(this));
        new ExpCommand(this).setArguments(
                new ExpAddArg(this),
                new ExpRemoveArg(this),
                new ExpResetArg(this),
                new ExpSetArg(this));
        new FlyCommand(this);
        new FoodCommand(this);
        new GamemodeCommand(this);
        new GammaBrightCommand(this);
        new GCCommand(this);
        new GiveCommand(this);
        new GodCommand(this);
        new HatCommand(this);
        new HeadCommand(this);
        new HealCommand(this);
        new JumpCommand(this);
        new KillCommand(this);
        new LightningCommand(this);
        new LoreCommand(this);
        new MoreCommand(this);
        new MotdCommand(this);
        new NameCommand(this);
        new PingCommand(this);
        new PotionCommand(this).setArguments(
                new PotionAddArg(this),
                new PotionRemoveArg(this),
                new PotionResetArg(this),
                new PotionSetArg(this));
        new PowerToolCommand(this);
        new RepairCommand(this);
        new SpeedCommand(this).setArguments(
                new SpeedFlyArg(this),
                new SpeedWalkArg(this));
        new SudoCommand(this);
        new TeleportCommand(this);
        new TeleportHereCommand(this);
        new TimeCommand(this);
        new TopCommand(this);
        new TrashCommand(this);
        new WarpCommand(this).setArguments(
                new WarpCreateArg(this),
                new WarpDeleteArg(this));
        new WhoisCommand(this);
    }

    private void loadListeners() {
        new CommandListener(this);
        new InventoryListener(this);
        new UserListener(this);
        new WorldListener(this);
    }
}
