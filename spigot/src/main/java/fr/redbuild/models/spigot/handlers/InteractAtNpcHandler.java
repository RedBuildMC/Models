package fr.redbuild.models.spigot.handlers;

import fr.redbuild.models.spigot.event.PlayerInteractAtNpcEvent;
import org.bukkit.entity.Player;

import java.util.UUID;

public interface InteractAtNpcHandler {

    void execute(Player player , PlayerInteractAtNpcEvent event, UUID uuid);
}
