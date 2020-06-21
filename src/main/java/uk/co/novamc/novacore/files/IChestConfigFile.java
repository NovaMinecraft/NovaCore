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

public class IChestConfigFile {
    static IChestConfigFile inst = new IChestConfigFile();
    public static IChestConfigFile getInstance() {
        return inst;
    }

    private File file;
    private FileConfiguration dataFile;

    Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("NovaCore");

    public void setup() {
        file = new File(plugin.getDataFolder(), "iChestConfig.yml");

        //check exists
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    dataFile = YamlConfiguration.loadConfiguration(file);
                    dataFile.set("iChest.updatespeed", 300);
                    dataFile.set("iChest.displayname", "&c[!] &9&liChest");
                    ArrayList<String> iChestLore = new ArrayList<>();
                    iChestLore.add("&7(Place to use)");
                    dataFile.set("iChest.lore", iChestLore);
                    dataFile.set("iChest.gui.title", "&9&liCHEST");
                    dataFile.set("iChest.gui.panecolour", 15);
                    dataFile.set("iChest.gui.exittext", "&8» &9Exit");
                    dataFile.set("iChest.gui.sellalltext", "&8» &9Sell All");
                    dataFile.set("iChest.gui.items.itemname", "&f*&9*&1* &9&l{material} &1*&9*&f*");
                    ArrayList<String> itemLore = new ArrayList<>();
                    itemLore.add("&1&l» &9&lQuantity: &fx{amount}");
                    itemLore.add("&7&o[ Left-Click for &b&oStack &7&o]");
                    itemLore.add("&7&o[ Right-Click for &d&oAll &7&o]");
                    dataFile.set("iChest.gui.items.itemlore", itemLore);
                    dataFile.set("Messages.sellallnoperm", "&8[&aNova&8] &7Buy this perk from the store!");
                    dataFile.set("Logging.absorbitems", false);
                    dataFile.set("UseMobDrops", true);
                    dataFile.set("BlockValues.LEAVES", 1.0);
                    dataFile.set("BlockValues.NETHERRACK", 1.0);
                    dataFile.set("BlockValues.SAND", 1.0);
                    dataFile.set("BlockValues.ICE", 2.0);
                    dataFile.set("BlockValues.DIRT", 1.0);
                    dataFile.set("BlockValues.COBBLESTONE", 2.0);
                    dataFile.set("BlockValues.MOSSY_COBBLESTONE", 2.0);
                    dataFile.set("BlockValues.STONE", 2.0);
                    dataFile.set("BlockValues.PRISMARINE", 4.0);
                    dataFile.set("BlockValues.RED_SANDSTONE", 4.0);
                    dataFile.set("BlockValues.SANDSTONE", 4.0);
                    dataFile.set("BlockValues.SNOWBLOCK", 3.0);
                    dataFile.set("BlockValues.GRAVEL", 5.0);
                    dataFile.set("BlockValues.SPONGE", 5.0);
                    dataFile.set("BlockValues.MYCELIUM", 4.0);
                    dataFile.set("BlockValues.SOULSAND", 4.0);
                    dataFile.set("BlockValues.JACK_O_LANTERN", 4.0);
                    dataFile.set("BlockValues.ENDSTONE", 6.0);
                    dataFile.set("BlockValues.QUARTZ_BLOCK", 4.0);
                    dataFile.set("BlockValues.SEALANTERN", 8.0);
                    dataFile.set("BlockValues.LOG", 3.0);
                    dataFile.set("BlockValues.STONEBRICKS", 8.0);
                    dataFile.set("BlockValues.PACKED_ICE", 18.0);
                    dataFile.set("BlockValues.GLOWSTONE", 22.0);
                    dataFile.set("BlockValues.HAYBLOCK", 27.0);
                    dataFile.set("BlockValues.OBSIDIAN", 250.0);
                    dataFile.set("BlockValues.GLASSPANE", 0.7);
                    dataFile.set("BlockValues.STAINED_GLASS_PANE", 0.7);
                    dataFile.set("BlockValues.CARPET", 1.0);
                    dataFile.set("BlockValues.GLASS", 2.0);
                    dataFile.set("BlockValues.STAINED_GLASS", 2.0);
                    dataFile.set("BlockValues.WOOL", 2.0);
                    dataFile.set("BlockValues.MILK_BUCKET", 0.1);
                    dataFile.set("BlockValues.EGG", 0.5);
                    dataFile.set("BlockValues.MELON", 0.6);
                    dataFile.set("BlockValues.MELON_SEEDS", 0.6);
                    dataFile.set("BlockValues.APPLE", 1.0);
                    dataFile.set("BlockValues.CHEST", 1.0);
                    dataFile.set("BlockValues.COOKED_RABBIT", 1.0);
                    dataFile.set("BlockValues.COOKIE", 1.0);
                    dataFile.set("BlockValues.FEATHER", 1.0);
                    dataFile.set("BlockValues.FLINT", 1.0);
                    dataFile.set("BlockValues.POISONOUS_POTATO", 1.0);
                    dataFile.set("MobDrops.BLAZE.item", "BLAZE_ROD");
                    dataFile.set("MobDrops.BLAZE.min", 0);
                    dataFile.set("MobDrops.BLAZE.max", 1);
                    dataFile.set("MobDrops.CAVE_SPIDER.item", "STRING");
                    dataFile.set("MobDrops.CAVE_SPIDER.min", 0);
                    dataFile.set("MobDrops.CAVE_SPIDER.max", 2);
                    dataFile.set("MobDrops.CHICKEN.item", "RAW_CHICKEN");
                    dataFile.set("MobDrops.CHICKEN.min", 1);
                    dataFile.set("MobDrops.CHICKEN.max", 1);
                    dataFile.set("MobDrops.COW.item", "RAW_BEEF");
                    dataFile.set("MobDrops.COW.min", 1);
                    dataFile.set("MobDrops.COW.max", 4);
                    dataFile.set("MobDrops.CREEPER.item", "SULPHUR");
                    dataFile.set("MobDrops.CREEPER.min", 0);
                    dataFile.set("MobDrops.CREEPER.max", 2);
                    dataFile.set("MobDrops.ENDERMAN.item", "ENDER_PEARL");
                    dataFile.set("MobDrops.ENDERMAN.min", 0);
                    dataFile.set("MobDrops.ENDERMAN.max", 1);
                    dataFile.set("MobDrops.GHAST.item", "GHAST_TEAR");
                    dataFile.set("MobDrops.GHAST.min", 0);
                    dataFile.set("MobDrops.GHAST.max", 1);
                    dataFile.set("MobDrops.GUARDIAN.item", "PRISMARINE_SHARD");
                    dataFile.set("MobDrops.GUARDIAN.min", 0);
                    dataFile.set("MobDrops.GUARDIAN.max", 2);
                    dataFile.set("MobDrops.HORSE.item", "LEATHER");
                    dataFile.set("MobDrops.HORSE.min", 0);
                    dataFile.set("MobDrops.HORSE.max", 2);
                    dataFile.set("MobDrops.IRON_GOLEM.item", "IRON_INGOT");
                    dataFile.set("MobDrops.IRON_GOLEM.min", 3);
                    dataFile.set("MobDrops.IRON_GOLEM.max", 5);
                    dataFile.set("MobDrops.MAGMA_CUBE.item", "MAGMA_CREAM");
                    dataFile.set("MobDrops.MAGMA_CUBE.min", 1);
                    dataFile.set("MobDrops.MAGMA_CUBE.max", 1);
                    dataFile.set("MobDrops.MUSHROOM_COW.item", "RAW_BEEF");
                    dataFile.set("MobDrops.MUSHROOM_COW.min", 1);
                    dataFile.set("MobDrops.MUSHROOM_COW.max", 4);
                    dataFile.set("MobDrops.PIG.item", "PORK");
                    dataFile.set("MobDrops.PIG.min", 1);
                    dataFile.set("MobDrops.PIG.max", 3);
                    dataFile.set("MobDrops.PIG_ZOMBIE.item", "GOLD_NUGGET");
                    dataFile.set("MobDrops.PIG_ZOMBIE.min", 0);
                    dataFile.set("MobDrops.PIG_ZOMBIE.max", 2);
                    dataFile.set("MobDrops.RABBIT.item", "RABBIT");

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