package fr.elitgaimix.models.spigot.handlers;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface LocationEditMode {
    
    void execute(Player player,Location location);

    void cancel(Player player);
}
