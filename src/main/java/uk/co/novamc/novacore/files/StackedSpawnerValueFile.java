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

public class StackedSpawnerValueFile {
    static StackedSpawnerValueFile inst = new StackedSpawnerValueFile();
    public static StackedSpawnerValueFile getInstance() {
        return inst;
    }

    private File file;
    private FileConfiguration dataFile;

    Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("NovaCore");

    public void setup() {
        file = new File(plugin.getDataFolder(), "StackedSpawnerValues.yml");

        //check exists
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    dataFile = YamlConfiguration.loadConfiguration(file);
                    dataFile.set("ZOMBIE", 20000);
                    dataFile.set("SHEEP", 21000);
                    dataFile.set("CHICKEN", 24000);
                    dataFile.set("PIG", 30000);
                    dataFile.set("COW", 38000);
                    dataFile.set("MUSHROOM_COW", 48000);
                    dataFile.set("SKELETON", 60000);
                    dataFile.set("SPIDER", 74000);
                    dataFile.set("SQUID", 90000);
                    dataFile.set("WITCH", 90000);
                    dataFile.set("MAGMA_CUBE", 110000);
                    dataFile.set("SLIME", 110000);
                    dataFile.set("BLAZE", 130000);
                    dataFile.set("CREEPER", 150000);
                    dataFile.set("ENDERMAN", 175000);
                    dataFile.set("GHAST", 200000);
                    dataFile.set("VILLAGER", 235000);
                    dataFile.set("PIG_ZOMBIE", 265000);
                    dataFile.set("IRON_GOLEM", 300000);
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

    public Long getLong(String path) {
        return dataFile.getLong(path);
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