package org.example.DeathMinigames.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.example.DeathMinigames.deathMinigames.Main;
import org.example.DeathMinigames.settings.MainMenu;

import java.util.ArrayList;

public class InventoryListener implements Listener {
    private ArrayList<Player> whitelistedPlayers = new ArrayList<>();
    private Player playerClicked;

    @EventHandler
    public void onSettingsClick(InventoryClickEvent event) {
        Main main = new Main();
        MainMenu mainMenu = new MainMenu();

        int i = event.getSlot();
        Player player = (Player) event.getWhoClicked();
        if(event.getView().getTitle().equals("Settings")) {
            event.setCancelled(true);
            switch(i) {
                case 0:
                    mainMenu.openSubMenu(player, 0);

                    break;
                case 1:
                    mainMenu.openSubMenu(player, 1);
                    break;
                case 2:
                    mainMenu.openSubMenu(player, 2);
                    break;
            }
        }
        else if(event.getView().getTitle().equals("Introduction")) {
            event.setCancelled(true);
            if(i == 44) {
                mainMenu.showPlayerSettings(player);
            }
            else {
                playerClicked = getPlayerFromWhitelistFromSpecificInt(i);
                if(main.checkConfigBoolean(playerClicked, "Introduction")) {
                    main.setIntroduction(playerClicked, false);
                } else if (!main.checkConfigBoolean(playerClicked, "Introduction")) {
                    main.setIntroduction(playerClicked, true);
                }
                player.sendMessage(Component.text("Changed Introduction of " + playerClicked.getName() + " to " + main.checkConfigBoolean(playerClicked, "Introduction")).color(NamedTextColor.RED));
            }
        }
        else if(event.getView().getTitle().equals("Difficulty")) {
            event.setCancelled(true);
            if(i == 44) {
                mainMenu.showPlayerSettings(player);
            }
            else {
                mainMenu.openSubSubMenu(player, getPlayerNameFromWhitelistFromSpecificInt(i), "Difficulty - PlayerSettings");
                playerClicked = getPlayerFromWhitelistFromSpecificInt(i);
            }
        }
        else if(event.getView().getTitle().equals("uses Plugin")) {
            event.setCancelled(true);
            if(i == 44) {
                mainMenu.showPlayerSettings(player);
            }
            else {
                playerClicked = getPlayerFromWhitelistFromSpecificInt(i);
                if(main.checkConfigBoolean(playerClicked, "UsesPlugin")) {
                    main.setUsesPlugin(playerClicked, false);
                } else if (!main.checkConfigBoolean(playerClicked, "UsesPlugin")) {
                    main.setUsesPlugin(playerClicked, true);
                }
                player.sendMessage(Component.text("Changed UsesPlugin of " + playerClicked.getName() + " to " + main.checkConfigBoolean(playerClicked, "UsesPlugin")).color(NamedTextColor.RED));
            }
        }
        else if(event.getView().getTitle().equals("Difficulty - PlayerSettings")) {
            event.setCancelled(true);
            if(i == 44) {
                mainMenu.showPlayerSettings(player);
            }
            else {
                main.setDifficulty(playerClicked, i);
                player.sendMessage(Component.text("Changed Difficulty of " + playerClicked.getName() + " to " + main.checkConfigBoolean(playerClicked, "Difficulty")).color(NamedTextColor.RED));
            }
        }
    }

    public String getPlayerNameFromWhitelistFromSpecificInt(int placeInWhitelist) {
        putWhitelistedPlayersInList();
        return getWhitelistedPlayersInArrayList().get(placeInWhitelist).getName();
    }

    public Player getPlayerFromWhitelistFromSpecificInt(int placeInWhitelist) {
        putWhitelistedPlayersInList();
        return getWhitelistedPlayersInArrayList().get(placeInWhitelist);
    }

    public void putWhitelistedPlayersInList() {
        for(OfflinePlayer player : Bukkit.getWhitelistedPlayers()) {
            whitelistedPlayers.add(player.getPlayer());
        }
    }

    public ArrayList<Player> getWhitelistedPlayersInArrayList() {
        return whitelistedPlayers;
    }
}
