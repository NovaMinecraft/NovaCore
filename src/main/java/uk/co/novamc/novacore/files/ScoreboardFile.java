package uk.co.novamc.novacore.files;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ScoreboardFile {
    static ScoreboardFile inst = new ScoreboardFile();
    public static ScoreboardFile getInstance() {
        return inst;
    }

    private File file;
    private FileConfiguration dataFile;

    Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("NovaCore");

    public void setup() {
        file = new File(plugin.getDataFolder(), "Scoreboard.yml");

        //check exists
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    dataFile = YamlConfiguration.loadConfiguration(file);
                    //update interval is in ticks
                    dataFile.set("update_interval", 20);
                    dataFile.set("title", "&aSky-Nova &2│ &f%server_online%");
                    ArrayList<String> boardLines = new ArrayList<>();
                    boardLines.add("&7[%localtime_time_HH:mm dd/MM/yyyy%]");
                    boardLines.add("&a┌─ &2Island");
                    boardLines.add("&a│ &7IS Worth: &f$%asbformatted_worth%");
                    boardLines.add("&a│ &7Members: &f%asbformatted_teamsize%/5");
                    boardLines.add("&a├─ &2Personal");
                    boardLines.add("&a│ &7Balance: &f$%vault_eco_balance_formatted%");
                    boardLines.add("&a│ &7Mobcoins: &f%lemonmobcoins_balance_formatted%");
                    boardLines.add("&a│ &7Vote Party: &f%VotingPlugin_VotePartyVotesCurrent%/15");
                    boardLines.add("&a│ &7Chunk: &7X &f%asbformatted_chunkx% &7Z &f%asbformatted_chunkz%");
                    boardLines.add("&a└─");
                    boardLines.add("&aNova &f│ &2Skyblock");
                    dataFile.set("lines", boardLines);
                    dataFile.save(file);
                } else {
                    System.out.println("[ERROR] File already exists!");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            dataFile = YamlConfiguration.loadConfiguration(file);
        }
    }

    public FileConfiguration get() {
        return dataFile;
    }

    public String getString(String path) {
        return ChatColor.translateAlternateColorCodes('&', dataFile.getString(path));
    }

    public int getInt(String path) {
        return dataFile.getInt(path);
    }

    public Boolean getBoolean(String path) {
        return dataFile.getBoolean(path);
    }

    public Object getPath(String path) {
        return dataFile.get(path);
    }

    public List getList(String path) {
        return dataFile.getList(path);
    }

    public List<String> getStringList(String path) {
        return dataFile.getStringList(path);
    }

    public void save() {
        try {
            dataFile.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void set(String path, Object value) {
        try {
            dataFile.set(path, value);
            dataFile.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        dataFile = YamlConfiguration.loadConfiguration(file);
    }
}