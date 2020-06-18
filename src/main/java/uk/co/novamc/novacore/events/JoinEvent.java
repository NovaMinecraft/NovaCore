package uk.co.novamc.novacore.events;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import uk.co.novamc.novacore.NovaCore;

public class JoinEvent implements Listener {

    private NovaCore plugin;
    public JoinEvent(NovaCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

        Player player = e.getPlayer();

        if (player.hasPlayedBefore()) {
            //set the event message
            e.setJoinMessage(PlaceholderAPI.setPlaceholders(player, plugin.joinLeaveFile.getString("join_message")));

        } else {

            //add to the total amount of players
            int totalPlayers = plugin.joinLeaveFile.getInt("total_players");
            totalPlayers++;
            plugin.joinLeaveFile.set("total_players", totalPlayers);

            //set the event message
            String playerNum = Integer.toString(plugin.joinLeaveFile.getInt("total_players"));
            e.setJoinMessage(PlaceholderAPI.setPlaceholders(player, plugin.joinLeaveFile.getString("join_message")).replace("{player_number}", playerNum));
        }
    }
}
