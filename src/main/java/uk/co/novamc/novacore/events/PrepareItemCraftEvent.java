package uk.co.novamc.novacore.events;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import uk.co.novamc.novacore.NovaCore;

public class PrepareItemCraftEvent implements Listener {

    private NovaCore plugin;
    public PrepareItemCraftEvent(NovaCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void craftEvent(org.bukkit.event.inventory.PrepareItemCraftEvent e) {
        if (plugin.getConfig().getBoolean("generic.disable_hopper_gapple_crafting")) {
            Material itemType = e.getRecipe().getResult().getType();
            if ((itemType.equals(Material.HOPPER) || (itemType.equals(Material.GOLDEN_APPLE)))) {
                e.getInventory().setResult(new ItemStack(Material.AIR));
                for (HumanEntity player:e.getViewers()) {
                    if (player instanceof Player) {
                        ((Player)player).sendMessage(ChatColor.RED + "You cannot craft this item, use /shop to obtain it!");
                    }
                }
            }
        }
    }
}
