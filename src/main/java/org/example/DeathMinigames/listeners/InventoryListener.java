package org.example.DeathMinigames.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.example.DeathMinigames.deathMinigames.Config;
import org.example.DeathMinigames.deathMinigames.Main;
import org.example.DeathMinigames.settings.GUI;
import org.example.DeathMinigames.settings.MainMenu;

import java.util.ArrayList;

public class InventoryListener implements Listener {
    private final ArrayList<Player> allPlayers = new ArrayList<>();
    private Player playerClicked;

    @EventHandler
    public void onSettingsClick(InventoryClickEvent event) {
        Config config = new Config();
        MainMenu mainMenu = new MainMenu();
        InventoryHolder invHolder = event.getInventory().getHolder();
        Main main = new Main();

        int ID;
        int slot = event.getSlot();
        Player player = (Player) event.getWhoClicked();
        if(invHolder instanceof MainMenu) {
            event.setCancelled(true);
            switch (slot) {
                case 0:
                    fillSetUpInventory();
                    MainMenu.setUp.showInventory(player);
                    break;
                case 1:
                    reloadInventory("Introduction", mainMenu);
                    MainMenu.introduction.showInventory(player);
                    break;
                case 2:
                    reloadInventory("UsesPlugin", mainMenu);
                    MainMenu.usesPlugin.showInventory(player);
                    break;
                case 3:
                    MainMenu.difficulty.showInventory(player);
                    break;
            }
        }
        else if(invHolder instanceof GUI) {
            GUI gui = (GUI) invHolder;
            ID = gui.getID();
            if(ID == MainMenu.introduction.getID()) {
                // Introduction
                event.setCancelled(true);
                if(slot == 44) {
                    mainMenu.showPlayerSettings(player);
                }
                else if (slot <= Config.knownPlayers.size()) {
                    playerClicked = getPlayerFromListFromSpecificInt(slot);
                    if(config.checkConfigBoolean(playerClicked, "Introduction")) {
                        config.setIntroduction(playerClicked, false);
                    } else if (!config.checkConfigBoolean(playerClicked, "Introduction")) {
                        config.setIntroduction(playerClicked, true);
                    }
                    reloadInventory("Introduction", slot, mainMenu);
                    player.sendMessage(Component.text("Changed Introduction of " + playerClicked.getName() + " to " + config.checkConfigBoolean(playerClicked, "Introduction")).color(NamedTextColor.RED));
                }
            } else if (ID == MainMenu.usesPlugin.getID()) {
                // UsesPlugin
                event.setCancelled(true);
                if(slot == 44) {
                    mainMenu.showPlayerSettings(player);
                }
                else if (slot <= Config.knownPlayers.size()) {
                    playerClicked = getPlayerFromListFromSpecificInt(slot);
                    if(config.checkConfigBoolean(playerClicked, "UsesPlugin")) {
                        config.setUsesPlugin(playerClicked, false);
                    } else if (!config.checkConfigBoolean(playerClicked, "UsesPlugin")) {
                        config.setUsesPlugin(playerClicked, true);
                    }
                    reloadInventory("UsesPlugin", slot, mainMenu);
                    player.sendMessage(Component.text("Changed UsesPlugin of " + playerClicked.getName() + " to " + config.checkConfigBoolean(playerClicked, "UsesPlugin")).color(NamedTextColor.RED));
                }
            }
            else if (ID == MainMenu.difficulty.getID()) {
                // Difficulty
                event.setCancelled(true);
                if (slot == 44) {
                    mainMenu.showPlayerSettings(player);
                } else if (slot <= Config.knownPlayers.size()) {
                    playerClicked = getPlayerFromListFromSpecificInt(slot);

                    main.getPlugin().getLogger().info(playerClicked.getName());
                    reloadInventory("Difficulty - Settings", slot, mainMenu);
                    MainMenu.difficultyPlayerSettings.showInventory(player);
                    main.getPlugin().getLogger().info(playerClicked.getName());
                }
            }
            else if(ID == MainMenu.difficultyPlayerSettings.getID()) {
                event.setCancelled(true);
                if(slot == 44) {
                    mainMenu.showPlayerSettings(player);
                }
                else if (slot < 11){
                    config.setDifficulty(playerClicked, slot);
                    reloadInventory("Difficulty - Settings", slot, mainMenu);
                    player.sendMessage(Component.text("Changed Difficulty of " + playerClicked.getName() + " to " + config.checkConfigInt(playerClicked, "Difficulty")).color(NamedTextColor.RED));
                }
            }
            else if(ID == MainMenu.setUp.getID()) {
                event.setCancelled(true);
                if(slot == 44) {
                    mainMenu.showPlayerSettings(player);
                }
                else if (slot <= 3){
                    MainMenu.setUp.showInventory(player);
                }
            }
        }
    }

