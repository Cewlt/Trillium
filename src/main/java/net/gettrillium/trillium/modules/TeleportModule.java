package net.gettrillium.trillium.modules;

import net.gettrillium.trillium.Utils;
import net.gettrillium.trillium.api.Configuration;
import net.gettrillium.trillium.api.Permission;
import net.gettrillium.trillium.api.TrilliumModule;
import net.gettrillium.trillium.api.command.Command;
import net.gettrillium.trillium.api.messageutils.Error;
import net.gettrillium.trillium.api.messageutils.Message;
import net.gettrillium.trillium.api.messageutils.Mood;
import net.gettrillium.trillium.api.player.TrilliumPlayer;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.HashMap;
import java.util.UUID;

public class TeleportModule extends TrilliumModule {

    HashMap<UUID, UUID> tpr = new HashMap<>();
    HashMap<UUID, UUID> tprh = new HashMap<>();

    public TeleportModule() {
        super("teleport");
    }

    @Command(command = "back", description = "Teleport to your last active position", usage = "/back")
    public void back(CommandSender cs, String[] args) {
        if (cs instanceof Player) {
            TrilliumPlayer player = player(cs.getName());
            if (player.getProxy().hasPermission(Permission.Ability.BACK)) {
                new Message(Mood.GOOD, "Back", "You have been sent back to your last location.").to(player);
                player.getProxy().teleport(player.getLastLocation());
            } else {
                new Message("Back", Error.NO_PERMISSION).to(cs);
            }
        } else {
            new Message("Back", Error.CONSOLE_NOT_ALLOWED).to(cs);
        }
    }

    @Command(command = "spawn", description = "Teleport to the server's spawn.", usage = "/spawn")
    public void spawn(CommandSender cs, String[] args) {
        if (cs instanceof Player) {
            TrilliumPlayer p = player((Player) cs);
            if (p.hasPermission(Permission.Teleport.SPAWN)) {
                p.getProxy().teleport(p.getProxy().getWorld().getSpawnLocation());
                new Message(Mood.GOOD, "Spawn", "You were successfully teleported to the spawn point.").to(p);
            } else {
                new Message("Spawn", Error.NO_PERMISSION).to(cs);
            }
        } else {
            new Message("Spawn", Error.CONSOLE_NOT_ALLOWED).to(cs);
        }
    }

