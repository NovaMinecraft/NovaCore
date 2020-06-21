package uk.co.novamc.novacore.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import uk.co.novamc.novacore.NovaCore;

import java.text.NumberFormat;
import java.util.*;

public class InventoryClick implements Listener {
    private NovaCore plugin;
    public InventoryClick(NovaCore plugin) {
        this.plugin = plugin;
    }

    private void sendMsg(CommandSender sender, String msg) {
        sender.sendMessage((ChatColor.translateAlternateColorCodes('&', msg)));
    }

    @EventHandler
    public void clickEvent(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();

        if (e.getClickedInventory() != null) {
            String inventoryTitle = e.getClickedInventory().getTitle();

            if (inventoryTitle.contains(plugin.iChestConfig.getString("iChest.gui.title"))) {
                String blockLocation = inventoryTitle.substring(inventoryTitle.lastIndexOf("|") + 2);
                switch(e.getRawSlot()) {

                    //blocks stored in iChest
                    case 20:
                        blockClick(e, 1, blockLocation);
                        break;
                    case 21:
                        blockClick(e, 2, blockLocation);
                        break;
                    case 22:
                        blockClick(e, 3, blockLocation);
                        break;
                    case 23:
                        blockClick(e, 4, blockLocation);
                        break;
                    case 24:
                        blockClick(e, 5, blockLocation);
                        break;

                    //exit button
                    case 40:
                        player.closeInventory();
                        break;

                    //sell all button
                    case 44:
                        if (player.hasPermission("ichest.sell")) {
                            Long sellAmount = 0L;
                            HashMap<String, Long> blocksAndAmount = plugin.iChestDatabase.getBlocksAndAmount(blockLocation);
                            if (!blocksAndAmount.isEmpty()) {

                                //loop through every item in the chest, to get the total value
                                for (Map.Entry<String, Long> entry : blocksAndAmount.entrySet()) {
                                    String material = entry.getKey();
                                    Long amount = entry.getValue();
                                    int value = plugin.getConfig().getInt("BlockValues." + material, 0);
                                    sellAmount += amount*value;
                                }

                                NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
                                String formattedAmount = numberFormat.format(sellAmount);

                                plugin.iChestDatabase.wipeMaterials(blockLocation);
                                plugin.econ.depositPlayer(player, sellAmount);
                                sendMsg(player, "&8[&aiChest&8] &7You sold all items for &6$" + formattedAmount);
                                refreshInventory(blockLocation, player);

                            } else {
                                sendMsg(player, "&cYour iChest is empty!");
                            }
                        } else {
                            sendMsg(player, plugin.iChestConfig.getString("Messages.sellallnoperm"));
                        }
                }
                e.setCancelled(true);
            }
        }
    }

    public void blockClick(InventoryClickEvent e, int index, String blockLocation) {

        Player player = (Player) e.getWhoClicked();
        int emptySlots = 0;

        //set "emptySlots" to 1 if left click to only give up to a stack
        if (e.getClick().equals(ClickType.RIGHT)) {
            emptySlots = getEmptySlots(player);
        } else if (e.getClick().equals(ClickType.LEFT)) {
            emptySlots = 1;
        }

        if (player.getInventory().firstEmpty() != -1) {
            String materialString = plugin.iChestDatabase.getMaterial(blockLocation, index);

            if (materialString != null) {
                Material material = Material.valueOf(materialString);
                int maxStackSize = material.getMaxStackSize();
                Long beforeAmount = plugin.iChestDatabase.getAmount(blockLocation, index);

                if (beforeAmount > (maxStackSize*emptySlots)) {
                    plugin.iChestDatabase.updateBlockAmount(blockLocation, beforeAmount-(64*emptySlots), index);
                    giveItems(player, maxStackSize*emptySlots, material);

                    //lower the item indexes and give the player what's left if the amount is less than what the player requests
                } else {
                    int amountToLower = 5 - index;
                    for (int x=1; x<=amountToLower; x++) {
                        int indexToLower = index + x;
                        plugin.iChestDatabase.lowerIndex(blockLocation, indexToLower);
                    }
                    giveItems(player, Math.toIntExact(beforeAmount), material);
                }

                refreshInventory(blockLocation, player);

            } else {
                sendMsg(player, "&cThere is nothing here!");
            }
        } else {
            sendMsg(player, "&cYour inventory is full!");
        }
    }

    public void giveItems(Player player, int amount, Material material) {
        int maxStackSize = material.getMaxStackSize();
        while (amount > maxStackSize) {
            player.getInventory().addItem(new ItemStack(material, maxStackSize));
            amount -= maxStackSize;
        }
        player.getInventory().addItem(new ItemStack(material, amount));
    }

    public int getEmptySlots(Player p) {
        PlayerInventory inventory = p.getInventory();
        ItemStack[] cont = inventory.getContents();
        int i = 0;
        for (ItemStack item : cont)
            if (item != null && item.getType() != Material.AIR) {
                i++;
            }
        return 36 - i;
    }

    public void refreshInventory(String blockLocation, Player player) {
        player.closeInventory();
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
