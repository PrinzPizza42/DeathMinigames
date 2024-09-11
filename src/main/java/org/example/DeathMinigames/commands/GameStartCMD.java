package org.example.DeathMinigames.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.DeathMinigames.deathMinigames.Main;

import static org.example.DeathMinigames.listeners.DeathListener.inventories;
import static org.example.DeathMinigames.listeners.DeathListener.waitingListMinigame;

public class GameStartCMD implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player player) {
            if (inventories.containsKey(player.getUniqueId())) {
                player.sendMessage("§6§oStarte Minispiel...");
                Location loc = new Location(player.getWorld(), 93, 73, 73);
                player.teleport(loc);
                waitingListMinigame.addLast(player);
                Main.getPlugin().getLogger().info(waitingListMinigame.getFirst().getName());
                Main.minigameStart(waitingListMinigame.getFirst());
            }
        }
        return false;
    }
}
