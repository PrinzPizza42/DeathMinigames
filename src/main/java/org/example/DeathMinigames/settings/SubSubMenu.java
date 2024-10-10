package org.example.DeathMinigames.settings;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.example.DeathMinigames.deathMinigames.Main;

import java.util.Objects;

public class SubSubMenu extends SubMenu {
    private Inventory inventory;
    private String playerToChangeSettings;

    public SubSubMenu(String nameOfTopic, String playerWhichSettingsToOpen) {
        super(nameOfTopic, false);
        Main main = new Main();

        this.playerToChangeSettings = playerWhichSettingsToOpen;
        if(Objects.equals(nameOfTopic, "Difficulty - PlayerSettings")) {
            addIntSettings(0, 11);
        }
        else if (Objects.equals(nameOfTopic, "Introduction - PlayerSettings")) {
            addBooleanSettings();
        }
        else if (Objects.equals(nameOfTopic, "uses Plugin - PlayerSettings")) {
            addBooleanSettings();
        }
    }

    private void addIntSettings(int min, int max) {
        for (int i = min; i < max; i++) {
            addClickableItemStack(Integer.toString(i), Material.GOLD_BLOCK, i + 1, i);
        }
    }

    private void addBooleanSettings() {
        addClickableItemStack(Boolean.toString(true), Material.GOLD_BLOCK, 1, 0);
        addClickableItemStack(Boolean.toString(false), Material.GOLD_BLOCK, 1, 1);
    }

    public String getPlayerToChangeSettings() {
        return playerToChangeSettings;
    }
}