    public Player getPlayerFromListFromSpecificInt(int placeInList) {
        return Bukkit.getPlayer(Config.knownPlayers.get(placeInList));
    }

    private void fillSetUpInventory() {
        MainMenu.setUp.addClickableItemStack("Parcour Start Height", Material.GREEN_CONCRETE, 1, 0);
        MainMenu.setUp.addClickableItemStack("Parcour End Height", Material.GOLD_BLOCK, 1, 1);
        MainMenu.setUp.addClickableItemStack("cost to lower the difficulty", Material.DIAMOND, 1, 2);
        MainMenu.setUp.addClickableItemStack("time to decide when respawning", Material.DIAMOND, 1, 3);
    }

    public void reloadInventory(String inventory, int slot, MainMenu mainMenu) {
        Config config = new Config();
        switch (inventory) {
            case "Introduction":
                if(config.checkConfigBoolean(playerClicked, "Introduction")) {
                    MainMenu.introduction.addClickableItemStack(playerClicked.getName(), Material.GREEN_CONCRETE_POWDER, 1, slot);
                }
                else {
                    MainMenu.introduction.addClickableItemStack(playerClicked.getName(), Material.RED_CONCRETE_POWDER, 1, slot);
                }
                break;
            case "UsesPlugin":
                if(config.checkConfigBoolean(playerClicked, "UsesPlugin")) {
                    MainMenu.usesPlugin.addClickableItemStack(playerClicked.getName(), Material.GREEN_CONCRETE_POWDER, 1, slot);
                }
                else {
                    MainMenu.usesPlugin.addClickableItemStack(playerClicked.getName(), Material.RED_CONCRETE_POWDER, 1, slot);
                }
                break;
            case "Difficulty - Settings":
                int difficulty = config.checkConfigInt(playerClicked, "Difficulty");
                mainMenu.difficultySettingsSetInventoryContents(difficulty);
        }
    }
    public void reloadInventory(String inventory, MainMenu mainMenu) {
        Config config = new Config();
        switch (inventory) {
            case "Introduction":
                for(int i = 0; i < Config.knownPlayers.size(); i++) {
                    Material material;
                    if(config.checkConfigBoolean(getPlayerFromListFromSpecificInt(i), "Introduction")) {
                        material = Material.GREEN_CONCRETE_POWDER;
                    }
                    else {
                        material = Material.RED_CONCRETE_POWDER;
                    }
                    MainMenu.introduction.addClickableItemStack(getPlayerFromListFromSpecificInt(i).getName(), material, 1, i);
                }
                break;
            case "UsesPlugin":
                for(int i = 0; i < Config.knownPlayers.size(); i++) {
                    Material material;
                    if(config.checkConfigBoolean(getPlayerFromListFromSpecificInt(i), "UsesPlugin")) {
                        material = Material.GREEN_CONCRETE_POWDER;
                    }
                    else {
                        material = Material.RED_CONCRETE_POWDER;
                    }
                    MainMenu.usesPlugin.addClickableItemStack(getPlayerFromListFromSpecificInt(i).getName(), material, 1, i);
                }
                break;
            case "Difficulty - Settings":
                int difficulty = config.checkConfigInt(playerClicked, "Difficulty");
                mainMenu.difficultySettingsSetInventoryContents(difficulty);
        }
    }

}
