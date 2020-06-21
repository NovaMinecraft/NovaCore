package uk.co.novamc.novacore;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class Placeholders extends PlaceholderExpansion {

    private NovaCore plugin;
    public Placeholders(NovaCore plugin) {
        this.plugin = plugin;
    }

    //do not unregister placeholder
    @Override
    public boolean persist() {
        return true;
    }

    //internal so not needed
    @Override
    public boolean canRegister() {
        return true;
    }

    //author from plugin.yml
    @Override
    public String getAuthor(){
        return plugin.getDescription().getAuthors().toString();
    }

    //identifier name
    @Override
    public String getIdentifier(){
        return "nova";
    }

    //version from plugin.yml
    @Override
    public String getVersion(){
        return plugin.getDescription().getVersion();
    }

    //value to use
    @Override
    public String onPlaceholderRequest(Player player, String identifier){

        if(player == null){
            return "";
        }

        // %nova_is_worth%
        if(identifier.equals("is_worth")){
            Long defaultWorth = Long.parseLong(PlaceholderAPI.setPlaceholders(player, "%askyblock_level%"));
            return withSuffix(defaultWorth);
        }

        // %nova_is_teamsize%
        if(identifier.equals("is_teamsize")){
            String teamSize = PlaceholderAPI.setPlaceholders(player, "%askyblock_team_size%");
            if (Integer.parseInt(teamSize) == 0) {
                return "1";
            } else {
                return teamSize;
            }
        }

        // %nova_chunkx%
        if(identifier.equals("chunkx")){
            return Integer.toString(player.getLocation().getChunk().getX());
        }

        // %nova_chunkz%
        if(identifier.equals("chunkz")){
            return Integer.toString(player.getLocation().getChunk().getZ());
        }

        //return null if an invalid placeholder
        return null;
    }

    public static String withSuffix(Long count) {
        if (count < 1000) return "" + count;
        double exp = Math.log10(count) / 3;
        return String.format("%.1f%c", count / Math.pow(1000, exp), "kMBTPE".charAt((int) (exp-1)));
    }
}
