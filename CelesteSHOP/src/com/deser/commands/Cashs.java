package com.deser.commands;

import com.deser.Main;
import com.deser.database.query.Query;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Cashs implements CommandExecutor {
    private Query query = new Query();
    private Plugin pl = Main.getPlugin(Main.class);

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        Player p = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("cashs")) {
            switch (args.length) {
                case 3:
                    //Add points (Admin command)
                    if (args[0].equals("add") || args[0].equals("adicionar")) {
                        if (p.hasPermission("DeserCash.admin")) {
                            try {
                                Player t = pl.getServer().getPlayer(args[1]);
                                int i = Integer.parseInt(args[2]);
                                if (query.playerExists(t)) {
                                    query.updateCashs(t,i + query.getCashs(t));
                                }
                                else {
                                    query.createPlayer(t, i);
                                }
                                p.sendMessage(ChatColor.YELLOW + "Pontos adicionados com sucesso.");
                           } catch (Exception e) {
                                p.sendMessage(ChatColor.YELLOW + "Argumentos invalidos.");
                               return false;
                           }
                        }
                        else {
                            p.sendMessage(ChatColor.YELLOW + "Você não tem permissão.");
                        }
                        return false;
                    }
                    //Give points (Player command)
                    else if (args[0].equals("give") || args[0].equals("enviar") || args[0].equals("pay")) {
                        try {
                            Player t = pl.getServer().getPlayer(args[1]);
                            int i = Integer.parseInt(args[2]);
                            if (i >= query.getCashs(p)) {
                                if (t != p) {
                                    if (query.playerExists(t)) {
                                        query.updateCashs(p, query.getCashs(p) - i);
                                        query.updateCashs(t, query.getCashs(p) + i);
                                        p.sendMessage(ChatColor.YELLOW + "Pontos enviados com sucesso.");
                                        t.sendMessage(ChatColor.YELLOW + "Você recebeu" + i + "pontos.");
                                    } 
                                    else {
                                        query.updateCashs(p, query.getCashs(p) - i);
                                        query.createPlayer(t, i);
                                        p.sendMessage(ChatColor.YELLOW + "Pontos enviados com sucesso.");
                                        t.sendMessage(ChatColor.YELLOW + "Você recebeu" + i + "pontos.");
                                    }
                                } 
                                else {
                                    p.sendMessage(ChatColor.YELLOW + "Você não pode enviar cashs para você mesmo.");
                                }
                            }
                            else {
                                p.sendMessage(ChatColor.YELLOW + "Você não tem essa quantia de pontos.");
                            }
                        } catch (Exception e) {
                            p.sendMessage(ChatColor.YELLOW + "Argumentos invalidos.");
                            return false;
                        }
                        return false;
                    }
                    return false;
                //Check own points (Player command)
                case 1:
                    Player t = pl.getServer().getPlayer(args[0]);
                    try {
                        if (query.playerExists(t)) {
                            p.sendMessage(ChatColor.YELLOW + "O jogador " + t.getName() + " possui " + query.getCashs(t) + " pontos.");
                            return false;
                        }
                    } catch (Exception e) {
                        p.sendMessage(ChatColor.YELLOW + "O jogador " + args[0] + " possui 0 pontos.");
                        return false;
                    }

                case 0:
                    try {
                        if (query.playerExists(p)) {
                            p.sendMessage(ChatColor.YELLOW + "Você possui " + query.getCashs(p) + " pontos.");
                            return false;
                        }
                    } catch (Exception e) {
                        p.sendMessage(ChatColor.YELLOW + "Você possui 0 pontos.");
                        return false;
                    }
                default:
                    p.sendMessage(ChatColor.YELLOW + "Argumentos invalidos");
                    return false;
            }
        }
        return false;
    }
}
