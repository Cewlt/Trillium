package net.gettrillium.trillium.modules;

import net.gettrillium.trillium.api.Permission;
import net.gettrillium.trillium.api.TrilliumAPI;
import net.gettrillium.trillium.api.TrilliumModule;
import net.gettrillium.trillium.api.command.Command;
import net.gettrillium.trillium.api.player.TrilliumPlayer;
import net.gettrillium.trillium.databases.CmdBinderDatabase;
import net.gettrillium.trillium.messageutils.Message;
import net.gettrillium.trillium.messageutils.Type;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.*;

public class CmdBinderModule extends TrilliumModule {

    public static HashMap<UUID, String> tcmdbconsole = new HashMap<>();
    public static HashMap<UUID, String> tcmdbplayer = new HashMap<>();
    public static HashMap<UUID, String> wcmdbplayer = new HashMap<>();
    public static HashMap<UUID, String> wcmdbconsole = new HashMap<>();
    public static HashMap<Location, String> touchconsole = new HashMap<>();
    public static HashMap<Location, String> touchplayer = new HashMap<>();
    public static HashMap<Location, String> walkconsole = new HashMap<>();
    public static HashMap<Location, String> walkplayer = new HashMap<>();
    public static Map<UUID, Map<ItemStack, String>> itemconsole = new HashMap<>();
    public static Map<UUID, Map<ItemStack, String>> itemplayer = new HashMap<>();
    public static ArrayList<Location> antilagcheckloc = new ArrayList<>();
    public static ArrayList<UUID> antilagcheckcmd = new ArrayList<>();
    public static ArrayList<UUID> antilagcheckitem = new ArrayList<>();

    public CmdBinderModule() {
        super("cmdbinder");
    }

