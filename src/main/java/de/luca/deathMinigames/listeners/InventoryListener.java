package de.luca.deathMinigames.listeners;

import de.j.stationofdoom.util.translations.TranslationFactory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import de.luca.deathMinigames.deathMinigames.Config;
import de.luca.deathMinigames.deathMinigames.Main;
import de.luca.deathMinigames.minigames.Minigame;
import de.luca.deathMinigames.settings.GUI;
import de.luca.deathMinigames.settings.MainMenu;

import java.util.ArrayList;

public class InventoryListener implements Listener {
    private final ArrayList<Player> allPlayers = new ArrayList<>();
    private Player playerClicked;

    @EventHandler
    public void onSettingsClick(InventoryClickEvent event) {
        Config config = new Config();
        MainMenu mainMenu = new MainMenu();
        InventoryHolder invHolder = event.getInventory().getHolder();
        Minigame minigame = new Minigame();

        int ID;
        int slot = event.getSlot();
        Player player = (Player) event.getWhoClicked();
        if(invHolder instanceof MainMenu) {
            event.setCancelled(true);
            switch (slot) {
                case 0:
                    reloadInventory("SetUp", mainMenu);
                    MainMenu.setUp.addBackButton(player);
                    MainMenu.setUp.showInventory(player);
                    break;
                case 1:
                    reloadInventory("Introduction", mainMenu);
                    MainMenu.introduction.addBackButton(player);
                    MainMenu.introduction.showInventory(player);
                    break;
                case 2:
                    reloadInventory("UsesPlugin", mainMenu);
                    MainMenu.usesPlugin.addBackButton(player);
                    MainMenu.usesPlugin.showInventory(player);
                    break;
                case 3:
                    MainMenu.difficulty.addBackButton(player);
                    MainMenu.difficulty.showInventory(player);
                    break;
            }
        }
        else if(invHolder instanceof GUI) {
            GUI gui = (GUI) invHolder;
            ID = gui.getID();
            if(ID == MainMenu.introduction.getID()) {
                event.setCancelled(true);
                if(slot == 53) {
                    mainMenu.showPlayerSettings(player);
                }
                else if (slot <= Config.knownPlayers.size()) {
                    playerClicked = getPlayerFromListFromSpecificInt(slot);
                    if(config.checkConfigBoolean(playerClicked, "Introduction")) {
                        minigame.playSoundToPlayer(player, 0.5F, Sound.ENTITY_ITEM_BREAK);
                        config.setIntroduction(playerClicked, false);
                    } else if (!config.checkConfigBoolean(playerClicked, "Introduction")) {
                        minigame.playSoundToPlayer(player, 0.5F, Sound.BLOCK_ANVIL_USE);
                        config.setIntroduction(playerClicked, true);
                    }
                    reloadInventory("Introduction", slot, mainMenu);
                    player.sendMessage(Component.text("Changed Introduction of " + playerClicked.getName() + " to " + config.checkConfigBoolean(playerClicked, "Introduction")).color(NamedTextColor.RED));
                }
            }
            else if (ID == MainMenu.usesPlugin.getID()) {
                event.setCancelled(true);
                if(slot == 53) {
                    mainMenu.showPlayerSettings(player);
                }
                else if (slot <= Config.knownPlayers.size()) {
                    playerClicked = getPlayerFromListFromSpecificInt(slot);
                    if(config.checkConfigBoolean(playerClicked, "UsesPlugin")) {
                        minigame.playSoundToPlayer(player, 0.5F, Sound.ENTITY_ITEM_BREAK);
                        config.setUsesPlugin(playerClicked, false);
                    } else if (!config.checkConfigBoolean(playerClicked, "UsesPlugin")) {
                        minigame.playSoundToPlayer(player, 0.5F, Sound.BLOCK_ANVIL_USE);
                        config.setUsesPlugin(playerClicked, true);
                    }
                    reloadInventory("UsesPlugin", slot, mainMenu);
                    player.sendMessage(Component.text("Changed UsesPlugin of " + playerClicked.getName() + " to " + config.checkConfigBoolean(playerClicked, "UsesPlugin")).color(NamedTextColor.RED));
                }
            }
            else if (ID == MainMenu.difficulty.getID()) {
                event.setCancelled(true);
                if (slot == 53) {
                    mainMenu.showPlayerSettings(player);
                } else if (slot <= Config.knownPlayers.size()) {
                    playerClicked = getPlayerFromListFromSpecificInt(slot);

                    Main.getPlugin().getLogger().info(playerClicked.getName());
                    reloadInventory("Difficulty - Settings", slot, mainMenu);
                    MainMenu.difficultyPlayerSettings.addBackButton(player);
                    MainMenu.difficultyPlayerSettings.showInventory(player);
                }
            }
            else if(ID == MainMenu.difficultyPlayerSettings.getID()) {
                event.setCancelled(true);
                if(slot == 53) {
                    mainMenu.showPlayerSettings(player);
                }
                else if (slot < 11){
                    minigame.playSoundToPlayer(player, 0.5F, Sound.BLOCK_ANVIL_USE);
                    config.setDifficulty(playerClicked, slot);
                    reloadInventory("Difficulty - Settings", slot, mainMenu);
                    player.sendMessage(Component.text("Changed Difficulty of " + playerClicked.getName() + " to " + config.checkConfigInt(playerClicked, "Difficulty")).color(NamedTextColor.RED));
                }
            }
            else if(ID == MainMenu.setUp.getID()) {
                event.setCancelled(true);
                if(slot == 53) {
                    mainMenu.showPlayerSettings(player);
                }
                else if (slot <= 4){
                    switch (slot) {
                        case 0:
                            reloadInventory("ParkourStartHeight", mainMenu);
                            MainMenu.parkourStartHeight.addBackButton(player);
                            MainMenu.parkourStartHeight.showInventory(player);
                            break;
                        case 1:
                            reloadInventory("ParkourLength", mainMenu);
                            MainMenu.parkourLength.addBackButton(player);
                            MainMenu.parkourLength.showInventory(player);
                            break;
                        case 2:
                            config.setWaitingListPosition(player.getLocation());
                            reloadInventory("SetUp", mainMenu);
                            player.sendMessage(Component.text(new TranslationFactory().getTranslation(player, "setWaitingListPosition")).color(NamedTextColor.GREEN));
                            break;
                        case 3:
                            reloadInventory("CostToLowerTheDifficulty", mainMenu);
                            MainMenu.costToLowerTheDifficulty.addBackButton(player);
                            MainMenu.costToLowerTheDifficulty.showInventory(player);
                            break;
                        case 4:
                            reloadInventory("TimeToDecideWhenRespawning", mainMenu);
                            MainMenu.timeToDecideWhenRespawning.addBackButton(player);
                            MainMenu.timeToDecideWhenRespawning.showInventory(player);
                            break;
                    }
                }
            }
            else if (ID == MainMenu.parkourStartHeight.getID()) {
                event.setCancelled(true);
                if(slot == 53) {
                    mainMenu.showPlayerSettings(player);
                }
                else if (slot <= 36){
                    minigame.playSoundToPlayer(player, 0.5F, Sound.BLOCK_ANVIL_USE);
                    int parkourStartHeight = slot * 10;
                    config.setParkourStartHeight(parkourStartHeight);
                    reloadInventory("ParkourStartHeight", mainMenu);
                }
            }
            else if (ID == MainMenu.parkourLength.getID()) {
                event.setCancelled(true);
                if(slot == 53) {
                    mainMenu.showPlayerSettings(player);
                }
                else if (slot < 20) {
                    minigame.playSoundToPlayer(player, 0.5F, Sound.BLOCK_ANVIL_USE);
                    config.setParkourLength(slot);
                    reloadInventory("ParkourLength", mainMenu);
                }
            }
            else if (ID == MainMenu.costToLowerTheDifficulty.getID()) {
                event.setCancelled(true);
                if(slot == 53) {
                    mainMenu.showPlayerSettings(player);
                }
                else if (slot < 9) {
                    minigame.playSoundToPlayer(player, 0.5F, Sound.BLOCK_ANVIL_USE);
                    slot = slot + 1;
                    config.setCostToLowerTheDifficulty(slot);
                    reloadInventory("CostToLowerTheDifficulty", mainMenu);
                }
            }
            else if (ID == MainMenu.timeToDecideWhenRespawning.getID()) {
                event.setCancelled(true);
                if(slot == 53) {
                    mainMenu.showPlayerSettings(player);
                }
                else if (slot < 29) {
                    minigame.playSoundToPlayer(player, 0.5F, Sound.BLOCK_ANVIL_USE);
                    slot = slot + 5;
                    config.setTimeToDecideWhenRespawning(slot);
                    reloadInventory("TimeToDecideWhenRespawning", mainMenu);
                }
            }
        }
    }

