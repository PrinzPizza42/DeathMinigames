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
import org.bukkit.inventory.Inventory;
import org.example.DeathMinigames.deathMinigames.Introduction;
import org.example.DeathMinigames.deathMinigames.Main;
import org.example.DeathMinigames.listeners.RespawnListener;
import org.example.DeathMinigames.minigames.Difficulty;
import org.example.DeathMinigames.minigames.Minigame;
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
            switch (args[0].toLowerCase()) {
                case "lowerdifficulty":
                    if(Difficulty.checkIfPlayerCanPay(player)) {
                        if(Difficulty.getDifficulty(player) > 0) {
                            Difficulty.PlayerPay(player);
                            Difficulty.lowerDifficulty(player);
                            Minigame.playSoundAtLocation(player.getEyeLocation(), 0.5F, Sound.ENTITY_ENDER_EYE_DEATH);
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
                    break;
                case "introplayerdecidestousefeatures":
                    if (!Introduction.checkIfPlayerGotIntroduced(player)) {
                        Introduction.setPlayerIntroduced(player);
                        Introduction.setPlayerPluginUsage(player, true);
                        Introduction.introEnd(player);
                        Main.minigameStart(player);
                        player.sendMessage(Component.text("Du hast dich entschieden.").color(NamedTextColor.GOLD));
                    } else {
                        player.sendMessage(Component.text("Du hast dich bereit entschieden.").color(NamedTextColor.RED));
                    }
                    break;
                case "introplayerdecidestonotusefeatures":
                    if (!Introduction.checkIfPlayerGotIntroduced(player)) {
                        Introduction.setPlayerIntroduced(player);
                        Introduction.setPlayerPluginUsage(player, false);
                        Introduction.introEnd(player);
                        Introduction.dropInv(player);
                        player.sendMessage(Component.text("Du hast dich entschieden.").color(NamedTextColor.GOLD));
                    } else {
                        player.sendMessage(Component.text("Du hast dich bereit entschieden.").color(NamedTextColor.RED));
                    }
                    break;
                case "setnotintroduced":
                    Introduction.setPlayerNotIntroduced(player);
                case "difficulty":
                    player.sendMessage(Component.text("Deine Schwierigkeit ist gerade bei ").color(NamedTextColor.GOLD)
                            .append(Component.text(Difficulty.getDifficulty(player)).color(NamedTextColor.RED)));
                    break;
            }
            if (inventories.containsKey(player.getUniqueId()) && !waitingListMinigame.contains(player.getUniqueId()) && playerInArena != player) {
                switch (args[0].toLowerCase()) {
                    case "start":
                        if(Introduction.checkIfPlayerUsesPlugin(player)) {
                            Minigame.playSoundAtLocation(player.getEyeLocation(), 0.5F, Sound.ENTITY_ENDER_EYE_DEATH);
                            player.resetTitle();
                            player.sendActionBar(Component.text("Starte Minispiel...")
                                    .color(NamedTextColor.GOLD)
                                    .decoration(TextDecoration.ITALIC, true));
                            Location loc = new Location(player.getWorld(), 93, 73, 73);
                            player.playSound(player.getEyeLocation(), Sound.BLOCK_PORTAL_TRAVEL, 0.5F, 1.0F);
                            player.teleport(loc);
                            waitingListMinigame.addLast(player);
                        }
                        RespawnListener.setPlayerDecided(true);
                        Main.minigameStart(player);
                        break;
                    case "ignore":
                        Minigame.playSoundToPlayer(player, 0.5F, Sound.ENTITY_ITEM_BREAK);
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
                        if(!Introduction.checkIfPlayerGotIntroduced(player)) {
                            player.sendMessage(Component.text("Usage: /game <start/ignore/difficulty>").color(NamedTextColor.RED));
                        }
                        break;
                }
            }
        }
        else if (args.length == 2) {
            if(player.isOp()) {
                switch (args[0]) {
                    case "difficulty":
                        int i = Integer.parseInt(args[1]);
                        Difficulty.setDifficulty(player, i);
                        player.sendMessage(Component.text("Deine Schwierigkeit wurde auf ").color(NamedTextColor.GOLD)
                                .append(Component.text(Difficulty.getDifficulty(player)).color(NamedTextColor.RED))
                                .append(Component.text(" gesetzt")).color(NamedTextColor.GOLD));
                    case "introPlayerDecidesToUseFeatures":
                        Introduction.setPlayerPluginUsage(player, true);
                    case "introPlayerDecidesToNotUseFeatures":
                        Introduction.setPlayerPluginUsage(player, false);
                }
            }
            else {
                player.sendMessage(Component.text("Du hast nicht das Recht dazu").color(NamedTextColor.RED));
            }
        }
        else if (args.length == 3) {
            if(player.isOp()) {
                switch (args[0]) {
                    case "difficulty":
                        int i = Integer.parseInt(args[2]);
                        Player player1 = Bukkit.getPlayer(args[1]);
                        Difficulty.setDifficulty(player1, i);
                        player.sendMessage(Component.text("Die Schwierigkeit von ").color(NamedTextColor.GOLD)
                                .append(Component.text(args[1])).color(NamedTextColor.RED)
                                .append(Component.text(" wurde auf ")).color(NamedTextColor.GOLD)
                                .append(Component.text(Difficulty.getDifficulty(player)).color(NamedTextColor.RED))
                                .append(Component.text(" gesetzt")).color(NamedTextColor.GOLD));
                        break;
                    case "introPlayerDecidesToUseFeatures":
                        Player player2 = Bukkit.getPlayer(args[1]);
                        Introduction.setPlayerPluginUsage(player2, true);
                        break;
                    case "introPlayerDecidesToNotUseFeatures":
                        Player player3 = Bukkit.getPlayer(args[1]);
                        Introduction.setPlayerPluginUsage(player3, false);
                        break;
                }
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
            suggestions.add("introPlayerDecidesToUseFeatures");
            suggestions.add("introPlayerDecidesToNotUseFeatures");
            return suggestions;
        } else if (args.length == 2) {
            switch (args[0]) {
                case "difficulty":
                    Collection<String> suggestions2 = new ArrayList<>();
                    for(Player on : Bukkit.getOnlinePlayers()) {
                        suggestions2.add(on.getName());
                        return suggestions2;
                    }
                    break;
                case "introPlayerDecidesToUseFeatures":
                    Collection<String> suggestions3 = new ArrayList<>();
                    for(Player on : Bukkit.getOnlinePlayers()) {
                        suggestions3.add(on.getName());
                        return suggestions3;
                    }
                    break;
                case "introPlayerDecidesToNotUseFeatures":
                    Collection<String> suggestions4 = new ArrayList<>();
                    for(Player on : Bukkit.getOnlinePlayers()) {
                        suggestions4.add(on.getName());
                        return suggestions4;
                    }
                    break;
            }
        }
        return BasicCommand.super.suggest(commandSourceStack, args);
    }
}
