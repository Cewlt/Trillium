package me.lordsaad.trillium.runnables;

import java.util.UUID;

import me.lordsaad.trillium.API;
import me.lordsaad.trillium.api.TrilliumAPI;
import me.lordsaad.trillium.commands.CommandAfk;
import me.lordsaad.trillium.messageutils.MType;
import me.lordsaad.trillium.messageutils.Message;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class AfkRunnable implements Runnable {
    @Override
    public void run() {
        if (TrilliumAPI.getInstance().getConfig().getBoolean("AFK.auto afk.enabled")) {
            for (UUID uuid : CommandAfk.afktimer.keySet()) {
                Player p = Bukkit.getPlayer(uuid);
                if (p != null) {
                    if (!API.isAfk(p)) {
                        if (!API.isVanished(p)) {
                            if (!p.hasPermission("tr.afk.exempt")) {
                                CommandAfk.afktimer.put(p.getUniqueId(), CommandAfk.afktimer.get(p.getUniqueId()) + 1);

                                if (CommandAfk.afktimer.get(p.getUniqueId()) >= TrilliumAPI.getInstance().getConfig().getInt("AFK.time until idle")) {

                                    if (TrilliumAPI.getInstance().getConfig().getBoolean("AFK.kick on afk")) {
                                        p.kickPlayer("You idled for too long. Sorry.");
                                        Message.b(MType.W, "AFK", p.getName() + " got kicked for idling for too long.");
                                        CommandAfk.afklist.remove(p.getUniqueId());
                                        CommandAfk.afktimer.remove(p.getUniqueId());

                                    } else {
                                        Message.b(MType.G, "AFK", p.getName() + " is now AFK.");
                                        CommandAfk.afklist.add(p.getUniqueId());
                                        CommandAfk.afktimer.remove(p.getUniqueId());
                                    }
                                }
                            }
                        }
                    }
                } else {
                    CommandAfk.afktimer.remove(uuid);
                }
            }
        }
    }
}