    @Command(command = "teleport", description = "Teleport to a person or a set of coordinates.", usage = "/tp <player> [player / <x>, <y>, <z>]", aliases = "tp")
    public void tp(CommandSender cs, String[] args) {
        if (!(cs instanceof Player)) {
            new Message("TP", Error.CONSOLE_NOT_ALLOWED).to(cs);
            return;
        }

        TrilliumPlayer p = player((Player) cs);

        if (args.length == 0) {
            if (!p.hasPermission(Permission.Teleport.TP)) {
                new Message("TP", Error.NO_PERMISSION).to(cs);
                return;
            }

            new Message("TP", Error.TOO_FEW_ARGUMENTS, "/tp <player> [player]").to(p);
        } else if (args.length == 1) {
            if (!p.hasPermission(Permission.Teleport.TP)) {
                new Message("TP", Error.NO_PERMISSION).to(p);
                return;
            }

            Player target = Bukkit.getPlayer(args[0]);

            if (target == null) {
                new Message("TP", Error.INVALID_PLAYER, args[0]).to(p);
                return;
            }

            p.getProxy().teleport(target);
            new Message(Mood.GOOD, "TP", "You teleported to " + target.getName()).to(p);
        } else if (args.length == 2) {
            if (!p.hasPermission(Permission.Teleport.TP_OTHER)) {
                new Message("TP", Error.NO_PERMISSION).to(p);
                return;
            }

            TrilliumPlayer target1 = player(args[0]);
            TrilliumPlayer target2 = player(args[1]);

            if (target1 == null) {
                new Message("TP", Error.INVALID_PLAYER, args[1]).to(p);
                return;
            }

            if (target2 == null) {
                new Message("TP", Error.INVALID_PLAYER, args[2]).to(p);
                return;
            }

            target1.getProxy().teleport(target2.getProxy());
            new Message(Mood.GOOD, "TP", "You teleported " + target1.getName() + " to " + target2.getName()).to(p);
            new Message(Mood.GOOD, "TP", p.getName() + " teleported you to " + target2.getName()).to(target1);
        } else {
            if (!p.getProxy().hasPermission(Permission.Teleport.TP_COORDS)) {
                new Message("TP", Error.NO_PERMISSION).to(cs);
                return;
            }

            Player pl = Bukkit.getPlayer(args[0]);

            if (pl == null) {
                new Message("TP", Error.INVALID_PLAYER, args[0]).to(p);
                return;
            }

            String xArg = args[1];
            String yArg = args[2];
            String zArg = args[3];

            if (StringUtils.isNumeric(xArg) && StringUtils.isNumeric(yArg) && StringUtils.isNumeric(zArg)) {
                int x = Integer.parseInt(xArg);
                int y = Integer.parseInt(yArg);
                int z = Integer.parseInt(zArg);

                Location loc = new Location(p.getProxy().getWorld(), x, y, z);

                pl.teleport(loc);
                new Message(Mood.GOOD, "TP", "You teleported to " + ChatColor.AQUA + x + ", " + y + ", " + z).to(p);
            } else {
                if (!xArg.startsWith("~") || !yArg.startsWith("~") || !zArg.startsWith("~")) {
                    new Message("TP", Error.TOO_FEW_ARGUMENTS, "/tp <player> [x] [y] [z]").to(p);
                    return;
                }

                if (!StringUtils.isNumeric(xArg.substring(1)) || !StringUtils.isNumeric(yArg.substring(1)) || !StringUtils.isNumeric(zArg.substring(1))) {
                    new Message(Mood.BAD, "TP", "Something isn't a number...").to(p);
                    return;
                }

                int x, y, z;

                if (xArg.substring(1).equals("") || xArg.substring(1).equals(" ")) {
                    x = 0;
                } else {
                    x = Integer.parseInt(xArg.substring(1));
                }

                if (yArg.substring(1).equals("") || yArg.substring(1).equals(" ")) {
                    y = 0;
                } else {
                    y = Integer.parseInt(yArg.substring(1));
                }

                if (zArg.substring(1).equals("") || zArg.substring(1).equals(" ")) {
                    z = 0;
                } else {
                    z = Integer.parseInt(zArg.substring(1));
                }

                Location loc = p.getProxy().getLocation();
                pl.teleport(new Location(loc.getWorld(), loc.getX() + x, loc.getY() + y, loc.getZ() + z));
                new Message(Mood.GOOD, "TP", "You teleported to " + ChatColor.AQUA + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ()).to(p);
            }
        }
    }

    @Command(command = "teleporthere", description = "Teleport a player to you.", usage = "/tph <player>", aliases = "tph")
    public void teleporthere(CommandSender cs, String[] args) {
        if (!(cs instanceof Player)) {
            new Message("TPH", Error.CONSOLE_NOT_ALLOWED).to(cs);
            return;
        }

        TrilliumPlayer p = player((Player) cs);

        if (!p.hasPermission(Permission.Teleport.TPHERE)) {
            new Message("TPH", Error.NO_PERMISSION).to(cs);
            return;
        }

        if (args.length == 0) {
            new Message("TPH", Error.TOO_FEW_ARGUMENTS, "/tphere <player>").to(p);
            return;
        }

        TrilliumPlayer target = player(args[0]);

        if (target == null) {
            new Message("TPH", Error.INVALID_PLAYER, args[0]).to(p);
            return;
        }

        target.getProxy().teleport(p.getProxy());
        new Message(Mood.GOOD, "TPH", "You teleported " + target.getProxy().getName() + " to you.").to(p);
        new Message(Mood.GOOD, "TPH", p.getProxy().getName() + " teleported you to them.").to(target);
    }

    @Command(command = "teleportrequest", description = "Request permission to teleport to a player.", usage = "/tpr <player>", aliases = "tpr")
    public void teleportrequest(CommandSender cs, String[] args) {
        if (cs instanceof Player) {
            TrilliumPlayer p = player((Player) cs);
            if (p.hasPermission(Permission.Teleport.TPREQEST)) {
                if (args.length != 0) {
                    TrilliumPlayer target = player(args[0]);
                    if (target != null) {

                        new Message(Mood.GENERIC, "TPR", target.getName() + " is now pending. Please stand by.").to(p);

                        new Message(Mood.GENERIC, "TPR", p.getName() + " would like to teleport to you.").to(target);
                        new Message(Mood.GENERIC, "TPR", ChatColor.AQUA + "/tpra " + ChatColor.BLUE + "to accept the teleport.").to(target);
                        new Message(Mood.GENERIC, "TPR", ChatColor.AQUA + "/tprd " + ChatColor.BLUE + "to deny the teleport.").to(target);

                        tpr.put(p.getProxy().getUniqueId(), target.getProxy().getUniqueId());

                    } else {
                        new Message("TPR", Error.INVALID_PLAYER, args[0]).to(p);
                    }
                } else {
                    new Message("TPR", Error.TOO_FEW_ARGUMENTS, "/tpr <player>").to(p);
                }
            } else {
                new Message("TPR", Error.NO_PERMISSION).to(p);
            }
        } else {
            new Message("TPR", Error.CONSOLE_NOT_ALLOWED).to(cs);
        }
    }

