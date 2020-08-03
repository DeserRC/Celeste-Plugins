package com.deser.commands;

import com.deser.Main;
import com.deser.database.query.Query;
import com.deser.listener.InventoryArguments;
import com.deser.listener.InventoryClick;
import com.deser.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Shop implements CommandExecutor {
    private Plugin pl = Main.getPlugin(Main.class);
    private List<String> lore = new ArrayList<>();
    private List<String> en = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        Player p = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("shop")) {
            openInventory(p);
        }
        return false;
    }

    public void openInventory(Player p) {
        Query query = new Query();
        Inventory inventory = Bukkit.createInventory(null, getRows(), getTitle());
        InventoryClick inventoryClick = new InventoryClick();

        //Get Config Itens
        for (String menu : pl.getConfig().getConfigurationSection("Shop.").getKeys(false)) {
            String name = pl.getConfig().getString("Shop." + menu + ".Name");
            Material material = Material.getMaterial(pl.getConfig().getString("Shop." + menu + ".Material"));
            String titlebuy = pl.getConfig().getString("Shop." + menu + ".BuyMessage.Title");
            String subtitlebuy = pl.getConfig().getString("Shop." + menu + ".BuyMessage.SubTile");
            String actionbarmessage = pl.getConfig().getString("Shop." + menu + ".BuyMessage.ActionBarMessage");
            String cmd = pl.getConfig().getString("Shop." + menu + ".Command");
            int data = pl.getConfig().getInt("Shop." + menu + ".Data");
            int amount = pl.getConfig().getInt("Shop." + menu + ".Amount");
            int slot = pl.getConfig().getInt("Shop." + menu + ".Slot");
            int price = pl.getConfig().getInt("Shop." + menu + ".Price");
            lore = pl.getConfig().getConfigurationSection("Shop.").getStringList(menu + ".Lore").stream().map(lore -> ChatColor.translateAlternateColorCodes('&', lore)).collect(Collectors.toList());
            en = pl.getConfig().getConfigurationSection("Shop.").getStringList(menu + ".Enchantament").stream().map(en -> ChatColor.translateAlternateColorCodes('&', en)).collect(Collectors.toList());

            inventory.setItem(slot, new ItemBuilder(material, amount)
                    .setData(data)
                    .setName(name.replace("&", "§"))
                    .setLore(lore)
                    .addEnchants(en)
                    .toItemStack());
            InventoryArguments ia = new InventoryArguments(cmd, price, slot, titlebuy.replace("&", "§"), subtitlebuy.replace("&", "§"), actionbarmessage.replace("&", "§"));
            inventoryClick.addList(ia);
        }
        inventory.setItem(4, new ItemBuilder(Material.EMERALD).setName(ChatColor.GREEN + "Você possui " + query.getCashs(p) + " cashs").toItemStack());
        p.openInventory(inventory);
    }

    //Get Config Menu
    public final Integer getRows() {
        return 9*pl.getConfig().getInt("Menu.Rows");
    }

    public final String getTitle() {
        return pl.getConfig().getString("Menu.Title").replace("&", "§");
    }

    public final String getInsufficientCash() {
        return pl.getConfig().getString("Menu.InsufficientCash").replace("&", "§");
    }
}