package com.redeceleste.celestehomes.managers;

import com.redeceleste.celestehomes.Main;
import com.redeceleste.celestehomes.models.InventoryArgument;
import com.redeceleste.celestehomes.models.impls.Inventory;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigManager {
    public static String Permission, NoPermission, Reload, MinimumCharactersHome, Delay, DelayFromOtherTeleport;
    public static String SetHomeArgumentsInvalid, BlackListWorldMessage, HomeLimitReached, FewCharacters, ContainsHome, HomeSucessCreate;
    public static String DelHomeArgumentsInvalid, DelHomeNotFound, HomeSucessDeleted;
    public static String HomeNotFound, HomeInvalidArgument, DelayFromOtherTeleportMessage, PlayerNotFound, MessageWaitingTeleportTitle, MessageWaitingTeleportSubTitle, MessageSucessTeleportTitle, MessageSucessTeleportSubTitle, MessageCancelTeleportTitle, MessageCancelTeleportSubTitle;
    public static String SoundWaitingTeleport, SoundSucessTeleport, SoundCancelTeleport;
    public static String TitleGUI;
    public static List<InventoryArgument> Itens = new ArrayList<>();
    public static List<InventoryArgument> Template = new ArrayList<>();
    public static List<String> BlackList = new ArrayList<>();

    private static List<String> lore, en = new ArrayList<>();
    private static final Plugin pl = Main.getPlugin(Main.class);

    public static void loadMessage() {
        BlackList = getList("BlackListWorld");

        Permission = get("Permission");
        NoPermission = get("NoPermission");
        Reload = get("Reload");
        MinimumCharactersHome = get("MinimumCharactersHome");
        Delay = get("Delay");
        DelayFromOtherTeleport = get("DelayFromOtherTeleport");

        SetHomeArgumentsInvalid = get("Message.SetHomeArgumentsInvalid");
        BlackListWorldMessage = get("Message.BlackListWorldMessage");
        HomeLimitReached = get("Message.HomeLimitReached");
        FewCharacters = get("Message.FewCharacters");
        ContainsHome = get("Message.ContainsHome");
        HomeSucessCreate = get("Message.HomeSucessCreate");

        DelHomeArgumentsInvalid = get("Message.DelHomeArgumentsInvalid");
        DelHomeNotFound = get("Message.DelHomeNotFound");
        HomeSucessDeleted = get("Message.HomeSucessDeleted");

        HomeInvalidArgument = get("Message.HomeInvalidArgument");
        HomeNotFound = get("Message.HomeNotFound");
        DelayFromOtherTeleportMessage = get("Message.DelayMessage");
        PlayerNotFound = get("Message.PlayerNotFound");

        MessageWaitingTeleportTitle = get("Message.MessageSucessTeleport.Title");
        MessageWaitingTeleportSubTitle = get("Message.MessageSucessTeleport.SubTitle");
        MessageSucessTeleportTitle = get("Message.MessageSucessTeleport.Title");
        MessageSucessTeleportSubTitle = get("Message.MessageSucessTeleport.SubTitle");
        MessageCancelTeleportTitle = get("Message.MessageCancelTeleport.Title");
        MessageCancelTeleportSubTitle = get("Message.MessageCancelTeleport.SubTitle");

        SoundWaitingTeleport = get("Sounds.SoundWaitingTeleport");
        SoundSucessTeleport = get("Sounds.SoundSucessTeleport");
        SoundCancelTeleport = get("Sounds.SoundCancelTeleport");

        TitleGUI = get("Inventory.Title");

        getItens();
        getTemplate();
    }

    private static String get(String path) {
        return pl.getConfig().getString(path, ChatColor.DARK_RED + "There was an error loading the message: " + ChatColor.YELLOW + path).replace('&', '\u00A7');
    }

    private static List<String> getList(String path) {
        return pl.getConfig().getStringList(path);
    }

    private static void getItens() {
        for (String menu : Main.getInstance().getConfig().getConfigurationSection("Inventory.CustomInventory").getKeys(false)) {
            Integer slot = Integer.parseInt(get("Inventory.CustomInventory." + menu + ".Slot"));
            Integer amount = Integer.parseInt(get("Inventory.CustomInventory." + menu + ".Amount"));
            Material material = Material.valueOf(get("Inventory.CustomInventory." + menu + ".Material"));
            Integer data = Integer.valueOf(get("Inventory.CustomInventory." + menu + ".Data"));
            String name = get("Inventory.CustomInventory." + menu + ".Name").replace("&", "§");
            Boolean glow = Boolean.parseBoolean(get("Inventory.CustomInventory." + menu + ".Glow"));
            lore = Main.getInstance().getConfig().getConfigurationSection("Inventory.CustomInventory.").getStringList(menu + ".Lore").stream().map(lore -> ChatColor.translateAlternateColorCodes('&', lore)).collect(Collectors.toList());
            en = Main.getInstance().getConfig().getConfigurationSection("Inventory.CustomInventory.").getStringList(menu + ".Enchantment");
            Itens.add(new Inventory(name, material, data, slot, amount, glow, lore, en));
        }
    }

    private static void getTemplate() {
        Material material = Material.valueOf(get("Inventory.HomeTemplate.Material"));
        Integer data = Integer.valueOf(get("Inventory.HomeTemplate.Data"));
        String name = get("Inventory.HomeTemplate.Name");
        Boolean glow = Boolean.parseBoolean(get("Inventory.HomeTemplate.Glow"));
        lore = getList("Inventory.HomeTemplate.Lore").stream().map(lore -> ChatColor.translateAlternateColorCodes('&', lore)).collect(Collectors.toList());
        en = getList("Inventory.HomeTemplate.Enchantment");

        Template.add(new Inventory(name, material, data, glow, lore, en));
    }
}