package com.redeceleste.celesteessentials.command.impl.admin;

import com.redeceleste.celesteessentials.CelesteEssentials;
import com.redeceleste.celesteessentials.builder.ItemBuilder;
import com.redeceleste.celesteessentials.command.Command;
import com.redeceleste.celesteessentials.manager.ConfigManager;
import com.redeceleste.celesteessentials.util.impl.BarUtil;
import com.redeceleste.celesteessentials.util.impl.ChatUtil;
import com.redeceleste.celesteessentials.util.impl.TitleUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static java.util.Arrays.asList;
import static org.bukkit.Material.values;

public class GiveCommand extends Command {
    private final CelesteEssentials main;
    private final ConfigManager config;
    private final ChatUtil chat;
    private final BarUtil bar;
    private final TitleUtil title;

    public GiveCommand(CelesteEssentials main) {
        super("give", "dar");
        this.main = main;
        this.config = main.getConfigManager();
        this.chat = main.getMessageFactory().getChat();
        this.bar = main.getMessageFactory().getBar();
        this.title = main.getMessageFactory().getTitle();
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        String perm = config.getConfig("Permission.Admin");
        String givePerm = config.getConfig("Permission.Give");
        if (!sender.hasPermission(perm) && !sender.hasPermission(givePerm)) {
            chat.send(sender, "No-Permission.Admin");
            return false;
        }

        if (args.length < 3 || Pattern.compile("[^0-9]").matcher(args[2]).find() || args[2].length() > 9 || Integer.parseInt(args[2]) < 0 || Integer.parseInt(args[2]) > 20000) {
            chat.send(sender, "Give.Invalid-Argument");
            bar.send(sender, "Give.Invalid-Argument-Bar");
            title.send(sender, "Give.Invalid-Argument-Title");
            return false;
        }

        Player t = Bukkit.getPlayer(args[0]);
        if (t == null) {
            chat.send(sender, "Give.Player-Not-Found",
                    chat.build("{player}", args[0]));
            bar.send(sender, "Give.Player-Not-Found-Bar",
                    chat.build("{player}", args[0]));
            title.send(sender, "Give.Player-Not-Found-Title",
                    chat.build("{player}", args[0]));
            return false;
        }

        Material material = null;
        int data;

        String[] split = args[1].split(":");
        for (Material type : values()) {
            String name = type.name();
            String id = String.valueOf(type.getId());
            if (split[0].equalsIgnoreCase(name)) material = type;
            if (split[0].equalsIgnoreCase(id)) material = type;
            if (material != null) break;
        }

        if (split.length == 1 || Pattern.compile("[^0-9]").matcher(split[1]).find()) data = 0;
        else data = Integer.parseInt(split[1]);

        if (material == null) {
            chat.send(sender, "Give.Material-Not-Found",
                    chat.build("{material}", split[0]));
            bar.send(sender, "Give.Material-Not-Found-Bar",
                    chat.build("{material}", split[0]));
            title.send(sender, "Give.Material-Not-Found-Title",
                    chat.build("{material}", split[0]));
            return false;
        }

        String name = null;
        List<String> lore = new ArrayList<>();
        List<String> enchant = new ArrayList<>();
        List<String> potion = new ArrayList<>();

        for (String arg : args) {
            if (arg.toLowerCase().contains("name:"))         name = getName(arg);
            else if (arg.toLowerCase().contains("lore:"))    lore = getLore(arg);
            else if (arg.toLowerCase().contains("enchant:")) enchant = getEnchant(arg);
            else if (arg.toLowerCase().contains("potion:"))  potion = getPotion(arg);
        }

        Inventory inv = t.getInventory();
        int amount = Integer.parseInt(args[2]);
        int maxAmount = material.getMaxStackSize();
        int slots = getSlots(inv, material.getMaxStackSize(), material);

        String materialName = material.name().replace("_", " ").toLowerCase();
        materialName = StringUtils.capitalize(materialName);

        if (slots < amount) amount = slots;
        if (maxAmount > amount) giveItem(inv, material, data, amount, amount, name, lore, enchant, potion);
        else if (slots != 0) giveItem(inv, material, data, amount, maxAmount, name, lore, enchant, potion);

        if (sender.equals(t)) {
            chat.send(sender, "Give.Success",
                    chat.build("{material}", materialName),
                    chat.build("{amount}", amount));
            bar.send(sender, "Give.Success-Bar",
                    chat.build("{material}", materialName),
                    chat.build("{amount}", amount));
            title.send(sender, "Give.Success-Title",
                    chat.build("{material}", materialName),
                    chat.build("{amount}", amount));
            return false;
        }

        chat.send(sender, "Give.Success-Other-Player",
                chat.build("{player}", t.getName()),
                chat.build("{material}", materialName),
                chat.build("{amount}", amount));
        bar.send(sender, "Give.Success-Other-Player-Bar",
                chat.build("{player}", t.getName()),
                chat.build("{material}", materialName),
                chat.build("{amount}", amount));
        title.send(sender, "Give.Success-Other-Player-Title",
                chat.build("{player}", t.getName()),
                chat.build("{material}", material.name()),
                chat.build("{amount}", amount));

        chat.send(t, "Give.Receive",
                chat.build("{executor}", sender.getName()),
                chat.build("{material}", materialName),
                chat.build("{amount}", amount));
        bar.send(t, "Give.Receive-Bar",
                chat.build("{executor}", sender.getName()),
                chat.build("{material}", materialName),
                chat.build("{amount}", amount));
        title.send(t, "Give.Receive-Title",
                chat.build("{executor}", sender.getName()),
                chat.build("{material}", materialName),
                chat.build("{amount}", amount));
        return false;
    }

    private String getName(String name) {
        return name
                .replaceAll("(?i)name:", "")
                .replace("&", "\u00A7")
                .replace("_", " ");
    }

    private List<String> getLore(String lore) {
        List<String> list = new ArrayList<>();
        String[] split = lore.split(",");
        for (String line : split) {
            line = line
                    .replaceAll("(?i)lore:", "")
                    .replace("&", "\u00A7")
                    .replace("_", " ");
            list.add(line);
        }
        return list;
    }

    private List<String> getEnchant(String enchant) {
        String[] split = enchant.replaceAll("(?i)enchant:", "").split(",");
        return new ArrayList<>(asList(split));
    }

    private List<String> getPotion(String effect) {
        String[] split = effect.replaceAll("(?i)potion:", "").split(",");
        return new ArrayList<>(asList(split));
    }

    private int getSlots(Inventory inv, int size, Material material) {
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

    private void giveItem(Inventory inv, Material material, int data, int amount, int giveAmount, String name, List<String> lore, List<String> enchant, List<String> potion) {
        int remainder = amount % giveAmount;
        for (int i=amount/giveAmount; i>0; i--) {
            inv.addItem(new ItemBuilder(material, giveAmount, data)
                    .setName(name)
                    .setLore(lore)
                    .addEnchant(enchant)
                    .addPotion(potion)
                    .toItemStack());
        }
        if (remainder == 0) return;
        inv.addItem(new ItemBuilder(material, remainder, data)
                .setName(name)
                .setLore(lore)
                .addEnchant(enchant)
                .addPotion(potion)
                .toItemStack());
    }
}
