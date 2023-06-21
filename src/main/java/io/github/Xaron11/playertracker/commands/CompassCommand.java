package io.github.Xaron11.playertracker.commands;

import io.github.Xaron11.playertracker.CompassUtils;
import io.github.Xaron11.playertracker.Config;
import io.github.Xaron11.playertracker.Messages;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CompassCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (checkCommandSender(sender)) {
            Player p = (Player)sender;
            if (Config.LimitCompassesInInventory != 0) {
                if (CompassUtils.compassLimitReached(p)) {
                    Messages.send(p, ChatColor.WHITE + "Compass limit reached.");
                    return false;
                }
            }
            ItemStack compass = CompassUtils.createCompass(p);
            p.getInventory().addItem(compass);
            return true;
        }

        return false;
    }

    public static boolean checkCommandSender(CommandSender sender) {
        if (sender instanceof Player) {
            return true;
        }

        Messages.send(sender, ChatColor.WHITE + "Only players can execute this command.");
        return false;
    }
}
