package org.example.DeathMinigames.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.example.DeathMinigames.deathMinigames.Main;
import org.example.DeathMinigames.minigames.Difficulty;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Difficulty difficulty = new Difficulty();
        Main main = new Main();

        if(!main.checkIfPlayerInFile(event.getPlayer())) {
            main.addNewPlayerInHashMap(event.getPlayer().getUniqueId());
            event.getPlayer().sendMessage(Component.text("Du wurdest der Liste hinzugefügt und hast eine Difficulty von: ").color(NamedTextColor.GOLD)
                    .append(Component.text(main.checkConfigInt(event.getPlayer(), "Difficulty")).color(NamedTextColor.RED)));
        }
    }
}
