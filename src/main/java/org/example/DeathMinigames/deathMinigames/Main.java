package org.example.DeathMinigames.deathMinigames;

import org.bukkit.OfflinePlayer;
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

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static org.example.DeathMinigames.listeners.DeathListener.playerInArena;

public final class Main extends JavaPlugin {

    private static Main plugin;
    public static HashMap<UUID, Boolean> configIntroduction = new HashMap<>();
    public static HashMap<UUID, Boolean> configUsesPlugin = new HashMap<>();
    public static HashMap<UUID, Integer> configDifficulty = new HashMap<>();

    @Override
    public void onEnable() {
        getLogger().info("Plugin enabled");
        plugin = this;

        cloneConfigToHasMap();

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
    }


    @Override
    public void onDisable() {
    }

    /**
     * starts a random minigame
     * @param player    the player who is starting a random minigame
     */
    public void minigameStart(Player player) {
        JumpAndRun jumpAndRun = new JumpAndRun();
        Minigame minigame = new Minigame();
        Introduction introduction = new Introduction();
        Main main = new Main();

        if(!introduction.checkIfPlayerGotIntroduced(player)) {
            introduction.introStart(player);
        }
        else if(main.checkConfigBoolean(player, "UsesPlugin")) {
            // get a random number, to start a random minigame
            int randomNum = ThreadLocalRandom.current().nextInt(1, 2 + 1);
            switch (randomNum) {
                case 1:
                    if(playerInArena == null) {
                        jumpAndRun.start();
                    }
                    else {
                        if(player.getUniqueId() != playerInArena.getUniqueId()) {
                            player.sendMessage(Component.text("Die Arena ist gerade besetzt, du wurdest in die Warteliste aufgenommen").color(NamedTextColor.GOLD));
                            Location locationBox = new Location(player.getWorld(), 115, 76, 53);
                            minigame.teleportPlayerInBox(player, locationBox);
                        }
                    }
                    break;

                case 2:
                    if(playerInArena == null) {
                        jumpAndRun.start();
                        //FightPVE;
                    }
                    else {
                        if(player.getUniqueId() != playerInArena.getUniqueId()) {
                            player.sendMessage(Component.text("Die Arena ist gerade besetzt, du wurdest in die Warteliste aufgenommen").color(NamedTextColor.GOLD));
                            Location locationBox = new Location(player.getWorld(), 115, 76, 53);
                            minigame.teleportPlayerInBox(player, locationBox);
                        }
                    }
                    break;
            }
        }
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public void addPlayerInConfig(UUID player) {
        getPlugin().getConfig().set(player + ".Introduction", false);
        getPlugin().getConfig().set(player + ".UsesPlugin", true);
        getPlugin().getConfig().set(player + ".Difficulty", 0);
        getPlugin().saveConfig();
    }

    public void addPlayerInHashMap(UUID player) {
        configIntroduction.put(player, (boolean) getConfig().get(player + ".Introduction"));
        configUsesPlugin.put(player, (boolean) getConfig().get(player + ".UsesPlugin"));
        configDifficulty.put(player, (int) getConfig().get(player + ".Difficulty"));
    }

    public void addNewPlayerInHashMap(UUID player) {
        configIntroduction.put(player, false);
        configUsesPlugin.put(player, true);
        configDifficulty.put(player, 0);

        addPlayerInConfig(player);
    }

    public boolean checkIfPlayerInFile(Player player) {
        Main main = new Main();

        if(main.getPlugin().getConfig().contains(player.getUniqueId().toString())) {
            return true;
        }
        else{
            return false;
        }
    }

    private void cloneConfigToHasMap() {
        for(OfflinePlayer player : getServer().getWhitelistedPlayers()) {
            if(getConfig().contains(player.getUniqueId().toString())) {
                addPlayerInHashMap(player.getUniqueId());
            }
        }
    }

    public void setIntroduction(Player player, boolean introduction) {
        configIntroduction.replace(player.getUniqueId(), introduction);
        getPlugin().getConfig().set(player.getUniqueId() + ".Introduction", introduction);
        getPlugin().saveConfig();
    }

    public void setUsesPlugin(Player player, boolean usesPlugin) {
        configUsesPlugin.replace(player.getUniqueId(), usesPlugin);
        getPlugin().getConfig().set(player.getUniqueId() + ".UsesPlugin", usesPlugin);
        getPlugin().saveConfig();
    }

    public void setDifficulty(Player player, int difficulty) {
        configDifficulty.replace(player.getUniqueId(), difficulty);
        getPlugin().getConfig().set(player.getUniqueId() + ".Difficulty", difficulty);
        getPlugin().saveConfig();
    }

    public boolean checkConfigBoolean(Player player, String topic) {
        switch(topic) {
            case "Introduction":
                return configIntroduction.get(player.getUniqueId());
            case "UsesPlugin":
                return configUsesPlugin.get(player.getUniqueId());
        }
        return false;
    }

    public int checkConfigInt(Player player, String topic) {
        switch(topic) {
            case "Difficulty":
                return configDifficulty.get(player.getUniqueId());

        }
        return 404;
    }
}
