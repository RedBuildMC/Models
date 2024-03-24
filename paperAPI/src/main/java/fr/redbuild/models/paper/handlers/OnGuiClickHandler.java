package fr.redbuild.models.paper.handlers;

import fr.redbuild.models.paper.Gui.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Optional;

public interface OnGuiClickHandler {
    void execute(InventoryClickEvent event,Player player, Optional<ItemBuilder> item);
}
