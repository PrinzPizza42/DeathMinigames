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
import org.example.DeathMinigames.deathMinigames.Config;
import org.example.DeathMinigames.deathMinigames.Introduction;
import org.example.DeathMinigames.deathMinigames.Main;
import org.example.DeathMinigames.listeners.RespawnListener;
import org.example.DeathMinigames.minigames.Difficulty;
import org.example.DeathMinigames.minigames.Minigame;
import org.example.DeathMinigames.settings.MainMenu;
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
        Difficulty difficulty = new Difficulty();
        Minigame minigame = new Minigame();
        Main main = new Main();
        RespawnListener respawnListener = new RespawnListener();
        Introduction introduction = new Introduction();
        MainMenu mainMenu = new MainMenu();
        Config config = new Config();

        Player player_2 = (Player) stack.getSender();
        if (args.length == 1) {
            switch (args[0].toLowerCase()) {
                case "settings":
                    if(player_2.isOp()) {
                        mainMenu.showPlayerSettings(player_2);
                    }
                    else {
                        player_2.sendMessage(Component.text("Du bist nicht dazu berechtigt").color(NamedTextColor.RED));
                    }
                    break;
                case "lowerdifficulty":
                    if(difficulty.checkIfPlayerCanPay(player_2)) {
                        if(config.checkConfigInt(player_2, "Difficulty") > 0) {
                            difficulty.playerPay(player_2);
                            difficulty.lowerDifficulty(player_2);
                            minigame.playSoundAtLocation(player_2.getEyeLocation(), 0.5F, Sound.ENTITY_ENDER_EYE_DEATH);
                            player_2.sendMessage(Component.text("Deine Schwierigkeit wurde um 1 auf ").color(NamedTextColor.GOLD)
                                    .append(Component.text(config.checkConfigInt(player_2, "Difficulty")).color(NamedTextColor.RED)));
                        }
                        else {
                            player_2.sendMessage(Component.text("Deine Schwierigkeit ist schon bei ").color(NamedTextColor.GOLD)
                                    .append(Component.text(config.checkConfigInt(player_2, "Difficulty")).color(NamedTextColor.RED)));
                        }
                    }
                    else {
                        player_2.sendMessage(Component.text("Du bist zu pleite um das zu bezahlen").color(NamedTextColor.RED));
                    }
                    break;
                case "introplayerdecidestousefeatures":
                    if (!introduction.checkIfPlayerGotIntroduced(player_2)) {
                        config.setIntroduction(player_2, true);
                        config.setUsesPlugin(player_2, true);
                        introduction.introEnd(player_2);
                        main.minigameStart(player_2);
                        player_2.sendMessage(Component.text("Du hast dich entschieden.").color(NamedTextColor.GOLD));
                    } else {
                        player_2.sendMessage(Component.text("Du hast dich bereit entschieden.").color(NamedTextColor.RED));
                    }
                    break;
                case "introplayerdecidestonotusefeatures":
                    if (!config.checkConfigBoolean(player_2, "Introduction")) {
                        config.setIntroduction(player_2, true);
                        config.setUsesPlugin(player_2, false);
                        introduction.introEnd(player_2);
                        introduction.dropInv(player_2);
                        player_2.sendMessage(Component.text("Du hast dich entschieden.").color(NamedTextColor.GOLD));
                    } else {
                        player_2.sendMessage(Component.text("Du hast dich bereit entschieden.").color(NamedTextColor.RED));
                    }
                    break;
                case "setnotintroduced":
                    config.setIntroduction(player_2, false);
                case "difficulty":
                    player_2.sendMessage(Component.text("Deine Schwierigkeit ist gerade bei ").color(NamedTextColor.GOLD)
                            .append(Component.text(config.checkConfigInt(player_2, "Difficulty")).color(NamedTextColor.RED)));
                    break;
            }
            if (inventories.containsKey(player_2.getUniqueId()) && !waitingListMinigame.contains(player_2) && playerInArena != player_2) {
                switch (args[0].toLowerCase()) {
                    case "start":
                        if(config.checkConfigBoolean(player_2, "UsesPlugin")) {
                            minigame.playSoundAtLocation(player_2.getEyeLocation(), 0.5F, Sound.ENTITY_ENDER_EYE_DEATH);
                            player_2.resetTitle();
                            player_2.sendActionBar(Component.text("Starte Minispiel...")
                                    .color(NamedTextColor.GOLD)
                                    .decoration(TextDecoration.ITALIC, true));
                            Location loc = new Location(player_2.getWorld(), 93, 73, 73);
                            player_2.playSound(player_2.getEyeLocation(), Sound.BLOCK_PORTAL_TRAVEL, 0.5F, 1.0F);
                            player_2.teleport(loc);
                            waitingListMinigame.addLast(player_2);
                        }
                        respawnListener.setPlayerDecided(true);
                        main.minigameStart(player_2);
                        break;
                    case "ignore":
                        minigame.playSoundToPlayer(player_2, 0.5F, Sound.ENTITY_ITEM_BREAK);
                        respawnListener.setPlayerDecided(true);
                        player_2.resetTitle();
                        if (!waitingListMinigame.contains(player_2) && inventories.containsKey(player_2.getUniqueId())) {
                            player_2.sendMessage(Component.text("Dein Inventar wird an deinen Todesort (").color(NamedTextColor.GOLD)
                                    .append(Component.text("X: " + deaths.get(player_2.getUniqueId()).getBlockX() + " Y: " + deaths.get(player_2.getUniqueId()).getBlockY() + " Z: " + deaths.get(player_2.getUniqueId()).getBlockZ()).color(NamedTextColor.RED))
                                    .append(Component.text(") gedroppt")).color(NamedTextColor.GOLD));
                            for (int i = 0; i < inventories.get(player_2.getUniqueId()).getSize(); i++) {
                                if (inventories.get(player_2.getUniqueId()).getItem(i) == null) continue;
                                player_2.getWorld().dropItem(deaths.get(player_2.getUniqueId()), inventories.get(player_2.getUniqueId()).getItem(i));
                            }
                            inventories.remove(player_2.getUniqueId());
                        }
                        break;
                    default:
                        if(!introduction.checkIfPlayerGotIntroduced(player_2)) {
                            player_2.sendMessage(Component.text("Usage: /game <start/ignore/difficulty>").color(NamedTextColor.RED));
                        }
                        break;
                }
            }
        }
        else if (args.length == 2) {
            if(player_2.isOp()) {
                switch (args[0]) {
                    case "difficulty":
                        if(args[1] != null) {
                            int i = Integer.parseInt(args[1]);
                            config.setDifficulty(player_2, i);
                            player_2.sendMessage(Component.text("Deine Schwierigkeit wurde auf ").color(NamedTextColor.GOLD)
                                    .append(Component.text(config.checkConfigInt(player_2, "Difficulty")).color(NamedTextColor.RED))
                                    .append(Component.text(" gesetzt")).color(NamedTextColor.GOLD));
                        }
                        else {
                            player_2.sendMessage(Component.text("Um die Schwierigkeit zu ändern muss eine Zahl eingegeben werden"));
                        }
                        break;
                    case "introPlayerDecidesToUseFeatures":
                        config.setUsesPlugin(player_2, true);
                        break;
                    case "introPlayerDecidesToNotUseFeatures":
                        config.setUsesPlugin(player_2, false);
                        break;
                }
            }
            else {
                player_2.sendMessage(Component.text("Du hast nicht das Recht dazu").color(NamedTextColor.RED));
            }
        }
        else if (args.length == 3) {
            if(player_2.isOp()) {
                switch (args[0]) {
                    case "difficulty":
                        if(args[2] != null) {
                            int i = Integer.parseInt(args[2]);
                            Player player1 = Bukkit.getPlayer(args[1]);
                            assert player1 != null;
                            config.setDifficulty(player1, i);
                            player_2.sendMessage(Component.text("Die Schwierigkeit von ").color(NamedTextColor.GOLD)
                                    .append(Component.text(args[1])).color(NamedTextColor.RED)
                                    .append(Component.text(" wurde auf ")).color(NamedTextColor.GOLD)
                                    .append(Component.text(config.checkConfigInt(player_2, "Difficulty")).color(NamedTextColor.RED))
                                    .append(Component.text(" gesetzt")).color(NamedTextColor.GOLD));
                        }
                        else {
                            player_2.sendMessage(Component.text("Um die Schwierigkeit zu ändern muss eine Zahl eingegeben werden"));
                        }
                        break;
                    case "introPlayerDecidesToUseFeatures":
                        Player player2 = Bukkit.getPlayer(args[1]);
                        if(player2 != null) {
                            config.setUsesPlugin(player2, true);
                            break;
                        }
                        else {
                            player_2.sendMessage(Component.text("Du hast keinen bekannten Spieler eingegeben"));
                        }
                    case "introPlayerDecidesToNotUseFeatures":
                        Player player3 = Bukkit.getPlayer(args[1]);
                        if(player3 != null) {
                            config.setUsesPlugin(player3, false);
                            break;
                        }
                        else {
                            player_2.sendMessage(Component.text("Du hast keinen bekannten Spieler eingegeben"));
                        }
                        break;
                }
            }
            else {
                player_2.sendMessage(Component.text("Du hast nicht das Recht dazu").color(NamedTextColor.RED));
            }
        }
    }

    @Override
    public @NotNull Collection<String> suggest(@NotNull CommandSourceStack commandSourceStack, @NotNull String[] args) {
        if (args.length == 0) {
            Collection<String> suggestions = new ArrayList<>();
            suggestions.add("difficulty");
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
            }
        }
        return BasicCommand.super.suggest(commandSourceStack, args);
    }
}
