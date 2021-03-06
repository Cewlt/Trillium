package net.gettrillium.trillium.modules;

import net.gettrillium.trillium.api.Configuration;
import net.gettrillium.trillium.api.Kit;
import net.gettrillium.trillium.api.Permission;
import net.gettrillium.trillium.api.TrilliumModule;
import net.gettrillium.trillium.api.command.Command;
import net.gettrillium.trillium.api.messageutils.Error;
import net.gettrillium.trillium.api.messageutils.Message;
import net.gettrillium.trillium.api.messageutils.Mood;
import net.gettrillium.trillium.api.player.TrilliumPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitModule extends TrilliumModule {

    @Command(command = "kit", description = "Get a certain kit.", usage = "/kit [kit name]")
    public void kit(CommandSender cs, String[] args) {
        if (getConfig().getBoolean(Configuration.Kit.ENABLED)) {
            if (cs instanceof Player) {
                TrilliumPlayer p = player((Player) cs);
                if (args.length == 0) {
                    new Message(Mood.GENERIC, "Kit", "Available kits:").to(p);
                    for (String s : getConfig().getConfigurationSection(Configuration.Kit.KIT_MAKER).getKeys(false)) {
                        new Message(Mood.GENERIC, "Kit", s).to(p);
                    }
                } else {
                    if (getConfig().getConfigurationSection(Configuration.Kit.KIT_MAKER).contains(args[0])) {
                        if (p.getProxy().hasPermission(Permission.Kit.USE + args[0])) {

                            new Kit(args[0]).giveTo(p.getProxy());

                            new Message(Mood.GOOD, "Kit", "You successfully received kit " + args[0]).to(p);

                        } else {
                            new Message(Mood.BAD, "Kit", "You don't have permission to use that kit.").to(p);
                        }
                    } else {
                        new Message(Mood.BAD, "Kit", args[0] + " is not a valid kit.").to(p);
                    }
                }
            } else {
                new Message("Kit", Error.CONSOLE_NOT_ALLOWED).to(cs);
            }
        } else {
            new Message(Mood.BAD, "Kit", "This feature is disabled.").to(cs);
        }
    }
}
