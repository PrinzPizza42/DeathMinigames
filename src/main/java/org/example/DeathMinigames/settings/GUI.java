package org.example.DeathMinigames.settings;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.example.DeathMinigames.deathMinigames.Config;
import org.example.DeathMinigames.listeners.InventoryListener;

import java.util.ArrayList;
import java.util.Random;

public class GUI implements InventoryHolder {
    private Inventory inventory;
    private int randomValue = new Random().nextInt();

    public GUI(String title, boolean addAllPlayers) {
        Config config = new Config();
        InventoryListener inventoryListener = new InventoryListener();

        inventory = Bukkit.createInventory(this, 54, title);
        if(addAllPlayers) {
            for(int i = 0; i < Config.knownPlayers.size(); i++) {
                Material material;
                if(config.checkConfigBoolean(inventoryListener.getPlayerFromListFromSpecificInt(i), title)) {
                    material = Material.GREEN_CONCRETE_POWDER;
                }
                else {
                    material = Material.RED_CONCRETE_POWDER;
                }
                ItemStack itemStack = new ItemStack(material);
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.displayName(Component.text(Bukkit.getPlayer(Config.knownPlayers.get(i)).getName()));
                itemStack.setItemMeta(itemMeta);

                inventory.setItem(i, itemStack);
            }
        }
        addClickableItemStack("Zurück", Material.RED_CONCRETE, 1, 53);
    }

    public void addClickableItemStack(String name, Material material, int amount, int slotWhereToPutTheItem) {
        // item has to be added in InventoryListener manually to make it clickable
        ItemStack itemStack = new ItemStack(material, amount);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemStack.setItemMeta(itemMeta);

        inventory.setItem(slotWhereToPutTheItem, itemStack);
    }

    public void addClickableContentsViaItemStackList(ItemStack[] itemStackList) {
        inventory.setContents(itemStackList);
    }

    public void showInventory(Player playerToShowTheInvTo) {
        playerToShowTheInvTo.openInventory(inventory);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public int getID() {
        return randomValue;
    }
}
