package org.example.DeathMinigames.Minigames;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import static org.example.DeathMinigames.Listeners.DeathListener.*;

public class Minigame {

    /**
     * sends the player the starting message of the minigame
     * @param player    the player in the minigame
     * @param message   the message as declared in the Minigame
     */
    public static void startMessage(Player player, String message) {
        player.sendMessage(message);
        waitingListMinigame.remove(player);
    }

    /**
     * sends the player a message that he lost the minigame annd removes him from the inventories HashMap
     * @param player    the player who lost the game
     */
    public static void loseMessage(Player player) {
        player.sendMessage("Du hast verloren");
        player.sendMessage("Dein Inventar wird an deinen Todesort " + "(X: " + deaths.get(player.getUniqueId()).getBlockX() + " Y: " + deaths.get(player.getUniqueId()).getBlockY() + " Z: " + deaths.get(player.getUniqueId()).getBlockZ() + ") gedroppt");
    }

    /**
     * drops the inventory of the player at his deathpoint, clears the playerDeathInventory, teleports him to his respawnlocation and removes him from the deaths HashMap
     * @param player    the player whose inventory is to be droopped
     */
    public static void dropInv(Player player) {
        for(int i = 0; i < playerDeathInventory.getFirst().getSize(); i++) {
            player.getWorld().dropItem(deaths.get(player.getUniqueId()), playerDeathInventory.getFirst().getItem(i));
        }
        playerDeathInventory.removeFirst();
        deaths.remove(player.getUniqueId());
        inventories.remove(player.getUniqueId());

        if(player.getRespawnLocation() == null) {
            player.teleport(player.getWorld().getSpawnLocation());
        } else player.teleport(player.getRespawnLocation());
    }

    /**
     * sends the player a message that he won the minigame
     * @param player    the player who won the minigame
     */
    public static void winMessage(Player player) {
        player.sendMessage("Du hast gewonnen, du bekommst jetzt deine Items");
    }

    /**
     * resets the arena, after the player has finished the minigame
     */
    public static void resetArena() {

    }

    /**
     * Opens an inventory for the player which has inside the items, he had in his inventory at the time of his death, teleports him to his respawnpoint, clears playerDeathInventory and removes him from the inventories and deaths HashMaps
     * @param player    the play to open the inventory to
     */
    public static void spawnChestWithInv(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 9*5, "Deine Items");
        inventory.setContents(playerDeathInventory.getFirst().getContents());

        player.openInventory(inventory);
        player.sendMessage("Inventar wird geöffnet");

        if(player.getRespawnLocation() == null) {
            player.teleport(player.getWorld().getSpawnLocation());
        } else player.teleport(player.getRespawnLocation());


        playerDeathInventory.removeFirst();
        inventories.remove(player.getUniqueId());
        deaths.remove(player.getUniqueId());
    }
}
