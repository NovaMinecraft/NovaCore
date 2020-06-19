package uk.co.novamc.novacore.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Plugins implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String string, String[] args) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&fPlugins (1): &aNovaCore"));
        return true;
    }
}
