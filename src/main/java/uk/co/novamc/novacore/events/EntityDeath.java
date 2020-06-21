package uk.co.novamc.novacore.events;

import com.songoda.ultimatestacker.entity.EntityStack;
import me.max.lemonmobcoins.common.LemonMobCoins;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import uk.co.novamc.novacore.NovaCore;

import java.util.HashMap;
import java.util.Random;
import java.util.Set;

public class EntityDeath implements Listener {

    private NovaCore plugin;
    public EntityDeath(NovaCore plugin) {
        this.plugin = plugin;
    }

    Random random = new Random();

    @EventHandler
    public void entityDeath(EntityDeathEvent e) {
        if (plugin.iChestConfig.getBoolean("UseMobDrops")) {

            Entity entity = e.getEntity();

            //ignore player deaths
            if (!(entity instanceof Player)) {
                Chunk chunk = entity.getLocation().getChunk();
                String entityType = e.getEntityType().name();

                //calculate how many mobs there are in the UltimateStacker stack
                EntityStack entityStack = com.songoda.ultimatestacker.UltimateStacker.getInstance().getEntityStackManager().getStack(entity);
                int amount = 1;
                if (entityStack != null) {
                    amount = entityStack.getAmount();
                }

                //get config information
                int minChance = plugin.iChestConfig.getInt("MobDrops." + entityType + ".min");
                int maxChance = plugin.iChestConfig.getInt("MobDrops." + entityType + ".max");
                Material drop = Material.getMaterial(plugin.iChestConfig.getString("MobDrops." + entityType + ".item"));
                String dropName = drop.name();

                //calculate how many drops should be added
                int dropAmount = 0;
                for (int x = 0; x < amount; x++) {
                    dropAmount += (random.nextInt(maxChance - minChance + 1) + minChance);
                }

                Set<String> locations = plugin.iChestDatabase.getLocations();

                for (String blockLocation : locations) {

                    //split the blockLocation into usable parts to get the chunk
                    String[] parts = blockLocation.split(" ");
                    Chunk chestChunk = Bukkit.getWorld(plugin.iChestDatabase.getWorld(blockLocation)).getBlockAt(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2])).getChunk();

                    if (chunk.equals(chestChunk)) {

                        HashMap<String, String> blocksStored = plugin.iChestDatabase.getStoredBlocks(blockLocation);

                        //add the items to the chest

                        //add the amount if the material is already in the iChest
                        if (blocksStored.containsKey(dropName)) {
                            int index = Integer.parseInt(blocksStored.get(dropName).substring(blocksStored.get(dropName).length() - 1));
                            Long blockAmount = plugin.iChestDatabase.getAmount(blockLocation, index);
                            Long newAmount = blockAmount + dropAmount;
                            plugin.iChestDatabase.updateBlockAmount(blockLocation, newAmount, index);
                            e.getDrops().clear();

                        } else {

                            //add new material if it doesn't already exist
                            if (blocksStored.size() < 5) {
                                int newIndex = blocksStored.size() + 1;
                                plugin.iChestDatabase.addMaterial(blockLocation, (long) dropAmount, newIndex, dropName);
                                blocksStored.put(dropName, "blockType" + newIndex);
                                e.getDrops().clear();
                            }
                        }

                        //stop looking for a chest
                        return;
                    }
                }

                e.getDrops().clear();
                Material material = Material.valueOf(dropName);
                int maxStackSize = material.getMaxStackSize();
                int dropAmountClone = dropAmount;

                if (dropAmountClone / maxStackSize < 100) {
                    //add stack by stack until there is a non full stack left
                    while (dropAmountClone > maxStackSize) {
                        e.getDrops().add(new ItemStack(material, maxStackSize));
                        dropAmountClone -= maxStackSize;
                    }

                    //add what's left
                    e.getDrops().add(new ItemStack(material, dropAmountClone));
                } else {
                    ItemStack errorItem = new ItemStack(Material.PAPER, 1);
                    ItemMeta itemMeta = errorItem.getItemMeta();
                    itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&cERROR"));
                    errorItem.setItemMeta(itemMeta);
                    e.getDrops().add(errorItem);
                }
            }
        }

        //mobcoins

        if (!(e.getEntity() instanceof Player) && e.getEntity().getKiller() != null) {
            Entity entity = e.getEntity();
            Player player = e.getEntity().getKiller();
            //get config data
            String path = "mobs." + entity.getType().name();
            int chance = plugin.mobcoinConfig.getInt(path + ".chance");
            int minAmount = plugin.mobcoinConfig.getInt(path + ".minamount");
            int maxAmount = plugin.mobcoinConfig.getInt(path + ".maxamount");
            //find the amount of mobs
            EntityStack eStack = com.songoda.ultimatestacker.UltimateStacker.getInstance().getEntityStackManager().getStack(entity);
            if (eStack != null) {
                int entityAmount = eStack.getAmount();
                //calculate amount of mobcoins to give for each mob in the stack
                int mobcoins = 0;
                for (int x=0; x<entityAmount; x++) {
                    if ((random.nextInt(99) + 1) <= chance) {
                        int addAmount = random.nextInt(maxAmount - minAmount + 1) + minAmount;
                        mobcoins += addAmount;
                    }
                }
                if (mobcoins > 0) {
                    LemonMobCoins.getLemonMobCoinsAPI().addCoinsToPlayer(player.getUniqueId(), mobcoins);
                    String message = plugin.mobcoinConfig.getString("recievemessage");
                    message = message.replace("{amount}", Integer.toString(mobcoins));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                }
            }
        }
    }
}