    @Command(command = "teleportrequesthere", description = "Request permission to teleport a player to you.", usage = "/tprh <player>", aliases = "tprh")
    public void teleportrequesthere(CommandSender cs, String[] args) {
        if (cs instanceof Player) {
            TrilliumPlayer p = player((Player) cs);
            if (p.hasPermission(Permission.Teleport.TPREQESTHERE)) {
                if (args.length != 0) {
                    TrilliumPlayer target = player(args[0]);
                    if (target != null) {

                        new Message(Mood.GENERIC, "TPRH", "Teleport request for " + target.getName() + " to here is now pending. Please stand by.").to(p);

                        new Message(Mood.GENERIC, "TPRH", p.getName() + ChatColor.BLUE + " would like you to teleport to him").to(target);
                        new Message(Mood.GENERIC, "TPRH", ChatColor.AQUA + "/tpra " + ChatColor.BLUE + "to accept the teleport.").to(target);
                        new Message(Mood.GENERIC, "TPRH", ChatColor.AQUA + "/tprd " + ChatColor.BLUE + "to deny the teleport.").to(target);
                        tprh.put(p.getProxy().getUniqueId(), target.getProxy().getUniqueId());

                    } else {
                        new Message("TPRH", Error.INVALID_PLAYER, args[0]).to(p);
                    }
                } else {
                    new Message("TPRH", Error.TOO_FEW_ARGUMENTS, "/tprh <player>").to(p);
                }
            } else {
                new Message("TPRH", Error.NO_PERMISSION).to(p);
            }
        } else {
            new Message("TPRH", Error.CONSOLE_NOT_ALLOWED).to(cs);
        }
    }

    @Command(command = "teleportrequestaccept", description = "Accept a teleport request.", usage = "/tpra", aliases = "tpra")
    public void teleportrequestaccept(CommandSender cs, String[] args) {
        if (cs instanceof Player) {
            TrilliumPlayer p = player((Player) cs);
            if (p.hasPermission(Permission.Teleport.TPRRESPOND)) {
                if (tpr.containsValue(p.getProxy().getUniqueId())) {

                    TrilliumPlayer requester = player(Bukkit.getPlayer(tpr.get(p.getProxy().getUniqueId())));

                    requester.getProxy().teleport(p.getProxy());
                    new Message(Mood.GOOD, "TPRA", "You teleported " + requester.getName() + " to you.").to(p);
                    new Message(Mood.GOOD, "TPRA", p.getName() + " accepted your teleport request.").to(requester);

                } else if (tprh.containsValue(p.getProxy().getUniqueId())) {

                    TrilliumPlayer requester = player(Bukkit.getPlayer(tprh.get(p.getProxy().getUniqueId())));

                    p.getProxy().teleport(requester.getProxy());
                    new Message(Mood.GOOD, "TPRA", "You teleported to " + requester.getName()).to(p);
                    new Message(Mood.GOOD, "TPRA", p.getName() + " accepted to teleport to you.").to(requester);

                } else {
                    new Message(Mood.BAD, "TPRA", "No pending teleport requests to accept.").to(p);
                }
            } else {
                new Message("TPRA", Error.NO_PERMISSION).to(p);
            }
        } else {
            new Message("TPRA", Error.CONSOLE_NOT_ALLOWED).to(cs);
        }
    }

