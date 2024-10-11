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
    private static ArrayList<String> knownPlayersString = new ArrayList<>(); // just for testing puposes

    public void addPlayerInConfig(UUID playerUUID) {
        Main main = new Main();
        main.getPlugin().getConfig().set(playerUUID + ".Introduction", false);
        main.getPlugin().getConfig().set(playerUUID + ".UsesPlugin", true);
        main.getPlugin().getConfig().set(playerUUID + ".Difficulty", 0);
        main.getPlugin().getConfig().set("KnownPlayers", knownPlayersString);
        main.getPlugin().saveConfig();
    }

    private void fillKnownPlayersArrayList() {
        Main main = new Main();

        for (String playerUUDIInString : main.getPlugin().getConfig().getStringList("KnownPlayers")) {
            knownPlayers.add(UUID.fromString(playerUUDIInString));
            knownPlayersString.add(playerUUDIInString);
        }

        if(!knownPlayers.isEmpty()) {
            main.getPlugin().getLogger().info("knownPlayers is not empty");
        }
        else {
            main.getPlugin().getLogger().info("knownPlayers is empty");
        }
        for (UUID playerUUID : knownPlayers) {
            main.getPlugin().getLogger().info(playerUUID.toString());
        }

        if(!knownPlayersString.isEmpty()) {
            main.getPlugin().getLogger().info("knownPlayersString is not empty");
        }
        else {
            main.getPlugin().getLogger().info("knownPlayersString is empty");
        }
        for (String playerUUIDString : knownPlayersString) {
            main.getPlugin().getLogger().info(playerUUIDString);
        }
    }

    public void addPlayerInHashMap(UUID playerUUID) {
        Main main = new Main();

        if(main.getPlugin().getConfig().getBoolean(playerUUID + ".Introduction")) {
            configIntroduction.add(playerUUID);
        }
        if(main.getPlugin().getConfig().getBoolean(playerUUID + ".UsesPlugin")) {
            configUsesPlugin.add(playerUUID);
        }
        configDifficulty.put(playerUUID, main.getPlugin().getConfig().getInt(playerUUID + ".Difficulty"));
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
        Main main = new Main();

        if(main.getPlugin().getConfig().contains(player_8.getUniqueId().toString())) {
            return true;
        }
        else{
            return false;
        }
    }

    public void cloneConfigToHasMap() {
        Main main = new Main();

        fillKnownPlayersArrayList();

        for(UUID playerUUID : knownPlayers) {
            if(main.getPlugin().getConfig().contains(playerUUID.toString())) {
                addPlayerInHashMap(playerUUID);
            }
        }
    }

    public void setIntroduction(Player player_9, boolean introduction) {
        Main main = new Main();
        if(introduction) {
            if(!configIntroduction.contains(player_9.getUniqueId())) {
                configIntroduction.add(player_9.getUniqueId());
            }
        }
        else {
            configIntroduction.remove(player_9.getUniqueId());
        }
        main.getPlugin().getConfig().set(player_9.getUniqueId() + ".Introduction", introduction);
        main.getPlugin().saveConfig();
    }

    public void setUsesPlugin(Player player_10, boolean usesPlugin) {
        Main main = new Main();
        if(usesPlugin) {
            if(!configUsesPlugin.contains(player_10.getUniqueId())) {
                configUsesPlugin.add(player_10.getUniqueId());
            }
        }
        else {
            configUsesPlugin.remove(player_10.getUniqueId());
        }
        main.getPlugin().getConfig().set(player_10.getUniqueId() + ".UsesPlugin", usesPlugin);
        main.getPlugin().saveConfig();
    }

    public void setDifficulty(Player player_11, int difficulty) {
        Main main = new Main();
        configDifficulty.replace(player_11.getUniqueId(), difficulty);
        main.getPlugin().getConfig().set(player_11.getUniqueId() + ".Difficulty", difficulty);
        main.getPlugin().saveConfig();
    }

    public boolean checkConfigBoolean(Player player_12, String topic) {
        try {
            switch (topic) {
                case "Introduction":
                    if (configIntroduction.contains(player_12.getUniqueId())) {
                        return true;
                    }
                    return false;
                case "UsesPlugin":
                    if (configUsesPlugin.contains(player_12.getUniqueId())) {
                        return true;
                    }
                    return false;
            }
        }
        catch(NullPointerException e){
            Main main = new Main();
            main.getPlugin().getLogger().info(e.getMessage());
            main.getPlugin().getLogger().info("cant check config boolean because player_12 is null");
        }
        return false;
    }

    public int checkConfigInt(Player player, String topic) {
        switch(topic) {
            case "Difficulty":
                return configDifficulty.get(player.getUniqueId());
        }
        return 404;
    }

}
