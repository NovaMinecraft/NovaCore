package uk.co.novamc.novacore.events;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import uk.co.novamc.novacore.NovaCore;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PlayerInteract implements Listener {

    private NovaCore plugin;
    public PlayerInteract(NovaCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {

        //paper command

        Player player = e.getPlayer();
        Action action = e.getAction();
        ItemStack itemInHand = player.getItemInHand();

        //if the player right clicks with paper in hand
        if (((action == Action.RIGHT_CLICK_AIR) || (action == Action.RIGHT_CLICK_BLOCK)) && itemInHand.getType() == Material.PAPER) {

            //convert to nbt item and get the pcommand tag
            NBTItem nbti = new NBTItem(itemInHand);
            String command = nbti.getString("Pcommand");

            if (command != null) {
                String placeholderCommand = command.replace("{player}", player.getName());

                //run command in console
                ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                Bukkit.dispatchCommand(console, placeholderCommand);

                //delete the item
                player.getInventory().removeItem(itemInHand);
            }
        }

        //iChest

        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Block block = e.getClickedBlock();
            if (block.getType().equals(Material.ENDER_CHEST)) {
                String blockLocation = block.getX() + " " + block.getY() + " " + block.getZ();

                //check the clicked block is an iChest
                if (plugin.iChestDatabase.getLocations().contains(blockLocation)) {
                    String stringPlayerUUID = player.getUniqueId().toString();
                    String chestOwner = plugin.iChestDatabase.getOwner(blockLocation);
                    List<?> trusted = plugin.iChestDatabase.getTrust(chestOwner);
                    if (trusted == null) {
                        trusted = new ArrayList<String>();
                    }

                    //only allow the owner or people they trust to open the chest
                    if (chestOwner.equals(stringPlayerUUID) || trusted.contains(stringPlayerUUID) || player.hasPermission("ichest.openall")) {

                        e.setCancelled(true);

                        String chestTitleIncLocation = plugin.iChestConfig.getString("iChest.gui.title") + ChatColor.translateAlternateColorCodes('&', " &7| " + blockLocation);
                        Inventory chestGUI = Bukkit.createInventory(player, 45 , chestTitleIncLocation);
                        ItemStack panes = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) plugin.iChestConfig.getInt("iChest.gui.panecolour"));
                        ItemStack exit = new ItemStack(Material.BARRIER, 1);
                        ItemStack sellAll = new ItemStack(Material.NETHER_STAR, 1);
                        String guiItemName = plugin.iChestConfig.getString("iChest.gui.items.itemname");
                        List<String> guiItemLore = plugin.iChestConfig.getStringList("iChest.gui.items.itemlore");
                        ArrayList<String> blocks = plugin.iChestDatabase.getRawBlocksStored(blockLocation);

                        //set metadata to the buttons

                        ItemMeta none = panes.getItemMeta();
                        none.setDisplayName(" ");
                        panes.setItemMeta(none);

                        ItemMeta exitMeta = exit.getItemMeta();
                        exitMeta.setDisplayName(plugin.iChestConfig.getString("iChest.gui.exittext"));
                        exit.setItemMeta(exitMeta);

                        ItemMeta sellAllMeta = sellAll.getItemMeta();
                        sellAllMeta.setDisplayName(plugin.iChestConfig.getString("iChest.gui.sellalltext"));
                        sellAll.setItemMeta(sellAllMeta);

                        //set blocks
                        for (int x=0; x<5; x++) {
                            String itemID = blocks.get(x);
                            ItemStack chestItem = new ItemStack(Material.AIR, 1);
                            if (itemID != null) {
                                chestItem = new ItemStack(Material.getMaterial(itemID), 1);
                                itemID = itemID.substring(0, 1).toUpperCase() + itemID.substring(1).toLowerCase();
                                NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
                                String formattedAmount = numberFormat.format(plugin.iChestDatabase.getAmount(blockLocation, x+1));

                                //set title and lore
                                ItemMeta itemMeta = chestItem.getItemMeta();
                                itemMeta.setDisplayName(guiItemName.replace("{material}", itemID));
                                ArrayList<String> uniqueLore = new ArrayList<>();
                                for (String line : guiItemLore) {
                                    uniqueLore.add(line.replace("{amount}", formattedAmount));
                                }
                                itemMeta.setLore(uniqueLore);
                                chestItem.setItemMeta(itemMeta);
                            }

                            chestGUI.setItem(x+20, chestItem);
                        }

                        //set panes
                        chestGUI.setItem(0, panes);
                        chestGUI.setItem(1, panes);
                        chestGUI.setItem(2, panes);
                        chestGUI.setItem(3, panes);
                        chestGUI.setItem(4, panes);
                        chestGUI.setItem(5, panes);
                        chestGUI.setItem(6, panes);
                        chestGUI.setItem(7, panes);
                        chestGUI.setItem(8, panes);
                        chestGUI.setItem(9, panes);
                        chestGUI.setItem(17, panes);
                        chestGUI.setItem(18, panes);
                        chestGUI.setItem(26, panes);
                        chestGUI.setItem(27, panes);
                        chestGUI.setItem(35, panes);
                        chestGUI.setItem(36, panes);
                        chestGUI.setItem(37, panes);
                        chestGUI.setItem(38, panes);
                        chestGUI.setItem(39, panes);
                        chestGUI.setItem(41, panes);
                        chestGUI.setItem(42, panes);
                        chestGUI.setItem(43, panes);

                        //set barrier
                        chestGUI.setItem(40, exit);

                        //set sell all
                        chestGUI.setItem(44, sellAll);

                        //make player open the inventory
                        player.openInventory(chestGUI);
                    }
                }
            }
        }
    }
}