    @Command(command = "teleportrequestdeny", description = "Deny a teleport request.", usage = "/tprd", aliases = "tprd")
    public void teleportrequestdeny(CommandSender cs, String[] args) {
        if (!(cs instanceof Player)) {
            new Message("TPRD", Error.CONSOLE_NOT_ALLOWED).to(cs);
        }

        TrilliumPlayer p = player((Player) cs);

        if (!p.hasPermission(Permission.Teleport.TPRRESPOND)) {
            new Message("TPRD", Error.NO_PERMISSION).to(p);
            return;
        }

        if (tpr.containsValue(p.getProxy().getUniqueId())) {
            TrilliumPlayer requester = player(Bukkit.getPlayer(tpr.get(p.getProxy().getUniqueId())));

            new Message(Mood.GOOD, "TPRD", "You denied " + ChatColor.AQUA + requester.getName() + "'s teleport request.").to(p);
            new Message(Mood.GOOD, "TPRD", p.getName() + " denied your teleport request.").to(requester);
            tpr.remove(p.getProxy().getUniqueId());
        } else if (tprh.containsValue(p.getProxy().getUniqueId())) {
            TrilliumPlayer requester = player(Bukkit.getPlayer(tprh.get(p.getProxy().getUniqueId())));

            new Message(Mood.GOOD, "TPRD", "You denied " + ChatColor.AQUA + requester.getName() + "'s teleport request.").to(p);
            new Message(Mood.GOOD, "TPRD", p.getName() + " denied your teleport request.").to(requester);
            tprh.remove(p.getProxy().getUniqueId());
        } else {
            new Message(Mood.BAD, "TPRD", "No pending teleport requests to deny.").to(p);
        }
    }

    @Command(command = "teleportcoordinates", description = "Teleport to a set of coordinates.", usage = "/tpc <x> <y> <z>", aliases = "tpc")
    public void teleportcoordinates(CommandSender cs, String[] args) {
        if (!(cs instanceof Player)) {
            new Message("TP", Error.CONSOLE_NOT_ALLOWED).to(cs);
            return;
        }

        TrilliumPlayer p = player((Player) cs);

        if (!p.getProxy().hasPermission(Permission.Teleport.TP_COORDS)) {
            new Message("TP", Error.NO_PERMISSION).to(p);
            return;
        }

        if (args.length < 3) {
            new Message("TP", Error.TOO_FEW_ARGUMENTS, "/tp <x> <y> <z>").to(p);
            return;
        }

        String xArg = args[0];
        String yArg = args[1];
        String zArg = args[2];

        if (StringUtils.isNumeric(xArg) && StringUtils.isNumeric(yArg) && StringUtils.isNumeric(zArg)) {
            int x = Integer.parseInt(xArg);
            int y = Integer.parseInt(yArg);
            int z = Integer.parseInt(zArg);

            Location loc = new Location(p.getProxy().getWorld(), x, y, z);
            p.getProxy().teleport(loc);
            new Message(Mood.GOOD, "TP", "You teleported to" + ChatColor.AQUA + x + ", " + y + ", " + z).to(p);
        } else {
            if (!xArg.startsWith("~") || !yArg.startsWith("~") || !zArg.startsWith("~")) {
                new Message("TP", Error.TOO_FEW_ARGUMENTS, "/tp <player> [x] [y] [z]").to(p);
                return;
            }

            if (!StringUtils.isNumeric(xArg.substring(1)) || !StringUtils.isNumeric(yArg.substring(1)) || !StringUtils.isNumeric(zArg.substring(1))) {
                new Message(Mood.BAD, "TP", "Something isn't a number...").to(p);
                return;
            }

            int x;
            int y;
            int z;

            if (xArg.substring(1).equals("") || xArg.substring(1).equals(" ")) {
                x = 0;
            } else {
                x = Integer.parseInt(xArg.substring(1));
            }

            if (yArg.substring(1).equals("") || yArg.substring(1).equals(" ")) {
                y = 0;
            } else {
                y = Integer.parseInt(xArg.substring(1));
            }

            if (zArg.substring(1).equals("") || zArg.substring(1).equals(" ")) {
                z = 0;
            } else {
                z = Integer.parseInt(xArg.substring(1));
            }

            Location loc = p.getProxy().getLocation();
            p.getProxy().teleport(new Location(loc.getWorld(), loc.getX() + x, loc.getY() + y, loc.getZ() + z));
            new Message(Mood.GOOD, "TPC", "You teleported to" + ChatColor.AQUA + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ()).to(p);
        }
    }

    @EventHandler
    public void onTP(PlayerTeleportEvent event) {
        TrilliumPlayer p = player(event.getPlayer());
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.COMMAND || event.getCause() == PlayerTeleportEvent.TeleportCause.PLUGIN || event.getCause() == PlayerTeleportEvent.TeleportCause.UNKNOWN) {
            p.setLastLocation(event.getFrom());
        }
        if (getConfig().getBoolean(Configuration.Broadcast.IMP_ENABLED)) {
            if (event.getCause() == PlayerTeleportEvent.TeleportCause.COMMAND) {
                Utils.broadcastImportantMessage();
            }
        }
    }
}
