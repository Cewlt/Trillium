package net.gettrillium.trillium.runnables;

import net.gettrillium.trillium.Utils;
import net.gettrillium.trillium.api.Configuration;
import net.gettrillium.trillium.api.TrilliumAPI;
import net.gettrillium.trillium.api.player.TrilliumPlayer;

public class GroupManagerRunnable implements Runnable {

    public void run() {
        if (TrilliumAPI.getInstance().getConfig().getBoolean(Configuration.GM.ENABLED)) {
            for (TrilliumPlayer p : TrilliumAPI.getOnlinePlayers()) {
                p.refreshPermissions();
                p.getProxy().sendMessage("" + Utils.timeToTickConverter(TrilliumAPI.getInstance().getConfig().getString(Configuration.GM.RELOAD)));
            }
        }
    }
}