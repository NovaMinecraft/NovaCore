package uk.co.novamc.novacore.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import uk.co.novamc.novacore.NovaCore;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class FoodLevelChange implements Listener {

    private NovaCore plugin;
    public FoodLevelChange(NovaCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent e) {
        if (plugin.getConfig().getBoolean("disable_hunger")) {
            if (e.getEntity() instanceof Player) {
                Player player = (Player) e.getEntity();
                if (player.getFoodLevel() < 20) {
                    player.setFoodLevel(20);
                }
            }
        }
    }
}
