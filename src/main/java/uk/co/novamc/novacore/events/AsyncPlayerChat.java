package uk.co.novamc.novacore.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import uk.co.novamc.novacore.NovaCore;

public class AsyncPlayerChat implements Listener {

    private NovaCore plugin;
    public AsyncPlayerChat(NovaCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMessage(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        if (player.hasPermission("staffchat.access")) {
            String message = e.getMessage();
            if (message.startsWith("@")) {
                e.setCancelled(true);
                String messageContent = message.substring(1);
                String formattedMessage = ChatColor.translateAlternateColorCodes('&', "&5&l[SC] " + player.getName() + " &f» &9" + messageContent);
                for (Player onlinePlayer : Bukkit.getServer().getOnlinePlayers()) {
                    if (onlinePlayer.hasPermission("staffchat.access")) {
                        onlinePlayer.sendMessage(formattedMessage);
                    }
                }
            }
        }
    }

}
