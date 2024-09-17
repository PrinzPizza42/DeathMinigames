package org.example.DeathMinigames.deathMinigames;

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
import org.example.DeathMinigames.listeners.*;
import org.example.DeathMinigames.minigames.JumpAndRun;
import org.example.DeathMinigames.minigames.Minigame;

import java.util.concurrent.ThreadLocalRandom;

import static org.example.DeathMinigames.listeners.DeathListener.playerInArena;

public final class Main extends JavaPlugin {

    private static Main plugin;

    @Override
    public void onEnable() {
        getLogger().info("Plugin enabled");
        plugin = this;

        LifecycleEventManager<Plugin> manager = this.getLifecycleManager();
        manager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands COMMANDS = event.registrar();
            COMMANDS.register("game", "game related commands", new GameCMD());
        });

        getServer().getPluginManager().registerEvents(new SnowballHitListener(), this);
        getServer().getPluginManager().registerEvents(new DeathListener(), this);
        getServer().getPluginManager().registerEvents(new RespawnListener(), this);
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
        getServer().getPluginManager().registerEvents(new JoinListener(), this);
    }

    @Override
    public void onDisable() {

    }

    /**
     * starts a random minigame
     * @param player    the player who is starting a random minigame
     */
    public static void minigameStart(Player player) {
        // get a random number, to start a random minigame
        int randomNum = ThreadLocalRandom.current().nextInt(1, 2 + 1);
        switch (randomNum) {
            case 1:
                if(playerInArena == null) {
                    JumpAndRun.start();
                }
                else {
                    if(player.getUniqueId() != playerInArena.getUniqueId()) {
                        player.sendMessage(Component.text("Die Arena ist gerade besetzt, du wurdest in die Warteliste aufgenommen").color(NamedTextColor.GOLD));
                        Location locationBox = new Location(player.getWorld(), 115, 76, 53);
                        Minigame.teleportPlayerInBox(player, locationBox);
                    }
                }
                break;

            case 2:
                if(playerInArena == null) {
                    JumpAndRun.start();
                    //FightPVE;
                }
                else {
                    if(player.getUniqueId() != playerInArena.getUniqueId()) {
                        player.sendMessage(Component.text("Die Arena ist gerade besetzt, du wurdest in die Warteliste aufgenommen").color(NamedTextColor.GOLD));
                        Location locationBox = new Location(player.getWorld(), 115, 76, 53);
                        Minigame.teleportPlayerInBox(player, locationBox);
                    }
                }
                break;
        }
    }

    public static Plugin getPlugin() {
        return plugin;
    }
}
