package uk.co.novamc.novacore.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import uk.co.novamc.novacore.NovaCore;

public class MainCommand implements CommandExecutor {

    private NovaCore plugin;
    public MainCommand(NovaCore plugin) {
        this.plugin = plugin;
    }

    private void sendMsg(CommandSender sender, String msg) {
        sender.sendMessage((ChatColor.translateAlternateColorCodes('&', msg)));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String string, String[] args) {
        if (args.length >=1) {
            String subCommand = args[0];
            boolean isPlayer = sender instanceof Player;
            if (subCommand.equals("reload") && (sender.hasPermission("nova.reload") || !isPlayer)) {
                plugin.reloadConfig();
                plugin.joinLeaveFile.reload();
                plugin.scoreboardFile.reload();
                plugin.paperCommandsFile.reload();
                plugin.stackValueFile.reload();
                plugin.iChestConfig.reload();
                plugin.iChestDatabase.reload();
                plugin.mobcoinConfig.reload();
                sendMsg(sender, "&aSuccessfully reloaded!");
            }
        }
        return true;
    }
}
