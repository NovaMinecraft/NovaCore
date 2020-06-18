package uk.co.novamc.novacore.files;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class JoinLeaveFile {
    static JoinLeaveFile inst = new JoinLeaveFile();

    public static JoinLeaveFile getInstance() {
        return inst;
    }

    private File file;
    private FileConfiguration dataFile;

    Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("NovaCore");

    public void setup() {
        file = new File(plugin.getDataFolder(), "JoinLeave.yml");

        //check exists
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    dataFile = YamlConfiguration.loadConfiguration(file);
                    dataFile.set("join_message", "&c&l[!&c&l] &r%luckperms_prefix%&7%player_name% is &2online");
                    dataFile.set("leave_message", "&c&l[!&c&l] &r%luckperms_prefix%&7%player_name% &7is &coffline");
                    dataFile.set("first_join_message", "&c&l[!&c&l] &7New player &2%player_name% &7has joined! &7(#&b&l{player_number}&7)");
                    dataFile.set("total_players", 0);
                    dataFile.save(file);
                } else {
                    System.out.println("[ERROR] JoinLeave File already exists!");
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