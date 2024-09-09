package org.example.DeathMinigames.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DeathListener implements Listener {
    public static Map<UUID, Inventory> inventories = new HashMap<>();
    public static Map<UUID, Location> deaths = new HashMap<>();
    public static ArrayList<Inventory> playerDeathInventory = new ArrayList<Inventory>();
    public static ArrayList<Player> waitingListMinigame = new ArrayList<Player>();
    public static Player playerInArena;

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        Inventory inventory = event.getPlayer().getInventory();
        Location deathpoint = event.getPlayer().getLocation();


        if (!deaths.containsKey(player.getUniqueId())) {
            deaths.put(player.getUniqueId(), deathpoint);
        }


        if (inventory.isEmpty()) {
            player.sendMessage("Inventar wurde nicht gespeichert, da es leer war");
        }
        else {
            player.sendMessage("Inventar wurde gespeichert");
            inventories.put(player.getUniqueId(), inventory);
            playerDeathInventory.addLast(inventories.get(player.getUniqueId()));
        }
        player.getInventory().clear();
    }
}
