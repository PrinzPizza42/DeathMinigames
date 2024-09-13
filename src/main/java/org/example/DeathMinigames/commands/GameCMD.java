package org.example.DeathMinigames.commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.DeathMinigames.deathMinigames.Main;
import org.example.DeathMinigames.minigames.Difficulty;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

import static org.example.DeathMinigames.listeners.DeathListener.*;

public class GameCMD implements BasicCommand {

    @Override
    public boolean canUse(@NotNull CommandSender sender) {
        return sender instanceof Player;
    }

    @Override
    public void execute(CommandSourceStack stack, String[] args) {
        Player player = (Player) stack.getSender();
            if (inventories.containsKey(player.getUniqueId())) {
                if (args.length == 1) {
                    switch (args[0].toLowerCase()) {
                        case "start":
                            player.sendActionBar(Component.text("Starte Minispiel...")
                                    .color(NamedTextColor.GOLD)
                                    .decoration(TextDecoration.ITALIC, true));
                            Location loc = new Location(player.getWorld(), 93, 73, 73);
                            player.teleport(loc);
                            waitingListMinigame.addLast(player);
                            Main.getPlugin().getLogger().info(waitingListMinigame.getFirst().getName());
                            Main.minigameStart(waitingListMinigame.getFirst());
                            break;
                        case "ignore":
                            if(!waitingListMinigame.contains(player) && inventories.containsKey(player.getUniqueId())) {
                                player.sendMessage(Component.text("Dein Inventar wird an deinen Todesort (").color(NamedTextColor.GOLD)
                                .append(Component.text("X: " + deaths.get(player.getUniqueId()).getBlockX() + " Y: " + deaths.get(player.getUniqueId()).getBlockY() + " Z: " + deaths.get(player.getUniqueId()).getBlockZ()).color(NamedTextColor.RED))
                                .append(Component.text(") gedroppt")).color(NamedTextColor.GOLD));
                                for (int i = 0; i < inventories.get(player.getUniqueId()).getSize(); i++) {
                                    if (inventories.get(player.getUniqueId()).getItem(i) == null) continue;
                                    player.getWorld().dropItem(deaths.get(player.getUniqueId()), inventories.get(player.getUniqueId()).getItem(i));
                                }
                                inventories.remove(player.getUniqueId());
                            }
                            break;

                        case "difficulty":
                        player.sendMessage(Component.text("Deine Schwierigkeit ist gerade bei ").color(NamedTextColor.GOLD).append(Component.text(Difficulty.getDifficulty(player)).color(NamedTextColor.RED)));

                        default:
                        player.sendMessage(Component.text("Usage: /game <start/ignore>").color(NamedTextColor.RED));
                        break;
                    }
                }

        }
    }

    @Override
    public @NotNull Collection<String> suggest(@NotNull CommandSourceStack commandSourceStack, @NotNull String[] args) {
        if (args.length == 0) {
            Collection<String> suggestions = new ArrayList<>();
            suggestions.add("start");
            suggestions.add("ignore");
            return suggestions;
        }
        return BasicCommand.super.suggest(commandSourceStack, args);
    }
}
