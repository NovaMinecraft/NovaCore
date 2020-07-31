package uk.co.novamc.novacore.events;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.ChatColor;
import org.bukkit.Location;
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
            String blockLocation = block.getX() + " " + block.getY() + " " + block.getZ();
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
                            itemMeta.setLore(plugin.iChestConfig.getStringList("iChest.lore"));
                            infChest.setItemMeta(itemMeta);

                            //set nbt tag
                            NBTItem nbti = new NBTItem(infChest);
                            nbti.setBoolean("isInfiniteChest", true);
                            infChest = nbti.getItem();
                            player.getInventory().addItem(infChest);

                            //delete hologram
                            for (Hologram hologram : HologramsAPI.getHolograms(plugin)) {
                                Location location = hologram.getLocation();
                                location.subtract(0.5, 1.6, 0.5);
                                if (block.getLocation().equals(location)) {
                                    hologram.delete();
                                }
                                // logger.info("Block Location: " + location.getX() + " " + location.getY() + " " + location.getZ());
                            }

                            //remove from config
                            ConfigurationSection configurationSection = plugin.iChestDatabase.getHolo();
                            if (configurationSection != null) {
                                for (String key : configurationSection.getKeys(false)) {
                                    int locX = configurationSection.getInt(key + ".location_x");
                                    int locY = configurationSection.getInt(key + ".location_y");
                                    int locZ = configurationSection.getInt(key + ".location_z");
                                    if (locX == block.getX() && locY == block.getY() && locZ == block.getZ()) {
                                        plugin.iChestDatabase.setHolo(key, null);
                                    }
                                }
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
