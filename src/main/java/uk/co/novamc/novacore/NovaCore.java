package uk.co.novamc.novacore;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import uk.co.novamc.novacore.commands.MainCommand;
import uk.co.novamc.novacore.events.JoinEvent;
import uk.co.novamc.novacore.events.QuitEvent;
import uk.co.novamc.novacore.files.JoinLeaveFile;

import java.util.logging.Logger;

public final class NovaCore extends JavaPlugin {

    private String chatColour(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public Logger logger;
    public JoinLeaveFile joinLeaveFile;

    @Override
    public void onEnable() {
        logger = getLogger();

        saveDefaultConfig();

        //join leave config file
        joinLeaveFile = JoinLeaveFile.getInstance();
        joinLeaveFile.setup();
        joinLeaveFile.get().options().copyDefaults(true);
        joinLeaveFile.save();

        //commands
        getCommand("nova").setExecutor(new MainCommand(this));

        //events
        getServer().getPluginManager().registerEvents(new JoinEvent(this), this);
        getServer().getPluginManager().registerEvents(new QuitEvent(this), this);

        logger.info(chatColour("Core has been enabled!"));
    }

    @Override
    public void onDisable() {
        logger.info(chatColour("Core has been disabled!"));
    }
}
