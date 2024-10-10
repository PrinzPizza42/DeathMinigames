package org.example.DeathMinigames.settings;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.example.DeathMinigames.deathMinigames.Main;

import java.util.ArrayList;

public class SubMenu {
    private Inventory inventory;
    private ArrayList<String> whitelistedPlayers = new ArrayList<>();

    public SubMenu(String name, boolean addWhitelistedPlayers) {
        inventory = Bukkit.createInventory(null, 45, name);
        putWhitelistedPlayersInList();
        if(addWhitelistedPlayers) {
            for(int i = 0; i < whitelistedPlayers.size(); i++) {
                Material material;
                if((i % 2) == 0) {
                    material = Material.RED_CONCRETE_POWDER;
                }
                else {
                    material = Material.GREEN_CONCRETE_POWDER;
                }
                ItemStack itemStack = new ItemStack(material);
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.displayName(Component.text(getWhitelistedPlayersInArrayList().get(i)));
                itemStack.setItemMeta(itemMeta);

                inventory.setItem(i, itemStack);
            }
        }
        addClickableItemStack("Zurück", Material.RED_CONCRETE, 1, 44);
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

    public void putWhitelistedPlayersInList() {
        for(OfflinePlayer player : Bukkit.getWhitelistedPlayers()) {
            whitelistedPlayers.add(player.getName());
        }
    }

    public ArrayList<String> getWhitelistedPlayersInArrayList() {
        return whitelistedPlayers;
    }
}
