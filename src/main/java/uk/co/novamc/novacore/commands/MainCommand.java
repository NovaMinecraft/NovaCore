package uk.co.novamc.novacore.commands;

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

    @Override
    public boolean onCommand(CommandSender sender, Command command, String string, String[] args) {
        if (args.length >=1) {
            String subCommand = args[1];
            boolean isPlayer = sender instanceof Player;
            if (subCommand.equals("reload") && (sender.hasPermission("nova.reload") || !isPlayer)) {
                plugin.reloadConfig();
                plugin.joinLeaveFile.reload();
                plugin.scoreboardFile.reload();
            }
        }
        return true;
    }
}
