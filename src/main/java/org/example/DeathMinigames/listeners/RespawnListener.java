package org.example.DeathMinigames.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.example.DeathMinigames.deathMinigames.Introduction;
import org.example.DeathMinigames.deathMinigames.Main;

import java.time.Duration;

import static org.example.DeathMinigames.listeners.DeathListener.*;

public class RespawnListener implements Listener {

    private static boolean playerDecided = false;
    private static int timeForPlayerToDecide = 10;

    public static void setPlayerDecided(boolean playerDecided) {
        RespawnListener.playerDecided = playerDecided;
    }

    private static void dropInv(Player player) {
        for(int i = 0; i < inventories.get(player.getUniqueId()).getSize(); i++) {
            if(inventories.get(player.getUniqueId()).getItem(i) == null) continue;
            assert inventories.get(player.getUniqueId()).getItem(i) != null;
            player.getWorld().dropItem(deaths.get(player.getUniqueId()), inventories.get(player.getUniqueId()).getItem(i));
        }
        deaths.remove(player.getUniqueId());
        inventories.remove(player.getUniqueId());
    }

    private static Inventory respawnMenu = Bukkit.createInventory(null, 9, Component.text("Respawn Menu", NamedTextColor.GOLD));;

    private static void createRespawnMenu() {
        respawnMenu.setItem(2, new ItemStack(Material.GREEN_WOOL));
        respawnMenu.setItem(6, new ItemStack(Material.RED_WOOL));
    }

    public static Inventory getRespawnMenu() {
        createRespawnMenu();
        Main.getPlugin().getLogger().info("Getting Respawn Menu");
        return respawnMenu;
    }

    private static void timerWhilePlayerDecides(Player player) {
        new BukkitRunnable() {
            public void run() {
                if(timeForPlayerToDecide > 0) {
                    if(!playerDecided) {
                        Title.Times times = Title.Times.times(Duration.ZERO, Duration.ofSeconds(1), Duration.ofMillis(500));
                        Title title = Title.title(Component.text("Entscheide dich im Chat").color(NamedTextColor.GOLD),
                                Component.text("Du hast noch ").color(NamedTextColor.GOLD)
                                .append(Component.text(timeForPlayerToDecide).color(NamedTextColor.RED))
                                .append(Component.text(" Sekunden zeit").color(NamedTextColor.GOLD)), times);
                        player.showTitle(title);
                        timeForPlayerToDecide--;
                    }
                    else {
                        timeForPlayerToDecide = 10;
                        RespawnListener.playerDecided = false;
                        Main.getPlugin().getLogger().info("runnable cancelled because player decided");
                        cancel();
                    }
                }
                else {
                    player.sendTitle("", "Dein Inventar wird an deinen Todesort gedroppt", 10, 40, 10);
                    player.sendMessage(Component.text("Todesort: ").color(NamedTextColor.GOLD)
                            .append(Component.text("X: " + deaths.get(player.getUniqueId()).getBlockX() + " ").color(NamedTextColor.RED))
                            .append(Component.text("Y: " + deaths.get(player.getUniqueId()).getBlockY() + " ").color(NamedTextColor.RED))
                            .append(Component.text("Z: " + deaths.get(player.getUniqueId()).getBlockZ()).color(NamedTextColor.RED)));
                    dropInv(player);
                    RespawnListener.playerDecided = false;
                    timeForPlayerToDecide = 10;
                    Main.getPlugin().getLogger().info("runnable cancelled because of waiting");
                    cancel();
                }
            }
        }.runTaskTimer(Main.getPlugin(), 0, 20);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        player.getInventory().clear();
        if (inventories.containsKey(event.getPlayer().getUniqueId()) && Introduction.checkIfPlayerUsesPlugin(event.getPlayer())) {
            Main.getPlugin().getLogger().info("inventories contains player");

            TextComponent startMinigame = new TextComponent("um deine Items spielen");
            TextComponent ignoreMinigame = new TextComponent("nicht um deine Items spielen");
            TextComponent middlePart = new TextComponent(" / ");

            player.sendMessage("§6Entscheide dich, möchtest du ein Minispiel um deine Items spielen oder deine Items an deiner Todesstelle gedroppt bekommen?");

            //TODO: replace deprecated code
            startMinigame.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/game start"));
            startMinigame.setColor(net.md_5.bungee.api.ChatColor.GREEN);
            startMinigame.setItalic(true);
            startMinigame.setUnderlined(true);
            ignoreMinigame.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/game ignore"));
            ignoreMinigame.setColor(net.md_5.bungee.api.ChatColor.RED);
            ignoreMinigame.setItalic(true);
            ignoreMinigame.setUnderlined(true);

            timerWhilePlayerDecides(player);



            event.getPlayer().spigot().sendMessage(startMinigame, middlePart, ignoreMinigame);
        }

    }
}
