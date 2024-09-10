package org.example.DeathMinigames.listeners;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import static org.example.DeathMinigames.listeners.DeathListener.inventories;

public class RespawnListener implements Listener {

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        if (inventories.get(event.getPlayer().getUniqueId()) != null) {
            Player player = event.getPlayer();
            player.getInventory().clear();

            TextComponent startMinigame = new TextComponent("um deine Items spielen");
            TextComponent ignoreMinigame = new TextComponent("nicht um deine Items spielen");
            TextComponent middlePart = new TextComponent(" / ");

            player.sendMessage("Entscheide dich, möchtest du ein Minispiel um deine Items spielen oder deine Items an deiner Todesstelle gedroppt bekommen?");

            startMinigame.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/game start"));
            startMinigame.setColor(net.md_5.bungee.api.ChatColor.GREEN);
            startMinigame.setItalic(true);
            startMinigame.setUnderlined(true);
            ignoreMinigame.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/game ignore"));
            ignoreMinigame.setColor(net.md_5.bungee.api.ChatColor.RED);
            ignoreMinigame.setItalic(true);
            ignoreMinigame.setUnderlined(true);

            event.getPlayer().spigot().sendMessage(startMinigame, middlePart,ignoreMinigame);
        }

    }
}
