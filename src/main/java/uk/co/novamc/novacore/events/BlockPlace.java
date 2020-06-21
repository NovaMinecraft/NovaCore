package uk.co.novamc.novacore.events;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import uk.co.novamc.novacore.NovaCore;

import java.util.UUID;

public class BlockPlace implements Listener {

    private NovaCore plugin;
    public BlockPlace(NovaCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if (plugin.getConfig().getBoolean("generic.one_spawner_per_chunk")) {
            Block block = e.getBlockPlaced();
            Player player = e.getPlayer();

            if (block.getType().equals(Material.MOB_SPAWNER)) {
                for (int x=0; x<16; x++) {
                    for (int y=0; y<256; y++) {
                        for (int z=0; z<16; z++) {
                            Block blockInChunk = block.getChunk().getBlock(x, y, z);
                            if (block.getLocation().equals(blockInChunk.getLocation())) {
                                continue;
                            }
                            if (blockInChunk.getType().equals(Material.MOB_SPAWNER)) {
                                CreatureSpawner placedSpawner = (CreatureSpawner) block.getState();
                                CreatureSpawner chunkSpawner = (CreatureSpawner) blockInChunk.getState();
                                if (placedSpawner.getSpawnedType().equals(chunkSpawner.getSpawnedType())) {
                                    e.setCancelled(true);
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou may not place more than one identical spawner in a chunk!"));
                                }
                            }
                        }
                    }
                }
            }

            ItemStack itemStack = e.getItemInHand();
            if (itemStack.getType().equals(Material.ENDER_CHEST)) {
                NBTItem nbti = new NBTItem(itemStack);
                if (nbti.hasKey("isInfiniteChest")) {

                    //if the iChest is valid
                    if (nbti.getBoolean("isInfiniteChest")) {

                        String blockLocation = block.getX() + " " + block.getY() + " " + block.getZ();
                        plugin.iChestDatabase.createChest(blockLocation, player, e.getBlock().getWorld());

                        //create hologram
                        Location location = block.getLocation();
                        location.add(0.5, 1.6, 0.5);
                        Hologram hologram = HologramsAPI.createHologram(plugin, location);
                        TextLine textLine1 = hologram.appendTextLine(plugin.iChestConfig.getString("iChest.displayname"));
                        String nameText = ChatColor.translateAlternateColorCodes('&', "&f&lOwner: &r" + player.getName()).trim();
                        TextLine textLine2 = hologram.insertTextLine(1, nameText);

                        //add to config
                        String hologramUUID = UUID.randomUUID().toString();
                        plugin.iChestDatabase.setHolo(hologramUUID + ".owner", player.getName());
                        plugin.iChestDatabase.setHolo(hologramUUID + ".world", player.getWorld().getName());
                        plugin.iChestDatabase.setHolo(hologramUUID + ".location_x", block.getX());
                        plugin.iChestDatabase.setHolo(hologramUUID + ".location_y", block.getY());
                        plugin.iChestDatabase.setHolo(hologramUUID + ".location_z", block.getZ());

                        plugin.logger.info("iChest has been created at " + blockLocation + " by " + player.getName());
                    }
                }
            }
        }
    }
}
