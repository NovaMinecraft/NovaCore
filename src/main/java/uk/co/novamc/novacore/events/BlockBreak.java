package uk.co.novamc.novacore.events;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.novamc.novacore.NovaCore;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BlockBreak implements Listener {
    private NovaCore plugin;
    public BlockBreak(NovaCore plugin) {
        this.plugin = plugin;
    }

    private void sendMsg(CommandSender sender, String msg) {
        sender.sendMessage((ChatColor.translateAlternateColorCodes('&', msg)));
    }

    final Logger logger = LoggerFactory.getLogger(BlockBreak.class);

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Block block = e.getBlock();
        Player player = e.getPlayer();
        if (block.getType().equals(Material.ENDER_CHEST)) {
            String blockLocation = (block.getX() + 1) + " " + block.getY() + " " + block.getZ();
            for (String possibleLoc : plugin.iChestDatabase.getLocations()) {
                //found chest data
                if (blockLocation.equals(possibleLoc)) {
                    ConfigurationSection chestData = plugin.iChestDatabase.getChest(blockLocation);
                    if (UUID.fromString(chestData.getString("owner")).equals(player.getUniqueId())) {
                        if (player.getInventory().firstEmpty() != -1) {

                            plugin.iChestDatabase.deleteChest(blockLocation);
                            block.setType(Material.AIR);

                            //create iChest item stack
                            ItemStack infChest = new ItemStack(Material.ENDER_CHEST, 1);
                            ItemMeta itemMeta = infChest.getItemMeta();
                            itemMeta.setDisplayName(plugin.iChestConfig.getString("iChest.displayname"));
                            List<String> lores = new ArrayList<>();
                            for (String lore : plugin.iChestConfig.getStringList("iChest.lore")) {
                                lores.add(ChatColor.translateAlternateColorCodes('&', lore));
                            }
                            itemMeta.setLore(lores);
                            infChest.setItemMeta(itemMeta);

                            //set nbt tag
                            NBTItem nbti = new NBTItem(infChest);
                            nbti.setBoolean("isInfiniteChest", true);
                            infChest = nbti.getItem();
                            player.getInventory().addItem(infChest);

                            //delete hologram
                            for (Hologram hologram : HologramsAPI.getHolograms(plugin)) {
                                if (hologram.getX() != block.getX() + 0.5) {
                                    return;
                                } else if (hologram.getY() != block.getY() + 1.6) {
                                    return;
                                } else if (hologram.getZ() != block.getZ() + 0.5) {
                                    return;
                                }
                                hologram.delete();
                            }

                            sendMsg(player, "&7You &c&lBroke &7your iChest and it is now in your inventory.");
                        } else {
                            e.setCancelled(true);
                            sendMsg(player, "&cYou're inventory is full!");
                        }
                    }
                }
            }
        }
    }
}
