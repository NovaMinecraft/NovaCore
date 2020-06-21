package uk.co.novamc.novacore.files;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class IChestDatabaseFile {
    static IChestDatabaseFile inst = new IChestDatabaseFile();
    public static IChestDatabaseFile getInstance() {
        return inst;
    }

    private File file;
    private FileConfiguration dataFile;

    Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("NovaCore");

    public void setup() {
        file = new File(plugin.getDataFolder(), "iChestDatabase.yml");

        //check exists
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    dataFile = YamlConfiguration.loadConfiguration(file);
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

    public void createChest(String blockLocation, Player player, World world) {
        try {
            dataFile.set("chests." + blockLocation + ".blockLocation", blockLocation);
            dataFile.set("chests." + blockLocation + ".owner", player.getUniqueId().toString());
            dataFile.set("chests." + blockLocation + ".world", world.getName());
            dataFile.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteChest(String blockLocation) {
        try {
            dataFile.set("chests." + blockLocation, null);
            dataFile.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Set<String> getLocations() {
        Set<String> keys = dataFile.getConfigurationSection("chests").getKeys(false);
        if (keys != null) {
            return keys;
        } else {
            return Collections.emptySet();
        }
    }

    public ConfigurationSection getChest(String blockLocation) {
        return dataFile.getConfigurationSection("chests." + blockLocation);
    }

    public HashMap<String, String> getStoredBlocks(String blockLocation) {
        HashMap<String, String> storedBlocks = new HashMap<>();
        ConfigurationSection chestData = dataFile.getConfigurationSection("chests." + blockLocation);
        if (chestData.getString("blockType1") != null) {
            storedBlocks.put(chestData.getString("blockType1"), "blockType1");
        }
        if (chestData.getString("blockType2") != null) {
            storedBlocks.put(chestData.getString("blockType2"), "blockType2");
        }
        if (chestData.getString("blockType3") != null) {
            storedBlocks.put(chestData.getString("blockType3"), "blockType3");
        }
        if (chestData.getString("blockType4") != null) {
            storedBlocks.put(chestData.getString("blockType4"), "blockType4");
        }
        if (chestData.getString("blockType5") != null) {
            storedBlocks.put(chestData.getString("blockType5"), "blockType5");
        }
        return storedBlocks;
    }

    public void updateBlockAmount(String blockLocation, Long newAmount, int index) {
        try {
            dataFile.set("chests." + blockLocation + ".blockAmount" + index, newAmount);
            dataFile.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addMaterial(String blockLocation, Long newAmount, int newIndex, String materialName) {
        try {
            dataFile.set("chests." + blockLocation + ".blockType" + newIndex, materialName);
            dataFile.set("chests." + blockLocation + ".blockAmount" + newIndex, newAmount);
            dataFile.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void purgeDatabase() {
        for (String blockLocation : getLocations()) {
            deleteChest(blockLocation);
        }
    }

    public String getMaterial(String blockLocation, int index) {
        return dataFile.getString("chests." + blockLocation + ".blockType" + index);
    }

    public Long getAmount(String blockLocation, int index) {
        return dataFile.getLong("chests." + blockLocation + ".blockAmount" + index);
    }

    public String getOwner(String blockLocation) {
        return dataFile.getString("chests." + blockLocation + ".owner");
    }

    public String getWorld(String blockLocation) {
        return dataFile.getString("chests." + blockLocation + ".world");
    }

    public void lowerIndex(String blockLocation, int originalIndex) {
        String blockType = getMaterial("chests." + blockLocation, originalIndex);
        Long blockAmount = getAmount("chests." + blockLocation, originalIndex);
        try {
            //shift index
            int newIndex = originalIndex - 1;

            //if the new index is 0 just do nothing (essentially removes index1)
            if (newIndex != 0) {
                dataFile.set("chests." + blockLocation + ".blockType" + newIndex, blockType);
                dataFile.set("chests." + blockLocation + ".blockAmount" + newIndex, blockAmount);
            }

            //remove old data
            dataFile.set("chests." + blockLocation + ".blockType" + originalIndex, null);
            dataFile.set("chests." + blockLocation + ".blockAmount" + originalIndex, null);
            dataFile.save(file);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, Long> getBlocksAndAmount(String blockLocation) {
        HashMap<String, Long> storedBlocks = new HashMap<>();
        ConfigurationSection chestData = dataFile.getConfigurationSection("chests." + blockLocation);
        if (chestData != null) {
            if (chestData.getString("blockType1") != null) {
                storedBlocks.put(chestData.getString("blockType1"), chestData.getLong("blockAmount1"));
            }
            if (chestData.getString("blockType2") != null) {
                storedBlocks.put(chestData.getString("blockType2"), chestData.getLong("blockAmount2"));
            }
            if (chestData.getString("blockType3") != null) {
                storedBlocks.put(chestData.getString("blockType3"), chestData.getLong("blockAmount3"));
            }
            if (chestData.getString("blockType4") != null) {
                storedBlocks.put(chestData.getString("blockType4"), chestData.getLong("blockAmount4"));
            }
            if (chestData.getString("blockType5") != null) {
                storedBlocks.put(chestData.getString("blockType5"), chestData.getLong("blockAmount5"));
            }
        }
        return storedBlocks;
    }

    public void wipeMaterials(String blockLocation) {
        try {
            dataFile.set("chests." + blockLocation + ".blockType1", null);
            dataFile.set("chests." + blockLocation + ".blockAmount1", null);
            dataFile.set("chests." + blockLocation + ".blockType2", null);
            dataFile.set("chests." + blockLocation + ".blockAmount2", null);
            dataFile.set("chests." + blockLocation + ".blockType3", null);
            dataFile.set("chests." + blockLocation + ".blockAmount3", null);
            dataFile.set("chests." + blockLocation + ".blockType4", null);
            dataFile.set("chests." + blockLocation + ".blockAmount4", null);
            dataFile.set("chests." + blockLocation + ".blockType5", null);
            dataFile.set("chests." + blockLocation + ".blockAmount5", null);
            dataFile.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getRawBlocksStored(String blockLocation) {
        ArrayList<String> rawBlock = new ArrayList<>();
        rawBlock.add(dataFile.getString("chests." + blockLocation + ".blockType1"));
        rawBlock.add(dataFile.getString("chests." + blockLocation + ".blockType2"));
        rawBlock.add(dataFile.getString("chests." + blockLocation + ".blockType3"));
        rawBlock.add(dataFile.getString("chests." + blockLocation + ".blockType4"));
        rawBlock.add(dataFile.getString("chests." + blockLocation + ".blockType5"));
        return rawBlock;
    }

    public ConfigurationSection getTrust() {
        return dataFile.getConfigurationSection("trust");
    }

    public void setTrust(String path, Object value) {
        dataFile.set("trust." + path, value);
    }

    public ConfigurationSection getHolo() {
        return dataFile.getConfigurationSection("holograms");
    }

    public void setHolo(String path, Object value) {
        dataFile.set("holograms." + path, value);
    }

    public void reload() {
        dataFile = YamlConfiguration.loadConfiguration(file);
    }
}