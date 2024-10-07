package org.example.DeathMinigames.minigames;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.example.DeathMinigames.deathMinigames.Introduction;
import org.example.DeathMinigames.deathMinigames.Main;

import static org.example.DeathMinigames.listeners.DeathListener.*;

public class Minigame {

    /**
     * sends the player the starting message of the minigame
     * @param player    the player in the minigame
     * @param message   the message as declared in the Minigame
     */
    public void startMessage(Player player, String message) {
        player.sendMessage(Component.text(message).color(NamedTextColor.GOLD));

        playerDeathInventory.setContents(inventories.get(player.getUniqueId()).getContents()); ;
        waitingListMinigame.remove(player);
    }

    /**
     * sends the player a message that he lost the minigame annd removes him from the inventories HashMap
     * @param player    the player who lost the game
     */
    public void loseMessage(Player player) {
        Main main = new Main();

        if(main.checkConfigBoolean(player, "UsesPlugin")) {
            player.sendTitle("§6Du hast verloren", "§6Dein Inventar wird an deinen Todesort (" + "§cX:" + deaths.get(player.getUniqueId()).getBlockX() + " §cY: " + deaths.get(player.getUniqueId()).getBlockY() + " §cZ: " + deaths.get(player.getUniqueId()).getBlockZ() + "§6) gedroppt", 1, 50, 20);
            player.sendMessage(Component.text("Todesort: ").color(NamedTextColor.GOLD)
                    .append(Component.text("X: " + deaths.get(player.getUniqueId()).getBlockX() + " ").color(NamedTextColor.RED))
                    .append(Component.text("Y: " + deaths.get(player.getUniqueId()).getBlockY() + " ").color(NamedTextColor.RED))
                    .append(Component.text("Z: " + deaths.get(player.getUniqueId()).getBlockZ()).color(NamedTextColor.RED)));
            player.sendMessage(Component.text("Wenn du deine Schwierigkeit verringern möchtest musst du 4 Diamanten opfern. Clicke dafür ").color(NamedTextColor.GOLD)
                    .append(Component.text("hier").color(NamedTextColor.GREEN).clickEvent(ClickEvent.runCommand("/game lowerDifficulty")).decorate(TextDecoration.UNDERLINED)
                    ));
            player.sendMessage(Component.text("oder gibt ").color(NamedTextColor.GOLD)
                    .append(Component.text("/game lowerDifficulty ").color(NamedTextColor.GREEN)
                            .append(Component.text("ein").color(NamedTextColor.GOLD))));
        }
    }

    /**
     * drops the inventory of the player at his deathpoint, clears the playerDeathInventory, teleports him to his respawnlocation and removes him from the deaths HashMap
     * @param player    the player whose inventory is to be droopped
     */
    public void dropInvWithTeleport(Player player, boolean doTeleport) {
        for(int i = 0; i < playerDeathInventory.getSize(); i++) {
            if(playerDeathInventory.getItem(i) == null) continue;
            assert playerDeathInventory.getItem(i) != null;
            player.getWorld().dropItem(deaths.get(player.getUniqueId()), playerDeathInventory.getItem(i));
        }
        playerDeathInventory.clear();
        deaths.remove(player.getUniqueId());
        inventories.remove(player.getUniqueId());

        if(doTeleport) {
            if(player.getRespawnLocation() == null) {
                player.playSound(player.getEyeLocation(), Sound.BLOCK_PORTAL_TRAVEL, 0.5F, 1.0F);
                player.teleport(player.getWorld().getSpawnLocation());
            } else {
                player.playSound(player.getEyeLocation(), Sound.BLOCK_PORTAL_TRAVEL, 0.5F, 1.0F);
                player.teleport(player.getRespawnLocation());
            }
        }
    }

    /**
     * drops the inventory of the player at his deathpoint, clears the playerDeathInventory and removes him from the deaths HashMap
     * @param player    the player whose inventory is to be droopped
     */
    public void dropInv(Player player) {
        for(int i = 0; i < playerDeathInventory.getSize(); i++) {
            if(playerDeathInventory.getItem(i) == null) continue;
            assert playerDeathInventory.getItem(i) != null;
            player.getWorld().dropItem(deaths.get(player.getUniqueId()), playerDeathInventory.getItem(i));
        }
        playerDeathInventory.clear();
        deaths.remove(player.getUniqueId());
        inventories.remove(player.getUniqueId());

    }

    /**
     * sends the player a message that he won the minigame
     * @param player    the player who won the minigame
     */
    public void winMessage(Player player) {
        Difficulty difficulty = new Difficulty();
        Main main = new Main();

        player.sendMessage(Component.text("Du hast gewonnen, du bekommst jetzt deine Items").color(NamedTextColor.GOLD));
        if(main.checkConfigInt(player, "Difficulty") < 10) {
            difficulty.higherDifficulty(player);
            player.sendMessage(Component.text("Deine Schwierigkeit wurde um 1 auf ").color(NamedTextColor.GOLD)
                    .append(Component.text(main.checkConfigInt(player, "Difficulty")).color(NamedTextColor.RED))
                    .append(Component.text(" erhöht.").color(NamedTextColor.GOLD)));
        }
    }

    /**
     * Opens an inventory for the player which has inside the items, he had in his inventory at the time of his death, teleports him to his respawnpoint, clears playerDeathInventory and removes him from the inventories and deaths HashMaps
     * @param player    the play to open the inventory to
     */
    public void spawnChestWithInv(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 45, Component.text("Deine Items").color(NamedTextColor.GOLD));
        inventory.setContents(playerDeathInventory.getContents());

        if(player.getRespawnLocation() == null) {
            player.playSound(player.getEyeLocation(), Sound.BLOCK_PORTAL_TRAVEL, 0.5F, 1.0F);
            player.teleport(player.getWorld().getSpawnLocation());
        } else {
            player.playSound(player.getEyeLocation(), Sound.BLOCK_PORTAL_TRAVEL, 0.5F, 1.0F);
            player.teleport(player.getRespawnLocation());
        }

        player.openInventory(inventory);
        playSoundAtLocation(player.getLocation(), 1F, Sound.ITEM_TOTEM_USE);

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
    public void playSoundAtLocation(Location location, Float volume, Sound sound) {
        location.getWorld().playSound(location, sound, volume, 1F);
    }

    public void playSoundToPlayer(Player player, Float volume, Sound sound) {
        player.playSound(player, sound, volume, 1F);
    }

    public void spawnParticles(Player player, Location location, Particle particle) {
        player.getWorld().spawnParticle(particle, location, 20, 1, 1, 1);
    }

    public void teleportPlayerInBox(Player player, Location locationOfBox) {
        player.teleport(locationOfBox);
    }

    public boolean checkIfWaitinglistIsEmpty() {
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
    public void sendPlayerStatistics(Player player) {

    }
}
