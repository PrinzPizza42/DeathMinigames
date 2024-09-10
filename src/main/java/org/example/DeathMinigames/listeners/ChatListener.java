package org.example.DeathMinigames.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import org.example.DeathMinigames.deathMinigames.Main;

import java.util.Arrays;

import static org.example.DeathMinigames.listeners.DeathListener.*;

public class ChatListener implements Listener {

    @EventHandler
    void onChatInput(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        Main.getPlugin().getLogger().info(player.getName());
        if (inventories.containsKey(player.getUniqueId())) {
            if (event.getMessage().equalsIgnoreCase("/game start")) {
                    event.setCancelled(true);
                    player.sendMessage("Starte Minispiel...");
                    Location loc = new Location(player.getWorld(), 93, 73, 73);
                    player.teleport(loc);
                    waitingListMinigame.addFirst(player);
                    Main.getPlugin().getLogger().info(waitingListMinigame.getFirst().getName());
                    Main.minigameStart(waitingListMinigame.getFirst());
            } else if (event.getMessage().equalsIgnoreCase("/game ignore")) {
                if(!waitingListMinigame.contains(player) && inventories.containsKey(player.getUniqueId())) {
                    event.setCancelled(true);
                    player.sendMessage("Dein Inventar wird an deinen Todesort " + "(X: " + deaths.get(player.getUniqueId()).getBlockX() + " Y: " + deaths.get(player.getUniqueId()).getBlockY() + " Z: " + deaths.get(player.getUniqueId()).getBlockZ() + ") gedroppt");
                    for(int i = 0; i < inventories.get(player.getUniqueId()).getSize(); i++) {
                        if(inventories.get(player.getUniqueId()).getItem(i) == null) continue;
                        player.getWorld().dropItem(deaths.get(player.getUniqueId()), inventories.get(player.getUniqueId()).getItem(i));
                    }
                    inventories.remove(player.getUniqueId());
                }

            }

        }
    }



}
