package io.github.Xaron11.playertracker.events;

import io.github.Xaron11.playertracker.CompassUtils;
import io.github.Xaron11.playertracker.Main;
import io.github.Xaron11.playertracker.Messages;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class CompassUIEvent implements Listener {

    @EventHandler
    public void onMenuClick(InventoryClickEvent e) {
        final Player player = (Player)e.getWhoClicked();
        if (e.getView().getTitle().equalsIgnoreCase(Main.PLAYER_TRACKER_NAME)) {
            if (e.getClickedInventory().getType() == InventoryType.PLAYER) {
                e.setCancelled(true);
            }

            ItemStack clickedItem = e.getCurrentItem();
            if (clickedItem != null && clickedItem.getType() == Material.PLAYER_HEAD) {
                ItemStack compass = player.getInventory().getItemInMainHand();
                if (Main.WorldHead.getItemMeta().getDisplayName().equalsIgnoreCase(e.getCurrentItem().getItemMeta().getDisplayName())) {

                    Messages.send(player, ChatColor.WHITE + "Tracking world spawn");
                    Messages.sendConsole(ChatColor.WHITE + player.getDisplayName() + " is now tracking the world spawn");

                    compass = CompassUtils.setTrackingNBT(compass, player.getWorld().getUID().toString());

                }
                else {
                    Player whoToTrack = player.getServer().getPlayer(e.getCurrentItem().getItemMeta().getDisplayName());

                    Messages.send(player, ChatColor.WHITE + "Tracking " + whoToTrack.getDisplayName());
                    Messages.sendConsole(ChatColor.WHITE + player.getDisplayName() + " is now tracking " + whoToTrack.getDisplayName());

                    compass = CompassUtils.setTrackingNBT(compass, whoToTrack.getUniqueId().toString());

                }
                player.getInventory().setItemInMainHand(compass);

                e.setCancelled(true);
                player.closeInventory();
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1.0F, 8.0F);
            }
        }
    }
}
