package org.example.DeathMinigames.minigames;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.example.DeathMinigames.deathMinigames.Config;
import org.example.DeathMinigames.deathMinigames.Main;

public class Difficulty {

    public void higherDifficulty(Player player) {
        Config config = new Config();

        if(config.checkConfigInt(player, "Difficulty") == 10) {
            player.sendMessage(Component.text("Du hast schon die maximale Schwierigkeit erreicht. Hast du super gemacht").color(NamedTextColor.RED));
        }
        else {
            config.setDifficulty(player, config.checkConfigInt(player, "Difficulty") + 1);
        }
    }

    public void lowerDifficulty(Player player) {
        Config config = new Config();

        config.setDifficulty(player, config.checkConfigInt(player, "Difficulty") - 1);
    }

    public boolean checkIfPlayerCanPay(Player player) {
       Config config = new Config();
        return player.getInventory().contains(Material.DIAMOND, config.checkConfigInt("CostToLowerTheDifficulty"));
    }

    public void playerPay(Player player) {
        Config config = new Config();
        ItemStack diamonds = new ItemStack(Material.DIAMOND, config.checkConfigInt("CostToLowerTheDifficulty"));
        player.getInventory().removeItem(diamonds);
    }
}
