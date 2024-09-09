package org.example.DeathMinigames.Listeners;

import org.bukkit.Location;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class SnowballHitListener implements Listener {
    @EventHandler
    public void onSnowballHit(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof Snowball)) return;
        Location location;
        location = event.getHitBlock().getLocation();
        location.createExplosion(100F, false, true);
    }
}
