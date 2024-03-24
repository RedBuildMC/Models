package fr.redbuild.models.paper.handlers;

import fr.redbuild.models.paper.event.SignUpdateEvent;
import org.bukkit.entity.Player;

import java.util.List;

public interface SignHandler {

    void execute(SignUpdateEvent event, Player player, List<String> text);
}
