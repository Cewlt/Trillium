package me.lordsaad.trillium.api.player;

import java.io.File;
import java.io.IOException;

import me.lordsaad.trillium.api.Configuration;
import me.lordsaad.trillium.api.TrilliumAPI;
import me.lordsaad.trillium.messageutils.MType;
import me.lordsaad.trillium.messageutils.Message;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class TrilliumPlayer {
    private Player proxy;
    private FileConfiguration config;
    
    private Location previousLocation;

    private boolean afk;
    private long lastActive;
    
    private boolean muted;

    public TrilliumPlayer(Player proxy) {
        this.proxy = proxy;
        try {
            load();
        } catch (IOException e) {
            e.printStackTrace(); //TODO: Warn
        }
    }

    public Player getProxy() {
        return proxy;
    }

    public boolean isAfk() {
        return afk;
    }

    public long getLastActive() {
        return lastActive;
    }

    public long getInactiveTime() {
        return System.currentTimeMillis() - lastActive;
    }

    public void toggleAfk() {
        this.afk = !this.afk;
        if (this.afk) {
            Message.b(MType.G, "AFK", getProxy().getName() + " is now AFK.");
        } else {
            Message.b(MType.G, "AFK", getProxy().getName() + " is no longer AFK.");
        }
    }
    
    public boolean isMuted() {
        return this.muted;
    }
    
    public void mute() {
        
    }
    
    public void unmute() {
        
    }

    public void active() {
        lastActive = System.currentTimeMillis();
    }

    public void dispose() {
        proxy = null;
        //TODO: Save data
    }

    private void load() throws IOException {
        boolean newUser = false;
        File dataStore = new File(TrilliumAPI.getInstance().getDataFolder() + File.separator + "players" + File.separator + proxy.getUniqueId() + ".yml");
        if (!dataStore.exists()) {
            dataStore.createNewFile();
            newUser = true;
        }

        config = YamlConfiguration.loadConfiguration(dataStore);

        if (newUser) {
            config.set(Configuration.Player.NICKNAME, proxy.getName());
            config.set(Configuration.Player.LOCATION, TrilliumAPI.getSerializer(Location.class).serialize(proxy.getLocation()));
            config.set(Configuration.Player.MUTED, false);
            config.set(Configuration.Player.GOD, false);
            config.set(Configuration.Player.VANISH, false);
            config.set(Configuration.Player.BAN_REASON, "");
        }
    }
}
