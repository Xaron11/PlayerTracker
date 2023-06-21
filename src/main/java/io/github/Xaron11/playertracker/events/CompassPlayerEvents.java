package io.github.Xaron11.playertracker.events;

import io.github.Xaron11.playertracker.CompassUtils;
import io.github.Xaron11.playertracker.Config;
import io.github.Xaron11.playertracker.Main;
import io.github.Xaron11.playertracker.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

public class CompassPlayerEvents implements Listener {

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent e) {
        Player p = e.getPlayer();
        for (int i = 0; i < p.getInventory().getSize(); i++) {
            ItemStack item = p.getInventory().getItem(i);
            if (item != null && CompassUtils.isItemAPlayerTracker(item)) {
                if (CompassUtils.getTrackingNBT(item).equals(e.getFrom().getUID().toString())) {
                    item = CompassUtils.setTrackingNBT(item, p.getWorld().getUID().toString());
                    p.getInventory().setItem(i, item);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (Config.GiveCompassOnJoin) {
            Player p = e.getPlayer();
            if (Config.GiveOnFirstJoinOnly) {
                if (p.hasPlayedBefore()) {
                    return;
                }
            }
            if (Config.LimitCompassesInInventory != 0) {
                if (CompassUtils.compassLimitReached(p)) {
                    Messages.send(p, ChatColor.WHITE + "Compass limit reached.");
                    return;
                }
            }
            p.getInventory().addItem(CompassUtils.createCompass(p));
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        if (Config.GiveCompassOnRespawn) {
            Player p = e.getPlayer();
            if (Config.LimitCompassesInInventory != 0) {
                if (CompassUtils.compassLimitReached(p)) {
                    Messages.send(p, ChatColor.WHITE + "Compass limit reached.");
                    return;
                }
            }
            p.getInventory().addItem(CompassUtils.createCompass(p));
        }
    }


    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e) {
        if (Config.DontLetTheCompassDrop) {
            Item item = e.getItemDrop();
            if (CompassUtils.isItemAPlayerTracker(item.getItemStack())) {
                e.setCancelled(true);
            }
        }
        else if (Config.DeleteCompassOnDrop) {
            Item item = e.getItemDrop();
            if (CompassUtils.isItemAPlayerTracker(item.getItemStack())) {
                item.remove();
            }
        }
    }

    private boolean showLimitMessage = true;

    @EventHandler
    public void onPlayerPickupItem(EntityPickupItemEvent e) {
        if (Config.LimitCompassesInInventory == 0) return;

        if (e.getEntity() instanceof Player) {
            ItemStack item = e.getItem().getItemStack();
            if (CompassUtils.isItemAPlayerTracker(item)) {
                Player p = (Player)e.getEntity();
                if (CompassUtils.compassLimitReached(p, item.getAmount())) {
                    showLimitMessage(p);
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerClickInventory(InventoryClickEvent e) {
        if (Config.LimitCompassesInInventory == 0) return;

        if (e == null || e.getClickedInventory() == null)
            return;

        InventoryType inv = e.getClickedInventory().getType();

        if (inv == InventoryType.PLAYER) {
            return;
        }

        ItemStack item = e.getCurrentItem();

        if (item != null && CompassUtils.isItemAPlayerTracker(item)) {
            Player p = (Player)e.getWhoClicked();
            if (CompassUtils.compassLimitReached(p, item.getAmount())) {
                showLimitMessage(p);
                e.setCancelled(true);
            }
        }
    }

    private void showLimitMessage(Player p) {
        if (showLimitMessage) {
            Messages.send(p, ChatColor.WHITE + "Compass limit reached.");
            showLimitMessage = false;
            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
                public void run() {
                    showLimitMessage = true;
                }
            }, 100);
        }
    }
}
