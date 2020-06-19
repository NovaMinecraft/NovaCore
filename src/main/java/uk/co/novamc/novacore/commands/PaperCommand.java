package uk.co.novamc.novacore.commands;

import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import uk.co.novamc.novacore.NovaCore;

public class PaperCommand implements CommandExecutor {

    private NovaCore plugin;
    public PaperCommand(NovaCore plugin) {
        this.plugin = plugin;
    }

    //send a message using colour codes
    private void sendMsg(CommandSender sender, String msg) {
        sender.sendMessage((ChatColor.translateAlternateColorCodes('&', msg)));
    }

    //Pcommand
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        //ensure the user is a player
        if (sender.hasPermission("pcommand.pcommand") || !(sender instanceof Player)) {
            Player otherPlayer;
            String otherPlayerName;
            String commandpath;
            //check the arguments exist
            if (args.length >= 2) {
                otherPlayerName = args[0];
                otherPlayer = Bukkit.getPlayer(otherPlayerName);
                commandpath = args[1];

            } else {
                sendMsg(sender, "&CYou must include the player and the pcommand!");
                return true;
            }

            ConfigurationSection section = plugin.paperCommandsFile.getConfigurationSection("pcommands");
            ConfigurationSection section2 = section.getConfigurationSection(commandpath);
            String cmd = section2.getString("command");

            //create an item stack of the paper and then convert it to an NBTItem object to add the tag
            ItemStack paper = new ItemStack(Material.PAPER, 1);

            //Change it's name
            ItemMeta meta = paper.getItemMeta();
            String displayName = ChatColor.translateAlternateColorCodes('&', section2.getString("name"));
            meta.setDisplayName(displayName);
            paper.setItemMeta(meta);

            NBTItem nbti = new NBTItem(paper);
            nbti.setString("Pcommand", cmd);

            //give the player the item
            otherPlayer.getInventory().addItem(nbti.getItem());
            otherPlayer.updateInventory();

            //message when the command is ran
            if (sender instanceof Player) {
                sendMsg(sender, "&AYou gave " + otherPlayerName + " pcommand: " + commandpath);
            }

        } else {
            sendMsg(sender, "&cYou do not have permission!");
        }

        return true;
    }
}