package uk.co.novamc.novacore.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import uk.co.novamc.novacore.NovaCore;

public class PlayerMove implements Listener {

    private NovaCore plugin;
    public PlayerMove(NovaCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (plugin.getConfig().getBoolean("generic.island_void_tp")) {
            String worldName = e.getPlayer().getWorld().getName();
            if (worldName.equals("Island") || worldName.equals("Island_nether"))
                if (e.getTo().getBlockY() < 2) {
                    e.getPlayer().performCommand("is go");
                }
        }
    }
}