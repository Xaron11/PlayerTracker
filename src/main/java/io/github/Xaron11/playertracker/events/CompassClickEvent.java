package io.github.Xaron11.playertracker.events;

import io.github.Xaron11.playertracker.CompassUtils;
import io.github.Xaron11.playertracker.Config;
import io.github.Xaron11.playertracker.Main;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;

public class CompassClickEvent implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Action action = event.getAction();
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (CompassUtils.isItemAPlayerTracker(item))
            if (player.hasPermission("playertracker.use") && action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) {

                ArrayList<Player> playerList = new ArrayList<Player>(player.getServer().getOnlinePlayers());
                playerList.remove(player);
                Inventory compassInventory = Bukkit.createInventory(player, 45, Main.PLAYER_TRACKER_NAME);
                compassInventory.addItem(Main.WorldHead);

                for (Player p : playerList) {
                    ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD, 1);
                    SkullMeta meta = (SkullMeta) playerHead.getItemMeta();
                    meta.setDisplayName(p.getDisplayName());
                    meta.setOwningPlayer(p);
                    playerHead.setItemMeta(meta);
                    compassInventory.addItem(playerHead);
                }

                player.openInventory(compassInventory);
                player.playSound(player.getLocation(), Sound.BLOCK_DISPENSER_DISPENSE, 1.0F, 8.0F);
            }
            else if (player.hasPermission("playertracker.distance") && action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK)) {
                if (!Config.ShowCompassTargetDistance && !Config.ShowCompassTargetCoords) {
                    return;
                }

                Location target = ((CompassMeta)item.getItemMeta()).getLodestone();
                String text = "";

                if (Config.ShowCompassTargetDistance) {
                    Location start = player.getLocation();
                    start.setY(0);
                    double distance = start.distance(target);
                    int rounded = (int)Math.round(distance);
                    text += ChatColor.GOLD + String.valueOf(rounded) + " blocks";
                }

                if (Config.ShowCompassTargetCoords) {
                    if (Config.ShowCompassTargetDistance) text += " ";
                    text += ChatColor.YELLOW + "(" + ChatColor.WHITE + target.getX() + ", " + target.getZ() + ChatColor.YELLOW + ")";
                }

                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(text));
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1.0F, 8.0F);
            }
    }
}
