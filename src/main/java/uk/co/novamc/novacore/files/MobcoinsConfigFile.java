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

public class MobcoinsConfigFile {
    static MobcoinsConfigFile inst = new MobcoinsConfigFile();
    public static MobcoinsConfigFile getInstance() {
        return inst;
    }

    private File file;
    private FileConfiguration dataFile;

    Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("NovaCore");

    public void setup() {
        file = new File(plugin.getDataFolder(), "Mobcoins.yml");

        //check exists
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    dataFile = YamlConfiguration.loadConfiguration(file);
                    //update interval is in ticks
                    dataFile.set("receive_message", "&6[&f+&6] &f&l{amount} &6Mobcoins");

                    dataFile.set("mobs.ZOMBIE.chance", 5);
                    dataFile.set("mobs.ZOMBIE.min_amount", 1);
                    dataFile.set("mobs.ZOMBIE.max_amount", 3);

                    dataFile.set("mobs.SHEEP.chance", 10);
                    dataFile.set("mobs.SHEEP.min_amount", 1);
                    dataFile.set("mobs.SHEEP.max_amount", 2);

                    dataFile.set("mobs.CHICKEN.chance", 5);
                    dataFile.set("mobs.CHICKEN.min_amount", 1);
                    dataFile.set("mobs.CHICKEN.max_amount", 2);

                    dataFile.set("mobs.PIG.chance", 5);
                    dataFile.set("mobs.PIG.min_amount", 1);
                    dataFile.set("mobs.PIG.max_amount", 4);

                    dataFile.set("mobs.COW.chance", 10);
                    dataFile.set("mobs.COW.min_amount", 1);
                    dataFile.set("mobs.COW.max_amount", 3);

                    dataFile.set("mobs.MUSHROOM_COW.chance", 5);
                    dataFile.set("mobs.MUSHROOM_COW.min_amount", 1);
                    dataFile.set("mobs.MUSHROOM_COW.max_amount", 2);

                    dataFile.set("mobs.SKELETON.chance", 15);
                    dataFile.set("mobs.SKELETON.min_amount", 1);
                    dataFile.set("mobs.SKELETON.max_amount", 2);

                    dataFile.set("mobs.SPIDER.chance", 10);
                    dataFile.set("mobs.SPIDER.min_amount", 1);
                    dataFile.set("mobs.SPIDER.max_amount", 4);

                    dataFile.set("mobs.SQUID.chance", 20);
                    dataFile.set("mobs.SQUID.min_amount", 1);
                    dataFile.set("mobs.SQUID.max_amount", 3);

                    dataFile.set("mobs.WITCH.chance", 15);
                    dataFile.set("mobs.WITCH.min_amount", 1);
                    dataFile.set("mobs.WITCH.max_amount", 5);

                    dataFile.set("mobs.MAGMA_CUBE.chance", 20);
                    dataFile.set("mobs.MAGMA_CUBE.min_amount", 1);
                    dataFile.set("mobs.MAGMA_CUBE.max_amount", 6);

                    dataFile.set("mobs.SLIME.chance", 20);
                    dataFile.set("mobs.SLIME.min_amount", 1);
                    dataFile.set("mobs.SLIME.max_amount", 6);

                    dataFile.set("mobs.BLAZE.chance", 10);
                    dataFile.set("mobs.BLAZE.min_amount", 1);
                    dataFile.set("mobs.BLAZE.max_amount", 2);

                    dataFile.set("mobs.CREEPER.chance", 15);
                    dataFile.set("mobs.CREEPER.min_amount", 1);
                    dataFile.set("mobs.CREEPER.max_amount", 4);

                    dataFile.set("mobs.GHAST.chance", 25);
                    dataFile.set("mobs.GHAST.min_amount", 1);
                    dataFile.set("mobs.GHAST.max_amount", 8);

                    dataFile.set("mobs.VILLAGER.chance", 5);
                    dataFile.set("mobs.VILLAGER.min_amount", 2);
                    dataFile.set("mobs.VILLAGER.max_amount", 6);

                    dataFile.set("mobs.PIG_ZOMBIE.chance", 5);
                    dataFile.set("mobs.PIG_ZOMBIE.min_amount", 3);
                    dataFile.set("mobs.PIG_ZOMBIE.max_amount", 6);

                    dataFile.set("mobs.IRON_GOLEM.chance", 2);
                    dataFile.set("mobs.IRON_GOLEM.min_amount", 5);
                    dataFile.set("mobs.IRON_GOLEM.max_amount", 10);

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