package org.example.DeathMinigames.minigames;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.example.DeathMinigames.deathMinigames.Main;

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
        if(getDifficulty(player) == 10) {
            player.sendMessage(Component.text("Du hast schon die maximale Schwierigkeit erreicht. Hast du super gemacht").color(NamedTextColor.RED));
        }
        else {
            setDifficulty(player, getDifficulty(player) + 1);
            Main.getPlugin().saveConfig();
        }
    }

    public static void lowerDifficulty(Player player) {
        setDifficulty(player, getDifficulty(player) - 1);
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

    public static boolean checkIfPlayerCanPay(Player player) {
       return player.getInventory().contains(Material.DIAMOND, 4);
    }

    public static void PlayerPay(Player player) {
        ItemStack diamonds = new ItemStack(Material.DIAMOND, 4);
        player.getInventory().removeItem(diamonds);
    }

    /**
     *
     * @param player
     * @return
     */
    public static boolean checkIfPlayerInFile(Player player) {
        if(Main.getPlugin().getConfig().contains(player.getName() + ".Difficulty")) {
            return true;
        }
        else{
            return false;
        }
    }
}
