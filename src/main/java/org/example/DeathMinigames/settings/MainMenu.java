package org.example.DeathMinigames.settings;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class MainMenu implements InventoryHolder {
    private Inventory inventory;
    public static GUI introduction = new GUI("Introduction", true);
    public static GUI difficulty = new GUI("Difficulty", true);
    public static GUI usesPlugin = new GUI("UsesPlugin", true);
    public static GUI difficultyPlayerSettings = new GUI("Difficulty - Settings", false);
    public static GUI setUp = new GUI("SetUp", false);

    public void showPlayerSettings(Player player) {
        inventory = Bukkit.createInventory(this, 9, "Settings");
        addSubmenus();
        showPlayerInv(player);
    }

    private void addSubmenus() {
        addClickableItemStack("SetUp", Material.GOLD_BLOCK, 1, 0);
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
}
