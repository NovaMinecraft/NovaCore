package uk.co.novamc.novacore.tasks;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.novamc.novacore.NovaCore;

import java.util.HashMap;

public class IChestAbsorb extends BukkitRunnable {

    private NovaCore plugin;
    public IChestAbsorb(NovaCore plugin) {
        this.plugin = plugin;
    }

    final Logger logger = LoggerFactory.getLogger(IChestAbsorb.class);

    @Override
    public void run() {
        int chestTotal = 0;
        int itemTotal = 0;
        for (String blockLocation : plugin.iChestDatabase.getLocations()) {
            chestTotal++;
            String[] coords = blockLocation.split(" ");
            ConfigurationSection chestData = plugin.iChestDatabase.getChest(blockLocation);
            Block chestBlock = Bukkit.getWorld(chestData.getString("world")).getBlockAt(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]), Integer.parseInt(coords[2]));
            HashMap<String, String> blocksStored = plugin.iChestDatabase.getStoredBlocks(blockLocation);
            for (Entity entity : chestBlock.getChunk().getEntities()) {
                //loop through each item
                if (entity instanceof Item) {
                    ItemStack itemStack = ((Item) entity).getItemStack();
                    itemTotal += itemStack.getAmount();
                    String materialName = itemStack.getType().name();
                    //if the item type is already in the chest
                    if (blocksStored.containsKey(materialName)) {
                        int index = Integer.parseInt(blocksStored.get(materialName).substring(blocksStored.get(materialName).length() - 1));
                        Long blockAmount = chestData.getLong("blockAmount" + index);
                        Long newAmount = blockAmount + itemStack.getAmount();
                        plugin.iChestDatabase.updateBlockAmount(blockLocation, newAmount, index);
                        entity.remove();
                    } else {
                        //try add new material
                        if (blocksStored.size() < 5) {
                            int newIndex = blocksStored.size() + 1;
                            int newAmount = itemStack.getAmount();
                            plugin.iChestDatabase.addMaterial(blockLocation, (long) newAmount, newIndex, materialName);
                            blocksStored.put(materialName, "blockType" + newIndex);
                            entity.remove();
                        }
                    }
                }
            }
        }
        if (plugin.iChestConfig.getBoolean("Logging.absorbitems")) {
            logger.info("Absorbed " + itemTotal + " item(s) into " + chestTotal + " iChest(s)");
        }
    }
}
