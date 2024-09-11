package org.example.DeathMinigames.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.example.DeathMinigames.listeners.DeathListener.*;
import static org.example.DeathMinigames.listeners.DeathListener.deaths;

public class GameIgnoreCMD implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player player) {
            if(!waitingListMinigame.contains(player) && inventories.containsKey(player.getUniqueId())) {
                player.sendMessage("§6Dein Inventar wird an deinen Todesort " + "(X: " + deaths.get(player.getUniqueId()).getBlockX() + " Y: " + deaths.get(player.getUniqueId()).getBlockY() + " Z: " + deaths.get(player.getUniqueId()).getBlockZ() + ") gedroppt");
                for(int i = 0; i < inventories.get(player.getUniqueId()).getSize(); i++) {
                    if(inventories.get(player.getUniqueId()).getItem(i) == null) continue;
                    player.getWorld().dropItem(deaths.get(player.getUniqueId()), inventories.get(player.getUniqueId()).getItem(i));
                }
                inventories.remove(player.getUniqueId());
            }
        }
        return false;
    }
}
