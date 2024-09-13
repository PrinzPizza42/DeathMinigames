package org.example.DeathMinigames.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.example.DeathMinigames.minigames.Difficulty;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if(!Difficulty.checkIfPlayerInMap(event.getPlayer())) {
            Difficulty.addPlayer(event.getPlayer());
            Bukkit.broadcast(Component.text("du wurdest der Liste hinzugefügt und hast eine Difficulty von: ").color(NamedTextColor.GOLD)
                    .append(Component.text(Difficulty.getDifficulty(event.getPlayer())).color(NamedTextColor.RED)));
        }
    }
}
