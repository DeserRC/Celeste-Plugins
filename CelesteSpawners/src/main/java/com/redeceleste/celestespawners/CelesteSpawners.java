package com.redeceleste.celestespawners;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.redeceleste.celestespawners.command.impl.KillAllCommand;
import com.redeceleste.celestespawners.command.impl.SpawnerCommand;
import com.redeceleste.celestespawners.command.impl.args.HelpArgument;
import com.redeceleste.celestespawners.command.impl.args.ShopArgument;
import com.redeceleste.celestespawners.command.impl.args.admin.*;
import com.redeceleste.celestespawners.exception.PluginNotFoundException;
import com.redeceleste.celestespawners.factory.*;
import com.redeceleste.celestespawners.listener.*;
import com.redeceleste.celestespawners.manager.ConfigManager;
import com.redeceleste.celestespawners.manager.HologramManager;
import com.redeceleste.celestespawners.manager.InventoryManager;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.ScheduledExecutorService;

import static com.gmail.filoghost.holographicdisplays.api.HologramsAPI.getHolograms;
import static java.lang.Runtime.getRuntime;
import static java.util.concurrent.Executors.newScheduledThreadPool;
import static java.util.concurrent.TimeUnit.MINUTES;
import static org.bukkit.Bukkit.getPluginManager;
import static org.bukkit.Bukkit.getScheduler;
import static org.bukkit.event.HandlerList.unregisterAll;

@Getter
public class CelesteSpawners extends JavaPlugin {
    @Getter
    private static CelesteSpawners instance;

    private ScheduledExecutorService scheduled;
    private Economy economy;
    private ConfigManager configManager;
    private InventoryManager inventoryManager;
    private HologramManager hologramManager;

    private ConnectionFactory connectionFactory;
    private MessageFactory messageFactory;
    private SpawnerFactory spawnerFactory;
    private TaskFactory taskFactory;

    @Override
    public void onEnable() {
        instance = this;

        scheduled = newScheduledThreadPool(getRuntime().availableProcessors() * 10);

        configManager = new ConfigManager(this);
        hologramManager = new HologramManager(this);

        connectionFactory = new ConnectionFactory(this);
        messageFactory = new MessageFactory(this);
        spawnerFactory = new SpawnerFactory(this);
        taskFactory = new TaskFactory(this);

        inventoryManager = new InventoryManager(this);

        loadCommands();
        loadListener();
        loadEconomy();
        loadHolograms();
        loadTasks();
    }

    @Override
    public void onDisable() {
        taskFactory.getKillAll().clear();
        taskFactory.getSpawnerUpdateSync().run();
        scheduled.shutdown();
        connectionFactory.getDataBase().closeConnection();
        getHolograms(this).forEach(Hologram::delete);
        unregisterAll();
    }

    private void loadCommands() {
        new SpawnerCommand(this).setArguments(
                new GiveArgument(this),
                new GiveCustomArgument(this),
                new HelpArgument(this),
                new ShopArgument(this));
        new KillAllCommand(this);
    }

    private void loadListener() {
        new BlockPlaceListener(this);
        new BlockBreakListener(this);
        new EntitySpawnListener(this);
        new EntityDeathListener(this);
        new ItemListener(this);
        new UserListener(this);
        new InventoryListener(this);
        new PurchaseListener(this);
    }

    private void loadEconomy() {
        try {
            if (getPluginManager().getPlugin("Vault") == null) {
                throw new PluginNotFoundException("Vault não encontrado.");
            }

            RegisteredServiceProvider<Economy> provider = getServer().getServicesManager().getRegistration(Economy.class);
            if (provider == null) {
                throw new PluginNotFoundException("Vault não encontrado.");
            }

            economy = provider.getProvider();

            if (economy == null) {
                throw new PluginNotFoundException("Vault não encontrado.");
            }
        } catch (PluginNotFoundException e) {
            System.err.print(e.getMessage());
            getPluginManager().disablePlugin(this);
        }
    }

    private void loadHolograms() {
        try {
            if (getPluginManager().getPlugin("HolographicDisplays") == null) {
                throw new PluginNotFoundException("HolographicDisplays não encontrado.");
            }

            getHolograms(this).forEach(Hologram::delete);
        } catch (PluginNotFoundException e) {
            System.err.print(e.getMessage());
            getPluginManager().disablePlugin(this);
        }
    }

    private void loadTasks() {
        taskFactory.getKillAll().clear();
        taskFactory.getSpawnerGet().run();

        int update = configManager.getConfig("Times.Update-Spawners");
        int spawn = configManager.getConfig("Times.Entity-Spawn");
        int killAll = configManager.getConfig("Times.Remove-Entities");

        scheduled.scheduleAtFixedRate(taskFactory.getSpawnerUpdateAsync(), update, update, MINUTES);
        getScheduler().runTaskTimer(this, taskFactory.getEntitySpawn(), spawn * 20, spawn * 20);
        scheduled.scheduleAtFixedRate(taskFactory.getKillAll(), killAll, killAll, MINUTES);
    }
}