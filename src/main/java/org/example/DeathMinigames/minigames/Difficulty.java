package org.example.DeathMinigames.minigames;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.example.DeathMinigames.deathMinigames.Main;

public class Difficulty {

    public void higherDifficulty(Player player) {
        Main main = new Main();

        if(main.checkConfigInt(player, "Difficulty") == 10) {
            player.sendMessage(Component.text("Du hast schon die maximale Schwierigkeit erreicht. Hast du super gemacht").color(NamedTextColor.RED));
        }
        else {
            main.setDifficulty(player, main.checkConfigInt(player, "Difficulty") + 1);
        }
    }

    public void lowerDifficulty(Player player) {
        Main main = new Main();

        main.setDifficulty(player, main.checkConfigInt(player, "Difficulty") - 1);
        main.getPlugin().saveConfig();
    }

    public boolean checkIfPlayerCanPay(Player player) {
       return player.getInventory().contains(Material.DIAMOND, 4);
    }

    public void PlayerPay(Player player) {
        ItemStack diamonds = new ItemStack(Material.DIAMOND, 4);
        player.getInventory().removeItem(diamonds);
    }
}
