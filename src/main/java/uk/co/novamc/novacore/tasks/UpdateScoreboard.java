package uk.co.novamc.novacore.tasks;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;
import uk.co.novamc.novacore.NovaCore;

import java.util.List;

public class UpdateScoreboard extends BukkitRunnable {

    private NovaCore plugin;
    public UpdateScoreboard(NovaCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
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
}
