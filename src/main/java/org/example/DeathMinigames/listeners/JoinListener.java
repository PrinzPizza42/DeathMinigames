package org.example.DeathMinigames.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.example.DeathMinigames.deathMinigames.Config;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Config config = new Config();
        Player player_14 = event.getPlayer();

        if(!config.checkIfPlayerInFile(player_14)) {
            config.addNewPlayer(player_14.getUniqueId());
            player_14.sendMessage(Component.text("Du wurdest der Liste hinzugefügt und hast eine Difficulty von: ").color(NamedTextColor.GOLD)
                    .append(Component.text(config.checkConfigInt(player_14, "Difficulty")).color(NamedTextColor.RED)));
        }
    }
}
