package uk.co.novamc.novacore.events;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import uk.co.novamc.novacore.NovaCore;

public class QuitEvent implements Listener {

    private NovaCore plugin;
    public QuitEvent(NovaCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        //set the event message
        e.setQuitMessage(PlaceholderAPI.setPlaceholders(e.getPlayer(), plugin.joinLeaveFile.getString("leave_message")));
    }
}
