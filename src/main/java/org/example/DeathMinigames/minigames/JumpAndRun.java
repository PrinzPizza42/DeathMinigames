package org.example.DeathMinigames.minigames;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.example.DeathMinigames.deathMinigames.Main;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import static org.bukkit.plugin.java.JavaPlugin.getPlugin;
import static org.example.DeathMinigames.listeners.DeathListener.*;
import static org.example.DeathMinigames.minigames.Minigame.checkIfWaitinglistIsEmpty;

public class JumpAndRun {
    private static ArrayList<Block> blocksToDelete = new ArrayList<Block> ();
    private static boolean woolPlaced = false;
    private static boolean goldPlaced = false;
    private static int _x = 0;
    private static int _y = 0;
    private static int _z = 0;

    /**
     * runs the minigame JumpAndRun
     */
    public static void start() {
        // get the player int the arena from the waiting list
        playerInArena = waitingListMinigame.getFirst();

        World w = playerInArena.getWorld();
        Location location = new Location(playerInArena.getWorld(), 93.5d, 75.5d, 81.5d);
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
                    if(!checkIfWaitinglistIsEmpty()) {
                        Main.minigameStart(waitingListMinigame.getFirst());
                    }
                    cancel();
                }
                else {
                    // check if the player is standing on green concrete, if true place the next block
                    if (checkIfOnConcrete(playerInArena) && !woolPlaced) {
                        // randomizer for coordinates and prefix
                        _x = randomizer(2, 3);
                        _z = randomizer(2, 3);
                        int randomNum = randomizer(1, 4);
                        switch(randomNum) {
                            case 1:
                                // _x & _z negative
                                _x = _x * -1;
                                _z = _z * -1;
                                break;
                            case 2:
                                // _x & _z positive
                                break;
                            case 3:
                                // _x negative & _z positive
                                _x = _x * -1;
                                break;
                            case 4:
                                // _x positive & _z negative
                                _z = _z * -1;
                                break;
                            case 5:
                                // Block with ladder jump
                                break;
                        }
                        _x = playerInArena.getLocation().getBlockX() + _x;
                        _y = playerInArena.getLocation().getBlockY();
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
                            blocksToDelete.add(nextBlock.getBlock());
                        }
                    }
                    else {
                        // replace the placed wool with concrete if the player is standing on it
                        replaceWoolWithConcrete(playerInArena);
                    }
                    /*if(_x <= 103 && _x >= 83 && _z <= 91 && _z >= 71) {
                        Bukkit.broadcast(Component.text("Wool will be placed").color(NamedTextColor.GREEN));
                    }*/
                }
            }
        }.runTaskTimer(getPlugin(Main.class), 0, 5);
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
            playerInArena = null;
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
            playerInArena = null;
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
                    woolPlaced = false;
                    cancel();
                }
            }
        }.runTaskTimer(Main.getPlugin(), 0, 5);
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
            return true;
        }
        else {
            return false;
        }
    }
}
