package net.gettrillium.trillium.modules;

import net.gettrillium.trillium.api.Permission;
import net.gettrillium.trillium.api.TrilliumModule;
import net.gettrillium.trillium.api.command.Command;
import net.gettrillium.trillium.api.messageutils.Message;
import net.gettrillium.trillium.api.messageutils.Type;
import net.gettrillium.trillium.api.player.TrilliumPlayer;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PunishModule extends TrilliumModule {

    public PunishModule() {
        super("punish");
    }

    @Command(command = "mute", description = "Silence/unsilence someone's voice.", usage = "/mute <player>")
    public void mute(CommandSender cs, String[] args) {
        if (cs.hasPermission(Permission.Punish.MUTE)) {
            if (args.length == 0) {
                Message.error(cs, "Mute", true, "/mute <player>");
            } else {
                Player target = Bukkit.getPlayer(args[0]);
                if (target != null) {
                    TrilliumPlayer player = player(target);
                    if (!player.isMuted()) {
                        player.setMuted(true);
                        Message.message(Type.GOOD, cs, "Mute", true, "You muted " + target.getName());
                        Message.message(Type.WARNING, cs, "Mute", true, cs.getName() + " muted you.");
                    } else {
                        player.setMuted(false);
                        Message.message(Type.GOOD, cs, "Mute", true, "You unmuted " + target.getName());
                        Message.message(Type.GOOD, cs, "Mute", true, cs.getName() + " unmuted you.");
                    }
                } else {
                    Message.error("Mute", cs, args[0]);
                }
            }
        } else {
            Message.error("Mute", cs);
        }
    }

    @Command(command = "kick", description = "Kick a player from the server.", usage = "/kick <player> [reason]")
    public void kick(CommandSender cs, String[] args) {
        if (cs.hasPermission(Permission.Punish.KICK)) {
            if (args.length < 2) {
                Message.error(cs, "Kick", true, "/kick <player> [reason]");
            } else {
                Player target = Bukkit.getPlayer(args[0]);
                if (target != null) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        sb.append(args[i]).append(" ");
                    }
                    String reason = sb.toString().trim();
                    Message.broadcast(Type.WARNING, "Kick", target.getName() + " got kicked for:");
                    Message.broadcast(Type.WARNING, "Kick", ChatColor.YELLOW + "'" + ChatColor.AQUA + reason + ChatColor.YELLOW + "'");
                    target.kickPlayer(reason);
                } else {
                    Message.error("Kick", cs, args[0]);
                }
            }
        } else {
            Message.error("Kick", cs);
        }
    }

    @Command(command = "ban", description = "Ban a player from the server.", usage = "/ban <player> [reason]")
    public void ban(CommandSender cs, String[] args) {
        if (cs.hasPermission(Permission.Punish.BAN)) {
            if (args.length == 0) {
                Message.error(cs, "Ban", true, "/ban <player> [reason]");
            } else {
                Player target = Bukkit.getPlayer(args[0]);
                String reason;
                if (args.length > 1) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        sb.append(args[i]).append(" ");
                    }
                    reason = sb.toString().trim();
                } else {
                    reason = "You are the weakest link. Good bye.";
                }

                if (target != null) {
                    Bukkit.getBanList(BanList.Type.NAME).addBan(target.getName(), reason, null, cs.getName());
                    target.kickPlayer(ChatColor.DARK_RED + "You got banned with reason: \n" + reason);
                    Message.broadcast(Type.WARNING, "Ban", target.getName() + " got banned with reason:");
                    Message.broadcast(Type.WARNING, "Ban", ChatColor.YELLOW + "'" + ChatColor.AQUA + reason + ChatColor.YELLOW + "'");
                } else {
                    Bukkit.getBanList(BanList.Type.NAME).addBan(args[0], reason, null, cs.getName());
                    Message.broadcast(Type.WARNING, "Ban", args[0] + " got banned with reason:");
                    Message.broadcast(Type.WARNING, "Ban", ChatColor.YELLOW + "'" + ChatColor.AQUA + reason + ChatColor.YELLOW + "'");
                }
            }
        } else {
            Message.error("Ban", cs);
        }
    }

    @Command(command = "unban", description = "Unban a player.", usage = "/unban <player>", aliases = "pardon")
    public void unban(CommandSender cs, String[] args) {
        if (cs.hasPermission(Permission.Punish.UNBAN)) {
            if (args.length == 0) {
                Message.error(cs, "Unban", true, "/unban <player>");
            } else {
                Bukkit.getBanList(BanList.Type.NAME).pardon(args[0]);
                Message.broadcast(Type.GOOD, "Unban", args[0] + " got unbanned.");
            }
        } else {
            Message.error("Unban", cs);
        }
    }

    @Command(command = "banip", description = "Ban the ip of a player from the server.", usage = "/banip <player> [reason]")
    public void banip(CommandSender cs, String[] args) {
        if (cs.hasPermission(Permission.Punish.BANIP)) {
            if (args.length == 0) {
                Message.error(cs, "BanIP", true, "/banip <player> [reason]");
            } else {
                Player target = Bukkit.getPlayer(args[0]);
                String reason;
                if (args.length > 1) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        sb.append(args[i]).append(" ");
                    }
                    reason = sb.toString().trim();
                } else {
                    reason = "You are the weakest link. Good bye.";
                }

                if (target != null) {
                    Bukkit.getBanList(BanList.Type.IP).addBan(String.valueOf(target.getAddress()), reason, null, cs.getName());
                    target.kickPlayer(ChatColor.DARK_RED + "You got banned with reason: \n" + reason);
                    Message.broadcast(Type.WARNING, "BanIP", target.getName() + " got banned with reason:");
                    Message.broadcast(Type.WARNING, "BanIP", ChatColor.YELLOW + "'" + ChatColor.AQUA + reason + ChatColor.YELLOW + "'");
                } else {
                    Bukkit.getBanList(BanList.Type.NAME).addBan(args[0], reason, null, cs.getName());
                    Message.broadcast(Type.WARNING, "BanIP", args[0] + " got banned with reason:");
                    Message.broadcast(Type.WARNING, "BanIP", ChatColor.YELLOW + "'" + ChatColor.AQUA + reason + ChatColor.YELLOW + "'");
                }
            }
        } else {
            Message.error("BanIP", cs);
        }
    }

    @Command(command = "unbanip", description = "Unban the IP of a player.", usage = "/unbanip <IP>", aliases = "pardonip")
    public void unbanip(CommandSender cs, String[] args) {
        if (cs.hasPermission(Permission.Punish.UNBANIP)) {
            if (args.length == 0) {
                Message.error(cs, "Unban", true, "/unbanip <IP>");
            } else {
                Bukkit.getBanList(BanList.Type.IP).pardon(args[0]);
                Message.broadcast(Type.GOOD, "Unban", args[0] + " got unbanned.");
            }
        } else {
            Message.error("Unban", cs);
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        TrilliumPlayer player = player(e.getPlayer());
        if (player.isMuted()) {
            e.setCancelled(true);
            Message.message(Type.WARNING, player.getProxy(), "Mute", true, "Your voice has been silenced.");
        }
    }
}
