package uk.co.novamc.novacore.events;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerInteract implements Listener {

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
    }

}
