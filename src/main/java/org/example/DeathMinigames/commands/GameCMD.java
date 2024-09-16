package org.example.DeathMinigames.commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.DeathMinigames.deathMinigames.Main;
import org.example.DeathMinigames.listeners.RespawnListener;
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
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("difficulty")) {
                player.sendMessage(Component.text("Deine Schwierigkeit ist gerade bei ").color(NamedTextColor.GOLD)
                        .append(Component.text(Difficulty.getDifficulty(player)).color(NamedTextColor.RED)));
            }
            else {
                if(args[0].equalsIgnoreCase("lowerDifficulty")) {
                    if(Difficulty.checkIfPlayerCanPay(player)) {
                        if(Difficulty.getDifficulty(player) > 0) {
                            Difficulty.PlayerPay(player);
                            Difficulty.lowerDifficulty(player);
                            player.sendMessage(Component.text("Deine Schwierigkeit wurde um 1 auf ").color(NamedTextColor.GOLD)
                                    .append(Component.text(Difficulty.getDifficulty(player)).color(NamedTextColor.RED)));
                        }
                        else {
                            player.sendMessage(Component.text("Deine Schwierigkeit ist schon bei ").color(NamedTextColor.GOLD)
                                .append(Component.text(Difficulty.getDifficulty(player)).color(NamedTextColor.RED)));
                        }
                    }
                    else {
                        player.sendMessage(Component.text("Du bist zu pleite um das zu bezahlen").color(NamedTextColor.RED));
                    }
                }
                else {
                    if (inventories.containsKey(player.getUniqueId())) {
                        switch (args[0].toLowerCase()) {
                            case "start":
                                RespawnListener.setPlayerDecided(true);
                                player.resetTitle();
                                player.sendActionBar(Component.text("Starte Minispiel...")
                                        .color(NamedTextColor.GOLD)
                                        .decoration(TextDecoration.ITALIC, true));
                                Location loc = new Location(player.getWorld(), 93, 73, 73);
                                player.playSound(player.getEyeLocation(), Sound.BLOCK_PORTAL_TRAVEL, 0.5F, 1.0F);
                                player.teleport(loc);
                                waitingListMinigame.addLast(player);
                                Main.minigameStart(waitingListMinigame.getFirst());
                                break;
                            case "ignore":
                                RespawnListener.setPlayerDecided(true);
                                player.resetTitle();
                                if (!waitingListMinigame.contains(player) && inventories.containsKey(player.getUniqueId())) {
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
                            default:
                                player.sendMessage(Component.text("Usage: /game <start/ignore/difficulty>").color(NamedTextColor.RED));
                                break;
                        }
                    }
                }
            }
        }
        else if (args.length == 2) {
            if(player.isOp()) {
                int i = Integer.parseInt(args[1]);
                Difficulty.setDifficulty(player, i);
                player.sendMessage(Component.text("Deine Schwierigkeit wurde auf ").color(NamedTextColor.GOLD)
                        .append(Component.text(Difficulty.getDifficulty(player)).color(NamedTextColor.RED))
                        .append(Component.text(" gesetzt")).color(NamedTextColor.GOLD));
            }
            else {
                player.sendMessage(Component.text("Du hast nicht das Recht dazu").color(NamedTextColor.RED));
            }
        }
        else if (args.length == 3) {
            if(player.isOp()) {
                int i = Integer.parseInt(args[2]);
                Difficulty.setDifficulty(player, i);
                player.sendMessage(Component.text("Die Schwierigkeit von ").color(NamedTextColor.GOLD)
                        .append(Component.text(args[1])).color(NamedTextColor.RED)
                        .append(Component.text(" wurde auf ")).color(NamedTextColor.GOLD)
                        .append(Component.text(Difficulty.getDifficulty(player)).color(NamedTextColor.RED))
                        .append(Component.text(" gesetzt")).color(NamedTextColor.GOLD));
            }
            else {
                player.sendMessage(Component.text("Du hast nicht das Recht dazu").color(NamedTextColor.RED));
            }
        }
    }

    @Override
    public @NotNull Collection<String> suggest(@NotNull CommandSourceStack commandSourceStack, @NotNull String[] args) {
        if (args.length == 0) {
            Collection<String> suggestions = new ArrayList<>();
            suggestions.add("start");
            suggestions.add("ignore");
            suggestions.add("difficulty");
            return suggestions;
        } else if (args.length == 2 && args[0].equalsIgnoreCase("difficulty")) {
            Collection<String> suggestions2 = new ArrayList<>();
            for(Player on : Bukkit.getOnlinePlayers()) {
                suggestions2.add(on.getName());
                return suggestions2;
            }
        }
        return BasicCommand.super.suggest(commandSourceStack, args);
    }
}
