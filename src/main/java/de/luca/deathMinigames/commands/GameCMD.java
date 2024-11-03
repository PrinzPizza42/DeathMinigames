package de.luca.deathMinigames.commands;

import de.j.stationofdoom.util.translations.TranslationFactory;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import de.luca.deathMinigames.deathMinigames.Config;
import de.luca.deathMinigames.deathMinigames.Introduction;
import de.luca.deathMinigames.deathMinigames.Main;
import de.luca.deathMinigames.listeners.RespawnListener;
import de.luca.deathMinigames.minigames.Difficulty;
import de.luca.deathMinigames.minigames.Minigame;
import de.luca.deathMinigames.settings.MainMenu;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Collection;

import static de.luca.deathMinigames.listeners.DeathListener.*;

public class GameCMD implements BasicCommand {

    @Override
    public boolean canUse(@NotNull CommandSender sender) {
        return sender instanceof Player;
    }

    @Override
    public void execute(CommandSourceStack stack, String[] args) {
        Difficulty difficulty = new Difficulty();
        Minigame minigame = new Minigame();
        RespawnListener respawnListener = new RespawnListener();
        Introduction introduction = new Introduction();
        MainMenu mainMenu = new MainMenu();
        Config config = new Config();
        TranslationFactory tf = new TranslationFactory();

        Player player = (Player) stack.getSender();
        if (args.length == 1) {
            switch (args[0].toLowerCase()) {
                case "settings":
                    if(player.isOp()) {
                        mainMenu.showPlayerSettings(player);
                    }
                    else {
                        player.sendMessage(Component.text(tf.getTranslation(player, "playerNotOP")).color(NamedTextColor.RED));
                    }
                    break;
                case "lowerdifficulty":
                    if(difficulty.checkIfPlayerCanPay(player)) {
                        if(config.checkConfigInt(player, "Difficulty") > 0) {
                            difficulty.playerPay(player);
                            difficulty.lowerDifficulty(player);
                            minigame.playSoundAtLocation(player.getEyeLocation(), 0.5F, Sound.ENTITY_ENDER_EYE_DEATH);
                            player.sendMessage(MiniMessage.miniMessage().deserialize(Component.text(tf.getTranslation(player, "changedDiff", config.checkConfigInt(player, "Difficulty"))).content()));
                        }
                        else {
                            player.sendMessage(Component.text(tf.getTranslation(player, "diffAlreadyAt")).color(NamedTextColor.GOLD)
                                    .append(Component.text(config.checkConfigInt(player, "Difficulty")).color(NamedTextColor.RED)));
                        }
                    }
                    else {
                        player.sendMessage(Component.text(tf.getTranslation(player, "notEnoughResources")).color(NamedTextColor.RED));
                    }
                    break;
                case "introplayerdecidestousefeatures":
                    if (!introduction.checkIfPlayerGotIntroduced(player)) {
                        config.setIntroduction(player, true);
                        config.setUsesPlugin(player, true);
                        introduction.introEnd(player);
                        Main.minigameStart(player);
                        player.sendMessage(Component.text(tf.getTranslation(player, "playerDecided")).color(NamedTextColor.GOLD));
                    }
                    else {
                        player.sendMessage(Component.text(tf.getTranslation(player, "playerAlreadyDecided")).color(NamedTextColor.RED));
                    }
                    break;
                case "introplayerdecidestonotusefeatures":
                    if (!config.checkConfigBoolean(player, "Introduction")) {
                        config.setIntroduction(player, true);
                        config.setUsesPlugin(player, false);
                        introduction.introEnd(player);
                        introduction.dropInv(player);
                        player.sendMessage(Component.text(tf.getTranslation(player, "playerDecided")).color(NamedTextColor.GOLD));
                    }
                    else {
                        player.sendMessage(Component.text(tf.getTranslation(player, "playerAlreadyDecided")).color(NamedTextColor.RED));
                    }
                    break;
                case "setnotintroduced":
                    config.setIntroduction(player, false);
                case "difficulty":
                    player.sendMessage(Component.text(tf.getTranslation(player, "diffAt")).color(NamedTextColor.GOLD)
                            .append(Component.text(config.checkConfigInt(player, "Difficulty")).color(NamedTextColor.RED)));
                    break;
                case "setwaitinglistposition":
                    Location location = player.getLocation();
                    config.setWaitingListPosition(location);
                    player.sendMessage(Component.text(tf.getTranslation(player, "setWaitingListPosition")).color(NamedTextColor.GREEN));
                    break;
                case "decidednottosetposition":
                    player.sendMessage(Component.text(tf.getTranslation(player, "decidedNotToSetPosition")).color(NamedTextColor.RED));
                    break;
            }
            if (inventories.containsKey(player.getUniqueId()) && !waitingListMinigame.contains(player) && playerInArena != player) {
                switch (args[0].toLowerCase()) {
                    case "start":
                        minigame.playSoundAtLocation(player.getEyeLocation(), 0.5F, Sound.ENTITY_ENDER_EYE_DEATH);
                        player.resetTitle();
                        player.sendActionBar(Component.text(tf.getTranslation(player, "startingMinigame"))
                                .color(NamedTextColor.GOLD)
                                .decoration(TextDecoration.ITALIC, true));
                        Location loc = new Location(player.getWorld(), 93, 73, 73);
                        player.playSound(player.getEyeLocation(), Sound.BLOCK_PORTAL_TRAVEL, 0.5F, 1.0F);
                        player.teleport(loc);
                        waitingListMinigame.addLast(player);
                        Main.getPlugin().getLogger().info("player does not use plugin but is trying to start ");
                        respawnListener.setPlayerDecided(true);
                        Main.minigameStart(player);
                        break;
                    case "ignore":
                        minigame.playSoundToPlayer(player, 0.5F, Sound.ENTITY_ITEM_BREAK);
                        respawnListener.setPlayerDecided(true);
                        player.resetTitle();
                        if (!waitingListMinigame.contains(player) && inventories.containsKey(player.getUniqueId())) {
                            player.sendMessage(Component.text(tf.getTranslation(player, "droppingInvAt")).color(NamedTextColor.GOLD)
                                    .append(Component.text("X: " + deaths.get(player.getUniqueId()).getBlockX() + " Y: " + deaths.get(player.getUniqueId()).getBlockY() + " Z: " + deaths.get(player.getUniqueId()).getBlockZ()).color(NamedTextColor.RED))
                                    .append(Component.text(")")).color(NamedTextColor.GOLD));
                            for (int i = 0; i < inventories.get(player.getUniqueId()).getSize(); i++) {
                                if (inventories.get(player.getUniqueId()).getItem(i) == null) continue;
                                player.getWorld().dropItem(deaths.get(player.getUniqueId()), inventories.get(player.getUniqueId()).getItem(i));
                            }
                            inventories.remove(player.getUniqueId());
                        }
                        break;
                    default:
                        if(!introduction.checkIfPlayerGotIntroduced(player)) {
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
                        if(args[1] != null) {
                            int i = Integer.parseInt(args[1]);
                            config.setDifficulty(player, i);
                            player.sendMessage(MiniMessage.miniMessage().deserialize(Component.text(tf.getTranslation(player, "setDiffTo", config.checkConfigInt(player, "Difficulty"))).content()));
                        }
                        else {
                            player.sendMessage(Component.text(tf.getTranslation(player, "youHaveToEnterANumber")));
                        }
                        break;
                    case "introPlayerDecidesToUseFeatures":
                        config.setUsesPlugin(player, true);
                        break;
                    case "introPlayerDecidesToNotUseFeatures":
                        config.setUsesPlugin(player, false);
                        break;
                }
            }
            else {
                player.sendMessage(Component.text(tf.getTranslation(player, "playerNotOP")).color(NamedTextColor.RED));
            }
        }
        else if (args.length == 3) {
            if(player.isOp()) {
                switch (args[0]) {
                    case "difficulty":
                        if(args[2] != null) {
                            int i = Integer.parseInt(args[2]);
                            Player player1 = Bukkit.getPlayer(args[1]);
                            assert player1 != null;
                            config.setDifficulty(player1, i);
                            player.sendMessage(MiniMessage.miniMessage().deserialize(Component.text(tf.getTranslation(player, "setDiffOfTo", player1, config.checkConfigInt(player1, "Difficulty"))).content()));
                        }
                        else {
                            player.sendMessage(Component.text(tf.getTranslation(player, "youHaveToEnterANumber")).color(NamedTextColor.RED));
                        }
                        break;
                    case "introPlayerDecidesToUseFeatures":
                        Player player2 = Bukkit.getPlayer(args[1]);
                        if(player2 != null) {
                            config.setUsesPlugin(player2, true);
                            break;
                        }
                        else {
                            player.sendMessage(Component.text());
                        }
                    case "introPlayerDecidesToNotUseFeatures":
                        Player player3 = Bukkit.getPlayer(args[1]);
                        if(player3 != null) {
                            config.setUsesPlugin(player3, false);
                            break;
                        }
                        else {
                            player.sendMessage(Component.text(tf.getTranslation(player, "didNotEnterKnownPlayer")));
                        }
                        break;
                }
            }
            else {
                player.sendMessage(Component.text(tf.getTranslation(player, "playerNotOP")).color(NamedTextColor.RED));
            }
        }
    }

    @Override
    public @NotNull Collection<String> suggest(@NotNull CommandSourceStack commandSourceStack, @NotNull String[] args) {
        if (args.length == 0) {
            Collection<String> suggestions = new ArrayList<>();
            suggestions.add("difficulty");
            suggestions.add("lowerDifficulty");
            return suggestions;
        }
        else if (args.length == 2) {
            if(args[1].equals("difficulty")) {
                Collection<String> suggestions2 = new ArrayList<>();
                for (Player on : Bukkit.getOnlinePlayers()) {
                    suggestions2.add(on.getName());
                    return suggestions2;
                }
            }
        }
        return BasicCommand.super.suggest(commandSourceStack, args);
    }
}