    @Command(command = "commandbinder", description = "Bind a command to a block, an air block, or an item.", usage = "/cb <touch/walk/item> <console/player> <command>", aliases = "cmdbinder, cmdb, cb, cbinder, cbind")
    public void commandbinder(CommandSender cs, String[] args) {
        if (cs instanceof Player) {
            TrilliumPlayer p = player((Player) cs);
            if (p.hasPermission(Permission.Admin.CMDBINDER)) {

                if (args.length < 3) {
                    Message.error(p.getProxy(), "Cmd Binder", true, "/cb <touch/walk/item> <console/player> <command>");
                    Message.message(Type.WARNING, p.getProxy(), "Cmd Binder", true, "or /cb <t/w/i> <c/p> <command>");
                    Message.message(Type.WARNING, p.getProxy(), "Cmd Binder", true, "Example: /cb t c tp [p] 110 45 247");
                } else {

                    if (args[0].equalsIgnoreCase("touch") || args[0].equalsIgnoreCase("t")) {
                        if (args[1].equalsIgnoreCase("console") || args[1].equalsIgnoreCase("c")) {

                            StringBuilder sb = new StringBuilder();
                            for (int i = 2; i < args.length; i++) {
                                sb.append(args[i]).append(" ");
                            }
                            String msg = sb.toString().trim();

                            tcmdbconsole.put(p.getProxy().getUniqueId(), msg);

                            Message.message(Type.GENERIC, p.getProxy(), "Cmd Binder", true, "Punch a block to bind the command to it.");
                            Message.message(Type.GENERIC, p.getProxy(), "Cmd Binder", true, "WARNING: Do not touch anything aimlessly!");
                            Message.message(Type.GENERIC, p.getProxy(), "Cmd Binder", true, "The first thing you touch now will bind the command you want");
                            Message.message(Type.GENERIC, p.getProxy(), "Cmd Binder", true, "to the block you punch next. Be careful where you punch!");
                            antilagcheckcmd.add(p.getProxy().getUniqueId());

                        } else if (args[1].equalsIgnoreCase("player") || args[1].equalsIgnoreCase("p")) {

                            StringBuilder sb = new StringBuilder();
                            for (int i = 2; i < args.length; i++) {
                                sb.append(args[i]).append(" ");
                            }
                            String msg = sb.toString().trim();

                            tcmdbplayer.put(p.getProxy().getUniqueId(), msg);

                            Message.message(Type.GENERIC, p.getProxy(), "Cmd Binder", true, "Punch a block to bind the command to it.");
                            Message.message(Type.GENERIC, p.getProxy(), "Cmd Binder", true, "WARNING: Do not touch anything aimlessly!");
                            Message.message(Type.GENERIC, p.getProxy(), "Cmd Binder", true, "The first thing you touch now will bind the command you want");
                            Message.message(Type.GENERIC, p.getProxy(), "Cmd Binder", true, "to the block you punch next. Be careful where you punch!");
                            antilagcheckcmd.add(p.getProxy().getUniqueId());

                        }
                    } else if (args[0].equalsIgnoreCase("walk") || args[0].equalsIgnoreCase("w")) {

                        if (args[1].equalsIgnoreCase("console") || args[1].equalsIgnoreCase("c")) {

                            StringBuilder sb = new StringBuilder();
                            for (int i = 2; i < args.length; i++) {
                                sb.append(args[i]).append(" ");
                            }
                            String msg = sb.toString().trim();

                            wcmdbconsole.put(p.getProxy().getUniqueId(), msg);

                            Message.message(Type.GENERIC, p.getProxy(), "Cmd Binder", true, "Right click a block to bind the command to it.");
                            Message.message(Type.GENERIC, p.getProxy(), "Cmd Binder", true, "WARNING: Do not touch anything aimlessly!");
                            Message.message(Type.GENERIC, p.getProxy(), "Cmd Binder", true, "The first block you right click will bind the command to the adjacent empty side");
                            Message.message(Type.GENERIC, p.getProxy(), "Cmd Binder", true, "so walking through that empty air block adjacent to the block you right clicked");
                            Message.message(Type.GENERIC, p.getProxy(), "Cmd Binder", true, "will make the command get run.");
                            antilagcheckcmd.add(p.getProxy().getUniqueId());

                        } else if (args[1].equalsIgnoreCase("player") || args[1].equalsIgnoreCase("p")) {

                            StringBuilder sb = new StringBuilder();
                            for (int i = 2; i < args.length; i++) {
                                sb.append(args[i]).append(" ");
                            }
                            String msg = sb.toString().trim();

                            wcmdbplayer.put(p.getProxy().getUniqueId(), msg);

                            Message.message(Type.GENERIC, p.getProxy(), "Cmd Binder", true, "Right click a block to bind the command to it.");
                            Message.message(Type.GENERIC, p.getProxy(), "Cmd Binder", true, "WARNING: Do not touch anything aimlessly!");
                            Message.message(Type.GENERIC, p.getProxy(), "Cmd Binder", true, "The first block you right click will bind the command to the adjacent empty side");
                            Message.message(Type.GENERIC, p.getProxy(), "Cmd Binder", true, "so walking through that empty air block adjacent to the block you right clicked");
                            Message.message(Type.GENERIC, p.getProxy(), "Cmd Binder", true, "will make the command get run.");
                            antilagcheckcmd.add(p.getProxy().getUniqueId());

                        }
                    } else if (args[0].equalsIgnoreCase("item") || args[0].equalsIgnoreCase("i")) {

                        if (args[1].equalsIgnoreCase("console") || args[1].equalsIgnoreCase("c")) {

                            if (p.getProxy().getItemInHand().getType() != null
                                    || p.getProxy().getItemInHand().getType() != Material.AIR) {

                                StringBuilder sb = new StringBuilder();
                                for (int i = 2; i < args.length; i++) {
                                    sb.append(args[i]).append(" ");
                                }
                                String msg = sb.toString().trim();

                                Map<ItemStack, String> iands = new HashMap<ItemStack, String>();
                                iands.put(p.getProxy().getItemInHand(), msg);
                                itemconsole.put(p.getProxy().getUniqueId(), iands);
                                antilagcheckitem.add(p.getProxy().getUniqueId());
                                Message.message(Type.GOOD, p.getProxy(), "Cmd Binder", true, "Command successfully bound to item.");
                                Message.message(Type.GOOD, p.getProxy(), "Cmd Binder", true, ChatColor.AQUA + tcmdbconsole.get(p.getProxy().getUniqueId()));
                            }

                        } else if (args[1].equalsIgnoreCase("player") || args[1].equalsIgnoreCase("p")) {

                            if (p.getProxy().getItemInHand().getType() != null
                                    || p.getProxy().getItemInHand().getType() != Material.AIR) {

                                StringBuilder sb = new StringBuilder();
                                for (int i = 2; i < args.length; i++) {
                                    sb.append(args[i]).append(" ");
                                }
                                String msg = sb.toString().trim();

                                Map<ItemStack, String> iands = new HashMap<ItemStack, String>();
                                iands.put(p.getProxy().getItemInHand(), msg);
                                itemplayer.put(p.getProxy().getUniqueId(), iands);
                                antilagcheckitem.add(p.getProxy().getUniqueId());
                                Message.message(Type.GOOD, p.getProxy(), "Cmd Binder", true, "Command successfully bound to item.");
                                Message.message(Type.GOOD, p.getProxy(), "Cmd Binder", true, ChatColor.AQUA + tcmdbconsole.get(p.getProxy().getUniqueId()));
                            }
                        }
                    } else {
                        Message.error(p.getProxy(), "Cmd Binder", true, "/cb <touch/walk/item> <console/player> <command>");
                    }
                }
            } else {
                Message.error("Cmd Binder", cs);
            }
        } else {
            Message.error("Cmd Binder", cs);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();

        for (Location loc : antilagcheckloc) {
            if (event.getClickedBlock().getLocation().getBlockX() == loc.getBlockX()
                    && event.getClickedBlock().getLocation().getBlockY() == loc.getBlockY()
                    && event.getClickedBlock().getLocation().getBlockZ() == loc.getBlockZ()) {

                if (touchconsole.containsKey(p.getLocation())) {
                    String cmd = touchconsole.get(p.getLocation());
                    cmd = cmd.replace("[p]", p.getName());
                    Bukkit.dispatchCommand(TrilliumAPI.getInstance().getServer().getConsoleSender(), cmd);
                    event.setCancelled(true);

                } else if (touchplayer.containsKey(p.getLocation())) {
                    String cmd = touchplayer.get(p.getLocation());
                    cmd = cmd.replace("[p]", p.getName());
                    Bukkit.dispatchCommand(p, cmd);
                    event.setCancelled(true);
                }
            }
        }

        if (antilagcheckitem.contains(p.getUniqueId())) {
            if (itemconsole.containsKey(p.getUniqueId())) {

                Map<ItemStack, String> iands = itemconsole.get(p.getUniqueId());
                for (ItemStack item : iands.keySet()) {
                    String cmd = iands.get(item);
                    cmd = cmd.replace("[p]", p.getName());
                    if (p.getItemInHand().equals(item)) {
                        Bukkit.dispatchCommand(TrilliumAPI.getInstance().getServer().getConsoleSender(), cmd);
                        event.setCancelled(true);
                    }
                }

            } else if (itemplayer.containsKey(p.getUniqueId())) {
                Map<ItemStack, String> iands = itemplayer.get(p.getUniqueId());
                for (ItemStack item : iands.keySet()) {
                    String cmd = iands.get(item);
                    cmd = cmd.replace("[p]", p.getName());
                    if (p.getItemInHand().equals(item)) {
                        Bukkit.dispatchCommand(p, cmd);
                        event.setCancelled(true);
                    }
                }
            }
        }

        if (antilagcheckcmd.contains(p.getUniqueId())) {

            YamlConfiguration yml = YamlConfiguration.loadConfiguration(CmdBinderDatabase.cbd());

            if (tcmdbconsole.containsKey(p.getUniqueId())) {
                if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                    int x = event.getClickedBlock().getLocation().getBlockX();
                    int y = event.getClickedBlock().getLocation().getBlockZ();
                    int z = event.getClickedBlock().getLocation().getBlockZ();

                    List<String> l = new ArrayList<String>();
                    l.add(yml.getString("touchconsole"));
                    l.add(p.getWorld().getName() + "'" + x + ";" + y + "," + z + "/" + tcmdbconsole.get(p.getUniqueId()));
                    yml.set("touchconsole", l);
                    Message.message(Type.GOOD, p, "Cmd Binder", true, "Command successfully bound to block.");
                    Message.message(Type.GOOD, p, "Cmd Binder", true, ChatColor.AQUA + tcmdbconsole.get(p.getUniqueId()));
                    event.setCancelled(true);
                    tcmdbconsole.remove(p.getUniqueId());
                    try {
                        yml.save(CmdBinderDatabase.cbd());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            } else if (tcmdbplayer.containsKey(p.getUniqueId())) {
                if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                    int x = event.getClickedBlock().getLocation().getBlockX();
                    int y = event.getClickedBlock().getLocation().getBlockZ();
                    int z = event.getClickedBlock().getLocation().getBlockZ();

                    List<String> l = new ArrayList<String>();
                    l.add(yml.getString("touchplayer"));
                    l.add(p.getWorld().getName() + "'" + x + ";" + y + "," + z + "/" + tcmdbplayer.get(p.getUniqueId()));
                    yml.set("touchplayer", l);
                    Message.message(Type.GOOD, p, "Cmd Binder", true, "Command successfully bound to block.");
                    Message.message(Type.GOOD, p, "Cmd Binder", true, ChatColor.AQUA + tcmdbconsole.get(p.getUniqueId()));
                    event.setCancelled(true);
                    tcmdbplayer.remove(p.getUniqueId());
                    try {
                        yml.save(CmdBinderDatabase.cbd());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (wcmdbconsole.containsKey(p.getUniqueId())) {
                if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (event.getClickedBlock().getRelative(event.getBlockFace()).getType() == null
                            || event.getClickedBlock().getRelative(event.getBlockFace()).getType() == Material.AIR) {
                        int x = event.getClickedBlock().getRelative(event.getBlockFace()).getLocation().getBlockX();
                        int y = event.getClickedBlock().getRelative(event.getBlockFace()).getLocation().getBlockY();
                        int z = event.getClickedBlock().getRelative(event.getBlockFace()).getLocation().getBlockZ();

                        List<String> l = new ArrayList<String>();
                        l.add(yml.getString("walkconsole"));
                        l.add(p.getWorld().getName() + "'" + x + ";" + y + "," + z + "/" + wcmdbconsole.get(p.getUniqueId()));
                        yml.set("walkconsole", l);
                        Message.message(Type.GOOD, p, "Cmd Binder", true, "Command successfully bound to air block.");
                        Message.message(Type.GOOD, p, "Cmd Binder", true, ChatColor.AQUA + tcmdbconsole.get(p.getUniqueId()));
                        event.setCancelled(true);
                        wcmdbconsole.remove(p.getUniqueId());
                        try {
                            yml.save(CmdBinderDatabase.cbd());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else if (wcmdbplayer.containsKey(p.getUniqueId())) {
                if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (event.getClickedBlock().getRelative(event.getBlockFace()).getType() == null
                            || event.getClickedBlock().getRelative(event.getBlockFace()).getType() == Material.AIR) {
                        int x = event.getClickedBlock().getRelative(event.getBlockFace()).getLocation().getBlockX();
                        int y = event.getClickedBlock().getRelative(event.getBlockFace()).getLocation().getBlockY();
                        int z = event.getClickedBlock().getRelative(event.getBlockFace()).getLocation().getBlockZ();

                        List<String> l = new ArrayList<String>();
                        l.add(yml.getString("walkplayer"));
                        l.add(p.getWorld().getName() + "'" + x + ";" + y + "," + z + "/" + wcmdbplayer.get(p.getUniqueId()));
                        yml.set("walkplayer", l);
                        Message.message(Type.GOOD, p, "Cmd Binder", true, "Command successfully bound to air block.");
                        Message.message(Type.GOOD, p, "Cmd Binder", true, ChatColor.AQUA + tcmdbconsole.get(p.getUniqueId()));
                        event.setCancelled(true);
                        wcmdbplayer.remove(p.getUniqueId());
                        try {
                            yml.save(CmdBinderDatabase.cbd());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player p = event.getPlayer();
        if (event.getFrom().getBlockX() != event.getTo().getBlockX() || event.getFrom().getBlockY() != event.getTo().getBlockY() || event.getFrom().getBlockZ() != event.getTo().getBlockZ()) {
            for (Location loc : antilagcheckloc) {
                if (p.getLocation().getBlockX() == loc.getBlockX() && p.getLocation().getBlockY() == loc.getBlockY() && p.getLocation().getBlockZ() == loc.getBlockZ()) {
                    if (walkconsole.containsKey(p.getLocation())) {
                        String cmd = walkconsole.get(p.getLocation());
                        cmd = cmd.replace("[p]", p.getName());
                        Bukkit.dispatchCommand(TrilliumAPI.getInstance().getServer().getConsoleSender(), cmd);

                    } else if (walkplayer.containsKey(p.getLocation())) {
                        String cmd = walkplayer.get(p.getLocation());
                        cmd = cmd.replace("[p]", p.getName());
                        Bukkit.dispatchCommand(p, cmd);
                    }
                }
            }
        }
    }
}
