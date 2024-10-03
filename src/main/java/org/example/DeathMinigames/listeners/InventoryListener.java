package org.example.DeathMinigames.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.example.DeathMinigames.deathMinigames.Main;
import org.example.DeathMinigames.minigames.Minigame;

import static org.example.DeathMinigames.listeners.DeathListener.*;

public class InventoryListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack clicked = event.getCurrentItem();
        Inventory inventory = event.getInventory();
        if (inventory == RespawnListener.getRespawnMenu()) {
            // minigame start
            if (clicked.getType() == Material.GREEN_WOOL) {
                event.setCancelled(true);
                Minigame.playSoundAtLocation(player.getEyeLocation(), 0.5F, Sound.ENTITY_ENDER_EYE_DEATH);
                RespawnListener.setPlayerDecided(true);
                player.resetTitle();
                player.sendActionBar(Component.text("Starte Minispiel...")
                        .color(NamedTextColor.GOLD)
                        .decoration(TextDecoration.ITALIC, true));
                player.playSound(player.getEyeLocation(), Sound.BLOCK_PORTAL_TRAVEL, 0.5F, 1.0F);
                waitingListMinigame.addLast(player);
                Main.minigameStart(player);
            }
            // minigame ignore
            else if (clicked.getType() == Material.RED_WOOL) {
                event.setCancelled(true);
                Minigame.playSoundToPlayer(player, 0.5F, Sound.ENTITY_ITEM_BREAK);
                RespawnListener.setPlayerDecided(true);
                player.resetTitle();
                if (!waitingListMinigame.contains(player) && inventories.containsKey(player.getUniqueId())) {
                    player.sendMessage(Component.text("Dein Inventar wird an deinen Todesort (").color(NamedTextColor.GOLD)
                            .append(Component.text("X: " + deaths.get(player.getUniqueId()).getBlockX() + " Y: " + deaths.get(player.getUniqueId()).getBlockY() + " Z: " + deaths.get(player.getUniqueId()).getBlockZ()).color(NamedTextColor.RED))
                            .append(Component.text(") gedroppt")).color(NamedTextColor.GOLD));
                    for (int i = 0; i < inventories.get(player.getUniqueId()).getSize(); i++) {
                        if (inventories.get(player.getUniqueId()).getItem(i) == null) continue;
                        player.getWorld().dropItem(deaths.get(player.getUniqueId()), inventories.get(player.getUniqueId()).getItem(i));
                    }
                    inventories.remove(player.getUniqueId());
                }
            }
            inventories.remove(player.getUniqueId());
        }
    }
}
