package org.example.DeathMinigames.deathMinigames;

import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.example.DeathMinigames.minigames.Minigame;

import java.util.ArrayList;

import static org.example.DeathMinigames.listeners.DeathListener.deaths;
import static org.example.DeathMinigames.listeners.DeathListener.inventories;

public class Introduction {
    private static ArrayList<Location> blocks = new ArrayList<>();

    public boolean checkIfPlayerGotIntroduced(Player player) {
        Config config = new Config();

        return config.checkConfigBoolean(player, "Introduction");
    }

    public void introStart(Player player) {
        Location locationIntro = new Location(player.getWorld(), 115.5F, 100, 53.5F);
        sendPlayerIntroMessage(player);
        teleportPlayerToGod(player, locationIntro);
    }

    public void introEnd(Player player) {
        Main main = new Main();

        assert checkIfPlayerGotIntroduced(player);
        main.getPlugin().getLogger().info(player.getName().toUpperCase() + " got introduced");
        deleteBarrierCage(player.getLocation());
        blocks.clear();
    }

    private void sendPlayerIntroMessage(Player player) {
        player.sendMessage(Component.text("Ich gebe dir eine zweite Chance, nutze sie gut."));
        player.sendMessage(Component.text("Du kannst versuchen um deine Items zu spielen. Da ich mich aber auch amüsieren will, werden die Spiele immer schwerer werden."));
        player.sendMessage(Component.text("Wenn dir die Spiele zu schwer werden kannst du mich natürlich auch immer mit Diamanten bezahlen, das ist ganz dir überlassen."));
        player.sendMessage(Component.text("Möchtest du diese Chance nutzen?"));

        TextComponent decideForPlugin = new TextComponent("Ja");
        TextComponent decideNotForPlugin = new TextComponent("Nein");
        TextComponent middlePart = new TextComponent(" / ");

        //TODO: replace deprecated code
        decideForPlugin.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/game introPlayerDecidesToUseFeatures"));
        decideForPlugin.setColor(net.md_5.bungee.api.ChatColor.GREEN);
        decideForPlugin.setItalic(true);
        decideForPlugin.setUnderlined(true);
        decideNotForPlugin.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/game introPlayerDecidesToNotUseFeatures"));
        decideNotForPlugin.setColor(net.md_5.bungee.api.ChatColor.RED);
        decideNotForPlugin.setItalic(true);
        decideNotForPlugin.setUnderlined(true);

        player.sendMessage(decideForPlugin, middlePart, decideNotForPlugin);
    }

    private void teleportPlayerToGod(Player player, Location location) {
        Minigame minigame = new Minigame();

        placeBarrierCageAroundLoc(location);
        player.teleport(location);
        minigame.playSoundAtLocation(location, 0.5F, Sound.ENTITY_ENDER_EYE_DEATH);
    }

    private void placeBarrierCageAroundLoc(Location location) {
        Location location1 = new Location(location.getWorld(), location.getX(), location.getY() - 1, location.getZ());
        Location location2 = new Location(location.getWorld(), location.getX() + 1, location.getY(), location.getZ());
        Location location3 = new Location(location.getWorld(), location.getX() - 1, location.getY(), location.getZ());
        Location location4 = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ() + 1);
        Location location5 = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ() - 1);
        Location location6 = new Location(location.getWorld(), location.getX() + 1, location.getY() + 1, location.getZ());
        Location location7 = new Location(location.getWorld(), location.getX() - 1, location.getY() + 1, location.getZ());
        Location location8 = new Location(location.getWorld(), location.getX(), location.getY() + 1, location.getZ() + 1);
        Location location9 = new Location(location.getWorld(), location.getX(), location.getY() + 1, location.getZ() - 1);
        Location location10 = new Location(location.getWorld(), location.getX(), location.getY() + 2, location.getZ());
        blocks.add(location1);
        blocks.add(location2);
        blocks.add(location3);
        blocks.add(location4);
        blocks.add(location5);
        blocks.add(location6);
        blocks.add(location7);
        blocks.add(location8);
        blocks.add(location9);
        blocks.add(location10);
        for (int i = 0; i < 10; i++ ) {
            location.getWorld().getBlockAt(blocks.get(i)).setType(Material.BARRIER);
        }
    }

    private void deleteBarrierCage(Location location) {
        for (int i = 0; i < 10; i++ ) {
            location.getWorld().getBlockAt(blocks.get(i)).setType(Material.AIR);
        }
    }

    public void dropInv(Player player) {
        Minigame minigame = new Minigame();

        minigame.loseMessage(player);
        for (int i = 0; i < inventories.get(player.getUniqueId()).getSize(); i++) {
            if (inventories.get(player.getUniqueId()).getItem(i) == null) continue;
            player.getWorld().dropItem(deaths.get(player.getUniqueId()), inventories.get(player.getUniqueId()).getItem(i));
        }
        teleportPlayer(player);
        minigame.playSoundToPlayer(player, 0.5F, Sound.ENTITY_ITEM_BREAK);
        inventories.remove(player.getUniqueId());
        deaths.remove(player.getUniqueId());
    }

    private void teleportPlayer(Player player) {
        player.playSound(player.getEyeLocation(), Sound.BLOCK_PORTAL_TRAVEL, 0.5F, 1.0F);
        if(player.getRespawnLocation() == null) {
                player.teleport(player.getWorld().getSpawnLocation());
            } else {
                player.teleport(player.getRespawnLocation());
            }
    }
}
