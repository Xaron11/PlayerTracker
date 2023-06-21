package io.github.Xaron11.playertracker.commands;

import io.github.Xaron11.playertracker.Config;
import io.github.Xaron11.playertracker.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CompassReloadCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Main.getInstance().reloadConfig();
        Config.load(Main.getInstance().getConfig());
        return true;
    }
}
