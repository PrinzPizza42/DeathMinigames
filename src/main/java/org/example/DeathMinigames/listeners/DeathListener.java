package org.example.DeathMinigames.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.Inventory;
import org.example.DeathMinigames.deathMinigames.Main;
import org.example.DeathMinigames.minigames.Minigame;

import java.util.*;

public class DeathListener implements Listener {
    public static Map<UUID, Inventory> inventories = new HashMap<>();
    public static Map<UUID, Location> deaths = new HashMap<>();
    public static Inventory playerDeathInventory = Bukkit.createInventory(null, 45);
    public static ArrayList<Player> waitingListMinigame = new ArrayList<Player>();
    public static Player playerInArena;

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        Inventory inventory = Bukkit.createInventory(null, 45);
        inventory.setContents(event.getPlayer().getInventory().getContents());
        Location deathpoint = event.getPlayer().getLocation();

        deaths.put(player.getUniqueId(), deathpoint);
        Main.getPlugin().getLogger().info("Player got new deathpoint");

        if (inventory.isEmpty()) {
            player.sendActionBar(Component.text("Inventar wurde nicht gespeichert, da es leer war")
                    .color(NamedTextColor.GOLD)
                    .decoration(TextDecoration.ITALIC, true));
        } else if (!inventories.containsKey(player.getUniqueId())){
            player.sendActionBar(Component.text("Inventar wurde gespeichert")
                    .color(NamedTextColor.GOLD)
                    .decoration(TextDecoration.ITALIC, true));
            inventories.put(player.getUniqueId(), inventory);
        }

    }
}
