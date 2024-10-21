package org.example.DeathMinigames.minigames;

import de.j.stationofdoom.util.translations.TranslationFactory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.example.DeathMinigames.deathMinigames.Config;

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
        Config config = new Config();
        TranslationFactory tf = new TranslationFactory();

        if(config.checkConfigBoolean(player, "UsesPlugin")) {
            player.sendMessage(Component.text(tf.getTranslation(player, "loseMessage1")).color(NamedTextColor.RED));
            player.sendMessage(Component.text(tf.getTranslation(player, "loseMessage2")).color(NamedTextColor.GOLD)
                    .append(Component.text("X: " + deaths.get(player.getUniqueId()).getBlockX() + " ").color(NamedTextColor.RED))
                    .append(Component.text("Y: " + deaths.get(player.getUniqueId()).getBlockY() + " ").color(NamedTextColor.RED))
                    .append(Component.text("Z: " + deaths.get(player.getUniqueId()).getBlockZ()).color(NamedTextColor.RED)));
            player.sendMessage(Component.text(tf.getTranslation(player, "loseMessage3")).color(NamedTextColor.GOLD)
                    .append(Component.text(tf.getTranslation(player, "loseMessage4")).color(NamedTextColor.GREEN).clickEvent(ClickEvent.runCommand("/game lowerDifficulty")).decorate(TextDecoration.UNDERLINED)
                    ));
            player.sendMessage(Component.text(tf.getTranslation(player, "loseMessage5")).color(NamedTextColor.GOLD)
                    .append(Component.text("/game lowerDifficulty ").color(NamedTextColor.GREEN)
                            .append(Component.text(tf.getTranslation(player, "loseMessage6")).color(NamedTextColor.GOLD))));
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
        Config config = new Config();
        TranslationFactory tf = new TranslationFactory();

        player.sendMessage(Component.text(tf.getTranslation(player, "winMessage1")).color(NamedTextColor.GOLD));
        if(config.checkConfigInt(player, "Difficulty") < 10) {
            difficulty.higherDifficulty(player);
            player.sendMessage(Component.text(tf.getTranslation(player, "changedDiff1")).color(NamedTextColor.GOLD)
                    .append(Component.text(config.checkConfigInt(player, "Difficulty")).color(NamedTextColor.RED))
                    .append(Component.text(tf.getTranslation(player, "changedDiff2")).color(NamedTextColor.GOLD)));
        }
    }

    /**
     * Opens an inventory for the player which has inside the items, he had in his inventory at the time of his death, teleports him to his respawnpoint, clears playerDeathInventory and removes him from the inventories and deaths HashMaps
     * @param player    the play to open the inventory to
     */
    public void showInv(Player player) {
        TranslationFactory tf = new TranslationFactory();
        Inventory inventory = Bukkit.createInventory(null, 45, Component.text(tf.getTranslation(player, "winInv")).color(NamedTextColor.GOLD));
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
