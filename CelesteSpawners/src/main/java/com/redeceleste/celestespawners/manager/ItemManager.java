package com.redeceleste.celestespawners.manager;

import com.redeceleste.celestespawners.CelesteSpawners;
import com.redeceleste.celestespawners.builder.ItemBuilder;
import lombok.SneakyThrows;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import static com.redeceleste.celestespawners.util.ReflectionUtil.*;

public class ItemManager {
    private final CelesteSpawners main;
    private final ConfigManager config;

    private final Class<?> ppocClass;

    private final Constructor<?> eiCon;
    private final Constructor<?> pposeCon;
    private final Constructor<?> ppoemCon;

    private final Method asNMSCopy;
    private final Method getID;
    private final Method getWatcher;

    @SneakyThrows
    public ItemManager(CelesteSpawners main) {
        this.main = main;
        this.config = main.getConfigManager();

        Class<?> eiClass    = getNMS("EntityItem");
        Class<?> wClass     = getNMS("World");
        Class<?> isClass    = getNMS("ItemStack");
        Class<?> pposeClass = getNMS("PacketPlayOutSpawnEntity");
        Class<?> eClass     = getNMS("Entity");
        Class<?> ppoemClass = getNMS("PacketPlayOutEntityMetadata");
        Class<?> dwClass    = getNMS("DataWatcher");
        Class<?> icisClass  = getOBC("inventory.CraftItemStack");
        ppocClass           = getNMS("PacketPlayOutCollect");

        eiCon        = getCon(eiClass, wClass, double.class, double.class, double.class, isClass);
        pposeCon     = getCon(pposeClass, eClass, int.class, int.class);
        ppoemCon     = getCon(ppoemClass, int.class, dwClass, boolean.class);

        asNMSCopy    = getMethod(icisClass, "asNMSCopy", ItemStack.class);
        getID        = getMethod(eiClass,"getId");
        getWatcher   = getMethod(eiClass,"getDataWatcher");
    }

    public void addItem(Item item) {
        addItem(item, 0);
    }

    public void addItem(Item item, int amount) {
        item.setTicksLived(2);

        ItemStack itemStack = item.getItemStack();
        if (containsMetaData(item)) amount += item.getMetadata("item").get(0).asInt();
        else amount += itemStack.getAmount();

        String name = config.getConfig("Stack-Items.Name");
        if (itemStack.getItemMeta().hasDisplayName()) {
            String itemName = itemStack.getItemMeta().getDisplayName();
            name = name.replace("{type}", itemName);
        } else {
            String itemName = itemStack.getType().name();
            name = name.replace("{type}", itemName);
        }

        String formatted = String.valueOf(amount);
        if (amount >= 1000) config.formatDecimal(amount);
        name = name.replace("{amount}", formatted);

        item.setCustomNameVisible(true);
        item.setCustomName(name);
        item.setMetadata("item", new FixedMetadataValue(main, amount));
    }

    public void give(Inventory inv, ItemStack item, int amount) {
        if (item.getType().getMaxStackSize() == 1) {
            ItemStack itemStack = item.clone();
            itemStack.setAmount(1);

            for (int i=0; i<amount; i++) {
                inv.addItem(itemStack);
            }
        } else {
            ItemStack itemStack = item.clone();
            itemStack.setAmount(amount);
            inv.addItem(new ItemBuilder(item.clone()).setAmount(amount).toItemStack());
        }
    }

    public int getSlots(Inventory inv, int size, Material material) {
        int slots = 0;
        for (ItemStack itemStack : inv.getContents()) {
            if (itemStack == null) {
                slots += size;
            } else if (itemStack.getType() == material) {
                if (itemStack.getAmount() == itemStack.getType().getMaxStackSize()) continue;
                slots += (64 - itemStack.getAmount());
            }
        }
        return slots;
    }

    public int getAmount(Item item) {
        if (!containsMetaData(item)) return 0;
        return item.getMetadata("item").get(0).asInt();
    }

    public boolean containsBlackList(Item item) {
        return item.hasMetadata("blacklist");
    }

    public boolean containsMetaData(Item item) {
        return item.hasMetadata("item");
    }

    public Item getNearestItem(Location loc, ItemStack itemStack) {
        int radius = config.getConfig("Stack-Items.Radius");
        for (Entity entity : loc.getWorld().getNearbyEntities(loc, radius, radius, radius)) {
            if (!(entity instanceof Item)) continue;
            Item item = (Item) entity;
            if (item.getItemStack().isSimilar(itemStack)) return item;
        }
        return null;
    }

    @SneakyThrows
    public void collectAnimation(Player p, Item item) {
        Location loc = item.getLocation();

        Constructor<?> ppocCon;

        Object handle = item.getWorld().getClass().getMethod("getHandle").invoke(item.getWorld());
        Object craftItemStack = invoke(asNMSCopy,null, item.getItemStack());
        Object entityItem = instance(eiCon, handle, loc.getX(), loc.getY(), loc.getZ(), craftItemStack);

        Object id = invoke(getID, entityItem);
        Object dataWatcher = invoke(getWatcher, entityItem);
        Object animation;

        Object spawnEntity = instance(pposeCon, entityItem, 2, 100);
        Object data = ppoemCon.newInstance(id, dataWatcher, true);

        try {
            ppocCon = getCon(ppocClass, int.class, int.class);
            animation = instance(ppocCon, id, p.getEntityId());
            sendPacket(p, animation);
        } catch (NoSuchMethodException e) {
            ppocCon = getCon(ppocClass, int.class, int.class, int.class);
            animation = instance(ppocCon, id, p.getEntityId(), 0);
            sendPacket(p, animation);
        }

        for (Player t : p.getWorld().getPlayers()) {
            if (!(loc.distanceSquared(t.getLocation()) < 100)) continue;
            sendPacket(t, spawnEntity);
            sendPacket(t, data);
            sendPacket(t, animation);
        }
    }
}
