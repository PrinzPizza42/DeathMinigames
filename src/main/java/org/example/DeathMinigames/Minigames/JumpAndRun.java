package org.example.DeathMinigames.Minigames;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.example.DeathMinigames.deathMinigames.Main;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

import static org.bukkit.Bukkit.getServer;
import static org.bukkit.plugin.java.JavaPlugin.getPlugin;
import static org.example.DeathMinigames.Listeners.DeathListener.*;

public class JumpAndRun {
    /**
     * runs the minigame JumpAndRun
     */
    public static void start() {
        playerInArena = waitingListMinigame.getFirst();
        World w = playerInArena.getWorld();
        Location location = new Location(playerInArena.getWorld(), 93d, 75d, 81d);
        playerInArena.teleport(location);
        Minigame.startMessage(playerInArena, "Du musst diesen Parkour bestehen, um deine Items wieder zu bekommen");

        new BukkitRunnable() {
            public void run() {
                checkIfPlayerLost(playerInArena, 74);
                checkIfPlayerWon(playerInArena, 81);
            }
        }.runTaskTimerAsynchronously(getPlugin(Main.class), 0, 20);


        // the location to place the first block on
        int x = 93;
        int y = 74;
        int z = 81;
        Location nextBlock = new Location(location.getWorld(), x, y, z);
        nextBlock.getBlock().setType(Material.GREEN_CONCRETE);

        int _x = 0;
        int _y = playerInArena.getLocation().getBlockY();
        int _z = 0;
        Bukkit.broadcastMessage(playerInArena.getName());

        // check if the player is standing on green concrete, if true place the next block
        Bukkit.broadcastMessage("Check if player is on concrete, if true place then next block");
        if (checkIfOnConcrete(playerInArena)) {
            // randomizer for coordinates and prefix
                _x = randomizer(1, 4);
                _z = randomizer(1, 4);
            int randomNum = randomizer(1, 4);
            if(randomNum == 1) {
                // _x & _z negative
                _x = _x * -1;
                _z = _z * -1;
                Bukkit.broadcastMessage("Case 1");
            } else if (randomNum == 2) {
                // _x & _z positive
                Bukkit.broadcastMessage("Case 2");
            }   else if (randomNum == 3) {
                // _x negative & _z positive
                Bukkit.broadcastMessage("Case 3");
                _x = _x * -1;
            } else if (randomNum == 4) {
                // _x positive & _z negative
                Bukkit.broadcastMessage("Case 4");
                _z = _z * -1;
            }
            _x = playerInArena.getLocation().getBlockX() + _x;
            _z = playerInArena.getLocation().getBlockZ() + _z;
            nextBlock.set(_x, _y, _z);
            nextBlock.getBlock().setType(Material.GREEN_WOOL);
            Bukkit.broadcastMessage("Green wool was placed at " + nextBlock.toString());

            replaceWoolWithConcrete(playerInArena);
        }

        Bukkit.broadcastMessage("End");
        // check if the player is 2 blocks under the recently placed block, if true let him fail the minigame

    }

    /**
     * checks if the given player is standing on green concrete
     * @param player    the given player
     * @return          true or false
     */
    private static boolean checkIfOnConcrete(Player player) {
        Location block = player.getLocation();
        block.setY(block.getBlockY() - 1);
        if (block.getBlock().getType() == Material.GREEN_CONCRETE) {
            Bukkit.broadcastMessage("check concrete true");
            return true;
        }
        else {
            return false;
        }
    }


    /**
     * gives back a random number between min and max
     * @param min   the minimum number
     * @param max   the maximum number
     * @return      the number as an int
     */
    private static int randomizer(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    /**
     * checks if the player has reached the height, at which he would have won
     * @param player        the player in question
     * @param heightToWin   the height at which he would have won
     * @return              true if he reaches that height or higher, false if he does not reach that height
     */
    private static boolean checkIfPlayerWon(Player player, int heightToWin) {
        if (player.getLocation().getBlockY() <= heightToWin && checkIfOnWool(player) == true) {
            Minigame.winMessage(player);
            Minigame.spawnChestWithInv(player);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * check if player lost, if true fails him
     * @param player        the player to check
     * @param heightToLose  the height, at which the last block was placed
     * @return              true if he lost, false if he did not lose
     */
    private static boolean checkIfPlayerLost(Player player, int heightToLose) {
        if (player.getLocation().getBlockY() <= heightToLose) {
            Minigame.loseMessage(player);
            Minigame.dropInv(player);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * cycle to check if the player is standing on green wool, if true replace it with green concrete, stops when concrete is placed
     * @param player    player to get the location of the block beneath him
     */
    private static void replaceWoolWithConcrete(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (checkIfOnWool(player) == true) {
                    Location block = player.getLocation();
                    block.setY(block.getBlockY() - 1);
                    block.getBlock().setType(Material.GREEN_CONCRETE);
                    Bukkit.broadcastMessage("Green wool changed to green concrete");
                    cancel();
                }
            }
        }.runTaskTimer(Main.getPlugin(), 0, 20);
    }

    /**
     * checks if the given player is standing on green wool
     * @param player    the given player
     * @return          true or false
     */
    private static boolean checkIfOnWool(Player player) {
        Location block = player.getLocation();
        block.setY(block.getBlockY() - 1);
        if (block.getBlock().getType() == Material.GREEN_WOOL) {
            Bukkit.broadcastMessage("check wool true");
            return true;
        }
        else {
            return false;
        }
    }
}
