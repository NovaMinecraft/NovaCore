package uk.co.novamc.novacore;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.novamc.novacore.commands.MainCommand;
import uk.co.novamc.novacore.commands.PaperCommand;
import uk.co.novamc.novacore.events.*;
import uk.co.novamc.novacore.files.JoinLeaveFile;
import uk.co.novamc.novacore.files.PaperCommandsFile;
import uk.co.novamc.novacore.files.ScoreboardFile;
import uk.co.novamc.novacore.tasks.UpdateScoreboard;

public final class NovaCore extends JavaPlugin {

    private String chatColour(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    final Logger logger = LoggerFactory.getLogger(NovaCore.class);
    public JoinLeaveFile joinLeaveFile;
    public ScoreboardFile scoreboardFile;
    public PaperCommandsFile paperCommandsFile;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        //join leave config file
        joinLeaveFile = JoinLeaveFile.getInstance();
        joinLeaveFile.setup();
        joinLeaveFile.get().options().copyDefaults(true);
        joinLeaveFile.save();

        //scoreboard config file
        scoreboardFile = ScoreboardFile.getInstance();
        scoreboardFile.setup();
        scoreboardFile.get().options().copyDefaults(true);
        scoreboardFile.save();

        //paper commands config file
        paperCommandsFile = PaperCommandsFile.getInstance();
        paperCommandsFile.setup();
        paperCommandsFile.get().options().copyDefaults(true);
        paperCommandsFile.save();

        //commands
        getCommand("nova").setExecutor(new MainCommand(this));
        getCommand("pcommand").setExecutor(new PaperCommand(this));

        //events
        getServer().getPluginManager().registerEvents(new PlayerJoin(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuit(this), this);
        getServer().getPluginManager().registerEvents(new PrepareItemCraft(this), this);
        getServer().getPluginManager().registerEvents(new FoodLevelChange(this), this);
        getServer().getPluginManager().registerEvents(new ProjectileLaunch(this), this);
        getServer().getPluginManager().registerEvents(new PlayerInteract(), this);
        getServer().getPluginManager().registerEvents(new BlockPlace(this), this);


        //tasks
        new UpdateScoreboard(this).runTaskTimer(this, 100, scoreboardFile.getInt("update_interval"));

        logger.info(chatColour("Core has been enabled!"));
    }

    @Override
    public void onDisable() {
        logger.info(chatColour("Core has been disabled!"));
    }
}
