package fr.redbuild.models.paper.handlers;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public interface OnDropHandler {
    void execute(Player player, ItemStack item,PlayerDropItemEvent event);
}
