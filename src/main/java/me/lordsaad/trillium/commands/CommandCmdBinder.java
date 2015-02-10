package me.lordsaad.trillium.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import me.lordsaad.trillium.messageutils.Crit;
import me.lordsaad.trillium.messageutils.MType;
import me.lordsaad.trillium.messageutils.Message;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandCmdBinder implements CommandExecutor {

    public static HashMap<UUID, String> tcmdbconsole = new HashMap<UUID, String>();
    public static HashMap<UUID, String> tcmdbplayer = new HashMap<UUID, String>();
    public static HashMap<UUID, String> wcmdbplayer = new HashMap<UUID, String>();
    public static HashMap<UUID, String> wcmdbconsole = new HashMap<UUID, String>();

    public static HashMap<Location, String> touchconsole = new HashMap<Location, String>();
    public static HashMap<Location, String> touchplayer = new HashMap<Location, String>();
    public static HashMap<Location, String> walkconsole = new HashMap<Location, String>();
    public static HashMap<Location, String> walkplayer = new HashMap<Location, String>();

    public static Map<UUID, Map<ItemStack, String>> itemconsole = new HashMap<UUID, Map<ItemStack, String>>();
    public static Map<UUID, Map<ItemStack, String>> itemplayer = new HashMap<UUID, Map<ItemStack, String>>();

    public static ArrayList<Location> antilagcheckloc = new ArrayList<Location>();
    public static ArrayList<UUID> antilagcheckcmd = new ArrayList<UUID>();
    public static ArrayList<UUID> antilagcheckitem = new ArrayList<UUID>();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("commandbinder")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (sender.hasPermission("tr.commandbinder")) {
                    if (args.length < 3) {
                        Message.earg(p, "Cmd Binder", "/cb <touch/walk/item> <console/player> <command>");
                        Message.m(MType.W, p, "Cmd Binder", "or /cb <t/w/i> <c/p> <command>");
                        Message.m(MType.W, p, "Cmd Binder", "Example: /cb t c tp [p] 110 45 247");
                    } else {

                        if (args[0].equalsIgnoreCase("touch") || args[0].equalsIgnoreCase("t")) {
                            if (args[1].equalsIgnoreCase("console") || args[1].equalsIgnoreCase("c")) {

                                StringBuilder sb = new StringBuilder();
                                for (int i = 2; i < args.length; i++) {
                                    sb.append(args[i]).append(" ");
                                }
                                String msg = sb.toString().trim();

                                tcmdbconsole.put(p.getUniqueId(), msg);

                                Message.m(MType.R, p, "Cmd Binder", "Punch a block to bind the command to it.");
                                Message.m(MType.R, p, "Cmd Binder", "WARNING: Do not touch anything aimlessly!");
                                Message.m(MType.R, p, "Cmd Binder", "The first thing you touch now will bind the command you want");
                                Message.m(MType.R, p, "Cmd Binder", "to the block you punch next. Be careful where you punch!");
                                antilagcheckcmd.add(p.getUniqueId());

                            } else if (args[1].equalsIgnoreCase("player") || args[1].equalsIgnoreCase("p")) {

                                StringBuilder sb = new StringBuilder();
                                for (int i = 2; i < args.length; i++) {
                                    sb.append(args[i]).append(" ");
                                }
                                String msg = sb.toString().trim();

                                tcmdbplayer.put(p.getUniqueId(), msg);

                                Message.m(MType.R, p, "Cmd Binder", "Punch a block to bind the command to it.");
                                Message.m(MType.R, p, "Cmd Binder", "WARNING: Do not touch anything aimlessly!");
                                Message.m(MType.R, p, "Cmd Binder", "The first thing you touch now will bind the command you want");
                                Message.m(MType.R, p, "Cmd Binder", "to the block you punch next. Be careful where you punch!");
                                antilagcheckcmd.add(p.getUniqueId());

                            }
                        } else if (args[0].equalsIgnoreCase("walk") || args[0].equalsIgnoreCase("w")) {

                            if (args[1].equalsIgnoreCase("console") || args[1].equalsIgnoreCase("c")) {

                                StringBuilder sb = new StringBuilder();
                                for (int i = 2; i < args.length; i++) {
                                    sb.append(args[i]).append(" ");
                                }
                                String msg = sb.toString().trim();

                                wcmdbconsole.put(p.getUniqueId(), msg);

                                Message.m(MType.R, p, "Cmd Binder", "Right click a block to bind the command to it.");
                                Message.m(MType.R, p, "Cmd Binder", "WARNING: Do not touch anything aimlessly!");
                                Message.m(MType.R, p, "Cmd Binder", "The first block you right click will bind the command to the adjacent empty side");
                                Message.m(MType.R, p, "Cmd Binder", "so walking through that empty air block adjacent to the block you right clicked");
                                Message.m(MType.R, p, "Cmd Binder", "will make the command get run.");
                                antilagcheckcmd.add(p.getUniqueId());

                            } else if (args[1].equalsIgnoreCase("player") || args[1].equalsIgnoreCase("p")) {

                                StringBuilder sb = new StringBuilder();
                                for (int i = 2; i < args.length; i++) {
                                    sb.append(args[i]).append(" ");
                                }
                                String msg = sb.toString().trim();

                                wcmdbplayer.put(p.getUniqueId(), msg);

                                Message.m(MType.R, p, "Cmd Binder", "Right click a block to bind the command to it.");
                                Message.m(MType.R, p, "Cmd Binder", "WARNING: Do not touch anything aimlessly!");
                                Message.m(MType.R, p, "Cmd Binder", "The first block you right click will bind the command to the adjacent empty side");
                                Message.m(MType.R, p, "Cmd Binder", "so walking through that empty air block adjacent to the block you right clicked");
                                Message.m(MType.R, p, "Cmd Binder", "will make the command get run.");
                                antilagcheckcmd.add(p.getUniqueId());

                            }
                        } else if (args[0].equalsIgnoreCase("item") || args[0].equalsIgnoreCase("i")) {

                            if (args[1].equalsIgnoreCase("console") || args[1].equalsIgnoreCase("c")) {

                                if (p.getItemInHand().getType() != null
                                        || p.getItemInHand().getType() != Material.AIR) {

                                    StringBuilder sb = new StringBuilder();
                                    for (int i = 2; i < args.length; i++) {
                                        sb.append(args[i]).append(" ");
                                    }
                                    String msg = sb.toString().trim();

                                    Map<ItemStack, String> iands = new HashMap<ItemStack, String>();
                                    iands.put(p.getItemInHand(), msg);
                                    itemconsole.put(p.getUniqueId(), iands);
                                    antilagcheckitem.add(p.getUniqueId());
                                    Message.m(MType.G, p, "Cmd Binder", "Command successfully bound to item.");
                                    Message.m(MType.G, p, "Cmd Binder", ChatColor.AQUA + CommandCmdBinder.tcmdbconsole.get(p.getUniqueId()));
                                }

                            } else if (args[1].equalsIgnoreCase("player") || args[1].equalsIgnoreCase("p")) {

                                if (p.getItemInHand().getType() != null
                                        || p.getItemInHand().getType() != Material.AIR) {

                                    StringBuilder sb = new StringBuilder();
                                    for (int i = 2; i < args.length; i++) {
                                        sb.append(args[i]).append(" ");
                                    }
                                    String msg = sb.toString().trim();

                                    Map<ItemStack, String> iands = new HashMap<ItemStack, String>();
                                    iands.put(p.getItemInHand(), msg);
                                    itemplayer.put(p.getUniqueId(), iands);
                                    antilagcheckitem.add(p.getUniqueId());
                                    Message.m(MType.G, p, "Cmd Binder", "Command successfully bound to item.");
                                    Message.m(MType.G, p, "Cmd Binder", ChatColor.AQUA + CommandCmdBinder.tcmdbconsole.get(p.getUniqueId()));
                                }
                            }
                        } else {
                            Message.earg(p, "Cmd Binder", "/cb <touch/walk/item> <console/player> <command>");
                        }
                    }
                } else {
                    Message.e(p, "Cmd Binder", Crit.P);
                }
            } else {
                Message.e(sender, "Cmd Binder", Crit.C);
            }
        }
        return true;
    }
}