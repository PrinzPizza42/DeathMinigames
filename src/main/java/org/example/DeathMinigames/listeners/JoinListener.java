package org.example.DeathMinigames.listeners;

import de.j.stationofdoom.util.translations.TranslationFactory;
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
        Player player = event.getPlayer();
        TranslationFactory tf = new TranslationFactory();

        if(!config.checkIfPlayerInFile(player)) {
            config.addNewPlayer(player.getUniqueId());
            player.sendMessage(Component.text(tf.getTranslation(player,"addedToPlayerList")).color(NamedTextColor.GOLD)
                    .append(Component.text(config.checkConfigInt(player, "Difficulty")).color(NamedTextColor.RED)));
        }
    }
}
