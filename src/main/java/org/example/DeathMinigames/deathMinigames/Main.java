package org.example.DeathMinigames.deathMinigames;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.example.DeathMinigames.listeners.ChatListener;
import org.example.DeathMinigames.listeners.DeathListener;
import org.example.DeathMinigames.listeners.RespawnListener;
import org.example.DeathMinigames.listeners.SnowballHitListener;
import org.example.DeathMinigames.minigames.JumpAndRun;

import java.util.concurrent.ThreadLocalRandom;

public final class Main extends JavaPlugin {

    private static Main plugin;

    @Override
    public void onEnable() {
        getLogger().info("Plugin enabled");
        plugin = this;
        getServer().getPluginManager().registerEvents(new SnowballHitListener(), this);
        getServer().getPluginManager().registerEvents(new DeathListener(), this);
        getServer().getPluginManager().registerEvents(new RespawnListener(), this);
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
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
        player.sendMessage("Random number is " + randomNum);
        switch (randomNum) {
            case 1:
                JumpAndRun.start();
                break;

            case 2:
                //FightPVE;
                JumpAndRun.start();
                break;
        }
    }

    public static Plugin getPlugin() {
        return plugin;
    }
}
