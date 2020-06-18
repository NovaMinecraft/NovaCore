package uk.co.novamc.novacore.events;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.*;
import uk.co.novamc.novacore.NovaCore;

import java.util.List;

public class JoinEvent implements Listener {

    private NovaCore plugin;
    public JoinEvent(NovaCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

        Player player = e.getPlayer();

        //join messages

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

        //scoreboard

        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("Dummy", "");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        //set the title
        String title = PlaceholderAPI.setPlaceholders(player, plugin.scoreboardFile.getString("title"));
        objective.setDisplayName(title);


        List<String> lines = plugin.scoreboardFile.getStringList("lines");
        int totalLines = lines.size();
        for (String line : lines) {
            line = PlaceholderAPI.setPlaceholders(player, line);
            Team team = scoreboard.registerNewTeam(Integer.toString(totalLines));

            String part1 = "";
            String part2 = "";
            String part3 = "";
            int length = line.length();
            if (length <= 16) {
                //set part 2 or it will be duplicate
                part2 = line;
            } else if (length <= 32) {
                part1 = line.substring(0, 15);
                part2 = line.substring(15);
            } else {
                part1 = line.substring(0, 15);
                part2 = line.substring(15, 31);
                part3 = line.substring(31);
            }

            team.setPrefix(part1); //prefix
            team.addEntry(part2); //middle part
            objective.getScore(part2).setScore(totalLines); //middle part
            team.setSuffix(part3); //suffix
            totalLines--;
        }

        player.setScoreboard(scoreboard);
    }
}
