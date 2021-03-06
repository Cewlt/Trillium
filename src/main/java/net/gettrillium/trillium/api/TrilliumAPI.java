package net.gettrillium.trillium.api;

import net.gettrillium.trillium.Trillium;
import net.gettrillium.trillium.api.command.Command;
import net.gettrillium.trillium.api.command.TrilliumCommand;
import net.gettrillium.trillium.api.player.TrilliumPlayer;
import net.gettrillium.trillium.api.serializer.Serializer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.entity.Player;
import org.bukkit.plugin.SimplePluginManager;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TrilliumAPI {
    private static Trillium instance;
    private static File playerFolder;
    private static Map<UUID, TrilliumPlayer> players;
    private static Map<Class<?>, Serializer<?>> serializers;
    private static Map<Class<? extends TrilliumModule>, TrilliumModule> modules;

    public static Trillium getInstance() {
        return instance;
    }

    public static void setInstance(Trillium instance) {
        String className = new Throwable().getStackTrace()[1].getClassName();
        if (className.equalsIgnoreCase(Trillium.class.getCanonicalName())) {
            TrilliumAPI.instance = instance;
            TrilliumAPI.players = new HashMap<>();
            TrilliumAPI.serializers = new HashMap<>();
            TrilliumAPI.modules = new HashMap<>();

            TrilliumAPI.instance.saveDefaultConfig();
            TrilliumAPI.playerFolder = new File(getInstance().getDataFolder(), "Player Database");
            playerFolder.mkdirs();
        } else {
            throw new IllegalStateException("Cannot set instance of TrilliumAPI");
        }
    }

    public static TrilliumPlayer getPlayer(String name) {
        Player p = Bukkit.getPlayer(name);
        if (p != null) {
            return players.get(p.getUniqueId());
        } else {
            return null;
        }
    }

    public static TrilliumPlayer getPlayer(Player player) {
        return players.get(player.getUniqueId());
    }

    public static TrilliumPlayer getPlayer(UUID uuid) {
        return players.get(uuid);
    }

    public static void loadPlayer(Player proxy) {
        if (!players.containsKey(proxy.getUniqueId())) {
            TrilliumPlayer p = new TrilliumPlayer(proxy);
            p.load();
            players.put(proxy.getUniqueId(), p);
        }
    }

    public static void disposePlayer(Player proxy) {
        if (players.containsKey(proxy.getUniqueId())) {
            TrilliumPlayer player = players.remove(proxy.getUniqueId());
            player.dispose();
        } // else { means that they are already unloaded... right?
    }

    public static File getPlayerFolder() {
        return playerFolder;
    }

    public static boolean isLoaded(Player p) {
        return players.containsKey(p.getUniqueId());
    }

    @SuppressWarnings("unchecked")
    public static <T> Serializer<T> getSerializer(Class<T> clazz) {
        return (Serializer<T>) serializers.get(clazz);
    }

    public static <T> void registerSerializer(Class<T> clazz, Serializer<T> serializer) {
        serializers.put(clazz, serializer);
    }

    public static void registerModule(TrilliumModule module) {
        instance.getServer().getPluginManager().registerEvents(module, instance);
        registerCommand(module);
        module.register();
        modules.put(module.getClass(), module);
    }

    public static boolean isModuleEnabled(Class<? extends TrilliumModule> module) {
        return modules.containsKey(module);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getModule(Class<T> module) {
        return (T) modules.get(module);
    }

    public static Collection<? extends TrilliumPlayer> getOnlinePlayers() {
        return players.values();
    }

    public static void registerCommand(TrilliumModule module) {
        try {
            Field field = SimplePluginManager.class.getDeclaredField("commandMap");
            field.setAccessible(true);
            CommandMap commandMap = (CommandMap) (field.get(getInstance().getServer().getPluginManager()));
            for (Method m : module.getClass().getDeclaredMethods()) {
                Command command = m.getAnnotation(Command.class);
                if (command != null) {
                    TrilliumCommand c = new TrilliumCommand(command.command(), command.description(), command.usage(), command.aliases(), m, module);
                    commandMap.register("trillium", c);
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
