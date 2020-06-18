package uk.co.novamc.novacore.events;

import org.bukkit.entity.EntityType;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import uk.co.novamc.novacore.NovaCore;

public class ProjectileLaunch implements Listener {

    private NovaCore plugin;
    public ProjectileLaunch(NovaCore plugin) {
        this.plugin = plugin;
    }

    public void onProjectileLaunch(ProjectileLaunchEvent e) {
        if (plugin.getConfig().getBoolean("disable_pots_on_island") && e.getEntity().getWorld().getName().equals("Island")) {
            if (e.getEntity().getType().equals(EntityType.SPLASH_POTION)) {
                e.setCancelled(true);
            }
        }
    }
}