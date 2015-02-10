package me.lordsaad.trillium.commands;

import me.lordsaad.trillium.messageutils.Crit;
import me.lordsaad.trillium.messageutils.MType;
import me.lordsaad.trillium.messageutils.Message;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandGamemode implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("gamemode")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (args.length == 1) {
                    if (p.hasPermission("tr.gamemode")) {

                        if (args[0].equalsIgnoreCase("creative") || args[0].equalsIgnoreCase("1") || args[0].equalsIgnoreCase("c")) {
                            Message.m(MType.G, p, "Gamemode", "Gamemode set to " + ChatColor.AQUA + "creative");
                            p.setGameMode(GameMode.CREATIVE);

                        } else if (args[0].equalsIgnoreCase("survival") || args[0].equalsIgnoreCase("0") || args[0].equalsIgnoreCase("s")) {
                            Message.m(MType.G, p, "Gamemode", "Gamemode set to " + ChatColor.AQUA + "survival");
                            p.setGameMode(GameMode.SURVIVAL);

                        } else if (args[0].equalsIgnoreCase("adventure") || args[0].equalsIgnoreCase("2") || args[0].equalsIgnoreCase("a")) {
                            Message.m(MType.G, p, "Gamemode", "Gamemode set to " + ChatColor.AQUA + "adventure");
                            p.setGameMode(GameMode.ADVENTURE);

                        } else if (args[0].equalsIgnoreCase("spectator") || args[0].equalsIgnoreCase("3") || args[0].equalsIgnoreCase("sp")) {
                            Message.m(MType.G, p, "Gamemode", "Gamemode set to " + ChatColor.AQUA + "spectator");
                            p.setGameMode(GameMode.SPECTATOR);

                        } else {
                            Message.m(MType.W, p, "Gamemode", "Mojang didn't add that gamemode yet...");
                        }

                    } else {
                        Message.e(p, "Gamemode", Crit.P);
                    }

                } else if (args.length > 1) {

                    if (p.hasPermission("tr.gamemode.other")) {
                        Player pl = Bukkit.getPlayer(args[1]);
                        if (pl != null) {

                            if (args[0].equalsIgnoreCase("creative") || args[0].equalsIgnoreCase("1") || args[0].equalsIgnoreCase("c")) {
                                Message.m(MType.G, p, "Gamemode", pl.getName() + "'s gamemode set to " + ChatColor.AQUA + "creative");
                                Message.m(MType.G, pl, "Gamemode", p.getName() + "set your gamemode to " + ChatColor.AQUA + "creative");
                                pl.setGameMode(GameMode.CREATIVE);

                            } else if (args[0].equalsIgnoreCase("survival") || args[0].equalsIgnoreCase("0") || args[0].equalsIgnoreCase("s")) {
                                Message.m(MType.G, p, "Gamemode", pl.getName() + "'s gamemode set to " + ChatColor.AQUA + "survival");
                                Message.m(MType.G, pl, "Gamemode", p.getName() + "set your gamemode to " + ChatColor.AQUA + "survival");
                                pl.setGameMode(GameMode.SURVIVAL);

                            } else if (args[0].equalsIgnoreCase("adventure") || args[0].equalsIgnoreCase("2") || args[0].equalsIgnoreCase("a")) {
                                Message.m(MType.G, p, "Gamemode", pl.getName() + "'s gamemode set to " + ChatColor.AQUA + "adventure");
                                Message.m(MType.G, pl, "Gamemode", p.getName() + "set your gamemode to " + ChatColor.AQUA + "adventure");
                                pl.setGameMode(GameMode.ADVENTURE);

                            } else if (args[0].equalsIgnoreCase("spectator") || args[0].equalsIgnoreCase("3") || args[0].equalsIgnoreCase("sp")) {
                                Message.m(MType.G, p, "Gamemode", pl.getName() + "'s gamemode set to " + ChatColor.AQUA + "spectator");
                                Message.m(MType.G, pl, "Gamemode", p.getName() + "set your gamemode to " + ChatColor.AQUA + "spectator");
                                pl.setGameMode(GameMode.SPECTATOR);

                            } else {
                                Message.m(MType.W, p, "Gamemode", "Mojang didn't add that gamemode yet...");
                            }

                        } else {
                            Message.eplayer(p, "Gamemode", args[1]);
                        }
                    } else {
                        Message.earg(p, "Gamemode", "/gm [1/2/3/4/survival/creative/adventure/spectator/s/c/a/sp] [player]");

                    }

                } else {
                    if (p.hasPermission("tr.gamemode")) {
                        if (p.getGameMode() == GameMode.CREATIVE) {
                            p.setGameMode(GameMode.SURVIVAL);
                            Message.m(MType.G, p, "Gamemode", "Gamemode set to " + ChatColor.AQUA + "survival.");
                        } else {
                            p.setGameMode(GameMode.CREATIVE);
                            Message.m(MType.G, p, "Gamemode", "Gamemode set to " + ChatColor.AQUA + "creative.");
                        }
                    } else {
                        Message.e(p, "Gamemode", Crit.P);
                    }
                }
            } else {
                Message.e(sender, "Gamemode", Crit.C);
            }
        }
        return true;
    }
}