package fr.redbuild.models.spigot.handlers;

import fr.redbuild.models.spigot.Gui.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Optional;

public interface OnGuiClickHandler {
    void execute(InventoryClickEvent event,Player player, Optional<ItemBuilder> item);
}