    public Player getPlayerFromListFromSpecificInt(int placeInList) {
        return Bukkit.getPlayer(Config.knownPlayers.get(placeInList));
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
                break;
            case "Settings":
                if(config.checkConfigBoolean("SetUp")) {
                    mainMenu.addClickableItemStack("SetUp", Material.GREEN_CONCRETE, 1, 0);
                }
                else {
                    mainMenu.addClickableItemStack("SetUp", Material.RED_CONCRETE, 1, 0);
                }
                mainMenu.addClickableItemStack("Introduction", Material.GREEN_CONCRETE, 1, 1);
                mainMenu.addClickableItemStack("UsesPlugin", Material.GREEN_CONCRETE, 1, 2);
                mainMenu.addClickableItemStack("Difficulty", Material.RED_CONCRETE, 1, 3);
                break;
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
                break;
            case "SetUp":
                mainMenu.setUpSettingsSetInventoryContents();
                break;
            case "ParkourStartHeight":
                mainMenu.parkourStartHeightSettingsSetInventoryContents();
                break;
            case "ParkourLength":
                mainMenu.parkourLengthSettingsSetInventoryContents();
                break;
            case "CostToLowerTheDifficulty":
                mainMenu.costToLowerTheDifficultySettingsSetInventoryContents();
                break;
            case "TimeToDecideWhenRespawning":
                mainMenu.timeToDecideWhenRespawningSettingsSetInventoryContents();
                break;
        }
    }

}
