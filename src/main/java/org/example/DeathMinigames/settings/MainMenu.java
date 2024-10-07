package org.example.DeathMinigames.settings;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.example.DeathMinigames.deathMinigames.Introduction;
import org.example.DeathMinigames.deathMinigames.Main;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;

public class MainMenu {
    private ArrayList<String> subMenus = new ArrayList<String>();
    private Inventory inventory = Bukkit.createInventory(null, 9, "Settings");
    public SubMenu introduction = new SubMenu("Introduction", true);
    public SubMenu difficulty = new SubMenu("Difficulty", true);
    public SubMenu usesPlugin = new SubMenu("uses Plugin", true);


    public void showPlayerSettings(Player player) {
        addSubmenus();
        showPlayerInv(player);
    }

    private void addSubmenus() {
        addNamesOfSubMenusToList();

        introduction.addClickableItemStack("Test", Material.DIAMOND, 1, 1);

        for(int i = 0; i < subMenus.size(); i++) {
            Material material;
            if((i % 2) == 0) {
                material = Material.RED_CONCRETE;
            }
            else {
                material = Material.GREEN_CONCRETE;
            }
            ItemStack itemStack = new ItemStack(material);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.displayName(Component.text(subMenus.get(i)));
            itemStack.setItemMeta(itemMeta);

            inventory.setItem(i, itemStack);
        }
    }

    private void addNamesOfSubMenusToList() {
        subMenus.addLast("Introduction");
        subMenus.addLast("Difficulty");
        subMenus.addLast("uses Plugin");
    }

    private void showPlayerInv(Player player) {
        player.openInventory(inventory);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void openSubMenu(Player player, int slotClicked) {
        switch (Integer.toString(slotClicked)) {
            case"0":
                introduction.showInventory(player);
                break;
            case"1":
                difficulty.showInventory(player);
                break;
            case"2":
                usesPlugin.showInventory(player);
                break;
        }
    }

    public void openSubSubMenu(Player player, String playerWhichSettingsToOpen, String nameOfTopic) {
        SubSubMenu subSubMenu = new SubSubMenu(nameOfTopic, playerWhichSettingsToOpen);
        subSubMenu.showInventory(player);
    }
}
