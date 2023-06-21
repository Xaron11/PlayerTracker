package io.github.Xaron11.playertracker;

import io.github.Xaron11.playertracker.commands.CompassCommand;
import io.github.Xaron11.playertracker.commands.CompassReloadCommand;
import io.github.Xaron11.playertracker.events.CompassClickEvent;
import io.github.Xaron11.playertracker.events.CompassPlayerEvents;
import io.github.Xaron11.playertracker.events.CompassUIEvent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.HashMap;
import java.util.Map;

public class Main extends JavaPlugin {

    public static final String PLAYER_TRACKER_PREFIX = ChatColor.GOLD + "[PlayerTracker]";
    public static final String PLAYER_TRACKER_NAME = ChatColor.GOLD + "Player Tracker";

    public static ItemStack WorldHead;

    private static Main _instance;

    public static Main getInstance() {
        return _instance;
    }

    private Map<String, Location> lodestones = new HashMap<String, Location>();

    @Override
    public void onEnable() {
        getLogger().info("onEnable is called!");

        _instance = this;

        Config.setDefaults(this.getConfig());
        this.saveConfig();
        Config.load(this.getConfig());

        setupWorldHead();

        getCommand("compass").setExecutor(new CompassCommand());
        getCommand("reloadcompass").setExecutor(new CompassReloadCommand());

        getServer().getPluginManager().registerEvents(new CompassClickEvent(), this);
        getServer().getPluginManager().registerEvents(new CompassUIEvent(), this);
        getServer().getPluginManager().registerEvents(new CompassPlayerEvents(), this);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
            public void run() {

                clearLodestones();

                for (World w : Bukkit.getWorlds()) {
                    if (!Config.DisabledWorlds.contains(w.getName())) {
                        Location loc = w.getSpawnLocation();
                        loc.setY(0);
                        loc.getBlock().setType(Material.LODESTONE);
                        lodestones.put(w.getUID().toString(), loc);
                    }
                }
                for (Player p : Bukkit.getOnlinePlayers()) {
                    Location loc = p.getLocation();
                    if (!Config.DisabledWorlds.contains(loc.getWorld().getName())) {
                        loc.setY(0);
                        loc.getBlock().setType(Material.LODESTONE);
                        lodestones.put(p.getUniqueId().toString(), loc);
                    }
                }
            }
        }, 0L, Config.CompassTrackingUpdateTime);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    for (int i = 0; i < p.getInventory().getSize(); i++) {
                        ItemStack item = p.getInventory().getItem(i);
                        if (item != null && CompassUtils.isItemAPlayerTracker(item)) {
                            String uid = CompassUtils.getTrackingNBT(item);
                            if (Main.getInstance().lodestones.containsKey(uid)) {
                                Location loc = Main.getInstance().lodestones.get(uid);
                                item = CompassUtils.setLodestone(item, loc);
                                p.getInventory().setItem(i, item);
                            }
                        }
                    }
                }
            }
        }, 0L, Config.CompassTrackingUpdateTime);

        Messages.sendConsole(ChatColor.GREEN + "Enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("onDisable is called!");

        clearLodestones();

        Messages.sendConsole(ChatColor.RED + "Disabled!");
    }

    private void clearLodestones() {
        for (Location loc : lodestones.values()) {
            if (loc.getWorld().getEnvironment() == World.Environment.THE_END) {
                loc.getBlock().setType(Material.AIR);
            } else {
                loc.getBlock().setType(Material.BEDROCK);
            }
        }
        lodestones.clear();
    }

    private static void setupWorldHead() {
        WorldHead = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta worldHeadMeta = (SkullMeta) WorldHead.getItemMeta();
        worldHeadMeta.setDisplayName(ChatColor.AQUA + "World Spawn");
        worldHeadMeta.setOwningPlayer(Bukkit.getServer().getOfflinePlayer("Dipicrylamine"));
        WorldHead.setItemMeta(worldHeadMeta);
    }
}
