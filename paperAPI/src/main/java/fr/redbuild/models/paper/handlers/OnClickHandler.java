package fr.redbuild.models.paper.handlers;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public interface OnClickHandler {
    void execute(InventoryClickEvent event,Player player);
}
