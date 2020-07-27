package uk.co.novamc.novacore.commands;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import uk.co.novamc.novacore.NovaCore;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class IChestCommands implements CommandExecutor {

    private NovaCore plugin;
    public IChestCommands(NovaCore plugin) {
        this.plugin = plugin;
    }

    private void sendMsg(CommandSender sender, String msg) {
        sender.sendMessage((ChatColor.translateAlternateColorCodes('&', msg)));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args[0].equals("give") && (sender.hasPermission("ichest.give") || !(sender instanceof Player))) {
            if (args.length == 3) {

                Player receiver = Bukkit.getPlayer(args[1]);
                int amount = Integer.parseInt(args[2]);

                //create the iChest item stack and set item meta
                ItemStack infChest = new ItemStack(Material.ENDER_CHEST, amount);
                ItemMeta itemMeta = infChest.getItemMeta();
                itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.iChestConfig.getString("iChest.displayname")));
                itemMeta.setLore(plugin.iChestConfig.getStringList("iChest.lore"));
                infChest.setItemMeta(itemMeta);

                //set the iChest nbt tag
                NBTItem nbti = new NBTItem(infChest);
                nbti.setBoolean("isInfiniteChest", true);
                infChest = nbti.getItem();

                //gie the player the iChest
                if (receiver.getInventory().firstEmpty() != -1) {
                    receiver.getInventory().addItem(infChest);
                    sendMsg(receiver, "&7You received " + amount + "x iChest(s)");

                    //drop the chest on the floor if their inventory is full
                } else {
                    receiver.getWorld().dropItem(receiver.getLocation().add(0, 1, 0), infChest);
                    sendMsg(receiver, amount + "x iChest(s) were dropped beside you (Full Inventory)");
                }
                sendMsg(sender, "&aYou gave " + receiver.getName() + " " + amount + "x iChest(s)");

            } else {
                sendMsg(sender, "&cYou must include the players name and amount of iChests");
            }
        } else if (args[0].equals("purge") && args[1].equals("db") && (sender.hasPermission("ichest.database.purge") || !(sender instanceof Player))) {

            plugin.iChestDatabase.purgeDatabase();
            sendMsg(sender, "&c&lPurged &7the database!");

        } else if (args[0].equals("trust") || sender instanceof Player) {

            Player player = (Player) sender;
            Player addPlayer = Bukkit.getPlayer(args[1]);

            UUID playerUUID = player.getUniqueId();
            UUID addPlayerUUID = addPlayer.getUniqueId();

            String playerUUIDString = playerUUID.toString();
            String addPlayerUUIDString = addPlayerUUID.toString();

            ArrayList<String> trusted = (ArrayList<String>) plugin.iChestDatabase.getTrust().getList(playerUUIDString);

            if (trusted == null) {
                trusted = new ArrayList<String>();
            }

            if (trusted.contains(addPlayerUUIDString)) {
                sendMsg(player, "&aYou already trust this player!");

                //save the new ArrayList of players to the file
            } else {
                trusted.add(addPlayerUUIDString);
                plugin.iChestDatabase.setTrust(playerUUIDString, trusted);
                sendMsg(player, "&8[&aiChest&8] &7Successfully trusted &a" + addPlayer.getName());
            }

        } else if (args[0].equals("untrust") || sender instanceof Player) {

            Player player = (Player) sender;
            Player removePlayer = Bukkit.getPlayer(args[1]);

            UUID playerUUID = player.getUniqueId();
            UUID remPlayerUUID = removePlayer.getUniqueId();

            String playerUUIDString = playerUUID.toString();
            String remPlayerUUIDString = remPlayerUUID.toString();

            List<?> trusted = plugin.iChestDatabase.getTrust().getList(playerUUIDString);

            if (trusted == null) {
                trusted = new ArrayList<String>();
            }

            //set the config to the ArrayList without the removed player
            if (trusted.contains(remPlayerUUIDString)) {
                trusted.remove(remPlayerUUIDString);
                plugin.iChestDatabase.setTrust(playerUUIDString, trusted);
                sendMsg(player, "&8[&aiChest&8] &7Successfully untrusted &c" + removePlayer.getName());

            } else {
                sendMsg(player, "&aYou already don't trust this player!");
            }

        } else {
            sendMsg(sender, "&cThis is not a command");
        }
        return true;
    }
}
