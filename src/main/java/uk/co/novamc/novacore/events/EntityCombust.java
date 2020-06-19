package uk.co.novamc.novacore.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustByBlockEvent;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import uk.co.novamc.novacore.NovaCore;

public class EntityCombust implements Listener {

    private NovaCore plugin;
    public EntityCombust(NovaCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onFoodLevelChange(EntityCombustEvent e) {
        if (plugin.getConfig().getBoolean("generic.mobs_no_burn")) {
            if (!(e instanceof EntityCombustByEntityEvent || e instanceof EntityCombustByBlockEvent)) {
                e.setCancelled(true);
            }
        }
    }
}