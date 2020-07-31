package uk.co.novamc.novacore;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.novamc.novacore.commands.*;
import uk.co.novamc.novacore.events.*;
import uk.co.novamc.novacore.files.*;
import uk.co.novamc.novacore.tasks.IChestAbsorb;
import uk.co.novamc.novacore.tasks.UpdateScoreboard;

public final class NovaCore extends JavaPlugin {

    private String chatColour(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    final Logger logger = LoggerFactory.getLogger(NovaCore.class);
    public JoinLeaveFile joinLeaveFile;
    public ScoreboardFile scoreboardFile;
    public PaperCommandsFile paperCommandsFile;
    public StackedSpawnerValueFile stackValueFile;
    public IChestConfigFile iChestConfig;
    public IChestDatabaseFile iChestDatabase;
    public MobcoinsConfigFile mobcoinConfig;
    public Economy econ = null;

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

        //stacked spawner value config file
        stackValueFile = StackedSpawnerValueFile.getInstance();
        stackValueFile.setup();
        stackValueFile.get().options().copyDefaults(true);
        stackValueFile.save();

        //iChest config file
        iChestDatabase = IChestDatabaseFile.getInstance();
        iChestDatabase.setup();
        iChestDatabase.get().options().copyDefaults(true);
        iChestDatabase.save();

        //iChest database file
        iChestConfig = IChestConfigFile.getInstance();
        iChestConfig.setup();
        iChestConfig.get().options().copyDefaults(true);
        iChestConfig.save();

        //mobcoin config file
        mobcoinConfig = MobcoinsConfigFile.getInstance();
        mobcoinConfig.setup();
        mobcoinConfig.get().options().copyDefaults(true);
        mobcoinConfig.save();

        //commands
        getCommand("nova").setExecutor(new MainCommand(this));
        getCommand("pcommand").setExecutor(new PaperCommand(this));
        getCommand("plugins").setExecutor(new Plugins());
        getCommand("spawner").setExecutor(new SpawnerBuy(this));
        getCommand("ichest").setExecutor(new IChestCommands(this));

        //events
        getServer().getPluginManager().registerEvents(new PlayerJoin(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuit(this), this);
        getServer().getPluginManager().registerEvents(new PrepareItemCraft(this), this);
        getServer().getPluginManager().registerEvents(new FoodLevelChange(this), this);
        getServer().getPluginManager().registerEvents(new ProjectileLaunch(this), this);
        getServer().getPluginManager().registerEvents(new PlayerInteract(this), this);
        getServer().getPluginManager().registerEvents(new BlockPlace(this), this);
        getServer().getPluginManager().registerEvents(new AsyncPlayerChat(this), this);
        getServer().getPluginManager().registerEvents(new EntityCombust(this), this);
        getServer().getPluginManager().registerEvents(new PlayerMove(this), this);
        getServer().getPluginManager().registerEvents(new BlockBreak(this), this);
        getServer().getPluginManager().registerEvents(new EntityDeath(this), this);
        getServer().getPluginManager().registerEvents(new InventoryClick(this), this);

        //tasks
        new UpdateScoreboard(this).runTaskTimer(this, 600, scoreboardFile.getInt("update_interval"));
        new IChestAbsorb(this).runTaskTimer(this, 600, iChestConfig.getInt("iChest.updatespeed"));

        //vaultAPI
        if (setupEconomy()) {
            logger.info("Found Vault, hooking in for economy");
        }

        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new Placeholders(this).register();
        }

        logger.info(chatColour("Core has been enabled!"));
    }

    @Override
    public void onDisable() {
        logger.info(chatColour("Core has been disabled!"));
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
}
