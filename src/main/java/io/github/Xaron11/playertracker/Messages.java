package io.github.Xaron11.playertracker;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class Messages {

    private Messages() {}

    public static void sendConsole(String message) {
        Bukkit.getConsoleSender().sendMessage(Main.PLAYER_TRACKER_PREFIX + " " + message);
    }

    public static void send(CommandSender sender, String message) {
        sender.sendMessage(Main.PLAYER_TRACKER_PREFIX + " " + message);
    }

    public static void send(Player player, String message) {
        player.sendMessage(Main.PLAYER_TRACKER_PREFIX + " " + message);
    }
}
