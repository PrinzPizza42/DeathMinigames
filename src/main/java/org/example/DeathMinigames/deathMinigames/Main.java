package org.example.DeathMinigames.deathMinigames;

import de.j.stationofdoom.util.translations.TranslationFactory;
import org.example.DeathMinigames.listeners.*;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.example.DeathMinigames.commands.GameCMD;
import org.example.DeathMinigames.minigames.JumpAndRun;
import org.example.DeathMinigames.minigames.Minigame;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import static org.example.DeathMinigames.listeners.DeathListener.playerInArena;

public final class Main extends JavaPlugin {

    private static Main plugin;

    @Override
    public void onEnable() {
        getLogger().info("Plugin enabled");
        plugin = this;

        Config config = new Config();
        if(!getPlugin().getConfig().contains("KnownPlayers")) {
            getPlugin().getConfig().set("KnownPlayers", new ArrayList<>().stream().toList());
            getPlugin().getLogger().info("created KnownPlayers");
        }

        config.cloneConfigToHasMap();

        LifecycleEventManager<Plugin> manager = this.getLifecycleManager();
        manager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands COMMANDS = event.registrar();
            COMMANDS.register("game", "game related commands", new GameCMD());
        });

        getServer().getPluginManager().registerEvents(new SnowballHitListener(), this);
        getServer().getPluginManager().registerEvents(new DeathListener(), this);
        getServer().getPluginManager().registerEvents(new RespawnListener(), this);
        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);

        TranslationFactory tf = new TranslationFactory();
        tf.addTranslationsFromFile(new InputStreamReader(Main.class.getResourceAsStream("/translations.json"), StandardCharsets.UTF_8));
    }


    @Override
    public void onDisable() {
    }

    /**
     * starts a random minigame
     * @param player    the player who is starting a random minigame
     */
    public static void minigameStart(Player player) {
        JumpAndRun jumpAndRun = new JumpAndRun();
        Minigame minigame = new Minigame();
        Introduction introduction = new Introduction();
        Config config = new Config();
        TranslationFactory tf = new TranslationFactory();

        if(!introduction.checkIfPlayerGotIntroduced(player)) {
            introduction.introStart(player);
        }
        else if(config.checkConfigBoolean(player, "UsesPlugin")) {
            if(playerInArena == null) {
                jumpAndRun.start();
            }
            else {
                if(player.getUniqueId() != playerInArena.getUniqueId()) {
                    player.sendMessage(Component.text(tf.getTranslation(player, "arenaIsFull")).color(NamedTextColor.GOLD));
                    Location locationBox = new Location(player.getWorld(), 115, 76, 53);
                    minigame.teleportPlayerInBox(player, locationBox);
                }
            }
        }
    }

    public static Plugin getPlugin() {
        return plugin;
    }

}
