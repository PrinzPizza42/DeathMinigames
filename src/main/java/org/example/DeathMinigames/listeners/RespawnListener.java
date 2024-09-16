package org.example.DeathMinigames.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.example.DeathMinigames.deathMinigames.Main;

import static org.example.DeathMinigames.listeners.DeathListener.*;

public class RespawnListener implements Listener {

    private static boolean playerDecided = false;
    private static int i = 10;

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

    private static void decidedTimer(Player player) {
        new BukkitRunnable() {
            public void run() {
                if(i > 0) {
                    if(!playerDecided) {
                        player.sendTitle("§6Entscheide dich im Chat", "§6Du hast noch §c" + i + "§6 Sekunden zeit", 0, 25, 10);
                        i--;
                    }
                    else {
                        i = 10;
                        RespawnListener.playerDecided = false;
                        Main.getPlugin().getLogger().info("runnabel cancelled");
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
                    i = 10;
                    Main.getPlugin().getLogger().info("runnabel cancelled");
                    cancel();
                }
            }
        }.runTaskTimer(Main.getPlugin(), 0, 20);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        if (inventories.containsKey(event.getPlayer().getUniqueId())) {
            Main.getPlugin().getLogger().info("inventories contains player");
            Player player = event.getPlayer();
            player.getInventory().clear();

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

            event.getPlayer().spigot().sendMessage(startMinigame, middlePart,ignoreMinigame);
            decidedTimer(player);
        }

    }
}
