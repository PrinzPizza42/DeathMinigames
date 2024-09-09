package org.example.DeathMinigames.Listeners;

import com.mojang.brigadier.Message;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import org.example.DeathMinigames.Minigames.Minigame;
import org.example.DeathMinigames.deathMinigames.Main;

import java.util.Objects;

import static org.example.DeathMinigames.Listeners.DeathListener.*;

public class ChatListener implements Listener {

    @EventHandler
    void onChatInput(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (inventories.containsKey(player.getUniqueId())) {
            if (event.getMessage().equalsIgnoreCase("/game start")) {

                    event.setCancelled(true);
                    player.sendMessage("Starte Minispiel...");
                    Location loc = new Location(player.getWorld(), 93, 73, 73);
                    player.teleport(loc);
                    waitingListMinigame.add(player);
                    Main.minigameStart(waitingListMinigame.getFirst());


            } else if (event.getMessage().equalsIgnoreCase("/game ignore")) {
                if(!waitingListMinigame.contains(player) && inventories.containsKey(player.getUniqueId())) {
                    event.setCancelled(true);
                    player.sendMessage("Dein Inventar wird an deinen Todesort " + "(X: " + deaths.get(player.getUniqueId()).getBlockX() + " Y: " + deaths.get(player.getUniqueId()).getBlockY() + " Z: " + deaths.get(player.getUniqueId()).getBlockZ() + ") gedroppt");
                    for(int i = 0; i < playerDeathInventory.getFirst().getSize(); i++) {
                        player.getWorld().dropItem(deaths.get(player.getUniqueId()), Objects.requireNonNull(playerDeathInventory.getFirst().getItem(i)));
                    }
                    playerDeathInventory.removeFirst();
                    inventories.remove(player.getUniqueId());
                }

            }

        }
        if(event.getMessage().equalsIgnoreCase("/test")) {
            event.getPlayer().sendMessage(playerDeathInventory.getFirst().getContents().toString());
            if (playerDeathInventory.getFirst().getItem(1).toString() == null) {
                event.getPlayer().sendMessage("das Item gitbt null aus");
            }
            else {
                event.getPlayer().sendMessage("keine Ahnung was hier schief läuft");
            }
        }
    }



}
