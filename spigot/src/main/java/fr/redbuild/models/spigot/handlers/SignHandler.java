package fr.redbuild.models.spigot.handlers;

import fr.redbuild.models.spigot.event.SignUpdateEvent;
import org.bukkit.entity.Player;

import java.util.List;

public interface SignHandler {

    void execute(SignUpdateEvent event, Player player, List<String> text);
}
