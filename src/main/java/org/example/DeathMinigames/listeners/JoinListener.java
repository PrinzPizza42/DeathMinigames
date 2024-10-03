package org.example.DeathMinigames.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.example.DeathMinigames.deathMinigames.Introduction;
import org.example.DeathMinigames.deathMinigames.Main;
import org.example.DeathMinigames.minigames.Difficulty;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if(!Difficulty.checkIfPlayerInFile(event.getPlayer())) {
            Difficulty.addPlayer(event.getPlayer());
            event.getPlayer().sendMessage(Component.text("Du wurdest der Liste hinzugefügt und hast eine Difficulty von: ").color(NamedTextColor.GOLD)
                    .append(Component.text(Difficulty.getDifficulty(event.getPlayer())).color(NamedTextColor.RED)));
        }
        if(!Introduction.checkIfPlayerInFile(event.getPlayer())) {
            Introduction.addPlayer(event.getPlayer());
        }
        if(!Main.checkIfPlayerInFile(event.getPlayer())) {
            Main.addPlayer(event.getPlayer());
        }
    }
}
