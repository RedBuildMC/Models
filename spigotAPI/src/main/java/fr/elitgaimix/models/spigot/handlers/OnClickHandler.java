package fr.elitgaimix.models.spigot.handlers;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public interface OnClickHandler {
    void execute(InventoryClickEvent event,Player player);
}
