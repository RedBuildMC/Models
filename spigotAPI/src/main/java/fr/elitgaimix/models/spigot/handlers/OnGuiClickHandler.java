package fr.elitgaimix.models.spigot.handlers;

import fr.elitgaimix.models.spigot.Gui.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Optional;

public interface OnGuiClickHandler {
    void execute(InventoryClickEvent event,Player player, Optional<ItemBuilder> item);
}
