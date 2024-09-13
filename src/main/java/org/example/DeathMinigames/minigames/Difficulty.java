package org.example.DeathMinigames.minigames;

import org.bukkit.entity.Player;
import org.example.DeathMinigames.deathMinigames.Main;

import java.util.HashMap;
import java.util.UUID;

public class Difficulty {
    /**
     * get the difficulty of a player in a minigame
     * @return  the difficulty in form of an int
     */
    public static int getDifficulty(Player player) {
        return Main.getPlugin().getConfig().getInt(player.getName() + ".Difficulty");
    }

    /**
     *
     * @param player
     * @param difficulty
     */
    public static void setDifficulty(Player player, int difficulty) {
        Main.getPlugin().getConfig().set(player.getName() + ".Difficulty", difficulty);
        Main.getPlugin().saveConfig();
    }

    public static void higherDifficulty(Player player) {
        setDifficulty(player, getDifficulty(player) + 1);
        Main.getPlugin().saveConfig();
    }
    /**
     *
     * @param player
     */
    public static void addPlayer(Player player) {
        Main.getPlugin().getConfig().set(player.getName() + ".Difficulty", 0);
        Main.getPlugin().saveConfig();
    }

    /**
     *
     * @param player
     * @return
     */
    public static boolean checkIfPlayerInMap(Player player) {
        if(Main.getPlugin().getConfig().contains(player.getName() + ".Difficulty")) {
            return true;
        }
        else{
            return false;
        }
    }
}
