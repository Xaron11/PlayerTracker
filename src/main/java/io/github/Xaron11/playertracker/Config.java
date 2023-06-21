package io.github.Xaron11.playertracker;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class Config {

    public static int LimitCompassesInInventory;
    public static boolean CompassVanishingEnchant;
    public static boolean HideCompassEnchant;
    public static boolean GiveCompassOnRespawn;
    public static boolean GiveCompassOnJoin;
    public static boolean GiveOnFirstJoinOnly;
    public static boolean DeleteCompassOnDrop;
    public static boolean DontLetTheCompassDrop;
    public static int CompassTrackingUpdateTime;
    public static boolean ShowCompassTargetDistance;
    public static boolean ShowCompassTargetCoords;
    public static List<String> DisabledWorlds;

    public static void setDefaults(FileConfiguration config) {
        config.addDefault("limit-compasses-in-inventory", 0);
        config.addDefault("compass-vanishing-enchant", true);
        config.addDefault("hide-compass-enchant", false);
        config.addDefault("give-compass-on-respawn", false);
        config.addDefault("give-compass-on-join", false);
        config.addDefault("give-on-first-join-only", false);
        config.addDefault("delete-compass-on-drop", false);
        config.addDefault("dont-let-the-compass-drop", false);
        config.addDefault("compass-tracking-update-time", 20);
        config.addDefault("show-compass-target-distance", true);
        config.addDefault("show-compass-target-coords", true);
        config.addDefault("disabled-worlds", new String[] {"world_the_end"});
        config.options().copyDefaults(true);
    }

    public static void load(FileConfiguration config) {
        LimitCompassesInInventory = config.getInt("limit-compasses-in-inventory");
        CompassVanishingEnchant = config.getBoolean("compass-vanishing-enchant");
        HideCompassEnchant = config.getBoolean("hide-compass-enchant");
        GiveCompassOnRespawn = config.getBoolean("give-compass-on-respawn");
        GiveCompassOnJoin = config.getBoolean("give-compass-on-join");
        GiveOnFirstJoinOnly = config.getBoolean("give-on-first-join-only");
        DeleteCompassOnDrop = config.getBoolean("delete-compass-on-drop");
        DontLetTheCompassDrop = config.getBoolean("dont-let-the-compass-drop");
        CompassTrackingUpdateTime = config.getInt("compass-tracking-update-time");
        ShowCompassTargetDistance = config.getBoolean("show-compass-target-distance");
        ShowCompassTargetCoords = config.getBoolean("show-compass-target-coords");
        DisabledWorlds = config.getStringList("disabled-worlds");
    }
}
