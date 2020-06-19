package uk.co.novamc.novacore.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import uk.co.novamc.novacore.NovaCore;

import java.text.NumberFormat;
import java.util.Locale;

public class SpawnerBuy implements CommandExecutor {
    private NovaCore plugin;
    public SpawnerBuy(NovaCore plugin) {
        this.plugin = plugin;
    }

    private void sendMsg(CommandSender sender, String msg) {
        sender.sendMessage((ChatColor.translateAlternateColorCodes('&', msg)));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 3) {
            if (args[0].equals("buy") && (sender instanceof Player)) {
                Player player = (Player) sender;
                String type = args[1];
                Long amount = Long.parseLong(args[2]);
                Long totalCost = 0L;
                try {
                    Long costPer = plugin.stackValueFile.getLong(type.toUpperCase());
                    totalCost = costPer * amount;

                    if (totalCost > 0) {
                        double balance = plugin.econ.getBalance(player);
                        if (balance >= totalCost) {

                            plugin.econ.withdrawPlayer(player, totalCost);
                            NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
                            String formattedAmount = numberFormat.format(totalCost);

                            Double newBalance = balance - totalCost;
                            String newBalanceFormatted = numberFormat.format(newBalance);

                            sendMsg(player, "&7You purchased &a" + type + "&8x" + amount + " &7for &6$" + formattedAmount + " &7new balance is &6$" + newBalanceFormatted);
                            Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "us givespawner " + sender.getName() + " " + type + " " + amount);
                        } else {
                            sendMsg(player, "&cYou do not have sufficient balance!");
                        }
                    } else {
                        sendMsg(player, "&cInvalid transaction, please reduce spawner amount!");
                    }

                } catch (NullPointerException exception) {
                    sendMsg(player, "&cInvalid mob type");
                }

            } else {
                sendMsg(sender, "Invalid parameter '" + args[0] + "' or non player");
            }
        } else {
            sendMsg(sender, "&cYou must include the type and the amount!");
        }
        return true;
    }
}
