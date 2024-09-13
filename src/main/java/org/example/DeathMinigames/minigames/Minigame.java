package org.example.DeathMinigames.minigames;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import static org.example.DeathMinigames.listeners.DeathListener.*;

public class Minigame {
    /**
     * sends the player the starting message of the minigame
     * @param player    the player in the minigame
     * @param message   the message as declared in the Minigame
     */
    public static void startMessage(Player player, String message) {
        player.sendMessage(Component.text(message).color(NamedTextColor.GOLD));
        playerDeathInventory.setContents(inventories.get(player.getUniqueId()).getContents()); ;
        waitingListMinigame.remove(player);
    }

    /**
     * sends the player a message that he lost the minigame annd removes him from the inventories HashMap
     * @param player    the player who lost the game
     */
    public static void loseMessage(Player player) {
        player.sendMessage(Component.text("Du hast verloren").color(NamedTextColor.GOLD));
        player.sendMessage(Component.text("Dein Inventar wird an deinen Todesort ").color(NamedTextColor.GOLD)
                .append(Component.text("(X: " + deaths.get(player.getUniqueId()).getBlockX() + " ").color(NamedTextColor.RED))
                .append(Component.text("Y: " + deaths.get(player.getUniqueId()).getBlockY() + " ").color(NamedTextColor.RED))
                .append(Component.text("Z: " + deaths.get(player.getUniqueId()).getBlockZ() + ") ").color(NamedTextColor.RED))
                .append(Component.text("gedroppt").color(NamedTextColor.GOLD)));
        Difficulty.higherDifficulty(player);
        player.sendMessage(Component.text("Deine Schwierigkeit wurde um 1 auf ").color(NamedTextColor.GOLD)
                .append(Component.text(Difficulty.getDifficulty(player)).color(NamedTextColor.RED))
                .append(Component.text(" erhöht.").color(NamedTextColor.GOLD)));
    }

    /**
     * drops the inventory of the player at his deathpoint, clears the playerDeathInventory, teleports him to his respawnlocation and removes him from the deaths HashMap
     * @param player    the player whose inventory is to be droopped
     */
    public static void dropInv(Player player) {
        for(int i = 0; i < playerDeathInventory.getSize(); i++) {
            if(playerDeathInventory.getItem(i) == null) continue;
            assert playerDeathInventory.getItem(i) != null;
            player.getWorld().dropItem(deaths.get(player.getUniqueId()), playerDeathInventory.getItem(i));
        }
        playerDeathInventory.clear();
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
        player.sendMessage(Component.text("Du hast gewonnen, du bekommst jetzt deine Items").color(NamedTextColor.GOLD));
    }

    /**
     * Opens an inventory for the player which has inside the items, he had in his inventory at the time of his death, teleports him to his respawnpoint, clears playerDeathInventory and removes him from the inventories and deaths HashMaps
     * @param player    the play to open the inventory to
     */
    public static void spawnChestWithInv(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 45, "§6Deine Items");
        inventory.setContents(playerDeathInventory.getContents());

        if(player.getRespawnLocation() == null) {
            player.teleport(player.getWorld().getSpawnLocation());
        } else player.teleport(player.getRespawnLocation());

        player.openInventory(inventory);
        Minigame.playSoundAtLocation(player.getLocation(), 1F, Sound.ITEM_TOTEM_USE);

        playerDeathInventory.clear();
        inventories.remove(player.getUniqueId());
        deaths.remove(player.getUniqueId());
    }

    /**
     * Plays a sound at a location
     * @param location  the location to play the sound at
     * @param volume    how loud the
     * @param sound     the sound to play
     */
    public static void playSoundAtLocation(Location location, Float volume, Sound sound) {
        location.getWorld().playSound(location, sound, volume, 1F);
    }

    public static void spawnParticles(Player player, Location location, Particle particle) {
        player.getWorld().spawnParticle(particle, location, 20, 1, 1, 1);
    }

    public static void teleportPlayerInBox(Player player, Location locationOfBox) {
        player.teleport(locationOfBox);
    }

    public static boolean checkIfWaitinglistIsEmpty() {
        if(waitingListMinigame.isEmpty()) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * send the player the statistics of the minigames
     * @param player
     */
    public static void sendStatistics(Player player) {

    }
}
