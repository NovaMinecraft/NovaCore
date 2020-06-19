package uk.co.novamc.novacore.files;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PaperCommandsFile {
    static PaperCommandsFile inst = new PaperCommandsFile();
    public static PaperCommandsFile getInstance() {
        return inst;
    }

    private File file;
    private FileConfiguration dataFile;

    Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("NovaCore");

    public void setup() {
        file = new File(plugin.getDataFolder(), "PaperCommands.yml");

        //check exists
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    dataFile = YamlConfiguration.loadConfiguration(file);
                    //update interval is in ticks
                    dataFile.set("pcommands.default.name", "Default Command");
                    dataFile.set("pcommands.default.command", "say Default command");
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
        return dataFile.getString(path);
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

    public ConfigurationSection getConfigurationSection(String path) {
        return dataFile.getConfigurationSection(path);
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