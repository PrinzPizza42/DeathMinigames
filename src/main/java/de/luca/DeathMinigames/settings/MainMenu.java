package de.luca.DeathMinigames.settings;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import de.luca.DeathMinigames.deathMinigames.Config;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MainMenu implements InventoryHolder {
    private Inventory inventory;
    public static GUI introduction = new GUI("Introduction", true);
    public static GUI difficulty = new GUI("Difficulty", true);
    public static GUI usesPlugin = new GUI("UsesPlugin", true);
    public static GUI difficultyPlayerSettings = new GUI("Difficulty - Settings", false);
    public static GUI setUp = new GUI("SetUp", false);
    public static GUI parkourStartHeight = new GUI("ParkourStartHeight", false);
    public static GUI parkourLength = new GUI("ParkourLength", false);
    public static GUI costToLowerTheDifficulty = new GUI("CostToLowerTheDifficulty", false);
    public static GUI timeToDecideWhenRespawning = new GUI("TimeToDecideWhenRespawning", false);

    public void showPlayerSettings(Player player) {
        inventory = Bukkit.createInventory(this, 9, "Settings");
        addSubmenus();
        showPlayerInv(player);
    }

    private void addSubmenus() {
        Config config = new Config();
        if(config.checkConfigBoolean("SetUp")) {
            addClickableItemStack("SetUp", Material.GREEN_CONCRETE, 1, 0);
        }
        else {
            addClickableItemStack("SetUp", Material.RED_CONCRETE, 1, 0);
        }
        addClickableItemStack("Introduction", Material.GREEN_CONCRETE, 1, 1);
        addClickableItemStack("UsesPlugin", Material.GREEN_CONCRETE, 1, 2);
        addClickableItemStack("Difficulty", Material.RED_CONCRETE, 1, 3);
    }

    private void showPlayerInv(Player player) {
        player.openInventory(inventory);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return null;
    }

    public void addClickableItemStack(String name, Material material, int amount, int slotWhereToPutTheItem) {
        // item has to be added in InventoryListener manually to make it clickable
        ItemStack itemStack = new ItemStack(material, amount);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemStack.setItemMeta(itemMeta);

        inventory.setItem(slotWhereToPutTheItem, itemStack);
    }

    public void difficultySettingsSetInventoryContents(int difficulty) {
        for(int i = 0; i < 11; i++) {
            MainMenu.difficultyPlayerSettings.addClickableItemStack(Integer.toString(i), Material.RED_CONCRETE_POWDER, 1, i);
        }
        MainMenu.difficultyPlayerSettings.addClickableItemStack(Integer.toString(difficulty), Material.GREEN_CONCRETE_POWDER, 1 ,difficulty);
    }

    public void setUpSettingsSetInventoryContents() {
        Config config = new Config();
        int startHeight = config.checkConfigInt("ParkourStartHeight");
        int parkourLength = config.checkConfigInt("ParkourLength");
        int costToLowerTheDifficulty = config.checkConfigInt("CostToLowerTheDifficulty");
        int timeToDecideWhenRespawning = config.checkConfigInt("TimeToDecideWhenRespawning");

        if(startHeight != 0) {
            setUp.addClickableItemStack("Parcour Start Height", Material.LADDER, startHeight, 0);
        }
        else {
            setUp.addClickableItemStack("Parcour Start Height", Material.LADDER, 1, 0);
        }
        if(parkourLength != 0) {
            setUp.addClickableItemStack("Parcour length", Material.LADDER, parkourLength, 1);
        }
        else {
            setUp.addClickableItemStack("Parcour length", Material.LADDER, 1, 1);
        }
        if(config.checkConfigLocation("WaitingListPosition")!=null) {
            ArrayList<String> lore = new ArrayList<>();
            lore.add("Current position:");
            lore.add("X: " + Integer.toString(config.checkConfigLocation("WaitingListPosition").getBlockX()));
            lore.add("Y: " + Integer.toString(config.checkConfigLocation("WaitingListPosition").getBlockY()));
            lore.add("Z: " + Integer.toString(config.checkConfigLocation("WaitingListPosition").getBlockZ()));
            setUp.addClickableItemStack("WaitingListPosition", Material.GREEN_CONCRETE_POWDER, 1, 2, lore);
        }
        else {
            setUp.addClickableItemStack("WaitingListPosition", Material.RED_CONCRETE_POWDER, 1, 2);
        }
        if(costToLowerTheDifficulty != 0) {
            setUp.addClickableItemStack("cost to lower the difficulty", Material.DIAMOND, costToLowerTheDifficulty, 3);
        }
        else {
            setUp.addClickableItemStack("cost to lower the difficulty", Material.DIAMOND, 1, 3);
        }
        if(timeToDecideWhenRespawning != 0) {
            setUp.addClickableItemStack("time to decide when respawning", Material.CLOCK, timeToDecideWhenRespawning, 4);
        }
        else {
            setUp.addClickableItemStack("time to decide when respawning", Material.CLOCK, 1, 4);
        }
    }

    public void parkourStartHeightSettingsSetInventoryContents() {
        Config config = new Config();
        int startHeight = config.checkConfigInt("ParkourStartHeight");
        for(int i = 0; i < 29; i++) {
            if(startHeight == i*10) {
                MainMenu.parkourStartHeight.addClickableItemStack(Integer.toString(i*10), Material.GREEN_CONCRETE_POWDER, 1, i);
            }
            else {
                MainMenu.parkourStartHeight.addClickableItemStack(Integer.toString(i*10), Material.RED_CONCRETE_POWDER, 1, i);
            }
        }
    }

    public void parkourLengthSettingsSetInventoryContents() {
        Config config = new Config();
        int length = config.checkConfigInt("ParkourLength");
        for(int i = 0; i < 20; i++) {
            if(length == i) {
                MainMenu.parkourLength.addClickableItemStack(Integer.toString(i), Material.GREEN_CONCRETE_POWDER, 1, i);
            }
            else {
                MainMenu.parkourLength.addClickableItemStack(Integer.toString(i), Material.RED_CONCRETE_POWDER, 1, i);
            }
        }
    }

    public void costToLowerTheDifficultySettingsSetInventoryContents() {
        Config config = new Config();
        int length = config.checkConfigInt("CostToLowerTheDifficulty");
        for(int i = 1; i < 11; i++) {
            if(length == i) {
                MainMenu.costToLowerTheDifficulty.addClickableItemStack(Integer.toString(i), Material.GREEN_CONCRETE_POWDER, 1, i-1);
            }
            else {
                MainMenu.costToLowerTheDifficulty.addClickableItemStack(Integer.toString(i), Material.RED_CONCRETE_POWDER, 1, i-1);
            }
        }
    }

    public void timeToDecideWhenRespawningSettingsSetInventoryContents() {
        Config config = new Config();
        int time = config.checkConfigInt("TimeToDecideWhenRespawning");
        for(int i = 5; i < 31; i++) {
            if(time == i) {
                MainMenu.timeToDecideWhenRespawning.addClickableItemStack(Integer.toString(time), Material.GREEN_CONCRETE_POWDER, 1, i-5);
            }
            else {
                MainMenu.timeToDecideWhenRespawning.addClickableItemStack(Integer.toString(i), Material.RED_CONCRETE_POWDER, 1, i-5);
            }
        }
    }

}
