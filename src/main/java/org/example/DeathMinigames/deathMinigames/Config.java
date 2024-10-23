package org.example.DeathMinigames.deathMinigames;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Config {
    public static HashMap<UUID, Integer> configDifficulty = new HashMap<>();
    public static ArrayList<UUID> configIntroduction = new ArrayList<>();
    public static ArrayList<UUID> configUsesPlugin = new ArrayList<>();
    public static ArrayList<UUID> knownPlayers = new ArrayList<>();
    private static ArrayList<String> knownPlayersString = new ArrayList<>();
    public static boolean configSetUp;
    public static int configParkourStartHeight;
    public static int configParkourLength;
    public static int configCostToLowerTheDifficulty;
    public static int configTimeToDecideWhenRespawning;


    public void addPlayerInConfig(UUID playerUUID) {
        Main.getPlugin().getConfig().set(playerUUID + ".Introduction", false);
        Main.getPlugin().getConfig().set(playerUUID + ".UsesPlugin", true);
        Main.getPlugin().getConfig().set(playerUUID + ".Difficulty", 0);
        Main.getPlugin().getConfig().set("KnownPlayers", knownPlayersString);
        Main.getPlugin().saveConfig();
    }

    private void fillKnownPlayersArrayList() {
        

        for (String playerUUDIInString : Main.getPlugin().getConfig().getStringList("KnownPlayers")) {
            knownPlayers.add(UUID.fromString(playerUUDIInString));
            knownPlayersString.add(playerUUDIInString);
        }

        if(!knownPlayers.isEmpty()) {
            Main.getPlugin().getLogger().info("knownPlayers is not empty");
        }
        else {
            Main.getPlugin().getLogger().info("knownPlayers is empty");
        }
        for (UUID playerUUID : knownPlayers) {
            Main.getPlugin().getLogger().info(playerUUID.toString());
        }

        if(!knownPlayersString.isEmpty()) {
            Main.getPlugin().getLogger().info("knownPlayersString is not empty");
        }
        else {
            Main.getPlugin().getLogger().info("knownPlayersString is empty");
        }
        for (String playerUUIDString : knownPlayersString) {
            Main.getPlugin().getLogger().info(playerUUIDString);
        }
    }

    public void addPlayerInHashMap(UUID playerUUID) {
        

        if(Main.getPlugin().getConfig().getBoolean(playerUUID + ".Introduction")) {
            configIntroduction.add(playerUUID);
        }
        if(Main.getPlugin().getConfig().getBoolean(playerUUID + ".UsesPlugin")) {
            configUsesPlugin.add(playerUUID);
        }
        configDifficulty.put(playerUUID, Main.getPlugin().getConfig().getInt(playerUUID + ".Difficulty"));
    }

    public void addNewPlayer(UUID playerUUID) {
        configIntroduction.add(playerUUID);
        configUsesPlugin.add(playerUUID);
        configDifficulty.put(playerUUID, 0);
        knownPlayers.add(playerUUID);
        knownPlayersString.add(playerUUID.toString());

        addPlayerInConfig(playerUUID);
    }

    public boolean checkIfPlayerInFile(Player player_8) {
        

        if(Main.getPlugin().getConfig().contains(player_8.getUniqueId().toString())) {
            return true;
        }
        else{
            return false;
        }
    }

    public void cloneConfigToHasMap() {
        

        fillKnownPlayersArrayList();
        if(Main.getPlugin().getConfig().contains("SetUp")) {
            setSetUp(Main.getPlugin().getConfig().getBoolean("SetUp"));
        }
        else {
            setSetUp(false);
        }
        if(Main.getPlugin().getConfig().contains("ParkourStartHeight")) {
            setParkourStartHeight(Main.getPlugin().getConfig().getInt("ParkourStartHeight"));
        }
        else {
            setParkourStartHeight(100);
        }
        if(Main.getPlugin().getConfig().contains("ParkourLength")) {
            setParkourLength(Main.getPlugin().getConfig().getInt("ParkourLength"));
        }
        else {
            setParkourLength(10);
        }
        if(Main.getPlugin().getConfig().contains("CostToLowerTheDifficulty")) {
            setCostToLowerTheDifficulty(Main.getPlugin().getConfig().getInt("CostToLowerTheDifficulty"));
        }
        else {
            setCostToLowerTheDifficulty(6);
        }
        if(Main.getPlugin().getConfig().contains("TimeToDecideWhenRespawning")) {
            setTimeToDecideWhenRespawning(Main.getPlugin().getConfig().getInt("TimeToDecideWhenRespawning"));
        }
        else {
            setTimeToDecideWhenRespawning(10);
        }

        for(UUID playerUUID : knownPlayers) {
            if(Main.getPlugin().getConfig().contains(playerUUID.toString())) {
                addPlayerInHashMap(playerUUID);
            }
        }
    }

    public void setIntroduction(Player player_9, boolean introduction) {
        
        if(introduction) {
            if(!configIntroduction.contains(player_9.getUniqueId())) {
                configIntroduction.add(player_9.getUniqueId());
            }
        }
        else {
            configIntroduction.remove(player_9.getUniqueId());
        }
        Main.getPlugin().getConfig().set(player_9.getUniqueId() + ".Introduction", introduction);
        Main.getPlugin().saveConfig();
    }

    public void setUsesPlugin(Player player_10, boolean usesPlugin) {
        
        if(usesPlugin) {
            if(!configUsesPlugin.contains(player_10.getUniqueId())) {
                configUsesPlugin.add(player_10.getUniqueId());
            }
        }
        else {
            configUsesPlugin.remove(player_10.getUniqueId());
        }
        Main.getPlugin().getConfig().set(player_10.getUniqueId() + ".UsesPlugin", usesPlugin);
        Main.getPlugin().saveConfig();
    }

    public void setDifficulty(Player player_11, int difficulty) {
        
        configDifficulty.replace(player_11.getUniqueId(), difficulty);
        Main.getPlugin().getConfig().set(player_11.getUniqueId() + ".Difficulty", difficulty);
        Main.getPlugin().saveConfig();
    }

    public void setSetUp(boolean bool) {
        
        configSetUp = bool;
        Main.getPlugin().getConfig().set("SetUp", bool);
        Main.getPlugin().saveConfig();
    }

    public void setParkourStartHeight(int height) {
        
        configParkourStartHeight = height;
        Main.getPlugin().getConfig().set("ParkourStartHeight", height);
        Main.getPlugin().saveConfig();
    }

    public void setParkourLength(int length) {
        
        configParkourLength = length;
        Main.getPlugin().getConfig().set("ParkourLength", length);
        Main.getPlugin().saveConfig();
    }

    public void setCostToLowerTheDifficulty(int cost) {
        
        configCostToLowerTheDifficulty = cost;
        Main.getPlugin().getConfig().set("CostToLowerTheDifficulty", cost);
        Main.getPlugin().saveConfig();
    }

    public void setTimeToDecideWhenRespawning(int time) {
        
        configTimeToDecideWhenRespawning = time;
        Main.getPlugin().getConfig().set("TimeToDecideWhenRespawning", time);
        Main.getPlugin().saveConfig();
    }

    public boolean checkConfigBoolean(Player player, String topic) {
        try {
            switch (topic) {
                case "Introduction":
                    if (configIntroduction.contains(player.getUniqueId())) {
                        return true;
                    }
                    return false;
                case "UsesPlugin":
                    if (configUsesPlugin.contains(player.getUniqueId())) {
                        return true;
                    }
                    return false;
            }
        }
        catch(NullPointerException e){
            
            Main.getPlugin().getLogger().info(e.getMessage());
            Main.getPlugin().getLogger().info("cant check config boolean because player_12 is null");
        }
        return false;
    }

    public boolean checkConfigBoolean(String topic) {
        try {
            if (topic.equals("SetUp")) {
                return configSetUp;
            }
        }
        catch(NullPointerException e){
            
            Main.getPlugin().getLogger().info(e.getMessage());
            Main.getPlugin().getLogger().info("cant check config boolean because player_12 is null");
        }
        return false;
    }

    public int checkConfigInt(Player player, String topic) {
        if(topic.equals("Difficulty")) {
            return configDifficulty.get(player.getUniqueId());
        }
        return 404;
    }

    public int checkConfigInt(String topic) {
        switch(topic) {
            case "ParkourStartHeight":
                return configParkourStartHeight;
            case "ParkourLength":
                return configParkourLength;
            case "CostToLowerTheDifficulty":
                return configCostToLowerTheDifficulty;
            case "TimeToDecideWhenRespawning":
                return configTimeToDecideWhenRespawning;
        }
        return 404;
    }
}
