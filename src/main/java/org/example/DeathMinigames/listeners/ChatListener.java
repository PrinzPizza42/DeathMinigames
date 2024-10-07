package org.example.DeathMinigames.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import org.example.DeathMinigames.deathMinigames.Main;

import org.example.DeathMinigames.listeners.DeathListener.*;

public class ChatListener implements Listener {

    @EventHandler
    void onChatInput(PlayerCommandPreprocessEvent event) {
        Main main = new Main();

        Player player = event.getPlayer();
        main.getPlugin().getLogger().info(player.getName());
        if (DeathListener.inventories.containsKey(player.getUniqueId())) {
            /*if (event.getMessage().equalsIgnoreCase("/game start")) {
                    event.setCancelled(true);
                    player.sendMessage("§6§oStarte Minispiel...");
                    Location loc = new Location(player.getWorld(), 93, 73, 73);
                    Location locOfBox = new Location(player.getWorld(), 93, 73, 93);
                    player.teleport(loc);
                    waitingListMinigame.addFirst(player);
                    Minigame.teleportPlayerInBox(player, locOfBox);
                    Main.getPlugin().getLogger().info(waitingListMinigame.getFirst().getName());
                    Main.minigameStart(waitingListMinigame.getFirst());
            } else if (event.getMessage().equalsIgnoreCase("/game ignore")) {
                if(!waitingListMinigame.contains(player) && inventories.containsKey(player.getUniqueId())) {
                    event.setCancelled(true);
                    player.sendMessage("§6Dein Inventar wird an deinen Todesort " + "($cX: " + deaths.get(player.getUniqueId()).getBlockX() + " Y: " + deaths.get(player.getUniqueId()).getBlockY() + " Z: " + deaths.get(player.getUniqueId()).getBlockZ() + "§6) gedroppt");
                    for(int i = 0; i < inventories.get(player.getUniqueId()).getSize(); i++) {
                        if(inventories.get(player.getUniqueId()).getItem(i) == null) continue;
                        player.getWorld().dropItem(deaths.get(player.getUniqueId()), inventories.get(player.getUniqueId()).getItem(i));
                    }
                    inventories.remove(player.getUniqueId());
                }

            }*/

        }
    }



}
