package io.github.Xaron11.playertracker;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;

public final class CompassUtils {

    private CompassUtils() {}

    public static ItemStack createCompass(final Player player) {
        ItemStack compass = new ItemStack(Material.COMPASS);

        compass = setTrackingNBT(compass, player.getWorld().getUID().toString());

        CompassMeta meta = (CompassMeta) compass.getItemMeta();
        meta.setDisplayName(Main.PLAYER_TRACKER_NAME);

        if (Config.CompassVanishingEnchant) {
            meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true);
        }
        if (Config.HideCompassEnchant) {
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        meta.setLodestoneTracked(true);
        compass.setItemMeta(meta);

        return compass;
    }

    public static ItemStack setTrackingNBT(ItemStack compass, String uid) {
        NBTItem nbt = new NBTItem(compass);
        nbt.setString("tracking", uid);
        return nbt.getItem();
    }

    public static String getTrackingNBT(ItemStack compass) {
        NBTItem nbt = new NBTItem(compass);
        String uid = nbt.getString("tracking");
        return uid;
    }

    public static boolean isItemAPlayerTracker(ItemStack item) {
        return item.getType() == Material.COMPASS && item.getItemMeta().getDisplayName().equalsIgnoreCase(Main.PLAYER_TRACKER_NAME);
    }

    public static boolean compassLimitReached(Player p) {
        if (p.hasPermission("playertracker.unlimited")) {
            return false;
        }

        int compasses = 0;
        int limit = Config.LimitCompassesInInventory;
        int inventorySize = p.getInventory().getSize();
        int i = 0;

        while (compasses < limit && i < inventorySize) {
            ItemStack item = p.getInventory().getItem(i);
            if (item != null && isItemAPlayerTracker(item)) {
                compasses += item.getAmount();
            }
            i++;
        }

        return compasses >= limit;
    }

    public static boolean compassLimitReached(Player p, int added) {
        if (p.hasPermission("playertracker.unlimited")) {
            return false;
        }

        int compasses = 0;
        int limit = Config.LimitCompassesInInventory;
        int inventorySize = p.getInventory().getSize();
        int i = 0;

        while (compasses + added <= limit && i < inventorySize) {
            ItemStack item = p.getInventory().getItem(i);
            if (item != null && isItemAPlayerTracker(item)) {
                compasses += item.getAmount();
            }
            i++;
        }

        return compasses + added > limit;
    }

    public static ItemStack setLodestone(ItemStack compass, Location location) {
        CompassMeta meta = (CompassMeta) compass.getItemMeta();
        meta.setLodestone(location);
        compass.setItemMeta(meta);
        return compass;
    }
}
