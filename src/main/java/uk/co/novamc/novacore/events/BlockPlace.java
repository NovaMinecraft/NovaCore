package uk.co.novamc.novacore.events;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import uk.co.novamc.novacore.NovaCore;

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
        }
    }
}
