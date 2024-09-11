package org.example.DeathMinigames.minigames;

import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.example.DeathMinigames.deathMinigames.Main;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import static org.bukkit.plugin.java.JavaPlugin.getPlugin;
import static org.example.DeathMinigames.listeners.DeathListener.*;

public class JumpAndRun {
    private static ArrayList<Block> blocksToDelete = new ArrayList<Block> ();
    private static boolean woolPlaced = false;
    private static boolean goldPlaced = false;
    /**
     * runs the minigame JumpAndRun
     */
    public static void start() {
        // get the player int the arena from the waiting list
        playerInArena = waitingListMinigame.getFirst();
        World w = playerInArena.getWorld();
        Location location = new Location(playerInArena.getWorld(), 93d, 75d, 81d);
        playerInArena.teleport(location);
        Minigame.startMessage(playerInArena, "Du musst diesen Parkour bestehen, um deine Items wieder zu bekommen");

        // get the location and place the first block
        int x = 93;
        int y = 74;
        int z = 81;
        Location nextBlock = new Location(location.getWorld(), x, y, z);
        nextBlock.getBlock().setType(Material.GREEN_CONCRETE);

        // check asynchronously if the player looses or wins, false run the generator of the parkour
        new BukkitRunnable() {
            public void run() {
                if(checkIfPlayerWon(playerInArena) || checkIfPlayerLost(playerInArena, 73)) {
                 cancel();
                }
                else {
                    // check if the player is standing on green concrete, if true place the next block
                    int _x = 0;
                    int _y = playerInArena.getLocation().getBlockY();
                    int _z = 0;
                    Bukkit.broadcastMessage("Check if player is on concrete"); //TODO: replace deprecated method
                    if (checkIfOnConcrete(playerInArena) && !woolPlaced) {
                        // randomizer for coordinates and prefix
                        _x = randomizer(1, 3);
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

                        // check if it is the last block, if true place a gold block
                        if(_y == 81 && !goldPlaced) {
                            nextBlock.getBlock().setType(Material.GOLD_BLOCK);
                            Minigame.playSoundAtLocation(nextBlock, 2F, Sound.BLOCK_AMETHYST_BLOCK_HIT);
                            Minigame.spawnParticles(playerInArena, nextBlock, Particle.GLOW);
                            blocksToDelete.add(nextBlock.getBlock());
                            goldPlaced = true;
                            woolPlaced = true;
                        }
                        else {
                            nextBlock.getBlock().setType(Material.GREEN_WOOL);
                            Minigame.playSoundAtLocation(nextBlock, 2F, Sound.BLOCK_AMETHYST_BLOCK_HIT);
                            Minigame.spawnParticles(playerInArena, nextBlock, Particle.GLOW);
                            woolPlaced = true;
                            Bukkit.broadcastMessage("Green wool was placed at " + nextBlock.toString());
                            blocksToDelete.add(nextBlock.getBlock());
                        }
                    }
                    else {
                        // replace the placed wool with concrete if the player is standing on it
                        replaceWoolWithConcrete(playerInArena);
                    }
                }
            }
        }.runTaskTimer(getPlugin(Main.class), 0, 20);

        Bukkit.broadcastMessage("End");
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
            Bukkit.broadcast(Component.text("check concrete true"));
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
     * @return              true if he reaches that height or higher, false if he does not reach that height
     */
    private static boolean checkIfPlayerWon(Player player) {
        if (checkIfOnGold(player) == true) {
            Minigame.winMessage(player);
            Minigame.spawnChestWithInv(player);
            woolPlaced = false;
            goldPlaced = false;
            for (Block block : blocksToDelete) {
                player.getWorld().setType(block.getLocation(), Material.AIR);
            }
            blocksToDelete.clear();
            Location loc = new Location(player.getWorld(), 93, 74, 81);
            player.getWorld().setType(loc, Material.AIR);
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
            woolPlaced = false;
            goldPlaced = false;
            for (Block block : blocksToDelete) {
                player.getWorld().setType(block.getLocation(), Material.AIR);
            }
            blocksToDelete.clear();
            Location loc = new Location(player.getWorld(), 93, 74, 81);
            player.getWorld().setType(loc, Material.AIR);
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
                    woolPlaced = false;
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

    /**
     * checks if the given player is standing on green wool
     * @param player    the given player
     * @return          true or false
     */
    private static boolean checkIfOnGold(Player player) {
        Location block = player.getLocation();
        block.setY(block.getBlockY() - 1);
        if (block.getBlock().getType() == Material.GOLD_BLOCK) {
            Bukkit.broadcastMessage("check gold true");
            return true;
        }
        else {
            return false;
        }
    }
}
