package fr.redbuild.models.paper.handlers;

import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public interface OnInteractHandler {

    void execute(Player player , Action click, PlayerInteractEvent event);
}
